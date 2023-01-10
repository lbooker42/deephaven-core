package io.deephaven.engine.table.impl.tuplesource.generated;

import io.deephaven.chunk.ByteChunk;
import io.deephaven.chunk.Chunk;
import io.deephaven.chunk.DoubleChunk;
import io.deephaven.chunk.IntChunk;
import io.deephaven.chunk.WritableChunk;
import io.deephaven.chunk.WritableObjectChunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.TupleSource;
import io.deephaven.engine.table.WritableColumnSource;
import io.deephaven.engine.table.impl.tuplesource.AbstractTupleSource;
import io.deephaven.engine.table.impl.tuplesource.ThreeColumnTupleSourceFactory;
import io.deephaven.tuple.generated.IntByteDoubleTuple;
import io.deephaven.util.BooleanUtils;
import io.deephaven.util.type.TypeUtils;
import org.jetbrains.annotations.NotNull;


/**
 * <p>{@link TupleSource} that produces key column values from {@link ColumnSource} types Integer, Byte, and Double.
 * <p>Generated by io.deephaven.replicators.TupleSourceCodeGenerator.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class IntegerReinterpretedBooleanDoubleColumnTupleSource extends AbstractTupleSource<IntByteDoubleTuple> {

    /** {@link ThreeColumnTupleSourceFactory} instance to create instances of {@link IntegerReinterpretedBooleanDoubleColumnTupleSource}. **/
    public static final ThreeColumnTupleSourceFactory<IntByteDoubleTuple, Integer, Byte, Double> FACTORY = new Factory();

    private final ColumnSource<Integer> columnSource1;
    private final ColumnSource<Byte> columnSource2;
    private final ColumnSource<Double> columnSource3;

    public IntegerReinterpretedBooleanDoubleColumnTupleSource(
            @NotNull final ColumnSource<Integer> columnSource1,
            @NotNull final ColumnSource<Byte> columnSource2,
            @NotNull final ColumnSource<Double> columnSource3
    ) {
        super(columnSource1, columnSource2, columnSource3);
        this.columnSource1 = columnSource1;
        this.columnSource2 = columnSource2;
        this.columnSource3 = columnSource3;
    }

    @Override
    public final IntByteDoubleTuple createTuple(final long rowKey) {
        return new IntByteDoubleTuple(
                columnSource1.getInt(rowKey),
                columnSource2.getByte(rowKey),
                columnSource3.getDouble(rowKey)
        );
    }

    @Override
    public final IntByteDoubleTuple createPreviousTuple(final long rowKey) {
        return new IntByteDoubleTuple(
                columnSource1.getPrevInt(rowKey),
                columnSource2.getPrevByte(rowKey),
                columnSource3.getPrevDouble(rowKey)
        );
    }

    @Override
    public final IntByteDoubleTuple createTupleFromValues(@NotNull final Object... values) {
        return new IntByteDoubleTuple(
                TypeUtils.unbox((Integer)values[0]),
                BooleanUtils.booleanAsByte((Boolean)values[1]),
                TypeUtils.unbox((Double)values[2])
        );
    }

    @Override
    public final IntByteDoubleTuple createTupleFromReinterpretedValues(@NotNull final Object... values) {
        return new IntByteDoubleTuple(
                TypeUtils.unbox((Integer)values[0]),
                TypeUtils.unbox((Byte)values[1]),
                TypeUtils.unbox((Double)values[2])
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <ELEMENT_TYPE> void exportElement(@NotNull final IntByteDoubleTuple tuple, final int elementIndex, @NotNull final WritableColumnSource<ELEMENT_TYPE> writableSource, final long destinationRowKey) {
        if (elementIndex == 0) {
            writableSource.set(destinationRowKey, tuple.getFirstElement());
            return;
        }
        if (elementIndex == 1) {
            writableSource.set(destinationRowKey, (ELEMENT_TYPE) BooleanUtils.byteAsBoolean(tuple.getSecondElement()));
            return;
        }
        if (elementIndex == 2) {
            writableSource.set(destinationRowKey, tuple.getThirdElement());
            return;
        }
        throw new IndexOutOfBoundsException("Invalid element index " + elementIndex + " for export");
    }

    @Override
    public final Object exportElement(@NotNull final IntByteDoubleTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return TypeUtils.box(tuple.getFirstElement());
        }
        if (elementIndex == 1) {
            return BooleanUtils.byteAsBoolean(tuple.getSecondElement());
        }
        if (elementIndex == 2) {
            return TypeUtils.box(tuple.getThirdElement());
        }
        throw new IllegalArgumentException("Bad elementIndex for 3 element tuple: " + elementIndex);
    }

    @Override
    public final Object exportElementReinterpreted(@NotNull final IntByteDoubleTuple tuple, int elementIndex) {
        if (elementIndex == 0) {
            return TypeUtils.box(tuple.getFirstElement());
        }
        if (elementIndex == 1) {
            return TypeUtils.box(tuple.getSecondElement());
        }
        if (elementIndex == 2) {
            return TypeUtils.box(tuple.getThirdElement());
        }
        throw new IllegalArgumentException("Bad elementIndex for 3 element tuple: " + elementIndex);
    }

    @Override
    protected void convertChunks(@NotNull WritableChunk<? super Values> destination, int chunkSize, Chunk<? extends Values> [] chunks) {
        WritableObjectChunk<IntByteDoubleTuple, ? super Values> destinationObjectChunk = destination.asWritableObjectChunk();
        IntChunk<? extends Values> chunk1 = chunks[0].asIntChunk();
        ByteChunk<? extends Values> chunk2 = chunks[1].asByteChunk();
        DoubleChunk<? extends Values> chunk3 = chunks[2].asDoubleChunk();
        for (int ii = 0; ii < chunkSize; ++ii) {
            destinationObjectChunk.set(ii, new IntByteDoubleTuple(chunk1.get(ii), chunk2.get(ii), chunk3.get(ii)));
        }
        destinationObjectChunk.setSize(chunkSize);
    }

    /** {@link ThreeColumnTupleSourceFactory} for instances of {@link IntegerReinterpretedBooleanDoubleColumnTupleSource}. **/
    private static final class Factory implements ThreeColumnTupleSourceFactory<IntByteDoubleTuple, Integer, Byte, Double> {

        private Factory() {
        }

        @Override
        public TupleSource<IntByteDoubleTuple> create(
                @NotNull final ColumnSource<Integer> columnSource1,
                @NotNull final ColumnSource<Byte> columnSource2,
                @NotNull final ColumnSource<Double> columnSource3
        ) {
            return new IntegerReinterpretedBooleanDoubleColumnTupleSource(
                    columnSource1,
                    columnSource2,
                    columnSource3
            );
        }
    }
}
