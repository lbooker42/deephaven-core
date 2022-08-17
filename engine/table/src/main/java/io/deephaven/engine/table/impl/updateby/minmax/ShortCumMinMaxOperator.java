package io.deephaven.engine.table.impl.updateby.minmax;

import io.deephaven.chunk.*;
import io.deephaven.chunk.attributes.ChunkLengths;
import io.deephaven.chunk.attributes.ChunkPositions;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.rowset.RowSequence;
import io.deephaven.engine.rowset.chunkattributes.RowKeys;
import io.deephaven.engine.table.MatchPair;
import io.deephaven.engine.table.impl.UpdateBy;
import io.deephaven.engine.table.impl.updateby.internal.BaseShortUpdateByOperator;
import io.deephaven.engine.table.impl.util.RowRedirection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static io.deephaven.util.QueryConstants.NULL_SHORT;

public class ShortCumMinMaxOperator extends BaseShortUpdateByOperator {
    private final boolean isMax;

    // region extra-fields
    // endregion extra-fields

    public ShortCumMinMaxOperator(@NotNull final MatchPair inputPair,
                                  final boolean isMax,
                                  @NotNull final UpdateBy.UpdateByRedirectionContext redirContext
                                  // region extra-constructor-args
                                  // endregion extra-constructor-args
                                  ) {
        super(inputPair, new String[] { inputPair.rightColumn }, redirContext);
        this.isMax = isMax;
        // region constructor
        // endregion constructor
    }

    // region extra-methods
    // endregion extra-methods

    @Override
    protected void doAddChunk(@NotNull final Context ctx,
                              @NotNull final RowSequence inputKeys,
                              @NotNull final Chunk<Values> workingChunk) {
        accumulate(workingChunk.asShortChunk(), ctx, 0, workingChunk.size());
        outputSource.fillFromChunk(ctx.fillContext.get(), ctx.outputValues.get(), inputKeys);
    }

    private void accumulate(@NotNull final ShortChunk<Values> asShorts,
                            @NotNull final Context ctx,
                            final int runStart,
                            final int runLength) {
        final WritableShortChunk<Values> localOutputValues = ctx.outputValues.get();
        for (int ii = runStart; ii < runStart + runLength; ii++) {
            final short currentVal = asShorts.get(ii);
            if(ctx.curVal == NULL_SHORT) {
                ctx.curVal = currentVal;
            } else if(currentVal != NULL_SHORT) {
                if((isMax && currentVal > ctx.curVal) ||
                   (!isMax && currentVal < ctx.curVal)  ) {
                    ctx.curVal = currentVal;
                }
            }
            localOutputValues.set(ii, ctx.curVal);
        }
    }
}
