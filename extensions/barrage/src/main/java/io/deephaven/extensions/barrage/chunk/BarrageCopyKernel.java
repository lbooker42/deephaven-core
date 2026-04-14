//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
package io.deephaven.extensions.barrage.chunk;

import io.deephaven.chunk.ChunkType;
import io.deephaven.chunk.WritableChunk;
import io.deephaven.chunk.attributes.Values;

/**
 * Typed kernel for copying individual cells from per-delta source chunks into a destination chunk, using a pre-computed
 * encoded mapping array. Each mapping entry encodes (deltaIndex, add-or-mod flag, position) in a single long using the
 * bit constants defined here.
 *
 * <p>
 * Using a typed kernel avoids the virtual dispatch and {@code System.arraycopy} overhead of calling
 * {@code WritableChunk.copyFromChunk(src, offset, pos, 1)} for each cell.
 */
public interface BarrageCopyKernel {

    // We use bit encoding for delta chunk mapping arrays to avoid binary search or iteration to determine source
    // chunks. The following are the bit mapping for the values (stored as long).
    // bits 0-39 = position within that delta's chunk list (up to 1,099,511,627,776 unique positions).
    long DELTA_POSITION_MASK = 0xFFFFFFFFFFL;
    // bits 40-61 = actual delta index into pendingDeltas (up to 4,194,304 unique deltas per update)
    int DELTA_INDEX_SHIFT = 40;
    long DELTA_INDEX_MASK = 0x3FFFFFL;
    // bit 62 = 0 for addChunks or 1 for modChunks
    int DELTA_MOD_FLAG_BIT = 62;

    static BarrageCopyKernel makeBarrageCopyKernel(final ChunkType chunkType) {
        switch (chunkType) {
            case Char:
                return CharBarrageCopyKernel.INSTANCE;
            case Byte:
                return ByteBarrageCopyKernel.INSTANCE;
            case Short:
                return ShortBarrageCopyKernel.INSTANCE;
            case Int:
                return IntBarrageCopyKernel.INSTANCE;
            case Long:
                return LongBarrageCopyKernel.INSTANCE;
            case Float:
                return FloatBarrageCopyKernel.INSTANCE;
            case Double:
                return DoubleBarrageCopyKernel.INSTANCE;
            default:
                return ObjectBarrageCopyKernel.INSTANCE;
        }
    }

    /**
     * Copy cells from per-delta source chunk arrays into a destination chunk.
     *
     * @param mapping encoded source references, one per output position
     * @param dest the output chunk to fill (sized to {@code mapping.length})
     * @param addChunks per-delta add chunk arrays: {@code addChunks[deltaIdx]} holds the chunks for one column from
     *        delta {@code deltaIdx}
     * @param modChunks per-delta mod chunk arrays: {@code modChunks[deltaIdx]} holds the chunks for one column from
     *        delta {@code deltaIdx}
     * @param deltaChunkSize the maximum number of rows per source chunk
     */
    void copyFromDeltaChunks(
            long[] mapping,
            WritableChunk<Values> dest,
            WritableChunk<Values>[][] addChunks,
            WritableChunk<Values>[][] modChunks,
            int deltaChunkSize);
}
