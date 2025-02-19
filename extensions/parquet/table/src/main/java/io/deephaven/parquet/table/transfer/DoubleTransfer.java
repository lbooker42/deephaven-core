//
// Copyright (c) 2016-2025 Deephaven Data Labs and Patent Pending
//
// ****** AUTO-GENERATED CLASS - DO NOT EDIT MANUALLY
// ****** Edit IntTransfer and run "./gradlew replicateParquetTransferObjects" to regenerate
//
// @formatter:off
package io.deephaven.parquet.table.transfer;

import io.deephaven.chunk.WritableDoubleChunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.rowset.RowSequence;
import io.deephaven.engine.rowset.RowSet;
import io.deephaven.engine.table.ColumnSource;
import org.jetbrains.annotations.NotNull;

import java.nio.DoubleBuffer;

final class DoubleTransfer extends FillingPrimitiveTransfer<WritableDoubleChunk<Values>, DoubleBuffer> {
    static DoubleTransfer create(@NotNull final ColumnSource<?> columnSource, @NotNull final RowSet tableRowSet,
            final int targetPageSizeInBytes) {
        final int targetElementsPerPage =
                Math.toIntExact(Math.min(tableRowSet.size(), targetPageSizeInBytes / Double.BYTES));
        final double[] backingArray = new double[targetElementsPerPage];
        return new DoubleTransfer(
                columnSource,
                tableRowSet,
                WritableDoubleChunk.writableChunkWrap(backingArray),
                DoubleBuffer.wrap(backingArray),
                targetElementsPerPage);
    }

    private DoubleTransfer(
            @NotNull final ColumnSource<?> columnSource,
            @NotNull final RowSequence tableRowSet,
            @NotNull final WritableDoubleChunk<Values> chunk,
            @NotNull final DoubleBuffer buffer,
            final int targetElementsPerPage) {
        super(columnSource, tableRowSet, chunk, buffer, targetElementsPerPage);
    }
}
