//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
package io.deephaven.engine.table.impl.chunkfilter;

import io.deephaven.chunk.*;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.rowset.chunkattributes.OrderedRowKeys;
import io.deephaven.util.QueryConstants;
import io.deephaven.util.compare.FloatComparisons;

/**
 * Creates range filters for float values, which pass the values that {@link FloatComparisons} orders between the
 * bounds.
 * <p>
 * {@link FloatComparisons} orders NULL_FLOAT first, then negative infinity, the other values as IEEE 754 compares them,
 * and NaN last, with -0.0 equal to 0.0. IEEE 754 comparisons agree with that order on every value but three: NULL_FLOAT
 * is -Float.MAX_VALUE, which they put after negative infinity; and NaN, which they find neither less, equal nor greater
 * than any value. So the factory turns the bounds into an inclusive range that IEEE 754 comparisons test with the same
 * result for those values too, which it can for every range but a few that start at negative infinity or end below it.
 * Those, and the ranges that start at NaN, it tests with {@link FloatComparisons}.
 * <p>
 * Each filter carries its own copy of the {@link FloatChunkFilter} loops, so that its {@code matches} call is never a
 * virtual call shared with other filters.
 */
public class FloatRangeComparator {
    private FloatRangeComparator() {} // static use only

    /**
     * Passes the values from {@code lower} to {@code upper}, both inclusive, as IEEE 754 compares them: never NaN. The
     * factory gives it finite bounds, the lower above NULL_FLOAT, with {@code lower <= upper}.
     * <p>
     * Two comparisons would branch on the first, which for a range in the middle of the data passes about half the
     * values. So this compares once, on the values' bits: flipping all but the sign bit of a negative value makes the
     * values order as signed ints as IEEE 754 orders them, with the negative NaNs first and the positive NaNs last.
     * Then a value is in range when its distance above the lower bound, as an unsigned number, is at most the width.
     */
    private final static class FloatBetweenFilter extends FloatChunkFilter {
        // the distance as an unsigned number is at most the width when the distance plus Integer.MIN_VALUE is at most
        // the width plus Integer.MIN_VALUE as signed numbers
        private final int offset;
        private final int limit;

        private FloatBetweenFilter(final float lower, final float upper) {
            // -0.0 is just below 0.0 in that order, where IEEE 754 compares them equal
            final int lowerKey = sortable(lower == 0 ? -0.0f : lower);
            final int upperKey = sortable(upper == 0 ? 0.0f : upper);
            offset = Integer.MIN_VALUE - lowerKey;
            limit = Integer.MIN_VALUE + (upperKey - lowerKey);
        }

        private static int sortable(final float value) {
            final int bits = Float.floatToRawIntBits(value);
            return bits ^ ((bits >> 31) & Integer.MAX_VALUE);
        }

        @Override
        public boolean matches(final float value) {
            return sortable(value) + offset <= limit;
        }

        // Identical code for all FloatChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final FloatChunk<? extends Values> floatChunk = values.asFloatChunk();
            final int len = floatChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(floatChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final FloatChunk<? extends Values> floatChunk = values.asFloatChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(floatChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final FloatChunk<? extends Values> floatChunk = values.asFloatChunk();
            final int len = values.size();
            int count = 0;
            // matches is one comparison, which costs less than a branch on each earlier result
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = results.get(ii) & matches(floatChunk.get(ii));
                results.set(ii, newResult);
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * Passes the values from {@code lower}, inclusive, as IEEE 754 compares them: never NaN.
     */
    private final static class FloatAtLeastFilter extends FloatChunkFilter {
        private final float lower;

        private FloatAtLeastFilter(final float lower) {
            this.lower = lower;
        }

        @Override
        public boolean matches(final float value) {
            return value >= lower;
        }

        // Identical code for all FloatChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final FloatChunk<? extends Values> floatChunk = values.asFloatChunk();
            final int len = floatChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(floatChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final FloatChunk<? extends Values> floatChunk = values.asFloatChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(floatChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final FloatChunk<? extends Values> floatChunk = values.asFloatChunk();
            final int len = values.size();
            int count = 0;
            // matches is one comparison, which costs less than a branch on each earlier result
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = results.get(ii) & matches(floatChunk.get(ii));
                results.set(ii, newResult);
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * Passes the values from {@code lower}, inclusive, as IEEE 754 compares them, and NaN.
     */
    private final static class FloatAtLeastOrNaNFilter extends FloatChunkFilter {
        private final float lower;

        private FloatAtLeastOrNaNFilter(final float lower) {
            this.lower = lower;
        }

        @Override
        public boolean matches(final float value) {
            // NaN is not less than lower
            return !(value < lower);
        }

        // Identical code for all FloatChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final FloatChunk<? extends Values> floatChunk = values.asFloatChunk();
            final int len = floatChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(floatChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final FloatChunk<? extends Values> floatChunk = values.asFloatChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(floatChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final FloatChunk<? extends Values> floatChunk = values.asFloatChunk();
            final int len = values.size();
            int count = 0;
            // matches is one comparison, which costs less than a branch on each earlier result
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = results.get(ii) & matches(floatChunk.get(ii));
                results.set(ii, newResult);
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * Passes the values up to {@code upper}, inclusive, as IEEE 754 compares them: never NaN.
     */
    private final static class FloatAtMostFilter extends FloatChunkFilter {
        private final float upper;

        private FloatAtMostFilter(final float upper) {
            this.upper = upper;
        }

        @Override
        public boolean matches(final float value) {
            return value <= upper;
        }

        // Identical code for all FloatChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final FloatChunk<? extends Values> floatChunk = values.asFloatChunk();
            final int len = floatChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(floatChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final FloatChunk<? extends Values> floatChunk = values.asFloatChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(floatChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final FloatChunk<? extends Values> floatChunk = values.asFloatChunk();
            final int len = values.size();
            int count = 0;
            // matches is one comparison, which costs less than a branch on each earlier result
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = results.get(ii) & matches(floatChunk.get(ii));
                results.set(ii, newResult);
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * Passes the values that {@link FloatComparisons} orders between {@code lower} and {@code upper}, for the ranges
     * that IEEE 754 comparisons cannot test.
     */
    private final static class FloatComparisonsFilter extends FloatChunkFilter {
        private final float lower;
        private final float upper;
        private final boolean lowerInclusive;
        private final boolean upperInclusive;

        private FloatComparisonsFilter(final float lower, final float upper, final boolean lowerInclusive,
                final boolean upperInclusive) {
            this.lower = lower;
            this.upper = upper;
            this.lowerInclusive = lowerInclusive;
            this.upperInclusive = upperInclusive;
        }

        @Override
        public boolean matches(final float value) {
            return (lowerInclusive ? FloatComparisons.geq(value, lower) : FloatComparisons.gt(value, lower))
                    && (upperInclusive ? FloatComparisons.leq(value, upper) : FloatComparisons.lt(value, upper));
        }

        // The FloatChunkFilter loops, replicated here to prevent megamorphism in the JVM. Its matches costs more than
        // a branch, so unlike the other classes, filterAnd skips the values an earlier filter left out.
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final FloatChunk<? extends Values> floatChunk = values.asFloatChunk();
            final int len = floatChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(floatChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final FloatChunk<? extends Values> floatChunk = values.asFloatChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(floatChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final FloatChunk<? extends Values> floatChunk = values.asFloatChunk();
            final int len = values.size();
            int count = 0;
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(floatChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * @return a filter that passes the values {@link FloatComparisons} orders between {@code lower} and {@code upper}
     */
    public static ChunkFilter makeFloatFilter(float lower, float upper, boolean lowerInclusive,
            boolean upperInclusive) {
        // nothing passes when the bounds cross, or meet and either one leaves out its value
        final int order = FloatComparisons.compare(lower, upper);
        if (order > 0 || order == 0 && !(lowerInclusive && upperInclusive)) {
            return ChunkFilter.FALSE_FILTER_INSTANCE;
        }

        // the least value IEEE 754 comparisons pass; negative infinity passes NULL_FLOAT too
        final float ieeeLower;
        if (lower == QueryConstants.NULL_FLOAT) {
            if (!lowerInclusive) {
                // negative infinity passes and NULL_FLOAT, which IEEE 754 puts after it, does not
                return new FloatComparisonsFilter(lower, upper, lowerInclusive, upperInclusive);
            }
            ieeeLower = Float.NEGATIVE_INFINITY;
        } else if (lower == Float.NEGATIVE_INFINITY) {
            if (lowerInclusive) {
                // negative infinity passes and NULL_FLOAT, which IEEE 754 puts after it, does not
                return new FloatComparisonsFilter(lower, upper, lowerInclusive, upperInclusive);
            }
            ieeeLower = Math.nextUp(QueryConstants.NULL_FLOAT);
        } else if (Float.isNaN(lower) || lower == Float.POSITIVE_INFINITY && !lowerInclusive) {
            // only NaN can pass
            return new FloatComparisonsFilter(lower, upper, lowerInclusive, upperInclusive);
        } else {
            // the next value up from -0.0 or 0.0 is Float.MIN_VALUE, as it should be when -0.0 equals 0.0
            ieeeLower = lowerInclusive ? lower : Math.nextUp(lower);
        }

        // the greatest value IEEE 754 comparisons pass, and whether NaN passes too
        final float ieeeUpper;
        final boolean nanPasses;
        if (Float.isNaN(upper)) {
            ieeeUpper = Float.POSITIVE_INFINITY;
            nanPasses = upperInclusive;
        } else if (upper == QueryConstants.NULL_FLOAT || upper == Float.NEGATIVE_INFINITY) {
            // only NULL_FLOAT and negative infinity can pass, which IEEE 754 orders the other way around
            return new FloatComparisonsFilter(lower, upper, lowerInclusive, upperInclusive);
        } else {
            ieeeUpper = upperInclusive ? upper : Math.nextDown(upper);
            nanPasses = false;
        }

        if (ieeeLower == Float.NEGATIVE_INFINITY) {
            return nanPasses ? ChunkFilter.TRUE_FILTER_INSTANCE : new FloatAtMostFilter(ieeeUpper);
        }
        if (nanPasses) {
            return new FloatAtLeastOrNaNFilter(ieeeLower);
        }
        if (ieeeUpper == Float.POSITIVE_INFINITY) {
            return new FloatAtLeastFilter(ieeeLower);
        }
        // an exclusive bound next to the other can leave no value between them
        if (ieeeLower > ieeeUpper) {
            return ChunkFilter.FALSE_FILTER_INSTANCE;
        }
        return new FloatBetweenFilter(ieeeLower, ieeeUpper);
    }
}
