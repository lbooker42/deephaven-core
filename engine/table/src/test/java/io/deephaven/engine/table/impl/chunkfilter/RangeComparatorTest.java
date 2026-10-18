//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
package io.deephaven.engine.table.impl.chunkfilter;

import io.deephaven.chunk.ByteChunk;
import io.deephaven.chunk.CharChunk;
import io.deephaven.chunk.Chunk;
import io.deephaven.chunk.DoubleChunk;
import io.deephaven.chunk.FloatChunk;
import io.deephaven.chunk.IntChunk;
import io.deephaven.chunk.LongChunk;
import io.deephaven.chunk.ShortChunk;
import io.deephaven.chunk.WritableBooleanChunk;
import io.deephaven.chunk.WritableLongChunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.rowset.chunkattributes.OrderedRowKeys;
import io.deephaven.util.QueryConstants;
import io.deephaven.util.compare.ByteComparisons;
import io.deephaven.util.compare.CharComparisons;
import io.deephaven.util.compare.DoubleComparisons;
import io.deephaven.util.compare.FloatComparisons;
import io.deephaven.util.compare.IntComparisons;
import io.deephaven.util.compare.LongComparisons;
import io.deephaven.util.compare.ShortComparisons;
import org.junit.Test;

import java.util.SplittableRandom;

import static org.junit.Assert.fail;

/**
 * Every range comparator passes exactly the values that its type's {@code *Comparisons} class orders between the
 * bounds, through each of its three loops, for every combination of inclusive and exclusive bounds, with the bounds in
 * either order. The filters do not call those classes on most ranges, so this is what holds them to the same order:
 * nulls first, NaN last, -0.0 equal to 0.0.
 * <p>
 * byte is checked for every pair of bounds and every value. The other types are checked for bounds and values drawn
 * from their edge cases, the values next to them, and random values.
 */
public class RangeComparatorTest {
    private static final boolean[] BOOLEANS = {true, false};
    private static final int RANDOM_BOUNDS = 12;
    private static final int RANDOM_VALUES = 200;

    private interface Factory {
        ChunkFilter make(int lowerIndex, int upperIndex, boolean lowerInclusive, boolean upperInclusive);
    }

    private interface Order {
        boolean passes(int valueIndex, int lowerIndex, int upperIndex, boolean lowerInclusive, boolean upperInclusive);
    }

    @Test
    public void testByte() {
        final byte[] values = new byte[1 << Byte.SIZE];
        for (int ii = 0; ii < values.length; ++ii) {
            values[ii] = (byte) (Byte.MIN_VALUE + ii);
        }
        checkAll("byte", text(values), values.length, ByteChunk.chunkWrap(values),
                (lo, up, li, ui) -> ByteRangeComparator.makeByteFilter(values[lo], values[up], li, ui),
                (vv, lo, up, li, ui) -> (li ? ByteComparisons.geq(values[vv], values[lo])
                        : ByteComparisons.gt(values[vv], values[lo]))
                        && (ui ? ByteComparisons.leq(values[vv], values[up])
                                : ByteComparisons.lt(values[vv], values[up])));
    }

    @Test
    public void testShort() {
        final SplittableRandom random = new SplittableRandom(0x73686F7274L);
        final short[] edges = {QueryConstants.NULL_SHORT, QueryConstants.MIN_SHORT, QueryConstants.MIN_SHORT + 1, -2,
                -1, 0, 1, 2, QueryConstants.MAX_SHORT - 1, QueryConstants.MAX_SHORT};
        final short[] bounds = withRandom(edges, RANDOM_BOUNDS, random);
        final short[] values = withRandom(edges, RANDOM_VALUES, random);
        checkAll("short", text(bounds), values.length, ShortChunk.chunkWrap(values),
                (lo, up, li, ui) -> ShortRangeComparator.makeShortFilter(bounds[lo], bounds[up], li, ui),
                (vv, lo, up, li, ui) -> (li ? ShortComparisons.geq(values[vv], bounds[lo])
                        : ShortComparisons.gt(values[vv], bounds[lo]))
                        && (ui ? ShortComparisons.leq(values[vv], bounds[up])
                                : ShortComparisons.lt(values[vv], bounds[up])));
    }

    @Test
    public void testChar() {
        final SplittableRandom random = new SplittableRandom(0x63686172L);
        final char[] edges = {QueryConstants.NULL_CHAR, QueryConstants.MIN_CHAR, QueryConstants.MIN_CHAR + 1, 2,
                0x7FFE, 0x7FFF, 0x8000, 0xFFFC, QueryConstants.MAX_CHAR - 1, QueryConstants.MAX_CHAR};
        final char[] bounds = withRandom(edges, RANDOM_BOUNDS, random);
        final char[] values = withRandom(edges, RANDOM_VALUES, random);
        checkAll("char", text(bounds), values.length, CharChunk.chunkWrap(values),
                (lo, up, li, ui) -> CharRangeComparator.makeCharFilter(bounds[lo], bounds[up], li, ui),
                (vv, lo, up, li, ui) -> (li ? CharComparisons.geq(values[vv], bounds[lo])
                        : CharComparisons.gt(values[vv], bounds[lo]))
                        && (ui ? CharComparisons.leq(values[vv], bounds[up])
                                : CharComparisons.lt(values[vv], bounds[up])));
    }

    @Test
    public void testInt() {
        final SplittableRandom random = new SplittableRandom(0x696E74L);
        final int[] edges = {QueryConstants.NULL_INT, QueryConstants.MIN_INT, QueryConstants.MIN_INT + 1,
                -(1 << 30), -2, -1, 0, 1, 2, 1 << 30, QueryConstants.MAX_INT - 1, QueryConstants.MAX_INT};
        final int[] bounds = withRandom(edges, RANDOM_BOUNDS, random);
        final int[] values = withRandom(edges, RANDOM_VALUES, random);
        checkAll("int", text(bounds), values.length, IntChunk.chunkWrap(values),
                (lo, up, li, ui) -> IntRangeComparator.makeIntFilter(bounds[lo], bounds[up], li, ui),
                (vv, lo, up, li, ui) -> (li ? IntComparisons.geq(values[vv], bounds[lo])
                        : IntComparisons.gt(values[vv], bounds[lo]))
                        && (ui ? IntComparisons.leq(values[vv], bounds[up])
                                : IntComparisons.lt(values[vv], bounds[up])));
    }

    @Test
    public void testLong() {
        final SplittableRandom random = new SplittableRandom(0x6C6F6E67L);
        final long[] edges = {QueryConstants.NULL_LONG, QueryConstants.MIN_LONG, QueryConstants.MIN_LONG + 1,
                -(1L << 62), -2, -1, 0, 1, 2, 1L << 62, QueryConstants.MAX_LONG - 1, QueryConstants.MAX_LONG};
        final long[] bounds = withRandom(edges, RANDOM_BOUNDS, random);
        final long[] values = withRandom(edges, RANDOM_VALUES, random);
        checkAll("long", text(bounds), values.length, LongChunk.chunkWrap(values),
                (lo, up, li, ui) -> LongRangeComparator.makeLongFilter(bounds[lo], bounds[up], li, ui),
                (vv, lo, up, li, ui) -> (li ? LongComparisons.geq(values[vv], bounds[lo])
                        : LongComparisons.gt(values[vv], bounds[lo]))
                        && (ui ? LongComparisons.leq(values[vv], bounds[up])
                                : LongComparisons.lt(values[vv], bounds[up])));
    }

    @Test
    public void testFloat() {
        final SplittableRandom random = new SplittableRandom(0x666C6F6174L);
        final float[] edges = {QueryConstants.NULL_FLOAT, Float.NEGATIVE_INFINITY,
                Math.nextUp(QueryConstants.NULL_FLOAT), -1, -Float.MIN_VALUE, -0.0f, 0.0f, Float.MIN_VALUE, 1,
                Math.nextDown(Float.MAX_VALUE), Float.MAX_VALUE, Float.POSITIVE_INFINITY, Float.NaN,
                // NaNs with other bits, which compare as NaN does
                Float.intBitsToFloat(0x7FC00001), Float.intBitsToFloat(0xFFC00000)};
        final float[] bounds = new float[edges.length + RANDOM_BOUNDS];
        final float[] values = new float[edges.length + RANDOM_VALUES];
        System.arraycopy(edges, 0, bounds, 0, edges.length);
        System.arraycopy(edges, 0, values, 0, edges.length);
        for (int ii = edges.length; ii < bounds.length; ++ii) {
            bounds[ii] = (float) (4 * random.nextDouble() - 2);
        }
        for (int ii = edges.length; ii < values.length; ++ii) {
            values[ii] = (float) (4 * random.nextDouble() - 2);
        }
        checkAll("float", text(bounds), values.length, FloatChunk.chunkWrap(values),
                (lo, up, li, ui) -> FloatRangeComparator.makeFloatFilter(bounds[lo], bounds[up], li, ui),
                (vv, lo, up, li, ui) -> (li ? FloatComparisons.geq(values[vv], bounds[lo])
                        : FloatComparisons.gt(values[vv], bounds[lo]))
                        && (ui ? FloatComparisons.leq(values[vv], bounds[up])
                                : FloatComparisons.lt(values[vv], bounds[up])));
    }

    @Test
    public void testDouble() {
        final SplittableRandom random = new SplittableRandom(0x646F75626C65L);
        final double[] edges = {QueryConstants.NULL_DOUBLE, Double.NEGATIVE_INFINITY,
                Math.nextUp(QueryConstants.NULL_DOUBLE), -1, -Double.MIN_VALUE, -0.0d, 0.0d, Double.MIN_VALUE, 1,
                Math.nextDown(Double.MAX_VALUE), Double.MAX_VALUE, Double.POSITIVE_INFINITY, Double.NaN,
                // NaNs with other bits, which compare as NaN does
                Double.longBitsToDouble(0x7FF8000000000001L), Double.longBitsToDouble(0xFFF8000000000000L)};
        final double[] bounds = new double[edges.length + RANDOM_BOUNDS];
        final double[] values = new double[edges.length + RANDOM_VALUES];
        System.arraycopy(edges, 0, bounds, 0, edges.length);
        System.arraycopy(edges, 0, values, 0, edges.length);
        for (int ii = edges.length; ii < bounds.length; ++ii) {
            bounds[ii] = 4 * random.nextDouble() - 2;
        }
        for (int ii = edges.length; ii < values.length; ++ii) {
            values[ii] = 4 * random.nextDouble() - 2;
        }
        checkAll("double", text(bounds), values.length, DoubleChunk.chunkWrap(values),
                (lo, up, li, ui) -> DoubleRangeComparator.makeDoubleFilter(bounds[lo], bounds[up], li, ui),
                (vv, lo, up, li, ui) -> (li ? DoubleComparisons.geq(values[vv], bounds[lo])
                        : DoubleComparisons.gt(values[vv], bounds[lo]))
                        && (ui ? DoubleComparisons.leq(values[vv], bounds[up])
                                : DoubleComparisons.lt(values[vv], bounds[up])));
    }

    /**
     * Check the filter for every pair of bounds and inclusions against {@code order}, over every value.
     */
    private static void checkAll(
            final String type,
            final String[] boundText,
            final int valueCount,
            final Chunk<Values> chunk,
            final Factory factory,
            final Order order) {
        final int boundCount = boundText.length;
        final boolean[] expected = new boolean[valueCount];
        try (final WritableLongChunk<OrderedRowKeys> rowKeys = WritableLongChunk.makeWritableChunk(valueCount);
                final WritableLongChunk<OrderedRowKeys> passed = WritableLongChunk.makeWritableChunk(valueCount);
                final WritableBooleanChunk<Values> results = WritableBooleanChunk.makeWritableChunk(valueCount)) {
            for (int ii = 0; ii < valueCount; ++ii) {
                rowKeys.set(ii, ii);
            }
            for (int lo = 0; lo < boundCount; ++lo) {
                for (int up = 0; up < boundCount; ++up) {
                    for (final boolean li : BOOLEANS) {
                        for (final boolean ui : BOOLEANS) {
                            for (int vv = 0; vv < valueCount; ++vv) {
                                expected[vv] = order.passes(vv, lo, up, li, ui);
                            }
                            final ChunkFilter filter = factory.make(lo, up, li, ui);
                            final String lower = boundText[lo];
                            final String upper = boundText[up];
                            checkLoops(filter, chunk, rowKeys, passed, results, expected,
                                    () -> type + " from " + lower + (li ? " inclusive" : " exclusive") + " to "
                                            + upper + (ui ? " inclusive" : " exclusive"));
                        }
                    }
                }
            }
        }
    }

    private interface Context {
        String describe();
    }

    /**
     * Check the filter's three loops against {@code expected}, building a message only on a failure, as there are a
     * great many checks.
     */
    private static void checkLoops(
            final ChunkFilter filter,
            final Chunk<Values> chunk,
            final WritableLongChunk<OrderedRowKeys> rowKeys,
            final WritableLongChunk<OrderedRowKeys> passed,
            final WritableBooleanChunk<Values> results,
            final boolean[] expected,
            final Context context) {
        final int size = expected.length;

        filter.filter(chunk, rowKeys, passed);
        int expectedCount = 0;
        for (int ii = 0; ii < size; ++ii) {
            if (expected[ii]) {
                if (expectedCount >= passed.size() || passed.get(expectedCount) != ii) {
                    fail(context.describe() + ": row keys loop did not pass value " + ii);
                }
                ++expectedCount;
            }
        }
        if (expectedCount != passed.size()) {
            fail(context.describe() + ": row keys loop passed " + passed.size() + " values, expected "
                    + expectedCount);
        }

        if (filter.filter(chunk, results) != expectedCount) {
            fail(context.describe() + ": boolean loop returned the wrong count");
        }
        for (int ii = 0; ii < size; ++ii) {
            if (results.get(ii) != expected[ii]) {
                fail(context.describe() + ": boolean loop wrong for value " + ii);
            }
        }

        // filterAnd only changes the results that are still true
        int expectedAndCount = 0;
        for (int ii = 0; ii < size; ++ii) {
            results.set(ii, ii % 3 != 0);
            expectedAndCount += ii % 3 != 0 && expected[ii] ? 1 : 0;
        }
        if (filter.filterAnd(chunk, results) != expectedAndCount) {
            fail(context.describe() + ": filterAnd loop returned the wrong count");
        }
        for (int ii = 0; ii < size; ++ii) {
            if (results.get(ii) != (ii % 3 != 0 && expected[ii])) {
                fail(context.describe() + ": filterAnd loop wrong for value " + ii);
            }
        }
    }

    private static String[] text(final byte[] bounds) {
        final String[] result = new String[bounds.length];
        for (int ii = 0; ii < bounds.length; ++ii) {
            result[ii] = Byte.toString(bounds[ii]);
        }
        return result;
    }

    private static String[] text(final short[] bounds) {
        final String[] result = new String[bounds.length];
        for (int ii = 0; ii < bounds.length; ++ii) {
            result[ii] = Short.toString(bounds[ii]);
        }
        return result;
    }

    private static String[] text(final char[] bounds) {
        final String[] result = new String[bounds.length];
        for (int ii = 0; ii < bounds.length; ++ii) {
            result[ii] = "0x" + Integer.toHexString(bounds[ii]);
        }
        return result;
    }

    private static String[] text(final int[] bounds) {
        final String[] result = new String[bounds.length];
        for (int ii = 0; ii < bounds.length; ++ii) {
            result[ii] = Integer.toString(bounds[ii]);
        }
        return result;
    }

    private static String[] text(final long[] bounds) {
        final String[] result = new String[bounds.length];
        for (int ii = 0; ii < bounds.length; ++ii) {
            result[ii] = Long.toString(bounds[ii]);
        }
        return result;
    }

    private static String[] text(final float[] bounds) {
        final String[] result = new String[bounds.length];
        for (int ii = 0; ii < bounds.length; ++ii) {
            result[ii] = Float.toString(bounds[ii]) + " (bits "
                    + Integer.toHexString(Float.floatToRawIntBits(bounds[ii])) + ")";
        }
        return result;
    }

    private static String[] text(final double[] bounds) {
        final String[] result = new String[bounds.length];
        for (int ii = 0; ii < bounds.length; ++ii) {
            result[ii] = Double.toString(bounds[ii]) + " (bits "
                    + Long.toHexString(Double.doubleToRawLongBits(bounds[ii])) + ")";
        }
        return result;
    }

    private static short[] withRandom(final short[] edges, final int randomCount, final SplittableRandom random) {
        final short[] result = new short[edges.length + randomCount];
        System.arraycopy(edges, 0, result, 0, edges.length);
        for (int ii = edges.length; ii < result.length; ++ii) {
            result[ii] = (short) random.nextInt();
        }
        return result;
    }

    private static char[] withRandom(final char[] edges, final int randomCount, final SplittableRandom random) {
        final char[] result = new char[edges.length + randomCount];
        System.arraycopy(edges, 0, result, 0, edges.length);
        for (int ii = edges.length; ii < result.length; ++ii) {
            result[ii] = (char) random.nextInt();
        }
        return result;
    }

    private static int[] withRandom(final int[] edges, final int randomCount, final SplittableRandom random) {
        final int[] result = new int[edges.length + randomCount];
        System.arraycopy(edges, 0, result, 0, edges.length);
        for (int ii = edges.length; ii < result.length; ++ii) {
            result[ii] = random.nextInt();
        }
        return result;
    }

    private static long[] withRandom(final long[] edges, final int randomCount, final SplittableRandom random) {
        final long[] result = new long[edges.length + randomCount];
        System.arraycopy(edges, 0, result, 0, edges.length);
        for (int ii = edges.length; ii < result.length; ++ii) {
            result[ii] = random.nextLong();
        }
        return result;
    }
}
