//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
package io.deephaven.engine.table.impl.chunkfilter;

import io.deephaven.chunk.WritableChunk;
import io.deephaven.chunk.WritableDoubleChunk;
import io.deephaven.chunk.WritableFloatChunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.table.MatchOptions;
import io.deephaven.util.QueryConstants;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * Checks the float and double match filters against IEEE 754 equality, for every count at which the factory changes
 * structure: −0.0 matches 0.0, NaN matches nothing unless {@code nanMatch} is set and NaN is a value, and then every
 * NaN bit pattern matches.
 */
public class FloatingPointChunkMatchFilterFactoryTest {

    private static final int[] COUNTS = {1, 2, 3, 4, 5, 6, 7, 8, 47, 48, 49, 100, 1000};

    private static final double NON_CANONICAL_NAN = Double.longBitsToDouble(0x7ff8000000000001L);
    private static final float NON_CANONICAL_FLOAT_NAN = Float.intBitsToFloat(0x7fc00001);

    @Test
    public void testMatchesIeeeEquality() {
        final Random random = new Random(0x666c6f6174L);
        for (final boolean isFloat : new boolean[] {true, false}) {
            for (final int count : COUNTS) {
                for (final double[] values : valueSets(count, random, isFloat)) {
                    final double[] rows = rows(values, random, isFloat);
                    for (final boolean nanMatch : new boolean[] {false, true}) {
                        for (final boolean inverted : new boolean[] {false, true}) {
                            check(isFloat, values, rows, nanMatch, inverted);
                        }
                    }
                }
            }
        }
    }

    private static double round(final double value, final boolean isFloat) {
        return isFloat ? (double) (float) value : value;
    }

    private static List<double[]> valueSets(final int count, final Random random, final boolean isFloat) {
        final List<double[]> sets = new ArrayList<>();
        final double[] plain = new double[count];
        for (int ii = 0; ii < count; ++ii) {
            plain[ii] = round(random.nextGaussian() * 1000, isFloat);
        }
        sets.add(plain);
        final double[] integral = new double[count];
        for (int ii = 0; ii < count; ++ii) {
            integral[ii] = ii;
        }
        sets.add(integral);
        if (count >= 2) {
            final double[] special = plain.clone();
            special[0] = -0.0;
            special[1] = isFloat ? Float.NaN : Double.NaN;
            if (count >= 3) {
                special[2] = isFloat ? QueryConstants.NULL_FLOAT : QueryConstants.NULL_DOUBLE;
            }
            sets.add(special);
            final double[] repeated = plain.clone();
            repeated[count - 1] = repeated[0];
            sets.add(repeated);
        }
        return sets;
    }

    private static double[] rows(final double[] values, final Random random, final boolean isFloat) {
        final List<Double> rows = new ArrayList<>();
        for (final double value : values) {
            rows.add(value);
            rows.add(round(value + 0.5, isFloat));
        }
        rows.add(0.0);
        rows.add(-0.0);
        rows.add(isFloat ? Float.NaN : Double.NaN);
        rows.add(isFloat ? (double) NON_CANONICAL_FLOAT_NAN : NON_CANONICAL_NAN);
        rows.add(Double.POSITIVE_INFINITY);
        rows.add(Double.NEGATIVE_INFINITY);
        rows.add(isFloat ? QueryConstants.NULL_FLOAT : QueryConstants.NULL_DOUBLE);
        for (int ii = 0; ii < 200; ++ii) {
            rows.add(round(random.nextGaussian() * 1000, isFloat));
        }
        return rows.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private static void check(
            final boolean isFloat,
            final double[] values,
            final double[] rows,
            final boolean nanMatch,
            final boolean inverted) {
        final MatchOptions options = MatchOptions.builder().nanMatch(nanMatch).inverted(inverted).build();
        final ChunkFilter filter;
        final WritableChunk<Values> chunk;
        if (isFloat) {
            final float[] floats = new float[values.length];
            for (int ii = 0; ii < values.length; ++ii) {
                floats[ii] = (float) values[ii];
            }
            filter = FloatChunkMatchFilterFactory.makeFilter(options, floats);
            final WritableFloatChunk<Values> floatChunk = WritableFloatChunk.makeWritableChunk(rows.length);
            for (int ii = 0; ii < rows.length; ++ii) {
                floatChunk.set(ii, (float) rows[ii]);
            }
            chunk = floatChunk;
        } else {
            filter = DoubleChunkMatchFilterFactory.makeFilter(options, values);
            final WritableDoubleChunk<Values> doubleChunk = WritableDoubleChunk.makeWritableChunk(rows.length);
            for (int ii = 0; ii < rows.length; ++ii) {
                doubleChunk.set(ii, rows[ii]);
            }
            chunk = doubleChunk;
        }
        boolean nanIsValue = false;
        for (final double value : values) {
            nanIsValue |= Double.isNaN(value);
        }
        final boolean[] expected = new boolean[rows.length];
        for (int ii = 0; ii < rows.length; ++ii) {
            boolean matched = nanMatch && nanIsValue && Double.isNaN(rows[ii]);
            for (final double value : values) {
                // IEEE 754 equality: -0.0 == 0.0, and NaN equals nothing
                matched |= value == rows[ii];
            }
            expected[ii] = matched != inverted;
        }
        final String context = (isFloat ? "float " : "double ") + filter.getClass().getSimpleName()
                + " nanMatch=" + nanMatch + " values=" + values.length;
        ChunkFilterLoops.check(context, filter, chunk, expected);
    }

    @Test
    public void testHashFallback() {
        // the filters used when no cuckoo table can be built, which match canonical bits
        final double[] values = {1.5, -0.0, 2.25, Double.NaN, 1e300};
        final double[] rows = {1.5, 0.0, -0.0, 2.25, Double.NaN, 3.0, 1e300, Double.NEGATIVE_INFINITY};
        for (final boolean inverted : new boolean[] {false, true}) {
            final float[] floats = new float[values.length];
            final int[] floatBits = new int[values.length];
            final long[] doubleBits = new long[values.length];
            for (int ii = 0; ii < values.length; ++ii) {
                floats[ii] = (float) values[ii];
                floatBits[ii] = FloatChunkMatchFilterFactory.getBits(floats[ii]);
                doubleBits[ii] = DoubleChunkMatchFilterFactory.getBits(values[ii]);
            }
            final ChunkFilter floatFilter = FloatChunkMatchFilterFactory.makeHashFilter(inverted, floatBits);
            final ChunkFilter doubleFilter = DoubleChunkMatchFilterFactory.makeHashFilter(inverted, doubleBits);
            final WritableFloatChunk<Values> floatChunk = WritableFloatChunk.makeWritableChunk(rows.length);
            final WritableDoubleChunk<Values> doubleChunk = WritableDoubleChunk.makeWritableChunk(rows.length);
            final boolean[] expected = new boolean[rows.length];
            for (int ii = 0; ii < rows.length; ++ii) {
                floatChunk.set(ii, (float) rows[ii]);
                doubleChunk.set(ii, rows[ii]);
                // bits equality: -0.0 matches 0.0, and NaN matches NaN
                boolean matched = false;
                for (final double value : values) {
                    matched |= value == rows[ii] || Double.isNaN(value) && Double.isNaN(rows[ii]);
                }
                expected[ii] = matched != inverted;
            }
            ChunkFilterLoops.check("float hash fallback", floatFilter, floatChunk, expected);
            ChunkFilterLoops.check("double hash fallback", doubleFilter, doubleChunk, expected);
        }
    }

    @Test
    public void testStructureChoice() {
        // a cuckoo table of the canonical bits from four values up
        assertStructure("Cuckoo", 4);
        assertStructure("Cuckoo", 100);
    }

    private static void assertStructure(final String structure, final int count) {
        final float[] floats = new float[count];
        final double[] doubles = new double[count];
        for (int ii = 0; ii < count; ++ii) {
            floats[ii] = ii * 1.5f;
            doubles[ii] = ii * 1.5;
        }
        for (final boolean inverted : new boolean[] {false, true}) {
            final MatchOptions options = inverted ? MatchOptions.INVERTED : MatchOptions.REGULAR;
            final String prefix = inverted ? "Inverse" + structure : structure;
            final String floatName =
                    FloatChunkMatchFilterFactory.makeFilter(options, floats).getClass().getSimpleName();
            final String doubleName =
                    DoubleChunkMatchFilterFactory.makeFilter(options, doubles).getClass().getSimpleName();
            assertTrue(floatName, floatName.startsWith(prefix));
            assertTrue(doubleName, doubleName.startsWith(prefix));
        }
    }
}
