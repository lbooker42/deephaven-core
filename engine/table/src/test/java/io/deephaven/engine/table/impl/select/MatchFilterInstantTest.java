//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
package io.deephaven.engine.table.impl.select;

import io.deephaven.engine.rowset.RowSet;
import io.deephaven.engine.rowset.RowSetFactory;
import io.deephaven.engine.rowset.WritableRowSet;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.MatchOptions;
import io.deephaven.engine.table.Table;
import io.deephaven.engine.table.impl.QueryTable;
import io.deephaven.engine.table.impl.sources.immutable.ImmutableInstantArraySource;
import io.deephaven.engine.table.impl.sources.immutable.ImmutableObjectArraySource;
import io.deephaven.engine.testutil.junit4.EngineCleanup;
import io.deephaven.time.DateTimeUtils;
import io.deephaven.util.QueryConstants;
import org.junit.Rule;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * A MatchFilter on an {@link Instant} column whose source holds epoch nanoseconds matches them as longs. It must match
 * exactly the rows that matching the column's Instants would: those of a column of Instant objects, which cannot be
 * read as nanoseconds.
 */
public class MatchFilterInstantTest {

    @Rule
    public final EngineCleanup base = new EngineCleanup();

    /**
     * The Instant whose epoch nanoseconds are {@link QueryConstants#NULL_LONG}: a nanosecond column holds that for
     * null, so this Instant matches no row, null or not.
     */
    private static final Instant NULL_LONG_INSTANT = Instant.ofEpochSecond(
            Math.floorDiv(QueryConstants.NULL_LONG, 1_000_000_000L),
            Math.floorMod(QueryConstants.NULL_LONG, 1_000_000_000L));

    @Test
    public void testMatchesAsInstants() {
        final Random random = new Random(0x696e7374L);
        final long[] nanos = new long[2000];
        for (int ii = 0; ii < nanos.length; ++ii) {
            // a few hundred distinct values, so that values match several rows; every tenth row null
            nanos[ii] = ii % 10 == 0 ? QueryConstants.NULL_LONG : 1_700_000_000_000_000_000L + random.nextInt(300);
        }
        nanos[1] = Long.MAX_VALUE;
        nanos[2] = QueryConstants.NULL_LONG + 1;
        final Instant[] instants = Arrays.stream(nanos).mapToObj(DateTimeUtils::epochNanosToInstant)
                .toArray(Instant[]::new);

        final ColumnSource<Instant> nanoSource = new ImmutableInstantArraySource(nanos);
        final ColumnSource<Instant> objectSource = new ImmutableObjectArraySource<>(Instant.class, null, instants);
        assertTrue(nanoSource.allowsReinterpret(long.class));
        assertFalse(objectSource.allowsReinterpret(long.class));
        final Table nanoTable = new QueryTable(RowSetFactory.flat(nanos.length).toTracking(), Map.of("T", nanoSource));
        final Table objectTable =
                new QueryTable(RowSetFactory.flat(nanos.length).toTracking(), Map.of("T", objectSource));

        for (final int count : new int[] {1, 2, 3, 4, 8, 64, 100}) {
            final List<Object> values = new ArrayList<>();
            values.add(null);
            values.add(Instant.MIN);
            values.add(Instant.MAX);
            values.add(NULL_LONG_INSTANT);
            values.add(DateTimeUtils.epochNanosToInstant(Long.MAX_VALUE));
            values.add(DateTimeUtils.epochNanosToInstant(QueryConstants.NULL_LONG + 1));
            while (values.size() < Math.max(count, 7)) {
                values.add(DateTimeUtils.epochNanosToInstant(1_700_000_000_000_000_000L + random.nextInt(300)));
            }
            // vary which of the special values each count includes
            final Object[] chosen = values.subList(values.size() - count, values.size()).toArray();
            for (final MatchOptions options : new MatchOptions[] {MatchOptions.REGULAR, MatchOptions.INVERTED}) {
                final String context = count + " values, " + options + ": " + Arrays.toString(chosen);
                final RowSet fromNanos = nanoTable.where(new MatchFilter(options, "T", chosen)).getRowSet();
                final RowSet fromObjects = objectTable.where(new MatchFilter(options, "T", chosen)).getRowSet();
                assertEquals(context, fromObjects, fromNanos);
                assertEquals(context, expectedSize(instants, chosen, options.inverted()), fromNanos.size());

                final MatchFilter filter = new MatchFilter(options, "T", chosen);
                filter.init(nanoTable.getDefinition());
                final RowSet all = nanoTable.getRowSet();
                try (final WritableRowSet inverse = filter.filterInverse(all, all, nanoTable, false);
                        final WritableRowSet complement = all.minus(fromNanos)) {
                    assertEquals(context, complement, inverse);
                }
            }
        }
    }

    private static long expectedSize(final Instant[] rows, final Object[] values, final boolean inverted) {
        final List<Object> valueList = Arrays.asList(values);
        return Arrays.stream(rows).filter(row -> valueList.contains(row) != inverted).count();
    }
}
