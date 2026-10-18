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
 * Creates chunk filters for byte values.
 * <p>
 * The strategy is that for one, two, or three values we have specialized classes that will do the appropriate simple
 * equality check.
 * <p>
 * For four or more values, {@link #makeFilter} chooses a structure that suits the type and the values. The 8 and 16 bit
 * types use a bitmap with a bit for every value of the type. The int and long factories use the structures in
 * {@code IntMultiValueChunkFilters} and {@code LongMultiValueChunkFilters} instead, which are maintained by hand.
 * <p>
 * Each filter carries its own copy of the {@link ByteChunkFilter} loops, so that its {@code matches} call is never a
 * virtual call shared with other filters.
 */
public class ByteChunkMatchFilterFactory {
    private ByteChunkMatchFilterFactory() {} // static use only

    public static ByteChunkFilter makeFilter(final MatchOptions matchOptions, final byte... values) {
        if (matchOptions.inverted()) {
            if (values.length == 1) {
                return new InverseSingleValueByteChunkFilter(values[0]);
            }
            if (values.length == 2) {
                return new InverseTwoValueByteChunkFilter(values[0], values[1]);
            }
            if (values.length == 3) {
                return new InverseThreeValueByteChunkFilter(values[0], values[1], values[2]);
            }
        } else {
            if (values.length == 1) {
                return new SingleValueByteChunkFilter(values[0]);
            }
            if (values.length == 2) {
                return new TwoValueByteChunkFilter(values[0], values[1]);
            }
            if (values.length == 3) {
                return new ThreeValueByteChunkFilter(values[0], values[1], values[2]);
            }
        }
        // region makeMultiValueFilter
        return matchOptions.inverted()
                ? new InverseBitmapByteChunkFilter(values)
                : new BitmapByteChunkFilter(values);
        // endregion makeMultiValueFilter
    }

    private final static class SingleValueByteChunkFilter extends ByteChunkFilter {
        private final byte value;

        private SingleValueByteChunkFilter(byte value) {
            this.value = value;
        }

        @Override
        public boolean matches(byte value) {
            return value == this.value;
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
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(byteChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    private final static class InverseSingleValueByteChunkFilter extends ByteChunkFilter {
        private final byte value;

        private InverseSingleValueByteChunkFilter(byte value) {
            this.value = value;
        }

        @Override
        public boolean matches(byte value) {
            return value != this.value;
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
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(byteChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    private final static class TwoValueByteChunkFilter extends ByteChunkFilter {
        private final byte value1;
        private final byte value2;

        private TwoValueByteChunkFilter(byte value1, byte value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        @Override
        public boolean matches(byte value) {
            return value == value1 || value == value2;
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
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(byteChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    private final static class InverseTwoValueByteChunkFilter extends ByteChunkFilter {
        private final byte value1;
        private final byte value2;

        private InverseTwoValueByteChunkFilter(byte value1, byte value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        @Override
        public boolean matches(byte value) {
            return value != value1 && value != value2;
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
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(byteChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    private final static class ThreeValueByteChunkFilter extends ByteChunkFilter {
        private final byte value1;
        private final byte value2;
        private final byte value3;

        private ThreeValueByteChunkFilter(byte value1, byte value2, byte value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
        }

        @Override
        public boolean matches(byte value) {
            return value == value1 || value == value2 || value == value3;
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
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(byteChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    private final static class InverseThreeValueByteChunkFilter extends ByteChunkFilter {
        private final byte value1;
        private final byte value2;
        private final byte value3;

        private InverseThreeValueByteChunkFilter(byte value1, byte value2, byte value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
        }

        @Override
        public boolean matches(byte value) {
            return value != value1 && value != value2 && value != value3;
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
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(byteChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    // region multiValueFilters
    /**
     * @return a bitmap with a bit for every byte value, set for each of {@code values}; null is one of the values a
     *         byte can hold, so it needs no special case
     */
    private static long[] makeBitmap(final byte... values) {
        final long[] bitmap = new long[(Byte.MAX_VALUE - Byte.MIN_VALUE + 1) >>> 6];
        for (final byte value : values) {
            final int index = value - Byte.MIN_VALUE;
            bitmap[index >>> 6] |= 1L << index;
        }
        return bitmap;
    }

    private static boolean bitmapContains(final long[] bitmap, final byte value) {
        final int index = value - Byte.MIN_VALUE;
        return (bitmap[index >>> 6] & (1L << index)) != 0;
    }

    private final static class BitmapByteChunkFilter extends ByteChunkFilter {
        private final long[] bitmap;

        private BitmapByteChunkFilter(byte... values) {
            this.bitmap = makeBitmap(values);
        }

        @Override
        public boolean matches(byte value) {
            return bitmapContains(bitmap, value);
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
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(byteChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    private final static class InverseBitmapByteChunkFilter extends ByteChunkFilter {
        private final long[] bitmap;

        private InverseBitmapByteChunkFilter(byte... values) {
            this.bitmap = makeBitmap(values);
        }

        @Override
        public boolean matches(byte value) {
            return !bitmapContains(bitmap, value);
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
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(byteChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }
    // endregion multiValueFilters
}
