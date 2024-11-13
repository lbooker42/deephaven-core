//
// Copyright (c) 2016-2024 Deephaven Data Labs and Patent Pending
//
// ****** AUTO-GENERATED CLASS - DO NOT EDIT MANUALLY
// ****** Edit CharChunkedCountOperator and run "./gradlew replicateOperators" to regenerate
//
// @formatter:off
package io.deephaven.engine.table.impl.by;

import io.deephaven.api.agg.spec.AggCountType;
import io.deephaven.chunk.*;
import io.deephaven.chunk.attributes.Values;
import org.jetbrains.annotations.NotNull;

class ByteChunkedCountOperator extends BaseChunkedCountOperator {

    private final ByteCountFunction countFunction;

    /**
     * Construct a count aggregation operator that tests individual data values.
     *
     * @param resultName The name of the result column
     */
    ByteChunkedCountOperator(
            @NotNull final String resultName,
            @NotNull final AggCountType countType) {
        super(resultName);
        this.countFunction = getByteCountFunction(countType);
    }

    @Override
    protected int doCount(int chunkStart, int chunkSize, Chunk<? extends Values> values) {
        final ByteChunk<? extends Values> asByteChunk = values.asByteChunk();
        int valueCount = 0;
        for (int ii = 0; ii < chunkSize; ++ii) {
            if (countFunction.count(asByteChunk.get(chunkStart + ii))) {
                valueCount++;
            }
        }
        return valueCount;
    }
}
