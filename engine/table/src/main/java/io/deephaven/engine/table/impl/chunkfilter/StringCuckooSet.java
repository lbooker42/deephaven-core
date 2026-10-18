//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
package io.deephaven.engine.table.impl.chunkfilter;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.SplittableRandom;

/**
 * An immutable set of Strings in a cuckoo hash table with one slot per position. Each slot stores its key's hash code,
 * which a String caches, so a lookup compares the hash code with the two slots its key can be in and calls
 * {@code equals} only where they agree.
 * <p>
 * Keys with the same hash code have the same two positions under every seed, so at most {@link #GROUP_LIMIT} of them
 * fit. The keys of a hash code that more keys share are kept in a {@link HashSet} instead, which holds keys of one hash
 * code in a tree, so that a large group of them costs a lookup O(log n) compares rather than a scan. A bitmap over
 * those hash codes keeps the other lookups from asking.
 * <p>
 * {@link #build} makes the table once, from every key, with these safeguards:
 * <ul>
 * <li>A key is looked up before it is inserted, and skipped if it is there: a key already in the table could otherwise
 * displace itself.</li>
 * <li>At most 45% of the positions hold a key ({@link #MAX_LOAD}).</li>
 * <li>An insert moves at most {@link #MAX_KICKS} keys. When one would move more, the whole table is built again from
 * the keys with a new seed, so no key is lost on the way; after {@link #SEEDS_PER_SIZE} failed seeds, the table
 * doubles.</li>
 * <li>After {@link #MAX_DOUBLINGS} doublings, {@link #build} gives up and returns null, so that no set of keys can make
 * it loop or allocate without bound.</li>
 * </ul>
 */
final class StringCuckooSet {
    static final int GROUP_LIMIT = 2;
    static final double MAX_LOAD = 0.45;
    static final int MAX_KICKS = 500;
    static final int SEEDS_PER_SIZE = 3;
    static final int MAX_DOUBLINGS = 3;

    private static final int MAX_CAPACITY = 1 << 30;
    private static final int FILTER_BITS = 4096;

    private final String[] keys;
    private final int[] hashes;
    // the table holds 1 << (32 - shift) positions
    private final int shift;
    private final long seed;

    // null when no hash code is shared by more than GROUP_LIMIT keys
    private final long[] overflowFilter;
    private final IntOpenHashSet overflowHashes;
    private final HashSet<String> overflow;

    private StringCuckooSet(
            final String[] keys,
            final int[] hashes,
            final int shift,
            final long seed,
            final String[] distinctKeys,
            final IntOpenHashSet routedHashes) {
        this.keys = keys;
        this.hashes = hashes;
        this.shift = shift;
        this.seed = seed;
        if (routedHashes.isEmpty()) {
            overflowFilter = null;
            overflowHashes = null;
            overflow = null;
            return;
        }
        overflowFilter = new long[FILTER_BITS / Long.SIZE];
        overflowHashes = routedHashes;
        overflow = new HashSet<>();
        for (final String key : distinctKeys) {
            final int hash = key.hashCode();
            if (routedHashes.contains(hash)) {
                overflow.add(key);
                final int bit = filterBit(hash);
                overflowFilter[bit >>> 6] |= 1L << bit;
            }
        }
    }

    /**
     * @param distinctKeys the keys, none null; a repeated key is skipped
     * @return a set of the keys, or null if no table could be built within the limits, which the caller then matches
     *         with another structure
     */
    static StringCuckooSet build(@NotNull final String[] distinctKeys) {
        return build(MAX_KICKS, MAX_DOUBLINGS, distinctKeys);
    }

    /**
     * {@link #build(String[])}, with the limits as arguments, so that tests can make it give up.
     */
    static StringCuckooSet build(final int maxKicks, final int maxDoublings,
            @NotNull final String[] distinctKeys) {
        final IntOpenHashSet routedHashes = new IntOpenHashSet();
        final SplittableRandom random = new SplittableRandom(0x9E3779B97F4A7C15L ^ distinctKeys.length);
        long capacity = Long.highestOneBit(Math.max(2, (long) Math.ceil(distinctKeys.length / MAX_LOAD)) - 1) << 1;
        int failedSeeds = 0;
        int doublings = 0;
        while (capacity <= MAX_CAPACITY) {
            final int tableShift = Integer.SIZE - Long.numberOfTrailingZeros(capacity);
            final long tableSeed = random.nextLong();
            final String[] tableSlots = new String[(int) capacity];
            final int[] tableHashes = new int[(int) capacity];
            final int[] groupToRoute = new int[1];
            final Outcome outcome = tryBuild(distinctKeys, routedHashes, tableSlots, tableHashes, tableShift,
                    tableSeed, maxKicks, groupToRoute);
            if (outcome == Outcome.PLACED) {
                return new StringCuckooSet(tableSlots, tableHashes, tableShift, tableSeed, distinctKeys,
                        routedHashes);
            }
            if (outcome == Outcome.GROUP_TOO_LARGE) {
                // at most once per hash code, so this cannot repeat without end
                routedHashes.add(groupToRoute[0]);
                continue;
            }
            if (++failedSeeds == SEEDS_PER_SIZE) {
                if (doublings++ == maxDoublings) {
                    return null;
                }
                failedSeeds = 0;
                capacity <<= 1;
            }
        }
        return null;
    }

    private enum Outcome {
        PLACED, TOO_MANY_KICKS, GROUP_TOO_LARGE
    }

    private static int filterBit(final int hash) {
        return (hash * 0x9E3779B9) >>> 20;
    }

    private static long mix(final int hash, final long seed) {
        long key = hash ^ seed;
        key ^= key >>> 33;
        key *= 0xff51afd7ed558ccdL;
        key ^= key >>> 33;
        key *= 0xc4ceb9fe1a85ec53L;
        key ^= key >>> 33;
        return key;
    }

    private static int firstPosition(final long mixed, final int shift) {
        return (int) (mixed >>> 32) >>> shift;
    }

    private static int secondPosition(final long mixed, final int shift) {
        return (int) mixed >>> shift;
    }

    /**
     * Place every key whose hash code is not routed to the overflow set.
     *
     * @return {@link Outcome#PLACED} if every key was placed; {@link Outcome#TOO_MANY_KICKS} if an insert would move
     *         more than {@code maxKicks} keys; or {@link Outcome#GROUP_TOO_LARGE} if a key's hash code is already that
     *         of {@link #GROUP_LIMIT} placed keys, with that hash code in {@code groupToRoute[0]}
     */
    private static Outcome tryBuild(
            final String[] distinctKeys,
            final IntOpenHashSet routedHashes,
            final String[] slots,
            final int[] hashes,
            final int shift,
            final long seed,
            final int maxKicks,
            final int[] groupToRoute) {
        for (final String key : distinctKeys) {
            final int hash = key.hashCode();
            if (!routedHashes.isEmpty() && routedHashes.contains(hash)) {
                continue;
            }
            final long keyMixed = mix(hash, seed);
            final int keyFirst = firstPosition(keyMixed, shift);
            final int keySecond = secondPosition(keyMixed, shift);
            // every placed key of this hash code, a repeat of this key included, is at one of these two positions
            final String inFirst = slots[keyFirst];
            final String inSecond = keySecond != keyFirst ? slots[keySecond] : null;
            final boolean sameFirst = inFirst != null && hashes[keyFirst] == hash;
            final boolean sameSecond = inSecond != null && hashes[keySecond] == hash;
            if (sameFirst && inFirst.equals(key) || sameSecond && inSecond.equals(key)) {
                continue;
            }
            if ((sameFirst ? 1 : 0) + (sameSecond ? 1 : 0) == GROUP_LIMIT) {
                groupToRoute[0] = hash;
                return Outcome.GROUP_TOO_LARGE;
            }

            String current = key;
            int currentHash = hash;
            int position = keyFirst;
            for (int kicks = 0;; ++kicks) {
                final String displaced = slots[position];
                final int displacedHash = hashes[position];
                slots[position] = current;
                hashes[position] = currentHash;
                if (displaced == null) {
                    break;
                }
                if (kicks == maxKicks) {
                    return Outcome.TOO_MANY_KICKS;
                }
                current = displaced;
                currentHash = displacedHash;
                final long mixed = mix(currentHash, seed);
                final int first = firstPosition(mixed, shift);
                position = position == first ? secondPosition(mixed, shift) : first;
            }
        }
        return Outcome.PLACED;
    }

    /**
     * @param key a String, not null
     * @return whether the set holds {@code key}
     */
    boolean contains(@NotNull final String key) {
        final int hash = key.hashCode();
        if (overflowFilter != null) {
            final int bit = filterBit(hash);
            if (((overflowFilter[bit >>> 6] >>> bit) & 1) != 0 && overflowHashes.contains(hash)) {
                return overflow.contains(key);
            }
        }
        final long mixed = mix(hash, seed);
        final int first = firstPosition(mixed, shift);
        if (hashes[first] == hash) {
            final String slotKey = keys[first];
            if (slotKey == key || key.equals(slotKey)) {
                return true;
            }
        }
        final int second = secondPosition(mixed, shift);
        if (hashes[second] == hash) {
            final String slotKey = keys[second];
            return slotKey == key || key.equals(slotKey);
        }
        return false;
    }
}
