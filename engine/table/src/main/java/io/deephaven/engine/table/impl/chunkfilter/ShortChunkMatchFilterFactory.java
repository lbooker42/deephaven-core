//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
// ****** AUTO-GENERATED CLASS - DO NOT EDIT MANUALLY
// ****** Edit CharChunkMatchFilterFactory and run "./gradlew replicateChunkFilters" to regenerate
//
// @formatter:off
package io.deephaven.engine.table.impl.chunkfilter;

import io.deephaven.chunk.*;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.rowset.chunkattributes.OrderedRowKeys;
import io.deephaven.engine.table.MatchOptions;

/**
 * Creates chunk filters for short values.
 * <p>
 * The strategy is that for one, two, or three values we have specialized classes that will do the appropriate simple
 * equality check.
 * <p>
 * For four or more values, {@link #makeFilter} chooses a structure that suits the type and the values. The 8 and 16 bit
 * types use a bitmap with a bit for every value of the type. The int and long factories use the structures in
 * {@code IntMultiValueChunkFilters} and {@code LongMultiValueChunkFilters} instead, which are maintained by hand.
 * <p>
 * Each filter carries its own copy of the {@link ShortChunkFilter} loops, so that its {@code matches} call is never a
 * virtual call shared with other filters.
 */
public class ShortChunkMatchFilterFactory {
    private ShortChunkMatchFilterFactory() {} // static use only

    public static ShortChunkFilter makeFilter(final MatchOptions matchOptions, final short... values) {
        if (matchOptions.inverted()) {
            if (values.length == 1) {
                return new InverseSingleValueShortChunkFilter(values[0]);
            }
            if (values.length == 2) {
                return new InverseTwoValueShortChunkFilter(values[0], values[1]);
            }
            if (values.length == 3) {
                return new InverseThreeValueShortChunkFilter(values[0], values[1], values[2]);
            }
        } else {
            if (values.length == 1) {
                return new SingleValueShortChunkFilter(values[0]);
            }
            if (values.length == 2) {
                return new TwoValueShortChunkFilter(values[0], values[1]);
            }
            if (values.length == 3) {
                return new ThreeValueShortChunkFilter(values[0], values[1], values[2]);
            }
        }
        // region makeMultiValueFilter
        return matchOptions.inverted()
                ? new InverseBitmapShortChunkFilter(values)
                : new BitmapShortChunkFilter(values);
        // endregion makeMultiValueFilter
    }

    private final static class SingleValueShortChunkFilter extends ShortChunkFilter {
        private final short value;

        private SingleValueShortChunkFilter(short value) {
            this.value = value;
        }

        @Override
        public boolean matches(short value) {
            return value == this.value;
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
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(shortChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    private final static class InverseSingleValueShortChunkFilter extends ShortChunkFilter {
        private final short value;

        private InverseSingleValueShortChunkFilter(short value) {
            this.value = value;
        }

        @Override
        public boolean matches(short value) {
            return value != this.value;
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
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(shortChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    private final static class TwoValueShortChunkFilter extends ShortChunkFilter {
        private final short value1;
        private final short value2;

        private TwoValueShortChunkFilter(short value1, short value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        @Override
        public boolean matches(short value) {
            return value == value1 || value == value2;
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
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(shortChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    private final static class InverseTwoValueShortChunkFilter extends ShortChunkFilter {
        private final short value1;
        private final short value2;

        private InverseTwoValueShortChunkFilter(short value1, short value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        @Override
        public boolean matches(short value) {
            return value != value1 && value != value2;
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
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(shortChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    private final static class ThreeValueShortChunkFilter extends ShortChunkFilter {
        private final short value1;
        private final short value2;
        private final short value3;

        private ThreeValueShortChunkFilter(short value1, short value2, short value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
        }

        @Override
        public boolean matches(short value) {
            return value == value1 || value == value2 || value == value3;
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
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(shortChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    private final static class InverseThreeValueShortChunkFilter extends ShortChunkFilter {
        private final short value1;
        private final short value2;
        private final short value3;

        private InverseThreeValueShortChunkFilter(short value1, short value2, short value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
        }

        @Override
        public boolean matches(short value) {
            return value != value1 && value != value2 && value != value3;
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
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(shortChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    // region multiValueFilters
    /**
     * @return a bitmap with a bit for every short value, set for each of {@code values}; null is one of the values a
     *         short can hold, so it needs no special case
     */
    private static long[] makeBitmap(final short... values) {
        final long[] bitmap = new long[(Short.MAX_VALUE - Short.MIN_VALUE + 1) >>> 6];
        for (final short value : values) {
            final int index = value - Short.MIN_VALUE;
            bitmap[index >>> 6] |= 1L << index;
        }
        return bitmap;
    }

    private static boolean bitmapContains(final long[] bitmap, final short value) {
        final int index = value - Short.MIN_VALUE;
        return (bitmap[index >>> 6] & (1L << index)) != 0;
    }

    private final static class BitmapShortChunkFilter extends ShortChunkFilter {
        private final long[] bitmap;

        private BitmapShortChunkFilter(short... values) {
            this.bitmap = makeBitmap(values);
        }

        @Override
        public boolean matches(short value) {
            return bitmapContains(bitmap, value);
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
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(shortChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    private final static class InverseBitmapShortChunkFilter extends ShortChunkFilter {
        private final long[] bitmap;

        private InverseBitmapShortChunkFilter(short... values) {
            this.bitmap = makeBitmap(values);
        }

        @Override
        public boolean matches(short value) {
            return !bitmapContains(bitmap, value);
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
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(shortChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }
    // endregion multiValueFilters
}
