//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
package io.deephaven.benchmark.engine.rangefilter;

import io.deephaven.engine.rowset.RowSetFactory;
import io.deephaven.engine.table.impl.QueryTable;
import io.deephaven.engine.table.impl.select.AbstractRangeFilter;
import io.deephaven.engine.table.impl.select.ByteRangeFilter;
import io.deephaven.engine.table.impl.select.CharRangeFilter;
import io.deephaven.engine.table.impl.select.DoubleRangeFilter;
import io.deephaven.engine.table.impl.select.FloatRangeFilter;
import io.deephaven.engine.table.impl.select.IntRangeFilter;
import io.deephaven.engine.table.impl.select.LongRangeFilter;
import io.deephaven.engine.table.impl.select.ShortRangeFilter;
import io.deephaven.engine.table.impl.sources.InMemoryColumnSource;
import io.deephaven.util.QueryConstants;
import io.deephaven.util.compare.ByteComparisons;
import io.deephaven.util.compare.CharComparisons;
import io.deephaven.util.compare.DoubleComparisons;
import io.deephaven.util.compare.FloatComparisons;
import io.deephaven.util.compare.IntComparisons;
import io.deephaven.util.compare.LongComparisons;
import io.deephaven.util.compare.ShortComparisons;

import java.util.Map;
import java.util.SplittableRandom;

/**
 * The one-column table that the range filter benchmarks filter, and the range filter they apply to it.
 * <p>
 * Each row is null with probability {@code nullFraction}, and otherwise holds {@code value(u)} for a uniform {@code u}
 * in [0, 1): an integral column spreads its values over the type's range, and a floating point column over [-1, 1). The
 * filter's bounds are values at fractions of that range, chosen so that about {@code selectivity} of the values that
 * are not null pass:
 * <ul>
 * <li>{@code between}: from {@code value(0.5 - selectivity / 2)} to {@code value(0.5 + selectivity / 2)}, both
 * inclusive, the range in the middle of the data.</li>
 * <li>{@code greater}: greater than {@code value(1 - selectivity)}, the filter {@code X > x} makes.</li>
 * <li>{@code less}: less than {@code value(selectivity)}, the filter {@code X < x} makes, which also passes the null
 * rows, as null is less than every other value.</li>
 * </ul>
 * Which rows pass is worked out from the {@code *Comparisons} classes, not from the filter, so that a benchmark can
 * check the filter against it.
 */
final class RangeFilterBenchmarkData {

    /**
     * The name of the generated table's only column.
     */
    static final String COLUMN = "X";

    private static final long ROWS_SEED = 0x72616E6765L;

    enum Shape {
        BETWEEN, GREATER, LESS
    }

    static final class GeneratedTable {
        final QueryTable table;
        // the column's values, which the table's column source wraps
        final Object data;
        // the filter, not yet initialized
        final AbstractRangeFilter filter;
        // whether each row passes the filter
        final boolean[] passes;

        private GeneratedTable(final Object data, final AbstractRangeFilter filter, final boolean[] passes) {
            this.table = new QueryTable(RowSetFactory.flat(passes.length).toTracking(),
                    Map.of(COLUMN, InMemoryColumnSource.getImmutableMemoryColumnSource(data)));
            this.data = data;
            this.filter = filter;
            this.passes = passes;
        }

        long passingRows() {
            long count = 0;
            for (final boolean pass : passes) {
                count += pass ? 1 : 0;
            }
            return count;
        }
    }

    private RangeFilterBenchmarkData() {}

    /**
     * @param type the column type: byte, short, char, int, long, float or double
     * @param shape the filter's bounds, as described above
     * @param selectivity about how much of the values that are not null pass, from 0 to 1
     * @param nullFraction the probability that a row is null
     * @param rows the number of rows
     */
    static GeneratedTable generate(
            final String type,
            final Shape shape,
            final double selectivity,
            final double nullFraction,
            final int rows) {
        if (!(selectivity >= 0 && selectivity <= 1)) {
            throw new IllegalArgumentException("selectivity must be from 0 to 1, not " + selectivity);
        }
        // a fraction for each row, or NaN for a null row
        final SplittableRandom random = new SplittableRandom(ROWS_SEED);
        final double[] fractions = new double[rows];
        for (int ii = 0; ii < rows; ++ii) {
            fractions[ii] = random.nextDouble() < nullFraction ? Double.NaN : random.nextDouble();
        }
        // the fractions of the range at the bounds; for greater and less, one of them is unused
        final double lowerFraction = shape == Shape.GREATER ? 1 - selectivity : 0.5 - selectivity / 2;
        final double upperFraction = shape == Shape.LESS ? selectivity : 0.5 + selectivity / 2;
        final boolean[] passes = new boolean[rows];

        switch (type) {
            case "byte": {
                final byte[] data = new byte[rows];
                for (int ii = 0; ii < rows; ++ii) {
                    data[ii] = Double.isNaN(fractions[ii]) ? QueryConstants.NULL_BYTE
                            : (byte) integral(fractions[ii],
                                    QueryConstants.MIN_BYTE, QueryConstants.MAX_BYTE);
                }
                final byte lower = (byte) integral(lowerFraction, QueryConstants.MIN_BYTE, QueryConstants.MAX_BYTE);
                final byte upper = (byte) integral(upperFraction, QueryConstants.MIN_BYTE, QueryConstants.MAX_BYTE);
                for (int ii = 0; ii < rows; ++ii) {
                    passes[ii] = shape == Shape.GREATER ? ByteComparisons.gt(data[ii], lower)
                            : shape == Shape.LESS ? ByteComparisons.lt(data[ii], upper)
                                    : ByteComparisons.geq(data[ii], lower) && ByteComparisons.leq(data[ii], upper);
                }
                return new GeneratedTable(data, shape == Shape.GREATER ? ByteRangeFilter.gt(COLUMN, lower)
                        : shape == Shape.LESS ? ByteRangeFilter.lt(COLUMN, upper)
                                : new ByteRangeFilter(COLUMN, lower, upper, true, true),
                        passes);
            }
            case "short": {
                final short[] data = new short[rows];
                for (int ii = 0; ii < rows; ++ii) {
                    data[ii] = Double.isNaN(fractions[ii]) ? QueryConstants.NULL_SHORT
                            : (short) integral(fractions[ii], QueryConstants.MIN_SHORT, QueryConstants.MAX_SHORT);
                }
                final short lower =
                        (short) integral(lowerFraction, QueryConstants.MIN_SHORT, QueryConstants.MAX_SHORT);
                final short upper =
                        (short) integral(upperFraction, QueryConstants.MIN_SHORT, QueryConstants.MAX_SHORT);
                for (int ii = 0; ii < rows; ++ii) {
                    passes[ii] = shape == Shape.GREATER ? ShortComparisons.gt(data[ii], lower)
                            : shape == Shape.LESS ? ShortComparisons.lt(data[ii], upper)
                                    : ShortComparisons.geq(data[ii], lower) && ShortComparisons.leq(data[ii], upper);
                }
                return new GeneratedTable(data, shape == Shape.GREATER ? ShortRangeFilter.gt(COLUMN, lower)
                        : shape == Shape.LESS ? ShortRangeFilter.lt(COLUMN, upper)
                                : new ShortRangeFilter(COLUMN, lower, upper, true, true),
                        passes);
            }
            case "char": {
                final char[] data = new char[rows];
                for (int ii = 0; ii < rows; ++ii) {
                    data[ii] = Double.isNaN(fractions[ii]) ? QueryConstants.NULL_CHAR
                            : (char) integral(fractions[ii], QueryConstants.MIN_CHAR, QueryConstants.MAX_CHAR);
                }
                final char lower = (char) integral(lowerFraction, QueryConstants.MIN_CHAR, QueryConstants.MAX_CHAR);
                final char upper = (char) integral(upperFraction, QueryConstants.MIN_CHAR, QueryConstants.MAX_CHAR);
                for (int ii = 0; ii < rows; ++ii) {
                    passes[ii] = shape == Shape.GREATER ? CharComparisons.gt(data[ii], lower)
                            : shape == Shape.LESS ? CharComparisons.lt(data[ii], upper)
                                    : CharComparisons.geq(data[ii], lower) && CharComparisons.leq(data[ii], upper);
                }
                return new GeneratedTable(data, shape == Shape.GREATER ? CharRangeFilter.gt(COLUMN, lower)
                        : shape == Shape.LESS ? CharRangeFilter.lt(COLUMN, upper)
                                : new CharRangeFilter(COLUMN, lower, upper, true, true),
                        passes);
            }
            case "int": {
                final int[] data = new int[rows];
                for (int ii = 0; ii < rows; ++ii) {
                    data[ii] = Double.isNaN(fractions[ii]) ? QueryConstants.NULL_INT
                            : (int) integral(fractions[ii], QueryConstants.MIN_INT, QueryConstants.MAX_INT);
                }
                final int lower = (int) integral(lowerFraction, QueryConstants.MIN_INT, QueryConstants.MAX_INT);
                final int upper = (int) integral(upperFraction, QueryConstants.MIN_INT, QueryConstants.MAX_INT);
                for (int ii = 0; ii < rows; ++ii) {
                    passes[ii] = shape == Shape.GREATER ? IntComparisons.gt(data[ii], lower)
                            : shape == Shape.LESS ? IntComparisons.lt(data[ii], upper)
                                    : IntComparisons.geq(data[ii], lower) && IntComparisons.leq(data[ii], upper);
                }
                return new GeneratedTable(data, shape == Shape.GREATER ? IntRangeFilter.gt(COLUMN, lower)
                        : shape == Shape.LESS ? IntRangeFilter.lt(COLUMN, upper)
                                : new IntRangeFilter(COLUMN, lower, upper, true, true),
                        passes);
            }
            case "long": {
                final long[] data = new long[rows];
                for (int ii = 0; ii < rows; ++ii) {
                    data[ii] = Double.isNaN(fractions[ii]) ? QueryConstants.NULL_LONG : longValue(fractions[ii]);
                }
                final long lower = longValue(lowerFraction);
                final long upper = longValue(upperFraction);
                for (int ii = 0; ii < rows; ++ii) {
                    passes[ii] = shape == Shape.GREATER ? LongComparisons.gt(data[ii], lower)
                            : shape == Shape.LESS ? LongComparisons.lt(data[ii], upper)
                                    : LongComparisons.geq(data[ii], lower) && LongComparisons.leq(data[ii], upper);
                }
                return new GeneratedTable(data, shape == Shape.GREATER ? LongRangeFilter.gt(COLUMN, lower)
                        : shape == Shape.LESS ? LongRangeFilter.lt(COLUMN, upper)
                                : new LongRangeFilter(COLUMN, lower, upper, true, true),
                        passes);
            }
            case "float": {
                final float[] data = new float[rows];
                for (int ii = 0; ii < rows; ++ii) {
                    data[ii] = Double.isNaN(fractions[ii]) ? QueryConstants.NULL_FLOAT
                            : (float) floatingPoint(fractions[ii]);
                }
                final float lower = (float) floatingPoint(lowerFraction);
                final float upper = (float) floatingPoint(upperFraction);
                for (int ii = 0; ii < rows; ++ii) {
                    passes[ii] = shape == Shape.GREATER ? FloatComparisons.gt(data[ii], lower)
                            : shape == Shape.LESS ? FloatComparisons.lt(data[ii], upper)
                                    : FloatComparisons.geq(data[ii], lower) && FloatComparisons.leq(data[ii], upper);
                }
                return new GeneratedTable(data, shape == Shape.GREATER ? FloatRangeFilter.gt(COLUMN, lower)
                        : shape == Shape.LESS ? FloatRangeFilter.lt(COLUMN, upper)
                                : new FloatRangeFilter(COLUMN, lower, upper, true, true),
                        passes);
            }
            case "double": {
                final double[] data = new double[rows];
                for (int ii = 0; ii < rows; ++ii) {
                    data[ii] = Double.isNaN(fractions[ii]) ? QueryConstants.NULL_DOUBLE : floatingPoint(fractions[ii]);
                }
                final double lower = floatingPoint(lowerFraction);
                final double upper = floatingPoint(upperFraction);
                for (int ii = 0; ii < rows; ++ii) {
                    passes[ii] = shape == Shape.GREATER ? DoubleComparisons.gt(data[ii], lower)
                            : shape == Shape.LESS ? DoubleComparisons.lt(data[ii], upper)
                                    : DoubleComparisons.geq(data[ii], lower)
                                            && DoubleComparisons.leq(data[ii], upper);
                }
                return new GeneratedTable(data, shape == Shape.GREATER ? DoubleRangeFilter.gt(COLUMN, lower)
                        : shape == Shape.LESS ? DoubleRangeFilter.lt(COLUMN, upper)
                                : new DoubleRangeFilter(COLUMN, lower, upper, true, true),
                        passes);
            }
            default:
                throw new IllegalArgumentException("Unsupported type " + type);
        }
    }

    /**
     * @return the value at {@code fraction} of the way from {@code first} to {@code last}, inclusive
     */
    private static long integral(final double fraction, final long first, final long last) {
        final double span = (double) last - first + 1;
        return Math.min(last, first + (long) Math.floor(fraction * span));
    }

    /**
     * @return the long value at {@code fraction} of the way across [-2^62, 2^62), which keeps clear of the null value
     *         without the rounding of a double near the ends of the long range
     */
    private static long longValue(final double fraction) {
        return (long) Math.floor((fraction - 0.5) * 0x1p63);
    }

    private static double floatingPoint(final double fraction) {
        return 2 * fraction - 1;
    }
}
