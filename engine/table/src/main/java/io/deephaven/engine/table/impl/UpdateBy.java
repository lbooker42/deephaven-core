package io.deephaven.engine.table.impl;

import gnu.trove.map.hash.TObjectIntHashMap;
import io.deephaven.api.ColumnName;
import io.deephaven.api.updateby.UpdateByOperation;
import io.deephaven.api.updateby.UpdateByControl;
import io.deephaven.chunk.Chunk;
import io.deephaven.chunk.LongChunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.exceptions.UncheckedTableException;
import io.deephaven.engine.rowset.*;
import io.deephaven.engine.table.*;
import io.deephaven.engine.table.impl.sources.LongSparseArraySource;
import io.deephaven.engine.table.impl.sources.ReinterpretUtils;
import io.deephaven.engine.table.impl.sources.sparse.SparseConstants;
import io.deephaven.engine.table.impl.util.InverseRowRedirectionImpl;
import io.deephaven.engine.table.impl.util.LongColumnSourceWritableRowRedirection;
import io.deephaven.engine.table.impl.util.WritableRowRedirection;
import org.apache.commons.lang3.mutable.MutableLong;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static io.deephaven.engine.rowset.RowSequence.NULL_ROW_KEY;

/**
 * The core of the {@link TableWithDefaults#updateBy(UpdateByControl, Collection, Collection)} operation.
 */
public abstract class UpdateBy {
    protected final ChunkSource.WithPrev<Values>[] inputSources;
    protected final int[] inputSourceSlots;
    protected final UpdateByOperator[] operators;
    protected final QueryTable source;

    protected final UpdateByRedirectionContext redirContext;

    protected final UpdateByControl control;

    public static class UpdateByRedirectionContext implements Context {
        @Nullable
        protected final WritableRowRedirection rowRedirection;
        protected final WritableRowSet freeRows;
        protected long maxInnerIndex;

        public UpdateByRedirectionContext(@Nullable final WritableRowRedirection rowRedirection) {
            this.rowRedirection = rowRedirection;
            this.freeRows = rowRedirection == null ? null : RowSetFactory.empty();
            this.maxInnerIndex = 0;
        }

        public boolean isRedirected() {
            return rowRedirection != null;
        }

        public long requiredCapacity() {
            return maxInnerIndex + 1;
        }

        @Nullable
        public WritableRowRedirection getRowRedirection() {
            return rowRedirection;
        }

        public void processUpdateForRedirection(@NotNull final TableUpdate upstream, final TrackingRowSet prevRowSet) {
            if (upstream.removed().isNonempty()) {
                final RowSetBuilderRandom freeBuilder = RowSetFactory.builderRandom();
                synchronized (rowRedirection) {
                    upstream.removed().forAllRowKeys(key -> freeBuilder.addKey(rowRedirection.remove(key)));
                    freeRows.insert(freeBuilder.build());
                }
            }

            if (upstream.shifted().nonempty()) {
                try (final WritableRowSet prevIndexLessRemoves = prevRowSet.copyPrev()) {
                    prevIndexLessRemoves.remove(upstream.removed());
                    final RowSet.SearchIterator fwdIt = prevIndexLessRemoves.searchIterator();

                    upstream.shifted().apply((start, end, delta) -> {
                        if (delta < 0 && fwdIt.advance(start)) {
                            for (long key = fwdIt.currentValue(); fwdIt.currentValue() <= end; key = fwdIt.nextLong()) {
                                if (shiftRedirectedKey(fwdIt, delta, key))
                                    break;
                            }
                        } else {
                            try (final RowSet.SearchIterator revIt = prevIndexLessRemoves.reverseIterator()) {
                                if (revIt.advance(end)) {
                                    for (long key = revIt.currentValue(); revIt.currentValue() >= start; key =
                                            revIt.nextLong()) {
                                        if (shiftRedirectedKey(revIt, delta, key))
                                            break;
                                    }
                                }
                            }
                        }
                    });
                }
            }

            if (upstream.added().isNonempty()) {
                final MutableLong lastAllocated = new MutableLong(0);
                synchronized (rowRedirection) {
                    final WritableRowSet.Iterator freeIt = freeRows.iterator();
                    upstream.added().forAllRowKeys(outerKey -> {
                        final long innerKey = freeIt.hasNext() ? freeIt.nextLong() : ++maxInnerIndex;
                        lastAllocated.setValue(innerKey);
                        rowRedirection.put(outerKey, innerKey);
                    });
                    freeRows.removeRange(0, lastAllocated.longValue());
                }
            }
        }

        private boolean shiftRedirectedKey(@NotNull final RowSet.SearchIterator iterator, final long delta,
                                           final long key) {
            synchronized (rowRedirection) {
                final long inner = rowRedirection.remove(key);
                if (inner != NULL_ROW_KEY) {
                    rowRedirection.put(key + delta, inner);
                }
                return !iterator.hasNext();
            }
        }

        @Override
        public void close() {
            try (final WritableRowSet ignored = freeRows) {
            }
        }
    }

    protected UpdateBy(@NotNull final UpdateByOperator[] operators,
            @NotNull final QueryTable source,
            @NotNull final UpdateByRedirectionContext redirContext,
            UpdateByControl control) {
        this.control = control;
        this.redirContext = redirContext;

        if (operators.length == 0) {
            throw new IllegalArgumentException("At least one operator must be specified");
        }

        this.source = source;
        this.operators = operators;
        // noinspection unchecked
        this.inputSources = new ChunkSource.WithPrev[operators.length];

        final TObjectIntHashMap<ChunkSource<Values>> sourceToSlotMap = new TObjectIntHashMap<>();
        this.inputSourceSlots = new int[operators.length];
        for (int opIdx = 0; opIdx < operators.length; opIdx++) {
            final ColumnSource<?> input = source.getColumnSource(operators[opIdx].getInputColumnName());
            final int maybeExistingSlot = sourceToSlotMap.get(input);
            if (maybeExistingSlot == sourceToSlotMap.getNoEntryValue()) {
                inputSourceSlots[opIdx] = opIdx;
                sourceToSlotMap.put(input, opIdx);
                inputSources[opIdx] = ReinterpretUtils.maybeConvertToPrimitive(input);
            } else {
                inputSourceSlots[opIdx] = maybeExistingSlot;
            }
        }
    }

    // region UpdateBy implementation
    /**
     * Apply the specified operations to each group of rows in the source table and produce a result table with the same
     * index as the source with each operator applied.
     * 
     * @param source the source to apply to.
     * @param clauses the operations to apply.
     * @param byColumns the columns to group by before applying operations
     * 
     * @return a new table with the same index as the source with all the operations applied.
     */
    public static Table updateBy(@NotNull final QueryTable source,
            @NotNull final Collection<? extends UpdateByOperation> clauses,
            @NotNull final Collection<? extends ColumnName> byColumns,
            @NotNull final UpdateByControl control) {

        // create the rowRedirection if instructed
        final WritableRowRedirection rowRedirection;
        if (control.useRedirectionOrDefault()) {
            if (!source.isRefreshing()) {
                if (!source.isFlat() && SparseConstants.sparseStructureExceedsOverhead(source.getRowSet(),
                        control.maxStaticSparseMemoryOverheadOrDefault())) {
                    rowRedirection = new InverseRowRedirectionImpl(source.getRowSet());
                } else {
                    rowRedirection = null;
                }
            } else {
                final JoinControl.RedirectionType type = JoinControl.getRedirectionType(source, 4.0, true);
                switch (type) {
                    case Sparse:
                        rowRedirection = new LongColumnSourceWritableRowRedirection(new LongSparseArraySource());
                        break;
                    case Hash:
                        rowRedirection = WritableRowRedirection.FACTORY.createRowRedirection(source.intSize());
                        break;

                    default:
                        throw new IllegalStateException("Unsupported redirection type " + type);
                }
            }
        } else {
            rowRedirection = null;
        }

        // create an UpdateByRedirectionContext for use by the UpdateBy objects
        UpdateByRedirectionContext ctx = new UpdateByRedirectionContext(rowRedirection);

        // TODO(deephaven-core#2693): Improve UpdateBy implementation for ColumnName
        // generate a MatchPair array for use by the existing algorithm
        MatchPair[] pairs = MatchPair.fromPairs(byColumns);

        final UpdateByOperatorFactory updateByOperatorFactory =
                new UpdateByOperatorFactory(source, pairs, ctx, control);
        final Collection<UpdateByOperator> ops = updateByOperatorFactory.getOperators(clauses);

        final StringBuilder descriptionBuilder = new StringBuilder("updateBy(ops={")
                .append(updateByOperatorFactory.describe(clauses))
                .append("}");

        final Set<String> problems = new LinkedHashSet<>();
        // noinspection rawtypes
        final Map<String, ColumnSource<?>> opResultSources = new LinkedHashMap<>();
        ops.forEach(op -> op.getOutputColumns().forEach((name, col) -> {
            if (opResultSources.putIfAbsent(name, col) != null) {
                problems.add(name);
            }
        }));

        if (!problems.isEmpty()) {
            throw new UncheckedTableException("Multiple Operators tried to produce the same output columns {" +
                    String.join(", ", problems) + "}");
        }

        // noinspection rawtypes
        final Map<String, ColumnSource<?>> resultSources = new LinkedHashMap<>(source.getColumnSourceMap());
        resultSources.putAll(opResultSources);

        final UpdateByOperator[] opArr = ops.toArray(UpdateByOperator.ZERO_LENGTH_OP_ARRAY);
        if (pairs.length == 0) {
            descriptionBuilder.append(")");
            Table ret = ZeroKeyUpdateBy.compute(
                    descriptionBuilder.toString(),
                    source,
                    opArr,
                    resultSources,
                    ctx,
                    control,
                    true);

            if (source.isRefreshing()) {
                // start tracking previous values
                if (rowRedirection != null) {
                    rowRedirection.startTrackingPrevValues();
                }
                ops.forEach(UpdateByOperator::startTrackingPrev);
            }
            return ret;
        }

        descriptionBuilder.append(", pairs={").append(MatchPair.matchString(pairs)).append("})");

        for (final MatchPair byColumn : pairs) {
            if (!source.hasColumns(byColumn.rightColumn)) {
                problems.add(byColumn.rightColumn);
                continue;
            }
        }

        if (!problems.isEmpty()) {
            throw new UncheckedTableException(descriptionBuilder + ": Missing byColumns in parent table {" +
                    String.join(", ", problems) + "}");
        }

        Table ret = BucketedPartitionedUpdateBy.compute(
                descriptionBuilder.toString(),
                source,
                opArr,
                resultSources,
                byColumns,
                ctx,
                control);

        if (source.isRefreshing()) {
            // start tracking previous values
            if (rowRedirection != null) {
                rowRedirection.startTrackingPrevValues();
            }
            ops.forEach(UpdateByOperator::startTrackingPrev);
        }
        return ret;
    }

    /**
     * The type of update to be applied. For use with invocations of
     * {@link UpdateByOperator#initializeFor(UpdateByOperator.UpdateContext, RowSet, UpdateType)} and
     * {@link UpdateByOperator#finishFor(UpdateByOperator.UpdateContext, UpdateType)}
     */
    public enum UpdateType {
        /**
         * Indicates that rows are being
         * {@link UpdateByOperator#addChunkBucketed(UpdateByOperator.UpdateContext, RowSequence, LongChunk, Chunk, long)} added}
         * to the operator.
         */
        Add,

        /**
         * Indicates that rows are being
         * {@link UpdateByOperator#removeChunk(UpdateByOperator.UpdateContext, LongChunk, Chunk, long) removed} from the
         * operator.
         */
        Remove,

        /**
         * Indicates that rows are being
         * {@link UpdateByOperator#modifyChunk(UpdateByOperator.UpdateContext, LongChunk, LongChunk, Chunk, Chunk, long)
         * modified} within the operator
         */
        Modify,

        /**
         * Indicates that rows are being
         * {@link UpdateByOperator#applyShift(UpdateByOperator.UpdateContext, RowSet, RowSetShiftData) shifted} within
         * the operator.
         */
        Shift,

        /**
         * Indicates that the {@link TableUpdate} has been processed and rows are being revisited based upon the
         * requests of individual operators for the purposes of doing recalculations on large portions of the table
         */
        Reprocess
    }
    // endregion
}
