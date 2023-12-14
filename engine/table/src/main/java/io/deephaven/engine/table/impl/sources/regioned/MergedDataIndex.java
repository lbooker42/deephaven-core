package io.deephaven.engine.table.impl.sources.regioned;

import io.deephaven.UncheckedDeephavenException;
import io.deephaven.base.verify.Assert;
import io.deephaven.base.verify.Require;
import io.deephaven.engine.primitive.iterator.CloseableIterator;
import io.deephaven.engine.rowset.RowSequence;
import io.deephaven.engine.rowset.RowSet;
import io.deephaven.engine.rowset.RowSetBuilderSequential;
import io.deephaven.engine.rowset.RowSetFactory;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.PartitionedTableFactory;
import io.deephaven.engine.table.Table;
import io.deephaven.engine.table.impl.by.AggregationProcessor;
import io.deephaven.engine.table.impl.by.AggregationRowLookup;
import io.deephaven.engine.table.impl.dataindex.BaseDataIndex;
import io.deephaven.engine.table.impl.locations.TableLocation;
import io.deephaven.engine.table.impl.perf.QueryPerformanceRecorder;
import io.deephaven.engine.table.impl.select.FunctionalColumn;
import io.deephaven.util.SafeCloseable;
import io.deephaven.util.annotations.InternalUseOnly;
import io.deephaven.vector.ObjectVector;
import org.gradle.internal.impldep.com.beust.jcommander.Strings;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * DataIndex that accumulates the individual per-{@link TableLocation} data indexes of a {@link Table} backed by a
 * {@link RegionedColumnSourceManager}.
 * 
 * @implNote This implementation is responsible for ensuring that the provided table accounts for the relative positions
 *           of individual table locations in the provided table of indices. Work to coalesce the index table is
 *           deferred until the first call to {@link #table()}. Refreshing inputs/indexes are not supported at this time
 *           due to concurrency limitations (w.r.t. the UpdateGraph) of the underlying table operations used to compute
 *           the merged index table, as well as a lack of use cases beyond "new static partitions are added to a live
 *           source table".
 */
@InternalUseOnly
class MergedDataIndex extends BaseDataIndex {

    private static final String LOCATION_DATA_INDEX_TABLE_COLUMN_NAME = "__DataIndexTable";

    private final String[] keyColumnNames;
    private final RegionedColumnSourceManager columnSourceManager;

    private final Map<ColumnSource<?>, String> keyColumnMap;

    /** The table containing the index. Consists of sorted key column(s) and an associated RowSet column. */
    private volatile Table indexTable;

    private volatile AggregationRowLookup lookupFunction;

    /** Whether this index is known to be corrupt. */
    private volatile boolean isCorrupt = false;

    MergedDataIndex(
            @NotNull final String[] keyColumnNames,
            @NotNull final ColumnSource<?>[] keySources,
            @NotNull final RegionedColumnSourceManager columnSourceManager) {

        Require.eq(keyColumnNames.length, "keyColumnNames.length", keySources.length, "keySources.length");

        this.keyColumnNames = keyColumnNames;
        this.columnSourceManager = columnSourceManager;

        // Create an in-order reverse lookup map for the key column names
        keyColumnMap = Collections.unmodifiableMap(IntStream.range(0, keySources.length).sequential()
                .collect(LinkedHashMap::new, (m, i) -> m.put(keySources[i], keyColumnNames[i]), Assert::neverInvoked));

        if (columnSourceManager.locationTable().isRefreshing()) {
            throw new UnsupportedOperationException("Refreshing location tables are not currently supported");
        }

        // Defer the actual index table creation until it is needed
    }

    @Override
    @NotNull
    public String[] keyColumnNames() {
        return keyColumnNames;
    }

    @Override
    @NotNull
    public Map<ColumnSource<?>, String> keyColumnMap() {
        return keyColumnMap;
    }

    @Override
    @NotNull
    public String rowSetColumnName() {
        return ROW_SET_COLUMN_NAME;
    }

    @Override
    @NotNull
    public Table table() {
        if (indexTable != null) {
            return indexTable;
        }
        synchronized (this) {
            if (indexTable != null) {
                return indexTable;
            }
            try {
                QueryPerformanceRecorder.withNugget(
                        String.format("Merge Data Indexes [%s]", String.join(", ", keyColumnNames)),
                        this::buildTable);
            } catch (Throwable t) {
                isCorrupt = true;
                throw t;
            }
        }
        return indexTable;
    }

    private void buildTable() {
        final Table locationTable = columnSourceManager.locationTable().coalesce();

        // Perform a parallelizable update to produce coalesced location index tables with their row sets shifted by
        // the appropriate region offset.
        // This potentially loads many small row sets into memory, but it avoids the risk of re-materializing row set
        // pages during the accumulation phase.
        final Table locationDataIndexes = locationTable.update(List.of(new FunctionalColumn<>(
                columnSourceManager.locationColumnName(), TableLocation.class,
                LOCATION_DATA_INDEX_TABLE_COLUMN_NAME, Table.class,
                this::loadIndexTableAndShiftRowSets))).dropColumns(columnSourceManager.locationColumnName());

        // Merge all the location index tables into a single table
        final Table mergedDataIndexes = PartitionedTableFactory.of(locationDataIndexes).merge();

        // Group the merged data indexes by the keys
        final Table groupedByKeyColumns = mergedDataIndexes.groupBy(keyColumnNames);

        // Combine the row sets from each group into a single row set
        final Table combined = groupedByKeyColumns.update(List.of(new FunctionalColumn<>(
                ROW_SET_COLUMN_NAME, ObjectVector.class,
                ROW_SET_COLUMN_NAME, RowSet.class,
                this::mergeRowSets)));
        Assert.assertion(combined.isFlat(), "combined.isFlat()");
        Assert.eq(groupedByKeyColumns.size(), "groupedByKeyColumns.size()", combined.size(), "combined.size()");

        // Cleanup after ourselves
        try (final CloseableIterator<RowSet> rowSets = mergedDataIndexes.objectColumnIterator(ROW_SET_COLUMN_NAME)) {
            rowSets.forEachRemaining(SafeCloseable::close);
        }

        lookupFunction = AggregationProcessor.getRowLookup(groupedByKeyColumns);
        indexTable = combined;
    }

    private Table loadIndexTableAndShiftRowSets(final long locationRowKey, @NotNull final TableLocation location) {
        final Table indexTable = location.getDataIndex(keyColumnNames);
        if (indexTable == null) {
            isCorrupt = true;
            throw new UncheckedDeephavenException(String.format("Failed to load data index [%s] for location %s",
                    Strings.join(", ", keyColumnNames), location));
        }
        return indexTable.coalesce().update(List.of(new FunctionalColumn<>(
                ROW_SET_COLUMN_NAME, RowSet.class,
                ROW_SET_COLUMN_NAME, RowSet.class,
                (final RowSet rowSet) -> rowSet
                        .shift(RegionedColumnSource.getFirstRowKey(Math.toIntExact(locationRowKey))))));
    }

    private RowSet mergeRowSets(
            @SuppressWarnings("unused") final long unusedRowKey,
            @NotNull final ObjectVector<RowSet> keyRowSets) {
        final RowSetBuilderSequential builder = RowSetFactory.builderSequential();
        try (final CloseableIterator<RowSet> rowSets = keyRowSets.iterator()) {
            rowSets.forEachRemaining(builder::appendRowSequence);
        }
        return builder.build();
    }

    @Override
    @NotNull
    public RowSetLookup rowSetLookup() {
        table();
        final ColumnSource<RowSet> rowSetColumnSource = rowSetColumn();
        return (final Object key, final boolean usePrev) -> {
            // Pass the key to the aggregation row lookup
            final int keyRowPosition = lookupFunction.get(key);
            if (keyRowPosition == lookupFunction.noEntryValue()) {
                return null;
            }
            // Return the RowSet at that row position (which is also the row key)
            // Note that since this is a static data index, there's no need to handle `usePrev
            return rowSetColumnSource.get(keyRowPosition);
        };
    }

    @Override
    @NotNull
    public PositionLookup positionLookup() {
        table();
        return (final Object key, final boolean usePrev) -> {
            // Pass the object to the aggregation lookup, then return the resulting row position (which is also the row
            // key).
            final int keyRowPosition = lookupFunction.get(key);
            if (keyRowPosition == lookupFunction.noEntryValue()) {
                return (int) RowSequence.NULL_ROW_KEY;
            }
            return keyRowPosition;
        };
    }

    @Override
    public boolean isRefreshing() {
        return false;
    }

    @Override
    public boolean validate() {
        if (isCorrupt) {
            return false;
        }
        try (final CloseableIterator<TableLocation> locations =
                columnSourceManager.locationTable().objectColumnIterator(columnSourceManager.locationColumnName())) {
            while (locations.hasNext()) {
                final TableLocation location = locations.next();
                if (!location.hasDataIndex(keyColumnNames)) {
                    return false;
                }
            }
        }
        return true;
    }
}