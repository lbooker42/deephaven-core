//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
// @formatter:off
package io.deephaven.engine.table.impl.chunkfilter;

import io.deephaven.chunk.*;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.rowset.chunkattributes.OrderedRowKeys;
import io.deephaven.engine.table.MatchOptions;

import java.util.Arrays;

/**
 * Creates chunk filters for float values.
 * <p>
 * The strategy is that for one, two, or three values we have specialized classes that will do the appropriate simple
 * equality check.
 * <p>
 * For four or more values, the filters match the canonical bits of the values ({@link #getBits}) with an
 * {@code IntCuckooSet}; or, if one cannot be built within its limits, a hash set.
 * <p>
 * Each filter carries its own copy of the {@link FloatChunkFilter} loops, so that its {@code matches} call is never a
 * virtual call shared with other filters.
 */
public class FloatChunkMatchFilterFactory {
    private FloatChunkMatchFilterFactory() {} // static use only

    public static FloatChunkFilter makeFilter(final MatchOptions matchOptions, final float... values) {
        // The NaN-aware filters differ from the plain ones only in holding NaN equal to itself, so a one-time pass
        // over the search values is worth it to take the faster "==" path when none of them is NaN.
        if (matchOptions.nanMatch() && containsNaN(values)) {
            if (matchOptions.inverted()) {
                if (values.length == 1) {
                    return new InverseSingleValueNaNFloatChunkFilter(values[0]);
                }
                if (values.length == 2) {
                    return new InverseTwoValueNaNFloatChunkFilter(values[0], values[1]);
                }
                if (values.length == 3) {
                    return new InverseThreeValueNaNFloatChunkFilter(values[0], values[1], values[2]);
                }
                return makeMultiValueFilter(true, true, values);
            } else {
                if (values.length == 1) {
                    return new SingleValueNaNFloatChunkFilter(values[0]);
                }
                if (values.length == 2) {
                    return new TwoValueNaNFloatChunkFilter(values[0], values[1]);
                }
                if (values.length == 3) {
                    return new ThreeValueNaNFloatChunkFilter(values[0], values[1], values[2]);
                }
                return makeMultiValueFilter(false, true, values);
            }
        }

        if (matchOptions.inverted()) {
            if (values.length == 1) {
                return new InverseSingleValueFloatChunkFilter(values[0]);
            }
            if (values.length == 2) {
                return new InverseTwoValueFloatChunkFilter(values[0], values[1]);
            }
            if (values.length == 3) {
                return new InverseThreeValueFloatChunkFilter(values[0], values[1], values[2]);
            }
            return makeMultiValueFilter(true, false, values);
        } else {
            if (values.length == 1) {
                return new SingleValueFloatChunkFilter(values[0]);
            }
            if (values.length == 2) {
                return new TwoValueFloatChunkFilter(values[0], values[1]);
            }
            if (values.length == 3) {
                return new ThreeValueFloatChunkFilter(values[0], values[1], values[2]);
            }
            return makeMultiValueFilter(false, false, values);
        }
    }

    private final static class SingleValueFloatChunkFilter extends FloatChunkFilter {
        private final float value;

        private SingleValueFloatChunkFilter(float value) {
            this.value = value;
        }

        @Override
        public boolean matches(float value) {
            return value == this.value;
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

    private final static class InverseSingleValueFloatChunkFilter extends FloatChunkFilter {
        private final float value;

        private InverseSingleValueFloatChunkFilter(float value) {
            this.value = value;
        }

        @Override
        public boolean matches(float value) {
            return value != this.value;
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

    private final static class TwoValueFloatChunkFilter extends FloatChunkFilter {
        private final float value1;
        private final float value2;

        private TwoValueFloatChunkFilter(float value1, float value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        @Override
        public boolean matches(float value) {
            return value == value1 || value == value2;
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

    private final static class InverseTwoValueFloatChunkFilter extends FloatChunkFilter {
        private final float value1;
        private final float value2;

        private InverseTwoValueFloatChunkFilter(float value1, float value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        @Override
        public boolean matches(float value) {
            return value != value1 && value != value2;
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

    private final static class ThreeValueFloatChunkFilter extends FloatChunkFilter {
        private final float value1;
        private final float value2;
        private final float value3;

        private ThreeValueFloatChunkFilter(float value1, float value2, float value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
        }

        @Override
        public boolean matches(float value) {
            return value == value1 || value == value2 || value == value3;
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

    private final static class InverseThreeValueFloatChunkFilter extends FloatChunkFilter {
        private final float value1;
        private final float value2;
        private final float value3;

        private InverseThreeValueFloatChunkFilter(float value1, float value2, float value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
        }

        @Override
        public boolean matches(float value) {
            return value != value1 && value != value2 && value != value3;
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
     * @param inverted whether the filter matches the rows that do not hold one of the values
     * @param keepNaN whether NaN is one of the values to match; without it a NaN among the values matches nothing, as
     *        under IEEE 754
     * @param values four or more values
     */
    private static FloatChunkFilter makeMultiValueFilter(final boolean inverted, final boolean keepNaN,
            final float... values) {
        final int[] keptBits = new int[values.length];
        int size = 0;
        for (final float value : values) {
            if (keepNaN || !Float.isNaN(value)) {
                keptBits[size++] = getBits(value);
            }
        }
        final int[] valueBitsArray = Arrays.copyOf(keptBits, size);
        final IntCuckooSet set = IntCuckooSet.build(valueBitsArray);
        if (set == null) {
            return makeHashFilter(inverted, valueBitsArray);
        }
        return inverted ? new InverseCuckooFloatChunkFilter(set) : new CuckooFloatChunkFilter(set);
    }

    /**
     * @return a filter that matches with a hash set of canonical bits, for when no cuckoo table can be built
     */
    static FloatChunkFilter makeHashFilter(final boolean inverted, final int[] hashedBits) {
        final IntOpenHashSet set = new IntOpenHashSet(hashedBits);
        return inverted ? new InverseHashFloatChunkFilter(set) : new HashFloatChunkFilter(set);
    }

    /**
     * Matches with a cuckoo table of the values' canonical bits.
     */
    private final static class CuckooFloatChunkFilter extends FloatChunkFilter {
        private final IntCuckooSet set;

        private CuckooFloatChunkFilter(final IntCuckooSet set) {
            this.set = set;
        }

        @Override
        public boolean matches(float value) {
            return set.contains(getBits(value));
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
     * The inverse of {@link CuckooFloatChunkFilter}.
     */
    private final static class InverseCuckooFloatChunkFilter extends FloatChunkFilter {
        private final IntCuckooSet set;

        private InverseCuckooFloatChunkFilter(final IntCuckooSet set) {
            this.set = set;
        }

        @Override
        public boolean matches(float value) {
            return !set.contains(getBits(value));
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
     * Matches with a hash set of the values' canonical bits.
     */
    private final static class HashFloatChunkFilter extends FloatChunkFilter {
        private final IntOpenHashSet set;

        private HashFloatChunkFilter(final IntOpenHashSet set) {
            this.set = set;
        }

        @Override
        public boolean matches(float value) {
            return set.contains(getBits(value));
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
     * The inverse of {@link HashFloatChunkFilter}.
     */
    private final static class InverseHashFloatChunkFilter extends FloatChunkFilter {
        private final IntOpenHashSet set;

        private InverseHashFloatChunkFilter(final IntOpenHashSet set) {
            this.set = set;
        }

        @Override
        public boolean matches(float value) {
            return !set.contains(getBits(value));
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

    private static boolean containsNaN(final float... values) {
        for (final float value : values) {
            if (Float.isNaN(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the canonicalized bit pattern for the given value. Specifically, ensures that any NaN values have the bit
     * pattern of {@link Float#NaN}, and -0.0 has the bit pattern of 0.0.
     */
    // region getBits
    public static int getBits(float value) {
        return Float.floatToIntBits(value == 0.0f ? 0.0f : value);
    }
    // endregion getBits

    private final static class SingleValueNaNFloatChunkFilter extends FloatChunkFilter {
        private final int valueBits;

        private SingleValueNaNFloatChunkFilter(float value) {
            valueBits = getBits(value);
        }

        @Override
        public boolean matches(float value) {
            return valueBits == getBits(value);
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

    private final static class InverseSingleValueNaNFloatChunkFilter extends FloatChunkFilter {
        private final int valueBits;

        private InverseSingleValueNaNFloatChunkFilter(float value) {
            valueBits = getBits(value);
        }

        @Override
        public boolean matches(float value) {
            return valueBits != getBits(value);
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

    private final static class TwoValueNaNFloatChunkFilter extends FloatChunkFilter {
        private final int valueBits1;
        private final int valueBits2;

        private TwoValueNaNFloatChunkFilter(float value1, float value2) {
            this.valueBits1 = getBits(value1);
            this.valueBits2 = getBits(value2);
        }

        @Override
        public boolean matches(float value) {
            final int valueBits = getBits(value);
            return valueBits == valueBits1 || valueBits == valueBits2;
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

    private final static class InverseTwoValueNaNFloatChunkFilter extends FloatChunkFilter {
        private final int valueBits1;
        private final int valueBits2;

        private InverseTwoValueNaNFloatChunkFilter(float value1, float value2) {
            this.valueBits1 = getBits(value1);
            this.valueBits2 = getBits(value2);
        }

        @Override
        public boolean matches(float value) {
            final int valueBits = getBits(value);
            return valueBits != valueBits1 && valueBits != valueBits2;
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

    private final static class ThreeValueNaNFloatChunkFilter extends FloatChunkFilter {
        private final int valueBits1;
        private final int valueBits2;
        private final int valueBits3;

        private ThreeValueNaNFloatChunkFilter(float value1, float value2, float value3) {
            this.valueBits1 = getBits(value1);
            this.valueBits2 = getBits(value2);
            this.valueBits3 = getBits(value3);
        }

        @Override
        public boolean matches(float value) {
            final int valueBits = getBits(value);
            return valueBits == valueBits1 || valueBits == valueBits2 || valueBits == valueBits3;
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

    private final static class InverseThreeValueNaNFloatChunkFilter extends FloatChunkFilter {
        private final int valueBits1;
        private final int valueBits2;
        private final int valueBits3;

        private InverseThreeValueNaNFloatChunkFilter(float value1, float value2, float value3) {
            this.valueBits1 = getBits(value1);
            this.valueBits2 = getBits(value2);
            this.valueBits3 = getBits(value3);
        }

        @Override
        public boolean matches(float value) {
            final int valueBits = getBits(value);
            return valueBits != valueBits1 && valueBits != valueBits2 && valueBits != valueBits3;
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
}
