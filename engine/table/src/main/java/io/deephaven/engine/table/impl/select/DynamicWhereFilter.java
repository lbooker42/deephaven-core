//
// Copyright (c) 2016-2024 Deephaven Data Labs and Patent Pending
//
package io.deephaven.engine.table.impl.select;

import io.deephaven.base.log.LogOutput;
import io.deephaven.base.verify.Assert;
import io.deephaven.base.verify.Require;
import io.deephaven.chunk.*;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.liveness.LivenessScopeStack;
import io.deephaven.engine.primitive.iterator.CloseableIterator;
import io.deephaven.engine.rowset.*;
import io.deephaven.engine.rowset.chunkattributes.OrderedRowKeys;
import io.deephaven.engine.table.*;
import io.deephaven.engine.table.impl.*;
import io.deephaven.engine.table.impl.indexer.DataIndexer;
import io.deephaven.engine.table.impl.select.setinclusion.SetInclusionKernel;
import io.deephaven.engine.table.impl.sources.ReinterpretUtils;
import io.deephaven.engine.table.iterators.ChunkedColumnIterator;
import io.deephaven.engine.updategraph.NotificationQueue;
import io.deephaven.engine.updategraph.UpdateGraph;
import io.deephaven.util.SafeCloseable;
import io.deephaven.util.annotations.ReferentialIntegrity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

/**
 * A where filter that extracts a set of inclusion or exclusion keys from a set table.
 * <p>
 * Each time the set table ticks, the entire where filter is recalculated.
 */
public class DynamicWhereFilter extends WhereFilterLivenessArtifactImpl implements NotificationQueue.Dependency {

    private static final int CHUNK_SIZE = 1 << 16;

    private final MatchPair[] matchPairs;
    private final boolean inclusion;

    // TODO: This is only used when we have a source table data index and it's populated before we know the source
    // table. Is there a better way to handle this? Would be great to eliminate this and read SetKernel internal
    // set instead but those are commonly unboxed trove sets.
    private final Set<Object> liveValues;

    @SuppressWarnings("FieldCanBeLocal")
    @ReferentialIntegrity
    private final InstrumentedTableUpdateListener setUpdateListener;

    private final SetInclusionKernel setKernel;

    private final QueryTable setTable;
    private TupleSource<Object> setKeySource;
    private List<Object> staticSetLookupKeys;

    private ColumnSource<?>[] sourceKeyColumns;
    /**
     * The optimal data index for this filter.
     */
    private @Nullable DataIndex sourceDataIndex;
    private int @Nullable [] tupleToIndexMap;
    private int @Nullable [] indexToTupleMap;

    private RecomputeListener listener;
    private QueryTable resultTable;

    public DynamicWhereFilter(
            @NotNull final QueryTable setTable,
            final boolean inclusion,
            final MatchPair... setColumnsNames) {
        if (setTable.isRefreshing()) {
            updateGraph.checkInitiateSerialTableOperation();
        }
        this.matchPairs = setColumnsNames;
        this.inclusion = inclusion;

        // We will use tuples when there are multiple key columns.
        liveValues = new HashSet<>();

        // Use reinterpreted column sources for the set table tuple source.
        final ColumnSource<?>[] setColumns = Arrays.stream(matchPairs)
                .map(mp -> setTable.getColumnSource(mp.rightColumn()))
                .map(ReinterpretUtils::maybeConvertToPrimitive)
                .toArray(ColumnSource[]::new);
        setKeySource = TupleSourceFactory.makeTupleSource(setColumns);
        setKernel = SetInclusionKernel.makeKernel(setKeySource.getChunkType(), inclusion);

        // Fill liveValues and the set kernel with the initial keys from the set table.
        if (setTable.getRowSet().isNonempty()) {
            try (final CloseableIterator<?> initialKeysIterator = ChunkedColumnIterator.make(
                    setKeySource, setTable.getRowSet(), getChunkSize(setTable.getRowSet()))) {
                initialKeysIterator.forEachRemaining(this::addKeyUnchecked);
            }
        }

        if (setTable.isRefreshing()) {
            this.setTable = setTable;

            final String[] setColumnNames =
                    Arrays.stream(matchPairs).map(MatchPair::rightColumn).toArray(String[]::new);
            final ModifiedColumnSet setColumnsMCS = setTable.newModifiedColumnSet(setColumnNames);
            setUpdateListener = new InstrumentedTableUpdateListenerAdapter(
                    "DynamicWhereFilter(" + Arrays.toString(setColumnsNames) + ")", setTable, false) {

                @Override
                public void onUpdate(final TableUpdate upstream) {
                    final boolean hasAdds = upstream.added().isNonempty();
                    final boolean hasRemoves = upstream.removed().isNonempty();
                    final boolean hasModifies = upstream.modified().isNonempty()
                            && upstream.modifiedColumnSet().containsAny(setColumnsMCS);
                    if (!hasAdds && !hasRemoves && !hasModifies) {
                        return;
                    }

                    // Remove removed keys
                    if (hasRemoves) {
                        try (final CloseableIterator<?> removedKeysIterator = ChunkedColumnIterator.make(
                                setKeySource.getPrevSource(), upstream.removed(), getChunkSize(upstream.removed()))) {
                            removedKeysIterator.forEachRemaining(DynamicWhereFilter.this::removeKey);
                        }
                    }

                    // Update modified keys
                    boolean trueModification = false;
                    if (hasModifies) {
                        // @formatter:off
                        try (final CloseableIterator<?> preModifiedKeysIterator = ChunkedColumnIterator.make(
                                     setKeySource.getPrevSource(), upstream.getModifiedPreShift(),
                                     getChunkSize(upstream.getModifiedPreShift()));
                             final CloseableIterator<?> postModifiedKeysIterator = ChunkedColumnIterator.make(
                                     setKeySource, upstream.modified(),
                                     getChunkSize(upstream.modified()))) {
                            // @formatter:on
                            while (preModifiedKeysIterator.hasNext()) {
                                Assert.assertion(postModifiedKeysIterator.hasNext(),
                                        "Pre and post modified row sets must be the same size; post is exhausted, but pre is not");
                                final Object oldKey = preModifiedKeysIterator.next();
                                final Object newKey = postModifiedKeysIterator.next();
                                if (!Objects.equals(oldKey, newKey)) {
                                    trueModification = true;
                                    removeKey(oldKey);
                                    addKey(newKey);
                                }
                            }
                            Assert.assertion(!postModifiedKeysIterator.hasNext(),
                                    "Pre and post modified row sets must be the same size; pre is exhausted, but post is not");
                        }
                    }

                    // Add added keys
                    if (hasAdds) {
                        try (final CloseableIterator<?> addedKeysIterator = ChunkedColumnIterator.make(
                                setKeySource, upstream.added(), getChunkSize(upstream.added()))) {
                            addedKeysIterator.forEachRemaining(DynamicWhereFilter.this::addKey);
                        }
                    }

                    // Pretend every row of the original table was modified, this is essential so that the where clause
                    // can be re-evaluated based on the updated live set.
                    if (listener != null) {
                        if (hasAdds || trueModification) {
                            if (inclusion) {
                                listener.requestRecomputeUnmatched();
                            } else {
                                listener.requestRecomputeMatched();
                            }
                        }
                        if (hasRemoves || trueModification) {
                            if (inclusion) {
                                listener.requestRecomputeMatched();
                            } else {
                                listener.requestRecomputeUnmatched();
                            }
                        }
                    }
                }

                @Override
                public void onFailureInternal(Throwable originalException, Entry sourceEntry) {
                    if (listener != null) {
                        resultTable.notifyListenersOnError(originalException, sourceEntry);
                    }
                }
            };
            setTable.addUpdateListener(setUpdateListener);

            manage(setUpdateListener);
        } else {
            this.setTable = null;
            setUpdateListener = null;
        }
    }

    /**
     * "Copy constructor" for DynamicWhereFilter's with static set tables.
     */
    private DynamicWhereFilter(
            @NotNull final Set<Object> liveValues,
            @Nullable final TupleSource<Object> setKeySource,
            @NotNull final SetInclusionKernel setKernel,
            final boolean inclusion,
            final MatchPair... setColumnsNames) {
        this.liveValues = liveValues;
        this.setKeySource = setKeySource;
        this.setKernel = setKernel;
        this.inclusion = inclusion;
        this.matchPairs = setColumnsNames;
        setTable = null;
        setUpdateListener = null;
    }

    @Override
    public UpdateGraph getUpdateGraph() {
        return updateGraph;
    }

    private void removeKey(Object key) {
        final boolean removed = liveValues.remove(key);
        if (!removed) {
            throw new RuntimeException("Inconsistent state, key not found in set: " + key);
        }
        setKernel.removeItem(key);
    }

    private void addKey(Object key) {
        final boolean added = liveValues.add(key);
        if (!added) {
            throw new RuntimeException("Inconsistent state, key already in set:" + key);
        }
        setKernel.addItem(key);
    }

    private void addKeyUnchecked(Object key) {
        liveValues.add(key);
        setKernel.addItem(key);
    }

    /**
     * {@inheritDoc}
     * <p>
     * If {@code sourceTable#isRefreshing()}, this method must only be invoked when it's
     * {@link UpdateGraph#checkInitiateSerialTableOperation() safe} to initialize serial table operations.
     */
    @Override
    public SafeCloseable beginOperation(@NotNull final Table sourceTable) {
        if (sourceDataIndex != null) {
            throw new IllegalStateException("Inputs already initialized, use copy() instead of re-using a WhereFilter");
        }
        getUpdateGraph(this, sourceTable);
        final String[] keyColumnNames = MatchPair.getLeftColumns(matchPairs);
        sourceKeyColumns = Arrays.stream(matchPairs)
                .map(mp -> sourceTable.getColumnSource(mp.leftColumn())).toArray(ColumnSource[]::new);
        try (final SafeCloseable ignored = sourceTable.isRefreshing() ? LivenessScopeStack.open() : null) {
            sourceDataIndex = optimalIndex(sourceTable, keyColumnNames);
            if (sourceDataIndex != null) {
                if (sourceDataIndex.isRefreshing()) {
                    manage(sourceDataIndex);
                }
                computeTupleIndexMaps();
            }
        }

        if (setTable == null && setKeySource != null) {
            // The setTable is static, and we can release the setKeySource. Before we do, let's convert the tuples in
            // liveValues to be lookup keys in the sourceDataIndex (if lookup keys are needed).
            if (sourceDataIndex != null && sourceDataIndex.keyColumns().length > 1) {
                staticSetLookupKeys = new ArrayList<>(liveValues.size());

                final int indexKeySize = sourceDataIndex.keyColumns().length;
                final Function<Object, Object> keyMappingFunction = indexKeySize == keyColumnNames.length
                        ? tupleToKeyMappingFunction()
                        : tupleToPartialKeyMappingFunction();

                liveValues.forEach(key -> {
                    final Object[] lookupKey = (Object[]) keyMappingFunction.apply(key);
                    // Store a copy because the mapping function returns the same array each invocation.
                    staticSetLookupKeys.add(Arrays.copyOf(lookupKey, indexKeySize));
                });
                // No reason to keep the tuples around anymore, the SetInclusionKernel has copies for running
                // filterLinear()
                liveValues.clear();
            }

            setKeySource = null;
        }

        return () -> {
        };
    }

    /**
     * Returns the optimal data index for the supplied table, or null if no index is available. The ideal index would
     * contain all key columns but a partial match is also acceptable.
     */
    @Nullable
    private static DataIndex optimalIndex(final Table inputTable, final String[] keyColumnNames) {
        final DataIndex fullIndex = DataIndexer.getDataIndex(inputTable, keyColumnNames);
        if (fullIndex != null) {
            return fullIndex;
        }
        return DataIndexer.getOptimalPartialIndex(inputTable, keyColumnNames);
    }

    /**
     * Calculates mappings from the offset of a {@link ColumnSource} in the {@code sourceDataIndex} to the offset of the
     * corresponding {@link ColumnSource} in the key sources from the set or source table of a DynamicWhereFilter
     * ({@code indexToTupleMap}, as well as the reverse ({@code tupleToIndexMap}). This allows for mapping keys from the
     * {@link #liveValues} to keys in the {@link #sourceDataIndex}.
     */
    private void computeTupleIndexMaps() {
        assert sourceDataIndex != null;

        if (sourceDataIndex.keyColumns().length == 1) {
            // Trivial mapping, no need to compute anything.
            return;
        }

        final ColumnSource<?>[] dataIndexSources = sourceDataIndex
                .keyColumnNamesByIndexedColumn()
                .keySet()
                .toArray(ColumnSource.ZERO_LENGTH_COLUMN_SOURCE_ARRAY);

        // Bi-directional mapping (note that the sizes can be different, e.g. partial matching).
        final int[] tupleToIndexMap = new int[sourceKeyColumns.length];
        final int[] indexToTupleMap = new int[dataIndexSources.length];

        boolean sameOrder = true;

        // The tuples will be in sourceKeyColumns order (same as set table key columns order). We need to find the
        // dataIndex offset for each key source.
        for (int ii = 0; ii < sourceKeyColumns.length; ++ii) {
            for (int jj = 0; jj < dataIndexSources.length; ++jj) {
                if (sourceKeyColumns[ii] == dataIndexSources[jj]) {
                    tupleToIndexMap[ii] = jj;
                    indexToTupleMap[jj] = ii;
                    sameOrder &= ii == jj;
                    break;
                }
            }
        }

        // Return null if the map is the identity map
        this.tupleToIndexMap = sameOrder ? null : tupleToIndexMap;
        this.indexToTupleMap = sameOrder ? null : indexToTupleMap;
    }

    @NotNull
    private Function<Object, Object> tupleToKeyMappingFunction() {
        final Object[] keysInDataIndexOrder = new Object[sourceKeyColumns.length];
        if (tupleToIndexMap == null) {
            return (final Object tupleKey) -> {
                setKeySource.exportAllTo(keysInDataIndexOrder, tupleKey);
                return keysInDataIndexOrder;
            };
        }
        return (final Object tupleKey) -> {
            setKeySource.exportAllTo(keysInDataIndexOrder, tupleKey, tupleToIndexMap);
            return keysInDataIndexOrder;
        };
    }

    @NotNull
    private Function<Object, Object> tupleToPartialKeyMappingFunction() {
        assert sourceDataIndex != null;

        final int partialKeySize = sourceDataIndex.keyColumns().length;

        // This function is not needed when the partial key is a single column and should not be called.
        Require.geq(partialKeySize, "partialKeySize", 2, "2");

        final Object[] keysInDataIndexOrder = new Object[partialKeySize];
        // TODO: decide if this is ridiculous or clever. Trying to avoid looping inside the lambda if possible and
        // selected the size > 3 case to match the size of non-array tuples before allowing looping.
        if (indexToTupleMap == null) {
            if (partialKeySize > 3) {
                return (final Object tupleKey) -> {
                    for (int ii = 0; ii < partialKeySize; ++ii) {
                        keysInDataIndexOrder[ii] = setKeySource.exportElement(tupleKey, ii);
                    }
                    return keysInDataIndexOrder;
                };
            } else if (partialKeySize == 3) {
                return (final Object tupleKey) -> {
                    keysInDataIndexOrder[0] = setKeySource.exportElement(tupleKey, 0);
                    keysInDataIndexOrder[1] = setKeySource.exportElement(tupleKey, 1);
                    keysInDataIndexOrder[2] = setKeySource.exportElement(tupleKey, 2);
                    return keysInDataIndexOrder;
                };
            } else {
                return (final Object tupleKey) -> {
                    keysInDataIndexOrder[0] = setKeySource.exportElement(tupleKey, 0);
                    keysInDataIndexOrder[1] = setKeySource.exportElement(tupleKey, 1);
                    return keysInDataIndexOrder;
                };
            }
        } else {
            if (partialKeySize > 3) {
                return (final Object tupleKey) -> {
                    for (int ii = 0; ii < partialKeySize; ++ii) {
                        keysInDataIndexOrder[ii] = setKeySource.exportElement(tupleKey, indexToTupleMap[ii]);
                    }
                    return keysInDataIndexOrder;
                };
            } else if (partialKeySize == 3) {
                final int index0 = indexToTupleMap[0];
                final int index1 = indexToTupleMap[1];
                final int index2 = indexToTupleMap[2];

                return (final Object tupleKey) -> {
                    keysInDataIndexOrder[0] = setKeySource.exportElement(tupleKey, index0);
                    keysInDataIndexOrder[1] = setKeySource.exportElement(tupleKey, index1);
                    keysInDataIndexOrder[2] = setKeySource.exportElement(tupleKey, index2);
                    return keysInDataIndexOrder;
                };
            } else {
                final int index0 = indexToTupleMap[0];
                final int index1 = indexToTupleMap[1];

                return (final Object tupleKey) -> {
                    keysInDataIndexOrder[0] = setKeySource.exportElement(tupleKey, index0);
                    keysInDataIndexOrder[1] = setKeySource.exportElement(tupleKey, index1);
                    return keysInDataIndexOrder;
                };
            }
        }
    }

    @Override
    public List<String> getColumns() {
        return Arrays.asList(MatchPair.getLeftColumns(matchPairs));
    }

    @Override
    public List<String> getColumnArrays() {
        return Collections.emptyList();
    }

    @Override
    public void init(TableDefinition tableDefinition) {}

    @NotNull
    @Override
    public WritableRowSet filter(
            @NotNull final RowSet selection,
            @NotNull final RowSet fullSet,
            @NotNull final Table table,
            final boolean usePrev) {
        if (usePrev) {
            throw new PreviousFilteringNotSupported();
        }

        if (matchPairs.length == 1) {
            // If we have a dataIndex, we can delegate to the column source.
            if (sourceDataIndex != null) {
                final ColumnSource<?> source = ReinterpretUtils.maybeConvertToPrimitive(sourceKeyColumns[0]);
                return source.match(!inclusion, false, false, sourceDataIndex, selection, liveValues.toArray());
            }
            return filterLinear(selection, inclusion);
        }

        if (sourceDataIndex != null) {
            // Does our index contain every key column?

            if (sourceDataIndex.keyColumnNames().size() == sourceKeyColumns.length) {
                // Even if we have an index, we may be better off with a linear search.
                if (selection.size() > (sourceDataIndex.table().size() * 2L)) {
                    return filterFullIndex(selection);
                } else {
                    return filterLinear(selection, inclusion);
                }
            }

            // We have a partial index, should we use it?
            if (selection.size() > (sourceDataIndex.table().size() * 4L)) {
                return filterPartialIndex(selection);
            }
        }
        return filterLinear(selection, inclusion);
    }

    @NotNull
    private WritableRowSet filterFullIndex(@NotNull final RowSet selection) {
        Assert.neqNull(sourceDataIndex, "sourceDataIndex");
        Assert.gt(sourceKeyColumns.length, "sourceKeyColumns.length", 1);

        final WritableRowSet filtered = inclusion ? RowSetFactory.empty() : selection.copy();
        // noinspection DataFlowIssue
        final DataIndex.RowKeyLookup rowKeyLookup = sourceDataIndex.rowKeyLookup();
        final ColumnSource<RowSet> rowSetColumn = sourceDataIndex.rowSetColumn();

        final Collection<Object> values;
        final Function<Object, Object> keyMappingFunction;
        if (staticSetLookupKeys != null) {
            values = staticSetLookupKeys;
            keyMappingFunction = (final Object key) -> key;
        } else {
            values = liveValues;
            keyMappingFunction = tupleToKeyMappingFunction();
        }

        values.forEach(key -> {
            final Object mappedKey = keyMappingFunction.apply(key);
            final long rowKey = rowKeyLookup.apply(mappedKey, false);
            final RowSet rowSet = rowSetColumn.get(rowKey);
            if (rowSet != null) {
                if (inclusion) {
                    try (final RowSet intersected = rowSet.intersect(selection)) {
                        filtered.insert(intersected);
                    }
                } else {
                    filtered.remove(rowSet);
                }
            }
        });
        return filtered;
    }

    @NotNull
    private WritableRowSet filterPartialIndex(@NotNull final RowSet selection) {
        Assert.neqNull(sourceDataIndex, "sourceDataIndex");
        Assert.gt(sourceKeyColumns.length, "sourceKeyColumns.length", 1);

        final WritableRowSet matching;
        try (final WritableRowSet possiblyMatching = RowSetFactory.empty()) {
            // First, compute a possibly-matching subset of selection based on the partial index.

            // noinspection DataFlowIssue
            final DataIndex.RowKeyLookup rowKeyLookup = sourceDataIndex.rowKeyLookup();
            final ColumnSource<RowSet> rowSetColumn = sourceDataIndex.rowSetColumn();

            if (sourceDataIndex.keyColumnNames().size() == 1) {
                // Only one indexed source, so we can use the RowSetLookup directly on the right sub-key.
                final int keyOffset = indexToTupleMap == null ? 0 : indexToTupleMap[0];
                liveValues.forEach(key -> {
                    final Object lookupKey = setKeySource == null ? key : setKeySource.exportElement(key, keyOffset);
                    final long rowKey = rowKeyLookup.apply(lookupKey, false);
                    final RowSet rowSet = rowSetColumn.get(rowKey);
                    if (rowSet != null) {
                        try (final RowSet intersected = rowSet.intersect(selection)) {
                            possiblyMatching.insert(intersected);
                        }
                    }
                });
            } else {
                final Collection<Object> values;
                final Function<Object, Object> keyMappingFunction;
                if (staticSetLookupKeys != null) {
                    values = staticSetLookupKeys;
                    keyMappingFunction = (final Object key) -> key;
                } else {
                    values = liveValues;
                    keyMappingFunction = tupleToPartialKeyMappingFunction();
                }

                values.forEach(key -> {
                    final Object mappedKey = keyMappingFunction.apply(key);
                    final long rowKey = rowKeyLookup.apply(mappedKey, false);
                    final RowSet rowSet = rowSetColumn.get(rowKey);
                    if (rowSet != null) {
                        try (final RowSet intersected = rowSet.intersect(selection)) {
                            possiblyMatching.insert(intersected);
                        }
                    }
                });
            }

            // Now, do linear filter on possiblyMatching to determine the values to include or exclude from selection.
            matching = filterLinear(possiblyMatching, true);
        }
        if (inclusion) {
            return matching;
        }
        try (final SafeCloseable ignored = matching) {
            return selection.minus(matching);
        }
    }

    private WritableRowSet filterLinear(final RowSet selection, final boolean filterInclusion) {
        if (selection.isEmpty()) {
            return RowSetFactory.empty();
        }

        final RowSetBuilderSequential indexBuilder = RowSetFactory.builderSequential();

        // We need to compare reinterpreted source tuples against reinterpreted set tuples.
        final ColumnSource<?>[] reinterpretedSources = Arrays.stream(sourceKeyColumns)
                .map(ReinterpretUtils::maybeConvertToPrimitive)
                .toArray(ColumnSource[]::new);
        final TupleSource<Object> tmpKeySource = TupleSourceFactory.makeTupleSource(reinterpretedSources);

        final int maxChunkSize = getChunkSize(selection);
        // @formatter:off
        try (final ColumnSource.GetContext keyGetContext = tmpKeySource.makeGetContext(maxChunkSize);
             final RowSequence.Iterator selectionIterator = selection.getRowSequenceIterator();
             final WritableLongChunk<OrderedRowKeys> selectionRowKeyChunk =
                     WritableLongChunk.makeWritableChunk(maxChunkSize);
             final WritableBooleanChunk<Values> matches = WritableBooleanChunk.makeWritableChunk(maxChunkSize)) {
            // @formatter:on

            while (selectionIterator.hasMore()) {
                final RowSequence selectionChunk = selectionIterator.getNextRowSequenceWithLength(maxChunkSize);

                final Chunk<Values> keyChunk = Chunk.downcast(tmpKeySource.getChunk(keyGetContext, selectionChunk));
                final int thisChunkSize = keyChunk.size();
                setKernel.matchValues(keyChunk, matches, filterInclusion);

                selectionRowKeyChunk.setSize(thisChunkSize);
                selectionChunk.fillRowKeyChunk(selectionRowKeyChunk);

                for (int ii = 0; ii < thisChunkSize; ++ii) {
                    if (matches.get(ii)) {
                        indexBuilder.appendKey(selectionRowKeyChunk.get(ii));
                    }
                }
            }
        }

        return indexBuilder.build();
    }

    private static int getChunkSize(@NotNull final RowSet selection) {
        return (int) Math.min(selection.size(), CHUNK_SIZE);
    }

    @Override
    public boolean isSimpleFilter() {
        /* This doesn't execute any user code, so it should be safe to execute it against untrusted data. */
        return true;
    }

    @Override
    public boolean isRefreshing() {
        return setUpdateListener != null;
    }

    @Override
    public void setRecomputeListener(RecomputeListener listener) {
        this.listener = listener;
        this.resultTable = listener.getTable();
        if (isRefreshing()) {
            listener.setIsRefreshing(true);
        }
    }

    @Override
    public DynamicWhereFilter copy() {
        if (setTable == null) {
            // Including setKeySource covers the case where we have not converted the tuples in liveValues to index
            // lookup keys.
            return new DynamicWhereFilter(liveValues, setKeySource, setKernel, inclusion, matchPairs);
        }
        return new DynamicWhereFilter(setTable, inclusion, matchPairs);
    }

    @Override
    public boolean satisfied(final long step) {
        final boolean indexSatisfied = sourceDataIndex == null || sourceDataIndex.table().satisfied(step);
        return indexSatisfied && (setUpdateListener == null || setUpdateListener.satisfied(step));
    }

    @Override
    public LogOutput append(LogOutput logOutput) {
        return logOutput.append("DynamicWhereFilter(").append(MatchPair.MATCH_PAIR_ARRAY_FORMATTER, matchPairs)
                .append(")");
    }
}
