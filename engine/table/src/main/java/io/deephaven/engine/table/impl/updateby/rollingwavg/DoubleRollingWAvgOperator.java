//
// Copyright (c) 2016-2024 Deephaven Data Labs and Patent Pending
//
// ****** AUTO-GENERATED CLASS - DO NOT EDIT MANUALLY
// ****** Edit CharRollingWAvgOperator and run "./gradlew replicateUpdateBy" to regenerate
//
// @formatter:off
package io.deephaven.engine.table.impl.updateby.rollingwavg;

import io.deephaven.chunk.DoubleChunk;
import io.deephaven.chunk.Chunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.table.impl.MatchPair;
import io.deephaven.engine.table.impl.updateby.UpdateByOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

import static io.deephaven.util.QueryConstants.NULL_DOUBLE;
import static io.deephaven.util.QueryConstants.NULL_DOUBLE;

public class DoubleRollingWAvgOperator extends BasePrimitiveRollingWAvgOperator {
    // region extra-fields
    // endregion extra-fields

    protected class Context extends BasePrimitiveRollingWAvgOperator.Context {
        protected DoubleChunk<? extends Values> influencerValuesChunk;

        protected Context(int affectedChunkSize, int influencerChunkSize) {
            super(affectedChunkSize, influencerChunkSize);
        }

        @Override
        public void setValueChunks(@NotNull final Chunk<? extends Values>[] valueChunks) {
            super.setValueChunks(valueChunks);
            influencerValuesChunk = valueChunks[0].asDoubleChunk();
        }

        @Override
        public void push(int pos, int count) {
            windowValues.ensureRemaining(count);
            windowWeightValues.ensureRemaining(count);

            for (int ii = 0; ii < count; ii++) {
                final double val = influencerValuesChunk.get(pos + ii);
                final double weight = influencerWeightValuesChunk.get(pos + ii);

                if (val == NULL_DOUBLE || weight == NULL_DOUBLE) {
                    windowValues.addUnsafe(NULL_DOUBLE);
                    windowWeightValues.addUnsafe(NULL_DOUBLE);
                    nullCount++;
                } else {
                    // Compute the product and add to the agg buffer.
                    final double weightedVal = weight * val;
                    windowValues.addUnsafe(weightedVal);
                    windowWeightValues.addUnsafe(weight);
                }
            }
        }
    }

    @NotNull
    @Override
    public UpdateByOperator.Context makeUpdateContext(final int affectedChunkSize, final int influencerChunkSize) {
        return new Context(affectedChunkSize, influencerChunkSize);
    }

    public DoubleRollingWAvgOperator(
            @NotNull final MatchPair pair,
            @NotNull final Collection<String> affectingColumns,
            @Nullable final String timestampColumnName,
            final long reverseWindowScaleUnits,
            final long forwardWindowScaleUnits,
            @NotNull final String weightColumnName
    // region extra-constructor-args
    // endregion extra-constructor-args
    ) {
        super(pair, affectingColumns, timestampColumnName, reverseWindowScaleUnits, forwardWindowScaleUnits,
                weightColumnName);
        // region constructor
        // endregion constructor
    }

    @Override
    public UpdateByOperator copy() {
        return new DoubleRollingWAvgOperator(
                pair,
                affectingColumns,
                timestampColumnName,
                reverseWindowScaleUnits,
                forwardWindowScaleUnits,
                weightColumnName
        // region extra-copy-args
        // endregion extra-copy-args
        );
    }

    /**
     * Get the names of the input column(s) for this operator.
     *
     * @return the names of the input column
     */
    @NotNull
    @Override
    protected String[] getInputColumnNames() {
        return new String[] {pair.rightColumn, weightColumnName};
    }
}
