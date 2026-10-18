//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
package io.deephaven.engine.table.impl.chunkfilter;

import io.deephaven.chunk.WritableObjectChunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.table.MatchOptions;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * Checks the BigDecimal match filters against {@link BigDecimal#compareTo(BigDecimal)}, for every count at which the
 * factory changes structure: a value matches a row that differs from it only in trailing zeros, zero matches zero at
 * every scale, and null matches null.
 */
public class BigDecimalChunkMatchFilterFactoryTest {

    private static final int THRESHOLD = BigDecimalChunkMatchFilterFactory.HASH_THRESHOLD;
    private static final int[] COUNTS = {1, 2, 3, 4, THRESHOLD - 1, THRESHOLD, THRESHOLD + 1, 100, 1000};

    @Test
    public void testMatchesCompareTo() {
        final Random random = new Random(0x626967L);
        for (final int count : COUNTS) {
            for (final List<BigDecimal> values : valueSets(count, random)) {
                check(values, rows(values, random));
            }
        }
    }

    private static BigDecimal randomValue(final Random random) {
        return BigDecimal.valueOf(random.nextInt(2_000_000) - 1_000_000, random.nextInt(5));
    }

    private static List<List<BigDecimal>> valueSets(final int count, final Random random) {
        final List<List<BigDecimal>> sets = new ArrayList<>();
        final List<BigDecimal> plain = new ArrayList<>();
        while (plain.size() < count) {
            plain.add(randomValue(random));
        }
        sets.add(plain);
        if (count >= 2) {
            final List<BigDecimal> special = new ArrayList<>(plain);
            special.set(0, null);
            special.set(1, new BigDecimal("0.000"));
            sets.add(special);
            final List<BigDecimal> trailing = new ArrayList<>(plain);
            // the same number twice, at different scales
            trailing.set(count - 1, plain.get(0).setScale(plain.get(0).scale() + 2));
            sets.add(trailing);
        }
        return sets;
    }

    private static List<BigDecimal> rows(final List<BigDecimal> values, final Random random) {
        final List<BigDecimal> rows = new ArrayList<>();
        for (final BigDecimal value : values) {
            if (value == null) {
                continue;
            }
            rows.add(new BigDecimal(value.unscaledValue(), value.scale()));
            rows.add(value.setScale(value.scale() + 1));
            rows.add(value.stripTrailingZeros());
            rows.add(value.add(BigDecimal.ONE.movePointLeft(6)));
        }
        rows.add(null);
        rows.add(BigDecimal.ZERO);
        rows.add(new BigDecimal("0E+3"));
        rows.add(new BigDecimal("0.0"));
        for (int ii = 0; ii < 200; ++ii) {
            rows.add(randomValue(random));
        }
        return rows;
    }

    private static void check(final List<BigDecimal> values, final List<BigDecimal> rows) {
        for (final boolean inverted : new boolean[] {false, true}) {
            final MatchOptions options = inverted ? MatchOptions.INVERTED : MatchOptions.REGULAR;
            final ChunkFilter filter =
                    ChunkMatchFilterFactory.getChunkFilter(BigDecimal.class, options, values.toArray());
            final WritableObjectChunk<BigDecimal, Values> chunk = WritableObjectChunk.makeWritableChunk(rows.size());
            final boolean[] expected = new boolean[rows.size()];
            for (int ii = 0; ii < rows.size(); ++ii) {
                final BigDecimal row = rows.get(ii);
                chunk.set(ii, row);
                boolean matched = false;
                for (final BigDecimal value : values) {
                    matched |= row == null ? value == null : value != null && value.compareTo(row) == 0;
                }
                expected[ii] = matched != inverted;
            }
            ChunkFilterLoops.check(filter.getClass().getSimpleName() + " values=" + values.size(), filter, chunk,
                    expected);
        }
    }

    @Test
    public void testStructureChoice() {
        final Object[] searched = new Object[THRESHOLD - 1];
        final Object[] hashed = new Object[THRESHOLD];
        for (int ii = 0; ii < hashed.length; ++ii) {
            hashed[ii] = BigDecimal.valueOf(ii, 2);
        }
        System.arraycopy(hashed, 0, searched, 0, searched.length);
        final String searchName = ChunkMatchFilterFactory.getChunkFilter(BigDecimal.class, MatchOptions.REGULAR,
                searched).getClass().getSimpleName();
        final String hashName = ChunkMatchFilterFactory.getChunkFilter(BigDecimal.class, MatchOptions.REGULAR,
                hashed).getClass().getSimpleName();
        assertTrue(searchName, searchName.startsWith("MultiValue"));
        assertTrue(hashName, hashName.startsWith("Hash"));
    }
}
