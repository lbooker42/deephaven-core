//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
// ****** AUTO-GENERATED CLASS - DO NOT EDIT MANUALLY
// ****** Edit CharBarrageCopyKernel and run "./gradlew replicateBarrageUtils" to regenerate
//
// @formatter:off
package io.deephaven.extensions.barrage.chunk;

import io.deephaven.chunk.WritableFloatChunk;
import io.deephaven.chunk.WritableChunk;
import io.deephaven.chunk.attributes.Values;

public class FloatBarrageCopyKernel {

    public static void copyFromDeltaChunks(
            final long[] mapping,
            final WritableFloatChunk<Values> dest,
            final WritableChunk<Values>[][] addChunks,
            final WritableChunk<Values>[][] modChunks,
            final int deltaChunkSize) {
        for (int pos = 0; pos < mapping.length; ++pos) {
            final long encoded = mapping[pos];
            final boolean fromMods = (encoded & (1L << BarrageCopyKernel.DELTA_MOD_FLAG_BIT)) != 0;
            final int deltaIdx =
                    (int) ((encoded >>> BarrageCopyKernel.DELTA_INDEX_SHIFT) & BarrageCopyKernel.DELTA_INDEX_MASK);
            final long srcPos = encoded & BarrageCopyKernel.DELTA_POSITION_MASK;

            final WritableChunk<Values>[] srcChunks = fromMods ? modChunks[deltaIdx] : addChunks[deltaIdx];
            final int srcChunkIdx = (int) (srcPos / deltaChunkSize);
            final int srcOff = (int) (srcPos % deltaChunkSize);
            dest.set(pos, srcChunks[srcChunkIdx].asFloatChunk().get(srcOff));
        }
    }

    private static class FloatBarrageCopyKernelContext implements BarrageCopyKernel {
        @Override
        public void copyFromDeltaChunks(
                final long[] mapping,
                final WritableChunk<Values> dest,
                final WritableChunk<Values>[][] addChunks,
                final WritableChunk<Values>[][] modChunks,
                final int deltaChunkSize) {
            FloatBarrageCopyKernel.copyFromDeltaChunks(
                    mapping, dest.asWritableFloatChunk(), addChunks, modChunks, deltaChunkSize);
        }
    }

    public static final BarrageCopyKernel INSTANCE = new FloatBarrageCopyKernelContext();
}
