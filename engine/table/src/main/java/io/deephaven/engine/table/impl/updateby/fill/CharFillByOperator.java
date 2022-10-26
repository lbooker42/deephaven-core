package io.deephaven.engine.table.impl.updateby.fill;

import io.deephaven.chunk.CharChunk;
import io.deephaven.chunk.Chunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.MatchPair;
import io.deephaven.engine.table.impl.UpdateBy;
import io.deephaven.engine.table.impl.updateby.internal.BaseCharUpdateByOperator;
import org.jetbrains.annotations.NotNull;

import static io.deephaven.util.QueryConstants.NULL_CHAR;

public class CharFillByOperator extends BaseCharUpdateByOperator {
    // region extra-fields
    // endregion extra-fields

    protected class Context extends BaseCharUpdateByOperator.Context {
        public CharChunk<? extends Values> charValueChunk;

        protected Context(int chunkSize) {
            super(chunkSize);
        }

        @Override
        public void setValuesChunk(@NotNull final Chunk<? extends Values> valuesChunk) {
            charValueChunk = valuesChunk.asCharChunk();
        }

        @Override
        public void push(long key, int pos) {
            char currentVal = charValueChunk.get(pos);
            if(currentVal != NULL_CHAR) {
                curVal = currentVal;
            }
        }

        @Override
        public void reset() {
            curVal = NULL_CHAR;
        }
    }

    public CharFillByOperator(@NotNull final MatchPair fillPair,
                              @NotNull final UpdateBy.UpdateByRedirectionContext redirContext
                              // region extra-constructor-args
                              // endregion extra-constructor-args
                              ) {
        super(fillPair, new String[] { fillPair.rightColumn }, redirContext);
        // region constructor
        // endregion constructor
    }

    @NotNull
    @Override
    public UpdateContext makeUpdateContext(int chunkSize) {
        return new Context(chunkSize);
    }

    // region extra-methods
    // endregion extra-methods
}
