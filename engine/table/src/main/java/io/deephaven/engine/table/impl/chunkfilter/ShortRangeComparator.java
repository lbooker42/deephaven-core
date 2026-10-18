//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
// ****** AUTO-GENERATED CLASS - DO NOT EDIT MANUALLY
// ****** Edit CharRangeComparator and run "./gradlew replicateChunkFilters" to regenerate
//
// @formatter:off
package io.deephaven.engine.table.impl.chunkfilter;

import io.deephaven.chunk.*;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.rowset.chunkattributes.OrderedRowKeys;
import io.deephaven.util.QueryConstants;
import io.deephaven.util.compare.ShortComparisons;

/**
 * Creates range filters for short values, which pass the values that {@link ShortComparisons} orders between the bounds.
 * <p>
 * The factory turns an exclusive bound into the inclusive bound next to it, and returns
 * {@link ChunkFilter#FALSE_FILTER_INSTANCE} for a range that holds no value and
 * {@link ChunkFilter#TRUE_FILTER_INSTANCE} for one that leaves out none. A range that leaves out no value at one end is
 * tested against the other bound alone. A range bounded at both ends is tested with one comparison too: a value is in
 * range when its distance above the lower bound, as an unsigned number, is at most the distance from the lower bound to
 * the upper.
 * <p>
 * Each filter carries its own copy of the {@link ShortChunkFilter} loops, so that its {@code matches} call is never a
 * virtual call shared with other filters.
 */
public class ShortRangeComparator {
    private ShortRangeComparator() {} // static use only

    /**
     * Passes the values from {@code lower} to {@code upper}, both inclusive, in numeric order. The factory gives it no
     * range that starts at NULL_SHORT, which numeric order puts last.
     */
    private final static class ShortBetweenFilter extends ShortChunkFilter {
        // value - lower is at most upper - lower as unsigned numbers when value + offset is at most limit as signed
        // ones, since adding Integer.MIN_VALUE to both sides turns the one comparison into the other
        private final int offset;
        private final int limit;

        private ShortBetweenFilter(final short lower, final short upper) {
            offset = Integer.MIN_VALUE - lower;
            limit = Integer.MIN_VALUE + (upper - lower);
        }

        @Override
        public boolean matches(final short value) {
            return value + offset <= limit;
        }

        // Identical code for all ShortChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final ShortChunk<? extends Values> shortChunk = values.asShortChunk();
            final int len = shortChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(shortChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final ShortChunk<? extends Values> shortChunk = values.asShortChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(shortChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final ShortChunk<? extends Values> shortChunk = values.asShortChunk();
            final int len = values.size();
            int count = 0;
            // matches is one comparison, which costs less than a branch on each earlier result
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = results.get(ii) & matches(shortChunk.get(ii));
                results.set(ii, newResult);
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * Passes the values from {@code lower}, inclusive: the ranges that leave out no value at the top.
     */
    private final static class ShortAtLeastFilter extends ShortChunkFilter {
        private final short lower;

        private ShortAtLeastFilter(final short lower) {
            this.lower = lower;
        }

        @Override
        public boolean matches(final short value) {
            // region atLeastMatches
            return value >= lower;
            // endregion atLeastMatches
        }

        // Identical code for all ShortChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final ShortChunk<? extends Values> shortChunk = values.asShortChunk();
            final int len = shortChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(shortChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final ShortChunk<? extends Values> shortChunk = values.asShortChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(shortChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final ShortChunk<? extends Values> shortChunk = values.asShortChunk();
            final int len = values.size();
            int count = 0;
            // matches is one comparison, which costs less than a branch on each earlier result
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = results.get(ii) & matches(shortChunk.get(ii));
                results.set(ii, newResult);
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * Passes the values up to {@code upper}, inclusive, and null: the ranges that leave out no value at the bottom.
     */
    private final static class ShortAtMostFilter extends ShortChunkFilter {
        private final short upper;

        private ShortAtMostFilter(final short upper) {
            this.upper = upper;
        }

        @Override
        public boolean matches(final short value) {
            // region atMostMatches
            return value <= upper;
            // endregion atMostMatches
        }

        // Identical code for all ShortChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final ShortChunk<? extends Values> shortChunk = values.asShortChunk();
            final int len = shortChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(shortChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final ShortChunk<? extends Values> shortChunk = values.asShortChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(shortChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final ShortChunk<? extends Values> shortChunk = values.asShortChunk();
            final int len = values.size();
            int count = 0;
            // matches is one comparison, which costs less than a branch on each earlier result
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = results.get(ii) & matches(shortChunk.get(ii));
                results.set(ii, newResult);
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * @return a filter that passes the values {@link ShortComparisons} orders between {@code lower} and {@code upper}
     */
    public static ChunkFilter makeShortFilter(short lower, short upper, boolean lowerInclusive, boolean upperInclusive) {
        // NULL_SHORT is the least value and MAX_SHORT the greatest, so neither has a neighbor beyond it
        if (!lowerInclusive) {
            if (lower == QueryConstants.MAX_SHORT) {
                return ChunkFilter.FALSE_FILTER_INSTANCE;
            }
            ++lower;
        }
        if (!upperInclusive) {
            if (upper == QueryConstants.NULL_SHORT) {
                return ChunkFilter.FALSE_FILTER_INSTANCE;
            }
            --upper;
        }
        if (ShortComparisons.gt(lower, upper)) {
            return ChunkFilter.FALSE_FILTER_INSTANCE;
        }
        // a bound that leaves out no value needs no comparison
        if (lower == QueryConstants.NULL_SHORT) {
            return upper == QueryConstants.MAX_SHORT ? ChunkFilter.TRUE_FILTER_INSTANCE : new ShortAtMostFilter(upper);
        }
        if (upper == QueryConstants.MAX_SHORT) {
            return new ShortAtLeastFilter(lower);
        }
        return new ShortBetweenFilter(lower, upper);
    }
}
