//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
package io.deephaven.engine.table.impl.chunkfilter;

import io.deephaven.chunk.WritableObjectChunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.table.MatchOptions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Checks the case-sensitive String match filters against a plain set, for every count at which the factory changes
 * structure, with null among the values and the rows, repeated values, and groups of Strings that share a hash code:
 * small groups stay in the cuckoo table and large ones go to its overflow set.
 */
public class CaseSensitiveStringChunkMatchFilterFactoryTest {

    private static final int THRESHOLD = StringChunkMatchFilterFactory.SCAN_THRESHOLD;
    private static final int[] COUNTS = {1, 2, 3, 4, THRESHOLD - 1, THRESHOLD, THRESHOLD + 1, 100, 1000};

    /**
     * @return the 2^n Strings of n pieces, each "Aa" or "BB", which all have the same hash code
     */
    private static List<String> collidingGroup(final int pieces, final String prefix) {
        final List<String> group = new ArrayList<>();
        for (int bits = 0; bits < 1 << pieces; ++bits) {
            final StringBuilder builder = new StringBuilder(prefix);
            for (int ii = 0; ii < pieces; ++ii) {
                builder.append((bits >>> ii & 1) == 0 ? "Aa" : "BB");
            }
            group.add(builder.toString());
        }
        return group;
    }

    private static String randomString(final Random random) {
        return Long.toString(random.nextLong() & Long.MAX_VALUE, 36);
    }

    @Test
    public void testMatchesPlainSet() {
        final Random random = new Random(0x737472696e67L);
        for (final int count : COUNTS) {
            for (final List<String> values : valueSets(count, random)) {
                check(values, rows(values, random));
            }
        }
    }

    private static List<List<String>> valueSets(final int count, final Random random) {
        final List<List<String>> sets = new ArrayList<>();
        final List<String> plain = new ArrayList<>();
        while (plain.size() < count) {
            plain.add(randomString(random));
        }
        sets.add(plain);
        final List<String> withNull = new ArrayList<>(plain);
        withNull.set(0, null);
        sets.add(withNull);
        if (count >= 2) {
            final List<String> repeated = new ArrayList<>(plain);
            repeated.set(count - 1, repeated.get(0));
            sets.add(repeated);
        }
        if (count >= 16) {
            // part of two groups that each share a hash code, so the rows hold Strings of the same hash code that are
            // not values: 2 values sharing one stay in the table, and 8 sharing another go to the overflow set
            final List<String> colliding = new ArrayList<>(plain.subList(0, count - 10));
            colliding.addAll(collidingGroup(3, "x").subList(0, 2));
            colliding.addAll(collidingGroup(4, "y").subList(0, 8));
            sets.add(colliding);
        }
        return sets;
    }

    private static List<String> rows(final List<String> values, final Random random) {
        final List<String> rows = new ArrayList<>();
        for (final String value : values) {
            // a copy, so that rows never hold the very object of a value
            rows.add(value == null ? null : new String(value.toCharArray()));
        }
        rows.add(null);
        // whole colliding groups, half of which are values when the values hold them
        rows.addAll(collidingGroup(3, "x"));
        rows.addAll(collidingGroup(4, "y"));
        rows.add("");
        for (int ii = 0; ii < 200; ++ii) {
            rows.add(randomString(random));
        }
        return rows;
    }

    private static void check(final List<String> values, final List<String> rows) {
        final Set<String> valueSet = new HashSet<>(values);
        for (final boolean inverted : new boolean[] {false, true}) {
            final MatchOptions options = inverted ? MatchOptions.INVERTED : MatchOptions.REGULAR;
            final ChunkFilter filter = ChunkMatchFilterFactory.getChunkFilter(String.class, options, values.toArray());
            final WritableObjectChunk<String, Values> chunk = WritableObjectChunk.makeWritableChunk(rows.size());
            final boolean[] expected = new boolean[rows.size()];
            for (int ii = 0; ii < rows.size(); ++ii) {
                chunk.set(ii, rows.get(ii));
                expected[ii] = valueSet.contains(rows.get(ii)) != inverted;
            }
            ChunkFilterLoops.check(filter.getClass().getSimpleName() + " values=" + values.size(), filter, chunk,
                    expected);
        }
    }

    @Test
    public void testCuckooSet() {
        final Random random = new Random(0x6f6e65L);
        for (final int count : new int[] {1, 2, 3, 17, 100, 1000, 20_000}) {
            final List<String> values = new ArrayList<>();
            while (values.size() < count) {
                values.add(randomString(random));
            }
            if (count >= 16) {
                // a pair sharing a hash code fits in its two positions; four sharing one go to the overflow set
                values.subList(0, 6).clear();
                values.addAll(collidingGroup(3, "p").subList(0, 2));
                values.addAll(collidingGroup(3, "q").subList(0, 4));
            }
            final Set<String> valueSet = new HashSet<>(values);
            // repeats included: the build skips them
            final List<String> withRepeats = new ArrayList<>(values);
            withRepeats.addAll(values.subList(0, Math.min(10, values.size())));
            final StringCuckooSet set = StringCuckooSet.build(withRepeats.toArray(new String[0]));
            final List<String> probes = new ArrayList<>();
            for (final String value : values) {
                probes.add(new String(value.toCharArray()));
            }
            probes.addAll(collidingGroup(3, "p"));
            probes.addAll(collidingGroup(3, "q"));
            probes.add("");
            for (int ii = 0; ii < 1000; ++ii) {
                probes.add(randomString(random));
            }
            for (final String probe : probes) {
                assertEquals(count + " " + probe, valueSet.contains(probe), set.contains(probe));
            }
        }
        // a large group, all in the overflow set
        final List<String> group = collidingGroup(11, "z");
        final Set<String> valueSet = new HashSet<>(group.subList(0, group.size() / 2));
        final StringCuckooSet set = StringCuckooSet.build(valueSet.toArray(new String[0]));
        for (final String member : group) {
            assertEquals(member, valueSet.contains(member), set.contains(new String(member.toCharArray())));
        }
    }

    @Test
    public void testOverflowGroup() {
        // every value shares one hash code, so every value is in the overflow set
        for (final int pieces : new int[] {6, 11}) {
            // 64 or 2048 Strings of one hash code, half of them values
            final List<String> group = collidingGroup(pieces, "z");
            final Set<String> valueSet = new HashSet<>(group.subList(0, group.size() / 2));
            final StringCuckooSet set = StringCuckooSet.build(valueSet.toArray(new String[0]));
            for (final String member : group) {
                assertEquals(member, valueSet.contains(member), set.contains(new String(member.toCharArray())));
            }
        }
    }

    @Test
    public void testStructureChoice() {
        for (final MatchOptions options : new MatchOptions[] {MatchOptions.REGULAR, MatchOptions.INVERTED}) {
            final String prefix = options.inverted() ? "Inverse" : "";
            final Object[] scanned = new Object[THRESHOLD];
            final Object[] hashed = new Object[THRESHOLD + 1];
            for (int ii = 0; ii < hashed.length; ++ii) {
                hashed[ii] = "v" + ii;
            }
            System.arraycopy(hashed, 0, scanned, 0, THRESHOLD);
            final String scanName = ChunkMatchFilterFactory.getChunkFilter(String.class, options, scanned)
                    .getClass().getSimpleName();
            final String cuckooName = ChunkMatchFilterFactory.getChunkFilter(String.class, options, hashed)
                    .getClass().getSimpleName();
            assertTrue(scanName, scanName.startsWith(prefix + "Scan"));
            assertTrue(cuckooName, cuckooName.startsWith(prefix + "Cuckoo"));
            // repeats do not count toward the threshold
            final Object[] repeated = Arrays.copyOf(scanned, THRESHOLD + 5);
            Arrays.fill(repeated, THRESHOLD, repeated.length, scanned[0]);
            final String repeatedName = ChunkMatchFilterFactory.getChunkFilter(String.class, options, repeated)
                    .getClass().getSimpleName();
            assertTrue(repeatedName, repeatedName.startsWith(prefix + "Scan"));
        }
    }
}
