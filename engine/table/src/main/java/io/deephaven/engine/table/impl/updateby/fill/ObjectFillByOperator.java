/*
 * ---------------------------------------------------------------------------------------------------------------------
 * AUTO-GENERATED CLASS - DO NOT EDIT MANUALLY - for any changes edit CharFillByOperator and regenerate
 * ---------------------------------------------------------------------------------------------------------------------
 */
package io.deephaven.engine.table.impl.updateby.fill;

import io.deephaven.engine.table.impl.util.ChunkUtils;

import io.deephaven.base.verify.Assert;
import io.deephaven.chunk.ObjectChunk;
import io.deephaven.chunk.Chunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.table.MatchPair;
import io.deephaven.engine.table.impl.updateby.internal.BaseObjectUpdateByOperator;
import io.deephaven.engine.table.impl.util.RowRedirection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ObjectFillByOperator<T> extends BaseObjectUpdateByOperator<T> {
    // region extra-fields
    // endregion extra-fields

    protected class Context extends BaseObjectUpdateByOperator<T>.Context {
        public ObjectChunk<T, ? extends Values> ObjectValueChunk;

        protected Context(final int chunkSize, final int chunkCount) {
            super(chunkSize, chunkCount);
        }

        @Override
        public void setValuesChunk(@NotNull final Chunk<? extends Values> valuesChunk) {
            ObjectValueChunk = valuesChunk.asObjectChunk();
        }

        @Override
        public void push(long key, int pos, int count) {
            Assert.eq(count, "push count", 1);

            T currentVal = ObjectValueChunk.get(pos);
            if(currentVal != null) {
                curVal = currentVal;
            }
        }
    }

    public ObjectFillByOperator(@NotNull final MatchPair fillPair,
                              @Nullable final RowRedirection rowRedirection
                              // region extra-constructor-args
                                      , final Class<T> colType
                              // endregion extra-constructor-args
                              ) {
        super(fillPair, new String[] { fillPair.rightColumn }, rowRedirection, colType);
        // region constructor
        // endregion constructor
    }

    @NotNull
    @Override
    public UpdateContext makeUpdateContext(final int chunkSize, final int chunkCount) {
        return new Context(chunkSize, chunkCount);
    }

    // region extra-methods
    // endregion extra-methods
}
