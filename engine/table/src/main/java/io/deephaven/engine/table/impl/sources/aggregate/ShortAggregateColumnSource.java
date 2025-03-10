//
// Copyright (c) 2016-2025 Deephaven Data Labs and Patent Pending
//
// ****** AUTO-GENERATED CLASS - DO NOT EDIT MANUALLY
// ****** Edit CharAggregateColumnSource and run "./gradlew replicateSourcesAndChunks" to regenerate
//
// @formatter:off
package io.deephaven.engine.table.impl.sources.aggregate;

import io.deephaven.engine.rowset.RowSequence;
import io.deephaven.vector.ShortVector;
import io.deephaven.engine.table.vectors.ShortVectorColumnWrapper;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.chunk.ObjectChunk;
import io.deephaven.chunk.WritableChunk;
import io.deephaven.chunk.WritableObjectChunk;
import io.deephaven.engine.rowset.RowSet;
import org.jetbrains.annotations.NotNull;

/**
 * {@link ColumnSource} implementation for aggregation result short columns.
 */
public final class ShortAggregateColumnSource extends BaseAggregateColumnSource<ShortVector, Short> {

    ShortAggregateColumnSource(@NotNull final ColumnSource<Short> aggregatedSource,
            @NotNull final ColumnSource<? extends RowSet> groupRowSetSource) {
        super(ShortVector.class, aggregatedSource, groupRowSetSource);
    }

    @Override
    public ShortVector get(final long rowKey) {
        if (rowKey == RowSequence.NULL_ROW_KEY) {
            return null;
        }
        return new ShortVectorColumnWrapper(aggregatedSource, groupRowSetSource.get(rowKey));
    }

    @Override
    public ShortVector getPrev(final long rowKey) {
        if (rowKey == RowSequence.NULL_ROW_KEY) {
            return null;
        }
        return new ShortVectorColumnWrapper(aggregatedSourcePrev, getPrevGroupRowSet(rowKey));
    }

    @Override
    public void fillChunk(
            @NotNull final FillContext context,
            @NotNull final WritableChunk<? super Values> destination,
            @NotNull final RowSequence rowSequence) {
        final ObjectChunk<RowSet, ? extends Values> groupRowSetChunk = groupRowSetSource
                .getChunk(((AggregateFillContext) context).groupRowSetGetContext, rowSequence).asObjectChunk();
        final WritableObjectChunk<ShortVector, ? super Values> typedDestination = destination.asWritableObjectChunk();
        final int size = rowSequence.intSize();
        for (int di = 0; di < size; ++di) {
            typedDestination.set(di, new ShortVectorColumnWrapper(aggregatedSource, groupRowSetChunk.get(di)));
        }
        typedDestination.setSize(size);
    }

    @Override
    public void fillPrevChunk(
            @NotNull final FillContext context,
            @NotNull final WritableChunk<? super Values> destination,
            @NotNull final RowSequence rowSequence) {
        final ObjectChunk<RowSet, ? extends Values> groupRowSetPrevChunk = groupRowSetSource
                .getPrevChunk(((AggregateFillContext) context).groupRowSetGetContext, rowSequence).asObjectChunk();
        final WritableObjectChunk<ShortVector, ? super Values> typedDestination = destination.asWritableObjectChunk();
        final int size = rowSequence.intSize();
        for (int di = 0; di < size; ++di) {
            final RowSet groupRowSetPrev = groupRowSetPrevChunk.get(di);
            final RowSet groupRowSetToUse = groupRowSetPrev.isTracking()
                    ? groupRowSetPrev.trackingCast().prev()
                    : groupRowSetPrev;
            typedDestination.set(di, new ShortVectorColumnWrapper(aggregatedSourcePrev, groupRowSetToUse));
        }
        typedDestination.setSize(size);
    }
}
