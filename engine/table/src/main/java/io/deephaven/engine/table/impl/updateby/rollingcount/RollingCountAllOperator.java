//
// Copyright (c) 2016-2024 Deephaven Data Labs and Patent Pending
//
package io.deephaven.engine.table.impl.updateby.rollingcount;

import io.deephaven.engine.table.impl.updateby.UpdateByOperator;
import io.deephaven.engine.table.impl.updateby.internal.BaseLongUpdateByOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Rolling count operator that counts all values in the window, including nulls.
 */
public class RollingCountAllOperator extends BaseLongUpdateByOperator {
    protected class Context extends BaseLongUpdateByOperator.Context {
        @SuppressWarnings("unused")
        protected Context(final int affectedChunkSize, final int influencerChunkSize) {
            super(affectedChunkSize);
        }

        @Override
        public void close() {
            super.close();
        }

        @Override
        public void push(int pos, int count) {
            curVal += count;
        }

        @Override
        public void pop(int count) {
            curVal -= count;
        }
    }

    @NotNull
    @Override
    public UpdateByOperator.Context makeUpdateContext(final int affectedChunkSize, final int influencerChunkSize) {
        return new Context(affectedChunkSize, influencerChunkSize);
    }

    public RollingCountAllOperator(
            @Nullable final String timestampColumnName,
            final long reverseWindowScaleUnits,
            final long forwardWindowScaleUnits) {
        super(null, new String[0], timestampColumnName, reverseWindowScaleUnits, forwardWindowScaleUnits, true);
    }

    @Override
    public UpdateByOperator copy() {
        return new RollingCountAllOperator(
                timestampColumnName,
                reverseWindowScaleUnits,
                forwardWindowScaleUnits);
    }
}
