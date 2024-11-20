//
// Copyright (c) 2016-2024 Deephaven Data Labs and Patent Pending
//
// ****** AUTO-GENERATED CLASS - DO NOT EDIT MANUALLY
// ****** Edit CharRollingCountOperator and run "./gradlew replicateUpdateBy" to regenerate
//
// @formatter:off
package io.deephaven.engine.table.impl.updateby.rollingcount;

import io.deephaven.api.agg.util.AggCountType;
import io.deephaven.base.ringbuffer.DoubleRingBuffer;
import io.deephaven.base.verify.Assert;
import io.deephaven.chunk.DoubleChunk;
import io.deephaven.chunk.Chunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.table.impl.MatchPair;
import io.deephaven.engine.table.impl.updateby.UpdateByOperator;
import io.deephaven.engine.table.impl.updateby.internal.BaseLongUpdateByOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DoubleRollingCountOperator extends BaseLongUpdateByOperator {
    private static final int BUFFER_INITIAL_CAPACITY = 128;

    private final AggCountType countType;
    private final AggCountType.DoubleCountFunction countFunction;

    protected class Context extends BaseLongUpdateByOperator.Context {
        protected DoubleChunk<? extends Values> influencerValuesChunk;
        protected DoubleRingBuffer buffer;

        @SuppressWarnings("unused")
        protected Context(final int affectedChunkSize, final int influencerChunkSize) {
            super(affectedChunkSize);
            buffer = new DoubleRingBuffer(BUFFER_INITIAL_CAPACITY, true);

            // curVal assigned to 0 (vs. default of NULL_LONG)
            curVal = 0;
        }

        @Override
        public void close() {
            super.close();
            buffer = null;
        }

        @Override
        public void setValueChunks(@NotNull final Chunk<? extends Values>[] valueChunks) {
            influencerValuesChunk = valueChunks[0].asDoubleChunk();
        }

        @Override
        public void push(int pos, int count) {
            buffer.ensureRemaining(count);

            for (int ii = 0; ii < count; ii++) {
                final double val = influencerValuesChunk.get(pos + ii);
                buffer.addUnsafe(val);

                // Run the count function on the value and increment the count when appropriate
                if (countFunction.count(val)) {
                    curVal++;
                }
            }
        }

        @Override
        public void pop(int count) {
            Assert.geq(buffer.size(), "doubleWindowValues.size()", count);

            for (int ii = 0; ii < count; ii++) {
                final double val = buffer.removeUnsafe();

                // Run the count function on the value and increment the count when appropriate
                if (countFunction.count(val)) {
                    curVal--;
                }
            }
        }

        @Override
        public void reset() {
            super.reset();
            buffer.clear();
            curVal = 0;
        }
    }

    @NotNull
    @Override
    public UpdateByOperator.Context makeUpdateContext(final int affectedChunkSize, final int influencerChunkSize) {
        return new Context(affectedChunkSize, influencerChunkSize);
    }

    public DoubleRollingCountOperator(
            @NotNull final MatchPair pair,
            @NotNull final String[] affectingColumns,
            @Nullable final String timestampColumnName,
            final long reverseWindowScaleUnits,
            final long forwardWindowScaleUnits,
            AggCountType countType) {
        super(pair, affectingColumns, timestampColumnName, reverseWindowScaleUnits, forwardWindowScaleUnits, true);
        this.countType = countType;
        countFunction = AggCountType.getDoubleCountFunction(countType);
    }

    @Override
    public UpdateByOperator copy() {
        return new DoubleRollingCountOperator(pair,
                affectingColumns,
                timestampColumnName,
                reverseWindowScaleUnits,
                forwardWindowScaleUnits,
                countType);
    }
}
