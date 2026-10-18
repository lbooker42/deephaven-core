//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
package io.deephaven.engine.table.impl.chunkfilter;

import io.deephaven.chunk.*;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.rowset.chunkattributes.OrderedRowKeys;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;

import static io.deephaven.util.QueryConstants.NULL_LONG;

/**
 * The long chunk filters for four or more values, which {@link LongChunkMatchFilterFactory} uses; it matches one to
 * three values itself. Maintained by hand, unlike that factory: the int and long structures differ in more than their
 * types, so ReplicateChunkFilters does not generate them.
 * <ul>
 * <li>A bitmap over the range of the values, when it would take no more memory than a cuckoo table of them. Each row
 * costs a range check and a bit test. The range check branches: few values make a narrow range, which most rows fall
 * outside of, or all inside of, so the branch predicts well.</li>
 * <li>Otherwise, a {@link LongCuckooSet}; or, if one cannot be built within its limits, a hash set. From four values
 * the table is level with or faster than scanning the values at low hit rates, and does not slow down when most rows
 * match, as a scan that stops at the first match does.</li>
 * </ul>
 * Each filter carries its own copy of the {@link LongChunkFilter} loops, so that its {@code matches} call is never a
 * virtual call shared with other filters.
 */
final class LongMultiValueChunkFilters {
    private LongMultiValueChunkFilters() {} // static use only

    /**
     * @param inverted whether the filter matches the rows that do not hold one of the values
     * @param values four or more values, which may repeat and may include
     *        {@link io.deephaven.util.QueryConstants#NULL_LONG}
     * @return a filter that matches the rows holding one of {@code values}, or with {@code inverted}, the other rows
     */
    static LongChunkFilter makeFilter(final boolean inverted, final long... values) {
        final LongBitmap bitmap = LongBitmap.makeIfDense(values);
        if (bitmap != null) {
            return inverted ? new InverseDenseBitmapLongChunkFilter(bitmap) : new DenseBitmapLongChunkFilter(bitmap);
        }
        final LongCuckooSet set = LongCuckooSet.build(values);
        if (set == null) {
            return makeHashFilter(inverted, values);
        }
        return inverted ? new InverseCuckooLongChunkFilter(set) : new CuckooLongChunkFilter(set);
    }

    /**
     * @return a filter that matches with a hash set of {@code values}, for when no cuckoo table can be built
     */
    static LongChunkFilter makeHashFilter(final boolean inverted, final long... values) {
        return inverted ? new InverseHashLongChunkFilter(values) : new HashLongChunkFilter(values);
    }

    /**
     * A bit for every long from the least to the greatest of the values, set for each value. Null is the least long, so
     * it is kept out of the range, which it would otherwise stretch to the whole type, and matched with a flag.
     */
    static final class LongBitmap {
        private final long min;
        // the greatest value less min, at most Integer.MAX_VALUE
        private final long span;
        private final long[] bits;
        private final boolean matchesNull;

        private LongBitmap(final long min, final long span, final long[] bits, final boolean matchesNull) {
            this.min = min;
            this.span = span;
            this.bits = bits;
            this.matchesNull = matchesNull;
        }

        /**
         * @return a bitmap of {@code values}, or null if it would take more memory than a cuckoo table of them
         */
        static LongBitmap makeIfDense(final long[] values) {
            boolean matchesNull = false;
            boolean hasValue = false;
            long min = 0;
            long max = 0;
            for (final long value : values) {
                if (value == NULL_LONG) {
                    matchesNull = true;
                } else if (!hasValue) {
                    min = max = value;
                    hasValue = true;
                } else if (value < min) {
                    min = value;
                } else if (value > max) {
                    max = value;
                }
            }
            if (!hasValue) {
                return null;
            }
            // negative when the range is too wide for a long, which is far too wide for a bitmap
            final long span = max - min;
            final long cuckooBits = 8L * Long.BYTES * LongCuckooSet.capacityFor(values.length);
            if (span < 0 || span >= cuckooBits || span > Integer.MAX_VALUE) {
                return null;
            }
            final long[] bits = new long[(int) (span >>> 6) + 1];
            for (final long value : values) {
                if (value != NULL_LONG) {
                    final long offset = value - min;
                    bits[(int) (offset >>> 6)] |= 1L << offset;
                }
            }
            return new LongBitmap(min, span, bits, matchesNull);
        }

        boolean contains(final long value) {
            final long offset = value - min;
            // a value below min, or one so far above it that the subtraction wraps, is above span as unsigned
            if (Long.compareUnsigned(offset, span) > 0) {
                return matchesNull && value == NULL_LONG;
            }
            return (bits[(int) (offset >>> 6)] & (1L << offset)) != 0;
        }
    }

    /**
     * Matches with an {@link LongBitmap}.
     */
    private static final class DenseBitmapLongChunkFilter extends LongChunkFilter {
        private final LongBitmap bitmap;

        private DenseBitmapLongChunkFilter(final LongBitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        public boolean matches(final long value) {
            return bitmap.contains(value);
        }

        // Identical code for all LongChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final LongChunk<? extends Values> longChunk = values.asLongChunk();
            final int len = longChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(longChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final LongChunk<? extends Values> longChunk = values.asLongChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(longChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final LongChunk<? extends Values> longChunk = values.asLongChunk();
            final int len = values.size();
            int count = 0;
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(longChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * The inverse of {@link DenseBitmapLongChunkFilter}.
     */
    private static final class InverseDenseBitmapLongChunkFilter extends LongChunkFilter {
        private final LongBitmap bitmap;

        private InverseDenseBitmapLongChunkFilter(final LongBitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        public boolean matches(final long value) {
            return !bitmap.contains(value);
        }

        // Identical code for all LongChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final LongChunk<? extends Values> longChunk = values.asLongChunk();
            final int len = longChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(longChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final LongChunk<? extends Values> longChunk = values.asLongChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(longChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final LongChunk<? extends Values> longChunk = values.asLongChunk();
            final int len = values.size();
            int count = 0;
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(longChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * Matches with a {@link LongCuckooSet} of the values.
     */
    private static final class CuckooLongChunkFilter extends LongChunkFilter {
        private final LongCuckooSet values;

        private CuckooLongChunkFilter(final LongCuckooSet values) {
            this.values = values;
        }

        @Override
        public boolean matches(final long value) {
            return values.contains(value);
        }

        // Identical code for all LongChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final LongChunk<? extends Values> longChunk = values.asLongChunk();
            final int len = longChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(longChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final LongChunk<? extends Values> longChunk = values.asLongChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(longChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final LongChunk<? extends Values> longChunk = values.asLongChunk();
            final int len = values.size();
            int count = 0;
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(longChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * The inverse of {@link CuckooLongChunkFilter}.
     */
    private static final class InverseCuckooLongChunkFilter extends LongChunkFilter {
        private final LongCuckooSet values;

        private InverseCuckooLongChunkFilter(final LongCuckooSet values) {
            this.values = values;
        }

        @Override
        public boolean matches(final long value) {
            return !values.contains(value);
        }

        // Identical code for all LongChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final LongChunk<? extends Values> longChunk = values.asLongChunk();
            final int len = longChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(longChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final LongChunk<? extends Values> longChunk = values.asLongChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(longChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final LongChunk<? extends Values> longChunk = values.asLongChunk();
            final int len = values.size();
            int count = 0;
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(longChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * Matches with a hash set of the values.
     */
    private static final class HashLongChunkFilter extends LongChunkFilter {
        private final LongOpenHashSet values;

        private HashLongChunkFilter(final long[] values) {
            this.values = new LongOpenHashSet(values);
        }

        @Override
        public boolean matches(final long value) {
            return values.contains(value);
        }

        // Identical code for all LongChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final LongChunk<? extends Values> longChunk = values.asLongChunk();
            final int len = longChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(longChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final LongChunk<? extends Values> longChunk = values.asLongChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(longChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final LongChunk<? extends Values> longChunk = values.asLongChunk();
            final int len = values.size();
            int count = 0;
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(longChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * The inverse of {@link HashLongChunkFilter}.
     */
    private static final class InverseHashLongChunkFilter extends LongChunkFilter {
        private final LongOpenHashSet values;

        private InverseHashLongChunkFilter(final long[] values) {
            this.values = new LongOpenHashSet(values);
        }

        @Override
        public boolean matches(final long value) {
            return !values.contains(value);
        }

        // Identical code for all LongChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final LongChunk<? extends Values> longChunk = values.asLongChunk();
            final int len = longChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(longChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final LongChunk<? extends Values> longChunk = values.asLongChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(longChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final LongChunk<? extends Values> longChunk = values.asLongChunk();
            final int len = values.size();
            int count = 0;
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(longChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }
}
