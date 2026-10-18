//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
package io.deephaven.engine.table.impl.chunkfilter;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Checks the cuckoo sets against a plain set, at sizes from one key to many: with zero, which an empty slot holds, the
 * type's extremes, and repeated keys among the keys, probed with every key, the keys either side of each, and random
 * keys.
 */
public class CuckooSetTest {

    private static final int[] SIZES = {1, 2, 7, 100, 10_000, 100_000};

    @Test
    public void testIntSets() {
        final Random random = new Random(0x696e74L);
        for (final int size : SIZES) {
            for (final boolean special : new boolean[] {false, true}) {
                final int[] keys = random.ints(size).toArray();
                if (special && size >= 4) {
                    keys[0] = 0;
                    keys[1] = Integer.MIN_VALUE;
                    keys[2] = Integer.MAX_VALUE;
                    keys[3] = keys[size - 1];
                }
                final Set<Integer> expected = new HashSet<>();
                Arrays.stream(keys).forEach(expected::add);
                checkInt("IntCuckooSet", IntCuckooSet.build(keys)::contains, expected, keys, random);
            }
        }
    }

    private static void checkInt(final String name, final IntPredicate set, final Set<Integer> expected,
            final int[] keys, final Random random) {
        final int[] probes = new int[keys.length * 3 + 1000];
        int count = 0;
        for (final int key : keys) {
            probes[count++] = key;
            probes[count++] = key - 1;
            probes[count++] = key + 1;
        }
        while (count < probes.length) {
            probes[count++] = random.nextInt();
        }
        probes[probes.length - 1] = 0;
        for (final int probe : probes) {
            assertEquals(name + " size " + keys.length + " probe " + probe, expected.contains(probe), set.test(probe));
        }
    }

    @Test
    public void testLongSets() {
        final Random random = new Random(0x6c6f6e67L);
        for (final int size : SIZES) {
            for (final boolean special : new boolean[] {false, true}) {
                final long[] keys = random.longs(size).toArray();
                if (special && size >= 4) {
                    keys[0] = 0;
                    keys[1] = Long.MIN_VALUE;
                    keys[2] = Long.MAX_VALUE;
                    keys[3] = keys[size - 1];
                }
                final Set<Long> expected = new HashSet<>();
                Arrays.stream(keys).forEach(expected::add);
                checkLong("LongCuckooSet", LongCuckooSet.build(keys)::contains, expected, keys, random);
            }
        }
    }

    private static void checkLong(final String name, final LongPredicate set, final Set<Long> expected,
            final long[] keys, final Random random) {
        final long[] probes = new long[keys.length * 3 + 1000];
        int count = 0;
        for (final long key : keys) {
            probes[count++] = key;
            probes[count++] = key - 1;
            probes[count++] = key + 1;
        }
        while (count < probes.length) {
            probes[count++] = random.nextLong();
        }
        probes[probes.length - 1] = 0;
        for (final long probe : probes) {
            assertEquals(name + " size " + keys.length + " probe " + probe, expected.contains(probe), set.test(probe));
        }
    }

    @Test
    public void testSequentialKeys() {
        // structured keys, which a weak hash would pile up
        final int[] intKeys = new int[50_000];
        final long[] longKeys = new long[50_000];
        for (int ii = 0; ii < intKeys.length; ++ii) {
            intKeys[ii] = ii * 1024;
            longKeys[ii] = (long) ii << 32;
        }
        final IntCuckooSet intSet = IntCuckooSet.build(intKeys);
        final LongCuckooSet longSet = LongCuckooSet.build(longKeys);
        for (int ii = 0; ii < intKeys.length; ++ii) {
            assertTrue(intSet.contains(intKeys[ii]));
            assertFalse(intSet.contains(intKeys[ii] + 1));
            assertTrue(longSet.contains(longKeys[ii]));
            assertFalse(longSet.contains(longKeys[ii] + 1));
        }
    }

    @Test
    public void testBuildGivesUp() {
        // with no key allowed to move and no growth, a table of many keys cannot be built, and build says so
        final Random random = new Random(0x67697665L);
        assertNull(IntCuckooSet.build(0, 0, random.ints(10_000).toArray()));
        assertNull(LongCuckooSet.build(0, 0, random.longs(10_000).toArray()));
        final String[] strings = random.longs(10_000).mapToObj(Long::toString).distinct().toArray(String[]::new);
        assertNull(StringCuckooSet.build(0, 0, strings));
        // and with the usual limits it builds them
        assertNotNull(IntCuckooSet.build(random.ints(10_000).toArray()));
        assertNotNull(StringCuckooSet.build(strings));
    }
}
