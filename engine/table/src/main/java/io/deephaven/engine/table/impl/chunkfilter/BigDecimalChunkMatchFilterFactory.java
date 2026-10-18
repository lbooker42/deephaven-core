//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
package io.deephaven.engine.table.impl.chunkfilter;

import io.deephaven.engine.table.MatchOptions;
import io.deephaven.util.compare.ObjectComparisons;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Match filters for {@link BigDecimal} columns, which match by {@link BigDecimal#compareTo(BigDecimal)}, as the query
 * language's {@code ==} does, rather than by {@link BigDecimal#equals(Object)}: {@code 5.0} matches {@code 5} and
 * {@code 5.00}, whatever their scales. {@code null} matches {@code null} and nothing else.
 */
class BigDecimalChunkMatchFilterFactory {
    private BigDecimalChunkMatchFilterFactory() {} // static use only

    /**
     * The fewest values that are hashed rather than binary searched. Measured with {@code MatchFilterWhereBenchmark} at
     * 10% hits, the binary search wins at four and eight values, is even at 16, and loses from 32; the hash set grows
     * far more slowly (29 to 33 ns per row from 4 to 64 values, against 18 to 40).
     */
    static final int HASH_THRESHOLD = 16;

    /**
     * Create a filter for the provided values.
     *
     * <p>
     * The non-null {@link BigDecimal} values are copied to an array of the filter's own, so the caller's array is
     * neither modified nor retained, and a {@code null} among the values is recorded as a flag. A value that is neither
     * null nor a {@link BigDecimal} is skipped: it can never match a {@link BigDecimal} column, so skipping it selects
     * what matching it by equality would. Of the filters below, only the multi-value filters sort their values, for
     * their binary search.
     */
    static ChunkFilter makeFilter(final MatchOptions matchOptions, final Object... values) {
        // sized for every value, of which only the first size are used
        final BigDecimal[] sortedValues = new BigDecimal[values.length];
        int size = 0;
        boolean matchesNull = false;
        for (final Object value : values) {
            if (value == null) {
                matchesNull = true;
            } else if (value instanceof BigDecimal) {
                sortedValues[size++] = (BigDecimal) value;
            }
        }

        if (size == 0 && !matchesNull) {
            return matchOptions.inverted() ? ChunkFilter.TRUE_FILTER_INSTANCE : ChunkFilter.FALSE_FILTER_INSTANCE;
        }
        if (matchOptions.inverted()) {
            switch (size) {
                case 1:
                    return new InverseSingleValueBigDecimalChunkFilter(matchesNull, sortedValues[0]);
                case 2:
                    return new InverseTwoValueBigDecimalChunkFilter(matchesNull, sortedValues[0], sortedValues[1]);
                case 3:
                    return new InverseThreeValueBigDecimalChunkFilter(matchesNull, sortedValues[0], sortedValues[1],
                            sortedValues[2]);
                default:
                    return size >= HASH_THRESHOLD
                            ? new InverseHashBigDecimalChunkFilter(matchesNull, sortedValues, size)
                            : new InverseMultiValueBigDecimalChunkFilter(matchesNull, sortedValues, size);
            }
        }
        switch (size) {
            case 1:
                return new SingleValueBigDecimalChunkFilter(matchesNull, sortedValues[0]);
            case 2:
                return new TwoValueBigDecimalChunkFilter(matchesNull, sortedValues[0], sortedValues[1]);
            case 3:
                return new ThreeValueBigDecimalChunkFilter(matchesNull, sortedValues[0], sortedValues[1],
                        sortedValues[2]);
            default:
                return size >= HASH_THRESHOLD
                        ? new HashBigDecimalChunkFilter(matchesNull, sortedValues, size)
                        : new MultiValueBigDecimalChunkFilter(matchesNull, sortedValues, size);
        }
    }

    private final static class SingleValueBigDecimalChunkFilter extends ObjectChunkFilter<BigDecimal> {
        private final boolean matchesNull;
        private final BigDecimal value;

        private SingleValueBigDecimalChunkFilter(boolean matchesNull, BigDecimal value) {
            this.matchesNull = matchesNull;
            this.value = value;
        }

        @Override
        public boolean matches(BigDecimal value) {
            return value == null ? matchesNull : ObjectComparisons.compareEquals(value, this.value);
        }
    }

    private final static class InverseSingleValueBigDecimalChunkFilter extends ObjectChunkFilter<BigDecimal> {
        private final boolean matchesNull;
        private final BigDecimal value;

        private InverseSingleValueBigDecimalChunkFilter(boolean matchesNull, BigDecimal value) {
            this.matchesNull = matchesNull;
            this.value = value;
        }

        @Override
        public boolean matches(BigDecimal value) {
            return value == null ? !matchesNull : !ObjectComparisons.compareEquals(value, this.value);
        }
    }

    private final static class TwoValueBigDecimalChunkFilter extends ObjectChunkFilter<BigDecimal> {
        private final boolean matchesNull;
        private final BigDecimal value1;
        private final BigDecimal value2;

        private TwoValueBigDecimalChunkFilter(boolean matchesNull, BigDecimal value1, BigDecimal value2) {
            this.matchesNull = matchesNull;
            this.value1 = value1;
            this.value2 = value2;
        }

        @Override
        public boolean matches(BigDecimal value) {
            return value == null ? matchesNull
                    : ObjectComparisons.compareEquals(value, value1) || ObjectComparisons.compareEquals(value, value2);
        }
    }

    private final static class InverseTwoValueBigDecimalChunkFilter extends ObjectChunkFilter<BigDecimal> {
        private final boolean matchesNull;
        private final BigDecimal value1;
        private final BigDecimal value2;

        private InverseTwoValueBigDecimalChunkFilter(boolean matchesNull, BigDecimal value1, BigDecimal value2) {
            this.matchesNull = matchesNull;
            this.value1 = value1;
            this.value2 = value2;
        }

        @Override
        public boolean matches(BigDecimal value) {
            return value == null ? !matchesNull
                    : !ObjectComparisons.compareEquals(value, value1)
                            && !ObjectComparisons.compareEquals(value, value2);
        }
    }

    private final static class ThreeValueBigDecimalChunkFilter extends ObjectChunkFilter<BigDecimal> {
        private final boolean matchesNull;
        private final BigDecimal value1;
        private final BigDecimal value2;
        private final BigDecimal value3;

        private ThreeValueBigDecimalChunkFilter(boolean matchesNull, BigDecimal value1, BigDecimal value2,
                BigDecimal value3) {
            this.matchesNull = matchesNull;
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
        }

        @Override
        public boolean matches(BigDecimal value) {
            return value == null ? matchesNull
                    : ObjectComparisons.compareEquals(value, value1) || ObjectComparisons.compareEquals(value, value2)
                            || ObjectComparisons.compareEquals(value, value3);
        }
    }

    private final static class InverseThreeValueBigDecimalChunkFilter extends ObjectChunkFilter<BigDecimal> {
        private final boolean matchesNull;
        private final BigDecimal value1;
        private final BigDecimal value2;
        private final BigDecimal value3;

        private InverseThreeValueBigDecimalChunkFilter(boolean matchesNull, BigDecimal value1, BigDecimal value2,
                BigDecimal value3) {
            this.matchesNull = matchesNull;
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
        }

        @Override
        public boolean matches(BigDecimal value) {
            return value == null ? !matchesNull
                    : !ObjectComparisons.compareEquals(value, value1) && !ObjectComparisons.compareEquals(value, value2)
                            && !ObjectComparisons.compareEquals(value, value3);
        }
    }

    /**
     * A filter over more than three values: the first {@code size} elements of {@code sortedValues}, the non-null
     * values, sorted in place by {@link BigDecimal#compareTo(BigDecimal)} so that a binary search, which compares the
     * same way, finds them. Any element after them is unused.
     */
    private static abstract class SortedValues extends ObjectChunkFilter<BigDecimal> {
        private final BigDecimal[] sorted;
        private final int size;
        private final boolean matchesNull;

        private SortedValues(final boolean matchesNull, final BigDecimal[] sortedValues, final int size) {
            Arrays.sort(sortedValues, 0, size);
            this.sorted = sortedValues;
            this.size = size;
            this.matchesNull = matchesNull;
        }

        final boolean contains(final BigDecimal value) {
            return value == null ? matchesNull : Arrays.binarySearch(sorted, 0, size, value) >= 0;
        }
    }

    private final static class MultiValueBigDecimalChunkFilter extends SortedValues {
        private MultiValueBigDecimalChunkFilter(boolean matchesNull, BigDecimal[] sortedValues, int size) {
            super(matchesNull, sortedValues, size);
        }

        @Override
        public boolean matches(BigDecimal value) {
            return contains(value);
        }
    }

    private final static class InverseMultiValueBigDecimalChunkFilter extends SortedValues {
        private InverseMultiValueBigDecimalChunkFilter(boolean matchesNull, BigDecimal[] sortedValues, int size) {
            super(matchesNull, sortedValues, size);
        }

        @Override
        public boolean matches(BigDecimal value) {
            return !contains(value);
        }
    }

    /**
     * The first {@code size} non-null values with their trailing zeros stripped, in a hash set. Two BigDecimals are
     * equal by {@link BigDecimal#compareTo(BigDecimal)} exactly when they are equal by {@code equals} once stripped, so
     * one strip and one hash lookup per row decide the match, where a binary search makes several {@code compareTo}
     * calls.
     */
    private static abstract class StrippedValues extends ObjectChunkFilter<BigDecimal> {
        private final ObjectOpenHashSet<BigDecimal> stripped;
        private final boolean matchesNull;

        private StrippedValues(final boolean matchesNull, final BigDecimal[] values, final int size) {
            this.stripped = new ObjectOpenHashSet<>(size);
            for (int ii = 0; ii < size; ++ii) {
                stripped.add(values[ii].stripTrailingZeros());
            }
            this.matchesNull = matchesNull;
        }

        final boolean contains(final BigDecimal value) {
            return value == null ? matchesNull : stripped.contains(value.stripTrailingZeros());
        }
    }

    private final static class HashBigDecimalChunkFilter extends StrippedValues {
        private HashBigDecimalChunkFilter(boolean matchesNull, BigDecimal[] values, int size) {
            super(matchesNull, values, size);
        }

        @Override
        public boolean matches(BigDecimal value) {
            return contains(value);
        }
    }

    private final static class InverseHashBigDecimalChunkFilter extends StrippedValues {
        private InverseHashBigDecimalChunkFilter(boolean matchesNull, BigDecimal[] values, int size) {
            super(matchesNull, values, size);
        }

        @Override
        public boolean matches(BigDecimal value) {
            return !contains(value);
        }
    }
}
