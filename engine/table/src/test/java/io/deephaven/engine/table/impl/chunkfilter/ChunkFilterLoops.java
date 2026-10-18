//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
package io.deephaven.engine.table.impl.chunkfilter;

import io.deephaven.chunk.WritableBooleanChunk;
import io.deephaven.chunk.WritableChunk;
import io.deephaven.chunk.WritableLongChunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.rowset.chunkattributes.OrderedRowKeys;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Checks a chunk filter's three loops against the expected result for each row. Each primitive filter has its own copy
 * of the loops, so every loop of every filter class needs checking.
 */
final class ChunkFilterLoops {
    private ChunkFilterLoops() {}

    /**
     * @param context what is being checked, for failure messages
     * @param filter the filter
     * @param chunk the rows' values, which this closes
     * @param expected whether each row matches
     */
    static void check(
            final String context,
            final ChunkFilter filter,
            final WritableChunk<Values> chunk,
            final boolean[] expected) {
        final int size = expected.length;
        try (final WritableLongChunk<OrderedRowKeys> rowKeys = WritableLongChunk.makeWritableChunk(size);
                final WritableLongChunk<OrderedRowKeys> matched = WritableLongChunk.makeWritableChunk(size);
                final WritableBooleanChunk<Values> results = WritableBooleanChunk.makeWritableChunk(size);
                final WritableChunk<Values> ignored = chunk) {
            for (int ii = 0; ii < size; ++ii) {
                rowKeys.set(ii, ii);
            }

            filter.filter(chunk, rowKeys, matched);
            int expectedCount = 0;
            for (int ii = 0; ii < size; ++ii) {
                if (expected[ii]) {
                    assertTrue(context + " row " + ii, expectedCount < matched.size());
                    assertEquals(context + " row " + ii, ii, matched.get(expectedCount));
                    ++expectedCount;
                }
            }
            assertEquals(context, expectedCount, matched.size());

            assertEquals(context, expectedCount, filter.filter(chunk, results));
            for (int ii = 0; ii < size; ++ii) {
                assertEquals(context + " row " + ii, expected[ii], results.get(ii));
            }

            // filterAnd only changes rows that are still true
            int expectedAndCount = 0;
            for (int ii = 0; ii < size; ++ii) {
                results.set(ii, ii % 3 != 0);
                expectedAndCount += ii % 3 != 0 && expected[ii] ? 1 : 0;
            }
            assertEquals(context, expectedAndCount, filter.filterAnd(chunk, results));
            for (int ii = 0; ii < size; ++ii) {
                assertEquals(context + " row " + ii, ii % 3 != 0 && expected[ii], results.get(ii));
            }
        }
    }

}
