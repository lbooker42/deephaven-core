//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
package io.deephaven.engine.table.impl.chunkfilter;

import java.util.SplittableRandom;

/**
 * An immutable set of int keys in a cuckoo hash table with one key per position. A key can only be at one of two
 * positions, so a lookup is two loads and two compares, without a branch on how many probes it takes. Maintained by
 * hand alongside {@link LongCuckooSet}.
 * <p>
 * {@link #build} makes the table once, from every key, with these safeguards:
 * <ul>
 * <li>A key is looked up before it is inserted, and skipped if it is there: a key already in the table could otherwise
 * displace itself.</li>
 * <li>At most 45% of the positions hold a key ({@link #MAX_LOAD}).</li>
 * <li>An insert moves at most {@link #MAX_KICKS} keys. When one would move more, the whole table is built again from
 * the keys with new hash seeds, so no key is lost on the way; after {@link #SEEDS_PER_SIZE} failed seeds, the table
 * doubles.</li>
 * <li>After {@link #MAX_DOUBLINGS} doublings, {@link #build} gives up and returns null, so that no set of keys can make
 * it loop or allocate without bound.</li>
 * </ul>
 * An empty position holds zero, so the key zero is recorded with a flag instead.
 */
final class IntCuckooSet {
    static final double MAX_LOAD = 0.45;
    static final int MAX_KICKS = 500;
    static final int SEEDS_PER_SIZE = 3;
    static final int MAX_DOUBLINGS = 3;

    private static final int MAX_CAPACITY = 1 << 30;
    private static final int MIX = 0x5bd1e995;

    private final int[] slots;
    // the table holds 1 << (32 - shift) positions
    private final int shift;
    private final int seed1;
    private final int seed2;
    private final boolean containsZero;

    private IntCuckooSet(final int[] slots, final int shift, final int seed1, final int seed2,
            final boolean containsZero) {
        this.slots = slots;
        this.shift = shift;
        this.seed1 = seed1;
        this.seed2 = seed2;
        this.containsZero = containsZero;
    }

    /**
     * @param keys the keys, which may repeat
     * @return a set of the keys, or null if no table could be built within the limits, which the caller then matches
     *         with another structure
     */
    static IntCuckooSet build(final int... keys) {
        return build(MAX_KICKS, MAX_DOUBLINGS, keys);
    }

    /**
     * {@link #build(int...)}, with the limits as arguments, so that tests can make it give up.
     */
    static IntCuckooSet build(final int maxKicks, final int maxDoublings, final int... keys) {
        boolean containsZero = false;
        for (final int key : keys) {
            containsZero |= key == 0;
        }
        final SplittableRandom random = new SplittableRandom(0x9E3779B97F4A7C15L ^ keys.length);
        long capacity = capacityFor(keys.length);
        for (int doublings = 0; doublings <= maxDoublings && capacity <= MAX_CAPACITY; ++doublings, capacity <<= 1) {
            final int shift = Integer.SIZE - Long.numberOfTrailingZeros(capacity);
            for (int attempt = 0; attempt < SEEDS_PER_SIZE; ++attempt) {
                final int seed1 = random.nextInt() | 1;
                final int seed2 = random.nextInt() | 1;
                final int[] table = tryBuild(keys, (int) capacity, shift, seed1, seed2, maxKicks);
                if (table != null) {
                    return new IntCuckooSet(table, shift, seed1, seed2, containsZero);
                }
            }
        }
        return null;
    }

    /**
     * @return the number of positions in a table for {@code size} keys: the least power of two that keeps the load at
     *         or below {@link #MAX_LOAD}, and at least two
     */
    static long capacityFor(final int size) {
        final long wanted = Math.max(2, (long) Math.ceil(size / MAX_LOAD));
        return Long.highestOneBit(wanted - 1) << 1;
    }

    /**
     * @return a table holding every key, or null if an insert would move more than {@code maxKicks} keys
     */
    private static int[] tryBuild(final int[] keys, final int capacity, final int shift, final int seed1,
            final int seed2, final int maxKicks) {
        final int[] table = new int[capacity];
        for (final int key : keys) {
            if (key == 0) {
                continue;
            }
            final int first = (key * seed1) >>> shift;
            if (table[first] == key || table[((key ^ MIX) * seed2) >>> shift] == key) {
                // a repeat: every key in the table is at one of its two positions
                continue;
            }
            int current = key;
            int position = first;
            for (int kicks = 0;; ++kicks) {
                final int displaced = table[position];
                table[position] = current;
                if (displaced == 0) {
                    break;
                }
                if (kicks == maxKicks) {
                    return null;
                }
                current = displaced;
                final int currentFirst = (current * seed1) >>> shift;
                position = position == currentFirst ? ((current ^ MIX) * seed2) >>> shift : currentFirst;
            }
        }
        return table;
    }

    boolean contains(final int key) {
        final boolean found = (slots[(key * seed1) >>> shift] == key) | (slots[((key ^ MIX) * seed2) >>> shift] == key);
        return (found & (key != 0)) | (containsZero & (key == 0));
    }
}
