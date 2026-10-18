//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
// ****** AUTO-GENERATED CLASS - DO NOT EDIT MANUALLY
// ****** Edit FloatChunkMatchFilterFactory and run "./gradlew replicateChunkFilters" to regenerate
//
// @formatter:off
package io.deephaven.engine.table.impl.chunkfilter;

import io.deephaven.chunk.*;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.rowset.chunkattributes.OrderedRowKeys;
import io.deephaven.engine.table.MatchOptions;

import java.util.Arrays;

/**
 * Creates chunk filters for double values.
 * <p>
 * The strategy is that for one, two, or three values we have specialized classes that will do the appropriate simple
 * equality check.
 * <p>
 * For four or more values, the filters match the canonical bits of the values ({@link #getBits}) with an
 * {@code LongCuckooSet}; or, if one cannot be built within its limits, a hash set.
 * <p>
 * Each filter carries its own copy of the {@link DoubleChunkFilter} loops, so that its {@code matches} call is never a
 * virtual call shared with other filters.
 */
public class DoubleChunkMatchFilterFactory {
    private DoubleChunkMatchFilterFactory() {} // static use only

    public static DoubleChunkFilter makeFilter(final MatchOptions matchOptions, final double... values) {
        // The NaN-aware filters differ from the plain ones only in holding NaN equal to itself, so a one-time pass
        // over the search values is worth it to take the faster "==" path when none of them is NaN.
        if (matchOptions.nanMatch() && containsNaN(values)) {
            if (matchOptions.inverted()) {
                if (values.length == 1) {
                    return new InverseSingleValueNaNDoubleChunkFilter(values[0]);
                }
                if (values.length == 2) {
                    return new InverseTwoValueNaNDoubleChunkFilter(values[0], values[1]);
                }
                if (values.length == 3) {
                    return new InverseThreeValueNaNDoubleChunkFilter(values[0], values[1], values[2]);
                }
                return makeMultiValueFilter(true, true, values);
            } else {
                if (values.length == 1) {
                    return new SingleValueNaNDoubleChunkFilter(values[0]);
                }
                if (values.length == 2) {
                    return new TwoValueNaNDoubleChunkFilter(values[0], values[1]);
                }
                if (values.length == 3) {
                    return new ThreeValueNaNDoubleChunkFilter(values[0], values[1], values[2]);
                }
                return makeMultiValueFilter(false, true, values);
            }
        }

        if (matchOptions.inverted()) {
            if (values.length == 1) {
                return new InverseSingleValueDoubleChunkFilter(values[0]);
            }
            if (values.length == 2) {
                return new InverseTwoValueDoubleChunkFilter(values[0], values[1]);
            }
            if (values.length == 3) {
                return new InverseThreeValueDoubleChunkFilter(values[0], values[1], values[2]);
            }
            return makeMultiValueFilter(true, false, values);
        } else {
            if (values.length == 1) {
                return new SingleValueDoubleChunkFilter(values[0]);
            }
            if (values.length == 2) {
                return new TwoValueDoubleChunkFilter(values[0], values[1]);
            }
            if (values.length == 3) {
                return new ThreeValueDoubleChunkFilter(values[0], values[1], values[2]);
            }
            return makeMultiValueFilter(false, false, values);
        }
    }

    private final static class SingleValueDoubleChunkFilter extends DoubleChunkFilter {
        private final double value;

        private SingleValueDoubleChunkFilter(double value) {
            this.value = value;
        }

        @Override
        public boolean matches(double value) {
            return value == this.value;
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

    private final static class InverseSingleValueDoubleChunkFilter extends DoubleChunkFilter {
        private final double value;

        private InverseSingleValueDoubleChunkFilter(double value) {
            this.value = value;
        }

        @Override
        public boolean matches(double value) {
            return value != this.value;
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

    private final static class TwoValueDoubleChunkFilter extends DoubleChunkFilter {
        private final double value1;
        private final double value2;

        private TwoValueDoubleChunkFilter(double value1, double value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        @Override
        public boolean matches(double value) {
            return value == value1 || value == value2;
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

    private final static class InverseTwoValueDoubleChunkFilter extends DoubleChunkFilter {
        private final double value1;
        private final double value2;

        private InverseTwoValueDoubleChunkFilter(double value1, double value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        @Override
        public boolean matches(double value) {
            return value != value1 && value != value2;
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

    private final static class ThreeValueDoubleChunkFilter extends DoubleChunkFilter {
        private final double value1;
        private final double value2;
        private final double value3;

        private ThreeValueDoubleChunkFilter(double value1, double value2, double value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
        }

        @Override
        public boolean matches(double value) {
            return value == value1 || value == value2 || value == value3;
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

    private final static class InverseThreeValueDoubleChunkFilter extends DoubleChunkFilter {
        private final double value1;
        private final double value2;
        private final double value3;

        private InverseThreeValueDoubleChunkFilter(double value1, double value2, double value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
        }

        @Override
        public boolean matches(double value) {
            return value != value1 && value != value2 && value != value3;
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
     * @param inverted whether the filter matches the rows that do not hold one of the values
     * @param keepNaN whether NaN is one of the values to match; without it a NaN among the values matches nothing, as
     *        under IEEE 754
     * @param values four or more values
     */
    private static DoubleChunkFilter makeMultiValueFilter(final boolean inverted, final boolean keepNaN,
            final double... values) {
        final long[] keptBits = new long[values.length];
        int size = 0;
        for (final double value : values) {
            if (keepNaN || !Double.isNaN(value)) {
                keptBits[size++] = getBits(value);
            }
        }
        final long[] valueBitsArray = Arrays.copyOf(keptBits, size);
        final LongCuckooSet set = LongCuckooSet.build(valueBitsArray);
        if (set == null) {
            return makeHashFilter(inverted, valueBitsArray);
        }
        return inverted ? new InverseCuckooDoubleChunkFilter(set) : new CuckooDoubleChunkFilter(set);
    }

    /**
     * @return a filter that matches with a hash set of canonical bits, for when no cuckoo table can be built
     */
    static DoubleChunkFilter makeHashFilter(final boolean inverted, final long[] hashedBits) {
        final LongOpenHashSet set = new LongOpenHashSet(hashedBits);
        return inverted ? new InverseHashDoubleChunkFilter(set) : new HashDoubleChunkFilter(set);
    }

    /**
     * Matches with a cuckoo table of the values' canonical bits.
     */
    private final static class CuckooDoubleChunkFilter extends DoubleChunkFilter {
        private final LongCuckooSet set;

        private CuckooDoubleChunkFilter(final LongCuckooSet set) {
            this.set = set;
        }

        @Override
        public boolean matches(double value) {
            return set.contains(getBits(value));
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
     * The inverse of {@link CuckooDoubleChunkFilter}.
     */
    private final static class InverseCuckooDoubleChunkFilter extends DoubleChunkFilter {
        private final LongCuckooSet set;

        private InverseCuckooDoubleChunkFilter(final LongCuckooSet set) {
            this.set = set;
        }

        @Override
        public boolean matches(double value) {
            return !set.contains(getBits(value));
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
     * Matches with a hash set of the values' canonical bits.
     */
    private final static class HashDoubleChunkFilter extends DoubleChunkFilter {
        private final LongOpenHashSet set;

        private HashDoubleChunkFilter(final LongOpenHashSet set) {
            this.set = set;
        }

        @Override
        public boolean matches(double value) {
            return set.contains(getBits(value));
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
     * The inverse of {@link HashDoubleChunkFilter}.
     */
    private final static class InverseHashDoubleChunkFilter extends DoubleChunkFilter {
        private final LongOpenHashSet set;

        private InverseHashDoubleChunkFilter(final LongOpenHashSet set) {
            this.set = set;
        }

        @Override
        public boolean matches(double value) {
            return !set.contains(getBits(value));
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

    private static boolean containsNaN(final double... values) {
        for (final double value : values) {
            if (Double.isNaN(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the canonicalized bit pattern for the given value. Specifically, ensures that any NaN values have the bit
     * pattern of {@link Double#NaN}, and -0.0 has the bit pattern of 0.0.
     */
    // region getBits
    public static long getBits(double value) {
        return Double.doubleToLongBits(value == 0.0d ? 0.0d : value);
    }

    // endregion getBits

    private final static class SingleValueNaNDoubleChunkFilter extends DoubleChunkFilter {
        private final long valueBits;

        private SingleValueNaNDoubleChunkFilter(double value) {
            valueBits = getBits(value);
        }

        @Override
        public boolean matches(double value) {
            return valueBits == getBits(value);
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

    private final static class InverseSingleValueNaNDoubleChunkFilter extends DoubleChunkFilter {
        private final long valueBits;

        private InverseSingleValueNaNDoubleChunkFilter(double value) {
            valueBits = getBits(value);
        }

        @Override
        public boolean matches(double value) {
            return valueBits != getBits(value);
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

    private final static class TwoValueNaNDoubleChunkFilter extends DoubleChunkFilter {
        private final long valueBits1;
        private final long valueBits2;

        private TwoValueNaNDoubleChunkFilter(double value1, double value2) {
            this.valueBits1 = getBits(value1);
            this.valueBits2 = getBits(value2);
        }

        @Override
        public boolean matches(double value) {
            final long valueBits = getBits(value);
            return valueBits == valueBits1 || valueBits == valueBits2;
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

    private final static class InverseTwoValueNaNDoubleChunkFilter extends DoubleChunkFilter {
        private final long valueBits1;
        private final long valueBits2;

        private InverseTwoValueNaNDoubleChunkFilter(double value1, double value2) {
            this.valueBits1 = getBits(value1);
            this.valueBits2 = getBits(value2);
        }

        @Override
        public boolean matches(double value) {
            final long valueBits = getBits(value);
            return valueBits != valueBits1 && valueBits != valueBits2;
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

    private final static class ThreeValueNaNDoubleChunkFilter extends DoubleChunkFilter {
        private final long valueBits1;
        private final long valueBits2;
        private final long valueBits3;

        private ThreeValueNaNDoubleChunkFilter(double value1, double value2, double value3) {
            this.valueBits1 = getBits(value1);
            this.valueBits2 = getBits(value2);
            this.valueBits3 = getBits(value3);
        }

        @Override
        public boolean matches(double value) {
            final long valueBits = getBits(value);
            return valueBits == valueBits1 || valueBits == valueBits2 || valueBits == valueBits3;
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

    private final static class InverseThreeValueNaNDoubleChunkFilter extends DoubleChunkFilter {
        private final long valueBits1;
        private final long valueBits2;
        private final long valueBits3;

        private InverseThreeValueNaNDoubleChunkFilter(double value1, double value2, double value3) {
            this.valueBits1 = getBits(value1);
            this.valueBits2 = getBits(value2);
            this.valueBits3 = getBits(value3);
        }

        @Override
        public boolean matches(double value) {
            final long valueBits = getBits(value);
            return valueBits != valueBits1 && valueBits != valueBits2 && valueBits != valueBits3;
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
}
