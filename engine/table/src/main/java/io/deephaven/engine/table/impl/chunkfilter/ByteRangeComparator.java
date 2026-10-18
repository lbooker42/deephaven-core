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
import io.deephaven.util.compare.ByteComparisons;

/**
 * Creates range filters for byte values, which pass the values that {@link ByteComparisons} orders between the bounds.
 * <p>
 * The factory turns an exclusive bound into the inclusive bound next to it, and returns
 * {@link ChunkFilter#FALSE_FILTER_INSTANCE} for a range that holds no value and
 * {@link ChunkFilter#TRUE_FILTER_INSTANCE} for one that leaves out none. A range that leaves out no value at one end is
 * tested against the other bound alone. A range bounded at both ends is tested with one comparison too: a value is in
 * range when its distance above the lower bound, as an unsigned number, is at most the distance from the lower bound to
 * the upper.
 * <p>
 * Each filter carries its own copy of the {@link ByteChunkFilter} loops, so that its {@code matches} call is never a
 * virtual call shared with other filters.
 */
public class ByteRangeComparator {
    private ByteRangeComparator() {} // static use only

    /**
     * Passes the values from {@code lower} to {@code upper}, both inclusive, in numeric order. The factory gives it no
     * range that starts at NULL_BYTE, which numeric order puts last.
     */
    private final static class ByteBetweenFilter extends ByteChunkFilter {
        // value - lower is at most upper - lower as unsigned numbers when value + offset is at most limit as signed
        // ones, since adding Integer.MIN_VALUE to both sides turns the one comparison into the other
        private final int offset;
        private final int limit;

        private ByteBetweenFilter(final byte lower, final byte upper) {
            offset = Integer.MIN_VALUE - lower;
            limit = Integer.MIN_VALUE + (upper - lower);
        }

        @Override
        public boolean matches(final byte value) {
            return value + offset <= limit;
        }

        // Identical code for all ByteChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final ByteChunk<? extends Values> byteChunk = values.asByteChunk();
            final int len = byteChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(byteChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final ByteChunk<? extends Values> byteChunk = values.asByteChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(byteChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final ByteChunk<? extends Values> byteChunk = values.asByteChunk();
            final int len = values.size();
            int count = 0;
            // matches is one comparison, which costs less than a branch on each earlier result
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = results.get(ii) & matches(byteChunk.get(ii));
                results.set(ii, newResult);
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * Passes the values from {@code lower}, inclusive: the ranges that leave out no value at the top.
     */
    private final static class ByteGEQFilter extends ByteChunkFilter {
        private final byte lower;

        private ByteGEQFilter(final byte lower) {
            this.lower = lower;
        }

        @Override
        public boolean matches(final byte value) {
            // region geqMatches
            return value >= lower;
            // endregion geqMatches
        }

        // Identical code for all ByteChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final ByteChunk<? extends Values> byteChunk = values.asByteChunk();
            final int len = byteChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(byteChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final ByteChunk<? extends Values> byteChunk = values.asByteChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(byteChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final ByteChunk<? extends Values> byteChunk = values.asByteChunk();
            final int len = values.size();
            int count = 0;
            // matches is one comparison, which costs less than a branch on each earlier result
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = results.get(ii) & matches(byteChunk.get(ii));
                results.set(ii, newResult);
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * Passes the values up to {@code upper}, inclusive, and null: the ranges that leave out no value at the bottom.
     */
    private final static class ByteLEQFilter extends ByteChunkFilter {
        private final byte upper;

        private ByteLEQFilter(final byte upper) {
            this.upper = upper;
        }

        @Override
        public boolean matches(final byte value) {
            // region leqMatches
            return value <= upper;
            // endregion leqMatches
        }

        // Identical code for all ByteChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final ByteChunk<? extends Values> byteChunk = values.asByteChunk();
            final int len = byteChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(byteChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final ByteChunk<? extends Values> byteChunk = values.asByteChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(byteChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final ByteChunk<? extends Values> byteChunk = values.asByteChunk();
            final int len = values.size();
            int count = 0;
            // matches is one comparison, which costs less than a branch on each earlier result
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = results.get(ii) & matches(byteChunk.get(ii));
                results.set(ii, newResult);
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * @return a filter that passes the values {@link ByteComparisons} orders between {@code lower} and {@code upper}
     */
    public static ChunkFilter makeByteFilter(byte lower, byte upper, boolean lowerInclusive, boolean upperInclusive) {
        // NULL_BYTE is the least value and MAX_BYTE the greatest, so neither has a neighbor beyond it
        if (!lowerInclusive) {
            if (lower == QueryConstants.MAX_BYTE) {
                return ChunkFilter.FALSE_FILTER_INSTANCE;
            }
            ++lower;
        }
        if (!upperInclusive) {
            if (upper == QueryConstants.NULL_BYTE) {
                return ChunkFilter.FALSE_FILTER_INSTANCE;
            }
            --upper;
        }
        if (ByteComparisons.gt(lower, upper)) {
            return ChunkFilter.FALSE_FILTER_INSTANCE;
        }
        // a bound that leaves out no value needs no comparison
        if (lower == QueryConstants.NULL_BYTE) {
            return upper == QueryConstants.MAX_BYTE ? ChunkFilter.TRUE_FILTER_INSTANCE : new ByteLEQFilter(upper);
        }
        if (upper == QueryConstants.MAX_BYTE) {
            return new ByteGEQFilter(lower);
        }
        return new ByteBetweenFilter(lower, upper);
    }
}
