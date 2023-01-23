package io.deephaven.engine.table.impl.updateby;

import io.deephaven.chunk.Chunk;
import io.deephaven.chunk.IntChunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.rowset.RowSequence;
import io.deephaven.engine.table.MatchPair;
import io.deephaven.engine.table.impl.util.RowRedirection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class UpdateByWindowedOperator extends UpdateByOperator {

    public abstract static class Context implements UpdateContext {
        /** Holds the chunks of input data for use by the accumulate call */
        protected final Chunk<? extends Values>[] chunkArr;
        protected int nullCount = 0;

        public Context(int chunkCount) {
            chunkArr = new Chunk[chunkCount];
        }

        @Override
        public void close() {}

        public abstract void accumulate(RowSequence inputKeys,
                Chunk<? extends Values> influencerValueChunkArr[],
                IntChunk<? extends Values> pushChunk,
                IntChunk<? extends Values> popChunk,
                int len);
    }

    /**
     * An operator that computes a windowed operation from a column
     *
     * @param pair the {@link MatchPair} that defines the input/output for this operation
     * @param affectingColumns the names of the columns that affect this operation
     * @param timestampColumnName the optional time stamp column for windowing (uses ticks if not provided)
     * @param reverseWindowScaleUnits the time (us) or ticks to extend the window backwards
     * @param forwardWindowScaleUnits the time (us) or ticks to extend the window forwards
     * @param rowRedirection the row redirection to use for the operator output columns
     */
    protected UpdateByWindowedOperator(@NotNull final MatchPair pair,
            @NotNull final String[] affectingColumns,
            @Nullable final String timestampColumnName,
            final long reverseWindowScaleUnits,
            final long forwardWindowScaleUnits,
            @Nullable final RowRedirection rowRedirection) {
        super(pair, affectingColumns, timestampColumnName, reverseWindowScaleUnits, forwardWindowScaleUnits,
                rowRedirection);
    }

    /**
     * Initialize the bucket context for this windowed operator
     */
    public void initializeUpdate(@NotNull final UpdateContext context) {}
}