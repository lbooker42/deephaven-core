//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
package io.deephaven.engine.table.impl.chunkfilter;

import io.deephaven.chunk.*;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.rowset.chunkattributes.OrderedRowKeys;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

import static io.deephaven.util.QueryConstants.NULL_INT;

/**
 * The int chunk filters for four or more values, which {@link IntChunkMatchFilterFactory} uses; it matches one to three
 * values itself. Maintained by hand, unlike that factory: the int and long structures differ in more than their types,
 * so ReplicateChunkFilters does not generate them.
 * <ul>
 * <li>A bitmap over the range of the values, when it would take no more memory than a cuckoo table of them. Each row
 * costs a range check and a bit test. The range check branches: few values make a narrow range, which most rows fall
 * outside of, or all inside of, so the branch predicts well.</li>
 * <li>Otherwise, a {@link IntCuckooSet}; or, if one cannot be built within its limits, a hash set. From four values the
 * table is level with or faster than scanning the values at low hit rates, and does not slow down when most rows match,
 * as a scan that stops at the first match does.</li>
 * </ul>
 * Each filter carries its own copy of the {@link IntChunkFilter} loops, so that its {@code matches} call is never a
 * virtual call shared with other filters.
 */
final class IntMultiValueChunkFilters {
    private IntMultiValueChunkFilters() {} // static use only

    /**
     * @param inverted whether the filter matches the rows that do not hold one of the values
     * @param values four or more values, which may repeat and may include
     *        {@link io.deephaven.util.QueryConstants#NULL_INT}
     * @return a filter that matches the rows holding one of {@code values}, or with {@code inverted}, the other rows
     */
    static IntChunkFilter makeFilter(final boolean inverted, final int... values) {
        final IntBitmap bitmap = IntBitmap.makeIfDense(values);
        if (bitmap != null) {
            return inverted ? new InverseDenseBitmapIntChunkFilter(bitmap) : new DenseBitmapIntChunkFilter(bitmap);
        }
        final IntCuckooSet set = IntCuckooSet.build(values);
        if (set == null) {
            return makeHashFilter(inverted, values);
        }
        return inverted ? new InverseCuckooIntChunkFilter(set) : new CuckooIntChunkFilter(set);
    }

    /**
     * @return a filter that matches with a hash set of {@code values}, for when no cuckoo table can be built
     */
    static IntChunkFilter makeHashFilter(final boolean inverted, final int... values) {
        return inverted ? new InverseHashIntChunkFilter(values) : new HashIntChunkFilter(values);
    }

    /**
     * A bit for every int from the least to the greatest of the values, set for each value. Null is the least int, so
     * it is kept out of the range, which it would otherwise stretch to the whole type, and matched with a flag.
     */
    static final class IntBitmap {
        private final int min;
        // the greatest value less min, at most Integer.MAX_VALUE
        private final int span;
        private final long[] bits;
        private final boolean matchesNull;

        private IntBitmap(final int min, final int span, final long[] bits, final boolean matchesNull) {
            this.min = min;
            this.span = span;
            this.bits = bits;
            this.matchesNull = matchesNull;
        }

        /**
         * @return a bitmap of {@code values}, or null if it would take more memory than a cuckoo table of them
         */
        static IntBitmap makeIfDense(final int[] values) {
            boolean matchesNull = false;
            boolean hasValue = false;
            int min = 0;
            int max = 0;
            for (final int value : values) {
                if (value == NULL_INT) {
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
            final long span = (long) max - min;
            final long cuckooBits = 8L * Integer.BYTES * IntCuckooSet.capacityFor(values.length);
            if (span >= cuckooBits || span > Integer.MAX_VALUE) {
                return null;
            }
            final long[] bits = new long[(int) (span >>> 6) + 1];
            for (final int value : values) {
                if (value != NULL_INT) {
                    final int offset = value - min;
                    bits[offset >>> 6] |= 1L << offset;
                }
            }
            return new IntBitmap(min, (int) span, bits, matchesNull);
        }

        boolean contains(final int value) {
            final int offset = value - min;
            // a value below min, or one so far above it that the subtraction wraps, is above span as unsigned
            if (Integer.compareUnsigned(offset, span) > 0) {
                return matchesNull && value == NULL_INT;
            }
            return (bits[offset >>> 6] & (1L << offset)) != 0;
        }
    }

    /**
     * Matches with an {@link IntBitmap}.
     */
    private static final class DenseBitmapIntChunkFilter extends IntChunkFilter {
        private final IntBitmap bitmap;

        private DenseBitmapIntChunkFilter(final IntBitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        public boolean matches(final int value) {
            return bitmap.contains(value);
        }

        // Identical code for all IntChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final IntChunk<? extends Values> intChunk = values.asIntChunk();
            final int len = intChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(intChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final IntChunk<? extends Values> intChunk = values.asIntChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(intChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final IntChunk<? extends Values> intChunk = values.asIntChunk();
            final int len = values.size();
            int count = 0;
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(intChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * The inverse of {@link DenseBitmapIntChunkFilter}.
     */
    private static final class InverseDenseBitmapIntChunkFilter extends IntChunkFilter {
        private final IntBitmap bitmap;

        private InverseDenseBitmapIntChunkFilter(final IntBitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        public boolean matches(final int value) {
            return !bitmap.contains(value);
        }

        // Identical code for all IntChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final IntChunk<? extends Values> intChunk = values.asIntChunk();
            final int len = intChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(intChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final IntChunk<? extends Values> intChunk = values.asIntChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(intChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final IntChunk<? extends Values> intChunk = values.asIntChunk();
            final int len = values.size();
            int count = 0;
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(intChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * Matches with a {@link IntCuckooSet} of the values.
     */
    private static final class CuckooIntChunkFilter extends IntChunkFilter {
        private final IntCuckooSet values;

        private CuckooIntChunkFilter(final IntCuckooSet values) {
            this.values = values;
        }

        @Override
        public boolean matches(final int value) {
            return values.contains(value);
        }

        // Identical code for all IntChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final IntChunk<? extends Values> intChunk = values.asIntChunk();
            final int len = intChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(intChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final IntChunk<? extends Values> intChunk = values.asIntChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(intChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final IntChunk<? extends Values> intChunk = values.asIntChunk();
            final int len = values.size();
            int count = 0;
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(intChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * The inverse of {@link CuckooIntChunkFilter}.
     */
    private static final class InverseCuckooIntChunkFilter extends IntChunkFilter {
        private final IntCuckooSet values;

        private InverseCuckooIntChunkFilter(final IntCuckooSet values) {
            this.values = values;
        }

        @Override
        public boolean matches(final int value) {
            return !values.contains(value);
        }

        // Identical code for all IntChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final IntChunk<? extends Values> intChunk = values.asIntChunk();
            final int len = intChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(intChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final IntChunk<? extends Values> intChunk = values.asIntChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(intChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final IntChunk<? extends Values> intChunk = values.asIntChunk();
            final int len = values.size();
            int count = 0;
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(intChunk.get(ii));
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
    private static final class HashIntChunkFilter extends IntChunkFilter {
        private final IntOpenHashSet values;

        private HashIntChunkFilter(final int[] values) {
            this.values = new IntOpenHashSet(values);
        }

        @Override
        public boolean matches(final int value) {
            return values.contains(value);
        }

        // Identical code for all IntChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final IntChunk<? extends Values> intChunk = values.asIntChunk();
            final int len = intChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(intChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final IntChunk<? extends Values> intChunk = values.asIntChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(intChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final IntChunk<? extends Values> intChunk = values.asIntChunk();
            final int len = values.size();
            int count = 0;
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(intChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }

    /**
     * The inverse of {@link HashIntChunkFilter}.
     */
    private static final class InverseHashIntChunkFilter extends IntChunkFilter {
        private final IntOpenHashSet values;

        private InverseHashIntChunkFilter(final int[] values) {
            this.values = new IntOpenHashSet(values);
        }

        @Override
        public boolean matches(final int value) {
            return !values.contains(value);
        }

        // Identical code for all IntChunkFilter classes, replicated here to prevent megamorphism in the JVM
        @Override
        public void filter(
                final Chunk<? extends Values> values,
                final LongChunk<OrderedRowKeys> keys,
                final WritableLongChunk<OrderedRowKeys> results) {
            final IntChunk<? extends Values> intChunk = values.asIntChunk();
            final int len = intChunk.size();

            results.setSize(0);
            for (int ii = 0; ii < len; ++ii) {
                if (matches(intChunk.get(ii))) {
                    results.add(keys.get(ii));
                }
            }
        }

        @Override
        public int filter(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final IntChunk<? extends Values> intChunk = values.asIntChunk();
            final int len = values.size();
            int count = 0;
            for (int ii = 0; ii < len; ++ii) {
                final boolean newResult = matches(intChunk.get(ii));
                results.set(ii, newResult);
                // count every true value
                count += newResult ? 1 : 0;
            }
            return count;
        }

        @Override
        public int filterAnd(final Chunk<? extends Values> values, final WritableBooleanChunk<Values> results) {
            final IntChunk<? extends Values> intChunk = values.asIntChunk();
            final int len = values.size();
            int count = 0;
            // Count the values that remain true
            for (int ii = 0; ii < len; ++ii) {
                final boolean result = results.get(ii);
                if (!result) {
                    // already false, no need to compute or increment the count
                    continue;
                }
                boolean newResult = matches(intChunk.get(ii));
                results.set(ii, newResult);
                // increment the count if the new result is TRUE
                count += newResult ? 1 : 0;
            }
            return count;
        }
    }
}
