//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
package io.deephaven.engine.table.impl.chunkfilter;

import io.deephaven.chunk.WritableByteChunk;
import io.deephaven.chunk.WritableCharChunk;
import io.deephaven.chunk.WritableChunk;
import io.deephaven.chunk.WritableIntChunk;
import io.deephaven.chunk.WritableLongChunk;
import io.deephaven.chunk.WritableShortChunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.table.MatchOptions;
import io.deephaven.util.QueryConstants;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Checks the match filters for byte, char, short, int and long against a plain set of the values, for every count at
 * which the factory changes structure, on value sets that are dense and sparse, that include null, repeat, or reach the
 * extremes of the type, and for regular and inverted filters. Each filter has its own copy of the three loops, so all
 * three are checked.
 */
public class IntegralChunkMatchFilterFactoryTest {

    private static final int[] COUNTS = {1, 2, 3, 4, 5, 6, 7, 8, 9, 15, 16, 17, 32, 33, 47, 48, 49, 100, 1000};

    private enum Type {
        BYTE(byte.class, Byte.MIN_VALUE, Byte.MAX_VALUE, QueryConstants.NULL_BYTE), CHAR(char.class,
                Character.MIN_VALUE, Character.MAX_VALUE, QueryConstants.NULL_CHAR), SHORT(short.class, Short.MIN_VALUE,
                        Short.MAX_VALUE, QueryConstants.NULL_SHORT), INT(int.class, Integer.MIN_VALUE,
                                Integer.MAX_VALUE, QueryConstants.NULL_INT), LONG(long.class, Long.MIN_VALUE,
                                        Long.MAX_VALUE, QueryConstants.NULL_LONG);

        private final Class<?> type;
        private final long min;
        private final long max;
        private final long nullValue;

        Type(final Class<?> type, final long min, final long max, final long nullValue) {
            this.type = type;
            this.min = min;
            this.max = max;
            this.nullValue = nullValue;
        }

        /**
         * @return the least and greatest values that are not null
         */
        private long first() {
            return nullValue == min ? min + 1 : min;
        }

        private long last() {
            return nullValue == max ? max - 1 : max;
        }

        private boolean contains(final long key) {
            return key >= min && key <= max;
        }

        private Object box(final long key) {
            if (key == nullValue) {
                // as MatchFilter passes a null value; the factory unboxes it to the type's null
                return null;
            }
            switch (this) {
                case BYTE:
                    return (byte) key;
                case CHAR:
                    return (char) key;
                case SHORT:
                    return (short) key;
                case INT:
                    return (int) key;
                default:
                    return key;
            }
        }

        private WritableChunk<Values> chunk(final long[] keys) {
            switch (this) {
                case BYTE: {
                    final WritableByteChunk<Values> chunk = WritableByteChunk.makeWritableChunk(keys.length);
                    for (int ii = 0; ii < keys.length; ++ii) {
                        chunk.set(ii, (byte) keys[ii]);
                    }
                    return chunk;
                }
                case CHAR: {
                    final WritableCharChunk<Values> chunk = WritableCharChunk.makeWritableChunk(keys.length);
                    for (int ii = 0; ii < keys.length; ++ii) {
                        chunk.set(ii, (char) keys[ii]);
                    }
                    return chunk;
                }
                case SHORT: {
                    final WritableShortChunk<Values> chunk = WritableShortChunk.makeWritableChunk(keys.length);
                    for (int ii = 0; ii < keys.length; ++ii) {
                        chunk.set(ii, (short) keys[ii]);
                    }
                    return chunk;
                }
                case INT: {
                    final WritableIntChunk<Values> chunk = WritableIntChunk.makeWritableChunk(keys.length);
                    for (int ii = 0; ii < keys.length; ++ii) {
                        chunk.set(ii, (int) keys[ii]);
                    }
                    return chunk;
                }
                default: {
                    final WritableLongChunk<Values> chunk = WritableLongChunk.makeWritableChunk(keys.length);
                    for (int ii = 0; ii < keys.length; ++ii) {
                        chunk.set(ii, keys[ii]);
                    }
                    return chunk;
                }
            }
        }
    }

    @Test
    public void testMatchesPlainSet() {
        final Random random = new Random(0x6d61746368L);
        for (final Type type : Type.values()) {
            for (final int count : COUNTS) {
                // leave room for a key that misses
                if (count >= type.last() - type.first()) {
                    continue;
                }
                for (final long[] values : valueSets(type, count, random)) {
                    check(type, values, randomKeys(type, values, random));
                }
            }
        }
    }

    /**
     * @return value sets of {@code count} values: dense ones in the middle and at both ends of the type, sparse ones,
     *         and ones that include null or repeat a value
     */
    private static List<long[]> valueSets(final Type type, final int count, final Random random) {
        final List<long[]> sets = new ArrayList<>();
        final long middle = type.first() / 2 + type.last() / 2;
        sets.add(consecutive(middle, count));
        sets.add(consecutive(type.first(), count));
        sets.add(consecutive(type.last() - count + 1, count));
        // every other value, so that the bitmap holds misses inside its range
        final long[] alternate = new long[count];
        for (int ii = 0; ii < count; ++ii) {
            alternate[ii] = type.first() + 2L * ii;
        }
        sets.add(alternate);
        final long[] sparse = sparse(type, count, random);
        sets.add(sparse);
        if (count >= 2) {
            // both extremes, whose range a long cannot hold
            final long[] extremes = sparse(type, count, random);
            extremes[0] = type.first();
            extremes[1] = type.last();
            if (distinct(extremes)) {
                sets.add(extremes);
            }
            final long[] denseWithNull = consecutive(middle, count);
            denseWithNull[count - 1] = type.nullValue;
            sets.add(denseWithNull);
            final long[] sparseWithNull = sparse.clone();
            sparseWithNull[0] = type.nullValue;
            sets.add(sparseWithNull);
            final long[] repeated = consecutive(middle, count);
            repeated[count - 1] = repeated[0];
            sets.add(repeated);
            final long[] onlyNull = new long[count];
            Arrays.fill(onlyNull, type.nullValue);
            sets.add(onlyNull);
        }
        return sets;
    }

    private static long[] consecutive(final long first, final int count) {
        final long[] values = new long[count];
        for (int ii = 0; ii < count; ++ii) {
            values[ii] = first + ii;
        }
        return values;
    }

    private static long[] sparse(final Type type, final int count, final Random random) {
        final Set<Long> values = new LinkedHashSet<>();
        while (values.size() < count) {
            values.add(randomKey(type, random));
        }
        return values.stream().mapToLong(Long::longValue).toArray();
    }

    private static boolean distinct(final long[] values) {
        return Arrays.stream(values).distinct().count() == values.length;
    }

    /**
     * @return a key from the type's non-null values
     */
    private static long randomKey(final Type type, final Random random) {
        if (type == Type.LONG) {
            long key;
            do {
                key = random.nextLong();
            } while (key == type.nullValue);
            return key;
        }
        return type.first() + (long) (random.nextDouble() * (type.last() - type.first() + 1));
    }

    /**
     * @return the values, the keys either side of each, null, the type's extremes, and random keys
     */
    private static long[] randomKeys(final Type type, final long[] values, final Random random) {
        final List<Long> keys = new ArrayList<>();
        for (final long value : values) {
            for (long key = value - 1; key <= value + 1; ++key) {
                if (type.contains(key)) {
                    keys.add(key);
                }
            }
        }
        keys.add(type.nullValue);
        keys.add(type.first());
        keys.add(type.last());
        keys.add(0L);
        for (int ii = 0; ii < 500; ++ii) {
            keys.add(randomKey(type, random));
        }
        return keys.stream().mapToLong(Long::longValue).toArray();
    }

    private static void check(final Type type, final long[] values, final long[] keys) {
        final Set<Long> valueSet = new HashSet<>();
        final Object[] boxed = new Object[values.length];
        for (int ii = 0; ii < values.length; ++ii) {
            valueSet.add(values[ii]);
            boxed[ii] = type.box(values[ii]);
        }
        for (final boolean inverted : new boolean[] {false, true}) {
            final MatchOptions options = inverted ? MatchOptions.INVERTED : MatchOptions.REGULAR;
            final ChunkFilter filter = ChunkMatchFilterFactory.getChunkFilter(type.type, options, boxed);
            final String context = type + " " + filter.getClass().getSimpleName() + " values="
                    + Arrays.toString(values);
            final boolean[] expected = new boolean[keys.length];
            for (int ii = 0; ii < keys.length; ++ii) {
                expected[ii] = valueSet.contains(keys[ii]) != inverted;
            }
            ChunkFilterLoops.check(context, filter, type.chunk(keys), expected);
        }
    }

    @Test
    public void testHashFallback() {
        // the filters used when no cuckoo table can be built
        final Random random = new Random(0x68617368L);
        for (final int count : new int[] {4, 100, 1000}) {
            for (final Type type : new Type[] {Type.INT, Type.LONG}) {
                final long[] values = sparse(type, count, random);
                values[0] = type.nullValue;
                final long[] keys = randomKeys(type, values, random);
                final Set<Long> valueSet = new HashSet<>();
                Arrays.stream(values).forEach(valueSet::add);
                for (final boolean inverted : new boolean[] {false, true}) {
                    final ChunkFilter filter = type == Type.INT
                            ? IntMultiValueChunkFilters.makeHashFilter(inverted,
                                    Arrays.stream(values).mapToInt(value -> (int) value).toArray())
                            : LongMultiValueChunkFilters.makeHashFilter(inverted, values);
                    final boolean[] expected = new boolean[keys.length];
                    for (int ii = 0; ii < keys.length; ++ii) {
                        expected[ii] = valueSet.contains(keys[ii]) != inverted;
                    }
                    ChunkFilterLoops.check(type + " hash fallback", filter, type.chunk(keys), expected);
                }
            }
        }
    }

    @Test
    public void testStructureChoice() {
        // the 8 and 16 bit types use a bitmap of every value from four values up
        for (final Type type : new Type[] {Type.BYTE, Type.CHAR, Type.SHORT}) {
            assertStructure("Bitmap", type, sparseBoxed(type, 4));
            assertStructure("Bitmap", type, sparseBoxed(type, 100));
        }
        // int and long use a bitmap over a dense range, and otherwise a cuckoo table from four values up
        assertStructure("DenseBitmap", Type.INT, boxed(Type.INT, consecutive(1000, 4)));
        assertStructure("DenseBitmap", Type.INT, boxed(Type.INT, consecutive(1000, 1000)));
        assertStructure("Cuckoo", Type.INT, sparseBoxed(Type.INT, 4));
        assertStructure("Cuckoo", Type.INT, sparseBoxed(Type.INT, 100));
        assertStructure("DenseBitmap", Type.LONG, boxed(Type.LONG, consecutive(1000, 4)));
        assertStructure("Cuckoo", Type.LONG, sparseBoxed(Type.LONG, 4));
        assertStructure("Cuckoo", Type.LONG, sparseBoxed(Type.LONG, 100));
        // null is kept out of the range, so it does not make a dense range sparse
        final long[] denseWithNull = consecutive(1000, 32);
        denseWithNull[0] = QueryConstants.NULL_LONG;
        assertStructure("DenseBitmap", Type.LONG, boxed(Type.LONG, denseWithNull));
    }

    private static Object[] boxed(final Type type, final long[] values) {
        return Arrays.stream(values).mapToObj(type::box).toArray();
    }

    private static Object[] sparseBoxed(final Type type, final int count) {
        // spread across the whole type, so that the range is never dense
        final long[] values = new long[count];
        for (int ii = 0; ii < count; ++ii) {
            values[ii] = type.first() + (type.last() / count - type.first() / count) * ii;
        }
        return boxed(type, values);
    }

    private static void assertStructure(final String structure, final Type type, final Object[] values) {
        for (final MatchOptions options : new MatchOptions[] {MatchOptions.REGULAR, MatchOptions.INVERTED}) {
            final String name = ChunkMatchFilterFactory.getChunkFilter(type.type, options, values)
                    .getClass().getSimpleName();
            final String prefix = options.inverted() ? "Inverse" + structure : structure;
            assertTrue(type + " " + values.length + " values: " + name, name.startsWith(prefix));
        }
    }
}
