//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
// ****** AUTO-GENERATED CLASS - DO NOT EDIT MANUALLY
// ****** Edit FloatRangeComparator and run "./gradlew replicateChunkFilters" to regenerate
//
// @formatter:off
package io.deephaven.engine.table.impl.chunkfilter;

import io.deephaven.chunk.*;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.rowset.chunkattributes.OrderedRowKeys;
import io.deephaven.util.QueryConstants;
import io.deephaven.util.compare.DoubleComparisons;

/**
 * Creates range filters for double values, which pass the values that {@link DoubleComparisons} orders between the
 * bounds.
 * <p>
 * {@link DoubleComparisons} orders NULL_DOUBLE first, then negative infinity, the other values as IEEE 754 compares them,
 * and NaN last, with -0.0 equal to 0.0. IEEE 754 comparisons agree with that order on every value but three: NULL_DOUBLE
 * is -Double.MAX_VALUE, which they put after negative infinity; and NaN, which they find neither less, equal nor greater
 * than any value. So the factory turns the bounds into an inclusive range that IEEE 754 comparisons test with the same
 * result for those values too, which it can for every range but a few that start at negative infinity or end below it.
 * Those, and the ranges that start at NaN, it tests with {@link DoubleComparisons}.
 * <p>
 * Each filter carries its own copy of the {@link DoubleChunkFilter} loops, so that its {@code matches} call is never a
 * virtual call shared with other filters.
 */
public class DoubleRangeComparator {
    private DoubleRangeComparator() {} // static use only

    /**
     * Passes the values from {@code lower} to {@code upper}, both inclusive, as IEEE 754 compares them: never NaN. The
     * factory gives it finite bounds, the lower above NULL_DOUBLE, with {@code lower <= upper}.
     * <p>
     * Two comparisons would branch on the first, which for a range in the middle of the data passes about half the
     * values. So this compares once, on the values' bits: flipping all but the sign bit of a negative value makes the
     * values order as signed longs as IEEE 754 orders them, with the negative NaNs first and the positive NaNs last.
     * Then a value is in range when its distance above the lower bound, as an unsigned number, is at most the width.
     */
    private final static class DoubleBetweenFilter extends DoubleChunkFilter {
        // the distance as an unsigned number is at most the width when the distance plus Long.MIN_VALUE is at most
        // the width plus Long.MIN_VALUE as signed numbers
        private final long offset;
        private final long limit;

        private DoubleBetweenFilter(final double lower, final double upper) {
            // -0.0 is just below 0.0 in that order, where IEEE 754 compares them equal
            final long lowerKey = sortable(lower == 0 ? -0.0d : lower);
            final long upperKey = sortable(upper == 0 ? 0.0d : upper);
            offset = Long.MIN_VALUE - lowerKey;
            limit = Long.MIN_VALUE + (upperKey - lowerKey);
        }

        private static long sortable(final double value) {
            final long bits = Double.doubleToRawLongBits(value);
            return bits ^ ((bits >> 63) & Long.MAX_VALUE);
        }

        @Override
        public boolean matches(final double value) {
            return sortable(value) + offset <= limit;
        }

        // Identical code for all DoubleChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final DoubleChunk<? extends Values> doubleChunk = values.asDoubleChunk();
            final int len = doubleChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(doubleChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final DoubleChunk<? extends Values> doubleChunk = values.asDoubleChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(doubleChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final DoubleChunk<? extends Values> doubleChunk = values.asDoubleChunk();
            final int len = values.size();
            int count = 0;
            // matches is one comparison, which costs less than a branch on each earlier result
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = results.get(ii) & matches(doubleChunk.get(ii));
                results.set(ii, newResult);
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * Passes the values from {@code lower}, inclusive, as IEEE 754 compares them: never NaN.
     */
    private final static class DoubleGEQFilter extends DoubleChunkFilter {
        private final double lower;

        private DoubleGEQFilter(final double lower) {
            this.lower = lower;
        }

        @Override
        public boolean matches(final double value) {
            return value >= lower;
        }

        // Identical code for all DoubleChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final DoubleChunk<? extends Values> doubleChunk = values.asDoubleChunk();
            final int len = doubleChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(doubleChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final DoubleChunk<? extends Values> doubleChunk = values.asDoubleChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(doubleChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final DoubleChunk<? extends Values> doubleChunk = values.asDoubleChunk();
            final int len = values.size();
            int count = 0;
            // matches is one comparison, which costs less than a branch on each earlier result
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = results.get(ii) & matches(doubleChunk.get(ii));
                results.set(ii, newResult);
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * Passes the values from {@code lower}, inclusive, as IEEE 754 compares them, and NaN.
     */
    private final static class DoubleGEQOrNaNFilter extends DoubleChunkFilter {
        private final double lower;

        private DoubleGEQOrNaNFilter(final double lower) {
            this.lower = lower;
        }

        @Override
        public boolean matches(final double value) {
            // NaN is not less than lower
            return !(value < lower);
        }

        // Identical code for all DoubleChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final DoubleChunk<? extends Values> doubleChunk = values.asDoubleChunk();
            final int len = doubleChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(doubleChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final DoubleChunk<? extends Values> doubleChunk = values.asDoubleChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(doubleChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final DoubleChunk<? extends Values> doubleChunk = values.asDoubleChunk();
            final int len = values.size();
            int count = 0;
            // matches is one comparison, which costs less than a branch on each earlier result
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = results.get(ii) & matches(doubleChunk.get(ii));
                results.set(ii, newResult);
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * Passes the values up to {@code upper}, inclusive, as IEEE 754 compares them: never NaN.
     */
    private final static class DoubleLEQFilter extends DoubleChunkFilter {
        private final double upper;

        private DoubleLEQFilter(final double upper) {
            this.upper = upper;
        }

        @Override
        public boolean matches(final double value) {
            return value <= upper;
        }

        // Identical code for all DoubleChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final DoubleChunk<? extends Values> doubleChunk = values.asDoubleChunk();
            final int len = doubleChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(doubleChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final DoubleChunk<? extends Values> doubleChunk = values.asDoubleChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(doubleChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final DoubleChunk<? extends Values> doubleChunk = values.asDoubleChunk();
            final int len = values.size();
            int count = 0;
            // matches is one comparison, which costs less than a branch on each earlier result
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = results.get(ii) & matches(doubleChunk.get(ii));
                results.set(ii, newResult);
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * Passes the values that {@link DoubleComparisons} orders between {@code lower} and {@code upper}, for the ranges
     * that IEEE 754 comparisons cannot test.
     */
    private final static class DoubleComparisonsFilter extends DoubleChunkFilter {
        private final double lower;
        private final double upper;
        private final boolean lowerInclusive;
        private final boolean upperInclusive;

        private DoubleComparisonsFilter(final double lower, final double upper, final boolean lowerInclusive,
                final boolean upperInclusive) {
            this.lower = lower;
            this.upper = upper;
            this.lowerInclusive = lowerInclusive;
            this.upperInclusive = upperInclusive;
        }

        @Override
        public boolean matches(final double value) {
            return (lowerInclusive ? DoubleComparisons.geq(value, lower) : DoubleComparisons.gt(value, lower))
                    && (upperInclusive ? DoubleComparisons.leq(value, upper) : DoubleComparisons.lt(value, upper));
        }

        // The DoubleChunkFilter loops, replicated here to prevent megamorphism in the JVM. Its matches costs more than
        // a branch, so unlike the other classes, filterAnd skips the values an earlier filter left out.
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final DoubleChunk<? extends Values> doubleChunk = values.asDoubleChunk();
            final int len = doubleChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(doubleChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final DoubleChunk<? extends Values> doubleChunk = values.asDoubleChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(doubleChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final DoubleChunk<? extends Values> doubleChunk = values.asDoubleChunk();
            final int len = values.size();
            int count = 0;
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(doubleChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * @return a filter that passes the values {@link DoubleComparisons} orders between {@code lower} and {@code upper}
     */
    public static ChunkFilter makeDoubleFilter(double lower, double upper, boolean lowerInclusive,
            boolean upperInclusive) {
        // nothing passes when the bounds cross, or meet and either one leaves out its value
        final int order = DoubleComparisons.compare(lower, upper);
        if (order > 0 || order == 0 && !(lowerInclusive && upperInclusive)) {
            return ChunkFilter.FALSE_FILTER_INSTANCE;
        }

        // the least value IEEE 754 comparisons pass; negative infinity passes NULL_DOUBLE too
        final double ieeeLower;
        if (lower == QueryConstants.NULL_DOUBLE) {
            if (!lowerInclusive) {
                // negative infinity passes and NULL_DOUBLE, which IEEE 754 puts after it, does not
                return new DoubleComparisonsFilter(lower, upper, lowerInclusive, upperInclusive);
            }
            ieeeLower = Double.NEGATIVE_INFINITY;
        } else if (lower == Double.NEGATIVE_INFINITY) {
            if (lowerInclusive) {
                // negative infinity passes and NULL_DOUBLE, which IEEE 754 puts after it, does not
                return new DoubleComparisonsFilter(lower, upper, lowerInclusive, upperInclusive);
            }
            ieeeLower = Math.nextUp(QueryConstants.NULL_DOUBLE);
        } else if (Double.isNaN(lower) || lower == Double.POSITIVE_INFINITY && !lowerInclusive) {
            // only NaN can pass
            return new DoubleComparisonsFilter(lower, upper, lowerInclusive, upperInclusive);
        } else {
            // the next value up from -0.0 or 0.0 is Double.MIN_VALUE, as it should be when -0.0 equals 0.0
            ieeeLower = lowerInclusive ? lower : Math.nextUp(lower);
        }

        // the greatest value IEEE 754 comparisons pass, and whether NaN passes too
        final double ieeeUpper;
        final boolean nanPasses;
        if (Double.isNaN(upper)) {
            ieeeUpper = Double.POSITIVE_INFINITY;
            nanPasses = upperInclusive;
        } else if (upper == QueryConstants.NULL_DOUBLE || upper == Double.NEGATIVE_INFINITY) {
            // only NULL_DOUBLE and negative infinity can pass, which IEEE 754 orders the other way around
            return new DoubleComparisonsFilter(lower, upper, lowerInclusive, upperInclusive);
        } else {
            ieeeUpper = upperInclusive ? upper : Math.nextDown(upper);
            nanPasses = false;
        }

        if (ieeeLower == Double.NEGATIVE_INFINITY) {
            return nanPasses ? ChunkFilter.TRUE_FILTER_INSTANCE : new DoubleLEQFilter(ieeeUpper);
        }
        if (nanPasses) {
            return new DoubleGEQOrNaNFilter(ieeeLower);
        }
        if (ieeeUpper == Double.POSITIVE_INFINITY) {
            return new DoubleGEQFilter(ieeeLower);
        }
        // an exclusive bound next to the other can leave no value between them
        if (ieeeLower > ieeeUpper) {
            return ChunkFilter.FALSE_FILTER_INSTANCE;
        }
        return new DoubleBetweenFilter(ieeeLower, ieeeUpper);
    }
}
