//
// Copyright (c) 2016-2025 Deephaven Data Labs and Patent Pending
//
package io.deephaven.engine.table.impl.updateby;

import io.deephaven.api.updateby.BadDataBehavior;
import io.deephaven.api.updateby.OperationControl;
import io.deephaven.api.updateby.UpdateByControl;
import io.deephaven.api.updateby.UpdateByOperation;
import io.deephaven.base.verify.Assert;
import io.deephaven.chunk.Chunk;
import io.deephaven.chunk.ObjectChunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.context.ExecutionContext;
import io.deephaven.engine.exceptions.TableInitializationException;
import io.deephaven.engine.rowset.RowSet;
import io.deephaven.engine.rowset.RowSetFactory;
import io.deephaven.engine.table.PartitionedTable;
import io.deephaven.engine.table.Table;
import io.deephaven.engine.table.impl.QueryTable;
import io.deephaven.engine.table.impl.TableDefaults;
import io.deephaven.engine.table.impl.locations.TableDataException;
import io.deephaven.engine.table.impl.util.ColumnHolder;
import io.deephaven.engine.table.vectors.ColumnVectors;
import io.deephaven.engine.testutil.ControlledUpdateGraph;
import io.deephaven.engine.testutil.EvalNugget;
import io.deephaven.engine.testutil.generator.CharGenerator;
import io.deephaven.engine.testutil.generator.SortedInstantGenerator;
import io.deephaven.engine.testutil.generator.TestDataGenerator;
import io.deephaven.test.types.OutOfBandTest;
import io.deephaven.time.DateTimeUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;

import static io.deephaven.engine.testutil.GenerateTableUpdates.generateAppends;
import static io.deephaven.engine.testutil.TstUtils.*;
import static io.deephaven.engine.testutil.TstUtils.validate;
import static io.deephaven.engine.testutil.testcase.RefreshingTableTestCase.simulateShiftAwareStep;
import static io.deephaven.engine.util.TableTools.*;
import static io.deephaven.function.Basic.isNull;
import static io.deephaven.time.DateTimeUtils.*;
import static io.deephaven.util.QueryConstants.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertThrows;

@Category(OutOfBandTest.class)
public class TestEms extends BaseUpdateByTest {
    private final BigDecimal allowableDelta = BigDecimal.valueOf(0.000000001);
    private final BigDecimal allowableFraction = BigDecimal.valueOf(0.000001);
    final static MathContext mathContextDefault = UpdateByControl.mathContextDefault();

    /**
     * These are used in the ticking table evaluations where we verify dynamic vs static tables.
     */
    final String[] columns = new String[] {
            "charCol",
            "byteCol",
            "shortCol",
            "intCol",
            "longCol",
            "floatCol",
            "doubleCol",
            "bigIntCol",
            "bigDecimalCol",
    };

    final int STATIC_TABLE_SIZE = 10_000;
    final int DYNAMIC_TABLE_SIZE = 1_000;
    final int DYNAMIC_UPDATE_SIZE = 100;
    final int DYNAMIC_UPDATE_STEPS = 20;

    // region Zero Key Tests
    @Test
    public void testStaticZeroKey() {
        final QueryTable t = createTestTable(STATIC_TABLE_SIZE, false, false, false, 0xFFFABBBC,
                new String[] {"ts", "charCol"}, new TestDataGenerator[] {
                        new SortedInstantGenerator(
                                parseInstant("2022-03-09T09:00:00.000 NY"),
                                parseInstant("2022-03-09T16:30:00.000 NY")),
                        new CharGenerator('A', 'z', 0.1)}).t;

        final OperationControl skipControl = OperationControl.builder()
                .onNullValue(BadDataBehavior.SKIP)
                .onNanValue(BadDataBehavior.SKIP).build();

        final OperationControl resetControl = OperationControl.builder()
                .onNullValue(BadDataBehavior.RESET)
                .onNanValue(BadDataBehavior.RESET).build();

        final Table actualSkip = t.updateBy(UpdateByOperation.Ems(skipControl, 100, columns));
        final Table actualReset = t.updateBy(UpdateByOperation.Ems(resetControl, 100, columns));

        for (String col : columns) {
            final Class<?> colType = t.getDefinition().getColumn(col).getDataType();
            assertWithEmsTicks(skipControl, 100, ColumnVectors.of(t, col).toArray(),
                    ColumnVectors.of(actualSkip, col).toArray(),
                    colType);
            assertWithEmsTicks(resetControl, 100, ColumnVectors.of(t, col).toArray(),
                    ColumnVectors.of(actualReset, col).toArray(),
                    colType);
        }

        final Table actualSkipTime = t.updateBy(UpdateByOperation.Ems(skipControl, "ts", 10 * MINUTE, columns));
        final Table actualResetTime = t.updateBy(UpdateByOperation.Ems(resetControl, "ts", 10 * MINUTE, columns));

        final Instant[] ts = ColumnVectors.ofObject(t, "ts", Instant.class).toArray();
        final long[] timestamps = new long[t.intSize()];
        for (int i = 0; i < t.intSize(); i++) {
            timestamps[i] = DateTimeUtils.epochNanos(ts[i]);
        }

        for (String col : columns) {
            final Class<?> colType = t.getDefinition().getColumn(col).getDataType();
            assertWithEmsTime(skipControl, 10 * MINUTE, timestamps, ColumnVectors.of(t, col).toArray(),
                    ColumnVectors.of(actualSkipTime, col).toArray(),
                    colType);
            assertWithEmsTime(resetControl, 10 * MINUTE, timestamps, ColumnVectors.of(t, col).toArray(),
                    ColumnVectors.of(actualResetTime, col).toArray(),
                    colType);
        }
    }
    // endregion

    // region Bucketed Tests
    @Test
    public void testStaticBucketed() {
        doTestStaticBucketed(false);
    }

    @Test
    public void testStaticGroupedBucketed() {
        doTestStaticBucketed(true);
    }

    private void doTestStaticBucketed(boolean grouped) {
        final TableDefaults t = createTestTable(STATIC_TABLE_SIZE, true, grouped, false, 0x31313131,
                new String[] {"ts", "charCol"}, new TestDataGenerator[] {
                        new SortedInstantGenerator(
                                parseInstant("2022-03-09T09:00:00.000 NY"),
                                parseInstant("2022-03-09T16:30:00.000 NY")),
                        new CharGenerator('A', 'z', 0.1)}).t;

        final OperationControl skipControl = OperationControl.builder()
                .onNullValue(BadDataBehavior.SKIP)
                .onNanValue(BadDataBehavior.SKIP).build();

        final OperationControl resetControl = OperationControl.builder()
                .onNullValue(BadDataBehavior.RESET)
                .onNanValue(BadDataBehavior.RESET).build();

        final Table actualSkip = t.updateBy(UpdateByOperation.Ems(skipControl, 100, columns), "Sym");
        final Table actualReset = t.updateBy(UpdateByOperation.Ems(resetControl, 100, columns), "Sym");

        final PartitionedTable preOp = t.partitionBy("Sym");
        final PartitionedTable postOpSkip = actualSkip.partitionBy("Sym");
        final PartitionedTable postOpReset = actualReset.partitionBy("Sym");

        preOp.partitionedTransform(postOpSkip, (source, actual) -> {
            Arrays.stream(columns).forEach(col -> {
                final Class<?> colType = source.getDefinition().getColumn(col).getDataType();
                assertWithEmsTicks(skipControl, 100, ColumnVectors.of(source, col).toArray(),
                        ColumnVectors.of(actual, col).toArray(),
                        colType);
            });
            return source;
        });

        preOp.partitionedTransform(postOpReset, (source, actual) -> {
            Arrays.stream(columns).forEach(col -> {
                final Class<?> colType = source.getDefinition().getColumn(col).getDataType();
                assertWithEmsTicks(resetControl, 100, ColumnVectors.of(source, col).toArray(),
                        ColumnVectors.of(actual, col).toArray(),
                        colType);
            });
            return source;
        });

        final Table actualSkipTime = t.updateBy(UpdateByOperation.Ems(skipControl, "ts", 10 * MINUTE, columns), "Sym");
        final Table actualResetTime =
                t.updateBy(UpdateByOperation.Ems(resetControl, "ts", 10 * MINUTE, columns), "Sym");

        final PartitionedTable postOpSkipTime = actualSkipTime.partitionBy("Sym");
        final PartitionedTable postOpResetTime = actualResetTime.partitionBy("Sym");

        preOp.partitionedTransform(postOpSkipTime, (source, actual) -> {
            final int sourceSize = source.intSize();
            final Instant[] ts = ColumnVectors.ofObject(source, "ts", Instant.class).toArray();
            final long[] timestamps = new long[sourceSize];
            for (int i = 0; i < sourceSize; i++) {
                timestamps[i] = DateTimeUtils.epochNanos(ts[i]);
            }
            Arrays.stream(columns).forEach(col -> {
                final Class<?> colType = source.getDefinition().getColumn(col).getDataType();
                assertWithEmsTime(skipControl, 10 * MINUTE, timestamps,
                        ColumnVectors.of(source, col).toArray(),
                        ColumnVectors.of(actual, col).toArray(),
                        colType);
            });
            return source;
        });

        preOp.partitionedTransform(postOpResetTime, (source, actual) -> {
            final int sourceSize = source.intSize();
            final Instant[] ts = ColumnVectors.ofObject(source, "ts", Instant.class).toArray();
            final long[] timestamps = new long[sourceSize];
            for (int i = 0; i < sourceSize; i++) {
                timestamps[i] = DateTimeUtils.epochNanos(ts[i]);
            }
            Arrays.stream(columns).forEach(col -> {
                final Class<?> colType = source.getDefinition().getColumn(col).getDataType();
                assertWithEmsTime(resetControl, 10 * MINUTE, timestamps,
                        ColumnVectors.of(source, col).toArray(),
                        ColumnVectors.of(actual, col).toArray(),
                        colType);
            });
            return source;
        });

    }

    @Test
    public void testThrowBehaviors() {
        final OperationControl throwControl = OperationControl.builder()
                .onNullValue(BadDataBehavior.THROW).build();

        final TableDefaults bytes = testTable(RowSetFactory.flat(4).toTracking(),
                byteCol("col", (byte) 0, (byte) 1, NULL_BYTE, (byte) 3));

        Throwable err = assertThrows(TableInitializationException.class,
                () -> bytes.updateBy(UpdateByOperation.Ems(throwControl, 10)));
        Assert.eqTrue(err.getCause().getClass() == TableDataException.class,
                "err.getCause().getClass() == TableDataException.class");


        err = assertThrows(TableInitializationException.class,
                () -> bytes.updateBy(UpdateByOperation.Ems(throwControl, 10)));
        Assert.eqTrue(err.getCause().getClass() == TableDataException.class,
                "err.getCause().getClass() == TableDataException.class");

        TableDefaults shorts = testTable(RowSetFactory.flat(4).toTracking(),
                shortCol("col", (short) 0, (short) 1, NULL_SHORT, (short) 3));

        err = assertThrows(TableInitializationException.class,
                () -> shorts.updateBy(UpdateByOperation.Ems(throwControl, 10)));
        Assert.eqTrue(err.getCause().getClass() == TableDataException.class,
                "err.getCause().getClass() == TableDataException.class");

        TableDefaults ints = testTable(RowSetFactory.flat(4).toTracking(),
                intCol("col", 0, 1, NULL_INT, 3));

        err = assertThrows(TableInitializationException.class,
                () -> ints.updateBy(UpdateByOperation.Ems(throwControl, 10)));
        Assert.eqTrue(err.getCause().getClass() == TableDataException.class,
                "err.getCause().getClass() == TableDataException.class");

        TableDefaults longs = testTable(RowSetFactory.flat(4).toTracking(),
                longCol("col", 0, 1, NULL_LONG, 3));

        err = assertThrows(TableInitializationException.class,
                () -> longs.updateBy(UpdateByOperation.Ems(throwControl, 10)));
        Assert.eqTrue(err.getCause().getClass() == TableDataException.class,
                "err.getCause().getClass() == TableDataException.class");

        TableDefaults floats = testTable(RowSetFactory.flat(4).toTracking(),
                floatCol("col", 0, 1, NULL_FLOAT, Float.NaN));

        err = assertThrows(TableInitializationException.class,
                () -> floats.updateBy(
                        UpdateByOperation.Ems(OperationControl.builder().onNullValue(BadDataBehavior.THROW).build(),
                                10)));
        err = err.getCause();
        Assert.eqTrue(err.getClass() == TableDataException.class,
                "err.getClass() == TableDataException.class");
        Assert.eqTrue(err.getMessage().contains("Encountered null value during Exponential Moving output processing"),
                "err.getMessage().contains(\"Encountered null value during Exponential Moving output processing\")");

        err = assertThrows(TableInitializationException.class,
                () -> floats.updateBy(
                        UpdateByOperation.Ems(OperationControl.builder().onNanValue(BadDataBehavior.THROW).build(),
                                10)));
        err = err.getCause();
        Assert.eqTrue(err.getClass() == TableDataException.class,
                "err.getClass() == TableDataException.class");
        Assert.eqTrue(err.getMessage().contains("Encountered NaN value during Exponential Moving output processing"),
                "err.getMessage().contains(\"Encountered NaN value during Exponential Moving output processing\")");

        TableDefaults doubles = testTable(RowSetFactory.flat(4).toTracking(),
                doubleCol("col", 0, 1, NULL_DOUBLE, Double.NaN));

        err = assertThrows(TableInitializationException.class,
                () -> doubles.updateBy(
                        UpdateByOperation.Ems(OperationControl.builder().onNullValue(BadDataBehavior.THROW).build(),
                                10)));
        err = err.getCause();
        Assert.eqTrue(err.getClass() == TableDataException.class,
                "err.getClass() == TableDataException.class");
        Assert.eqTrue(err.getMessage().contains("Encountered null value during Exponential Moving output processing"),
                "err.getMessage().contains(\"Encountered null value during Exponential Moving output processing\")");

        err = assertThrows(TableInitializationException.class,
                () -> doubles.updateBy(
                        UpdateByOperation.Ems(OperationControl.builder().onNanValue(BadDataBehavior.THROW).build(),
                                10)));
        err = err.getCause();
        Assert.eqTrue(err.getClass() == TableDataException.class,
                "err.getClass() == TableDataException.class");
        Assert.eqTrue(err.getMessage().contains("Encountered NaN value during Exponential Moving output processing"),
                "err.getMessage().contains(\"Encountered NaN value during Exponential Moving output processing\")");


        TableDefaults bi = testTable(RowSetFactory.flat(4).toTracking(),
                col("col", BigInteger.valueOf(0), BigInteger.valueOf(1), null, BigInteger.valueOf(3)));

        err = assertThrows(TableInitializationException.class,
                () -> bi.updateBy(UpdateByOperation.Ems(throwControl, 10)));
        Assert.eqTrue(err.getCause().getClass() == TableDataException.class,
                "err.getCause().getClass() == TableDataException.class");

        TableDefaults bd = testTable(RowSetFactory.flat(4).toTracking(),
                col("col", BigDecimal.valueOf(0), BigDecimal.valueOf(1), null, BigDecimal.valueOf(3)));

        err = assertThrows(TableInitializationException.class,
                () -> bd.updateBy(UpdateByOperation.Ems(throwControl, 10)));
        Assert.eqTrue(err.getCause().getClass() == TableDataException.class,
                "err.getCause().getClass() == TableDataException.class");
    }

    @Test
    public void testTimeThrowBehaviors() {
        final ColumnHolder ts = col("ts",
                parseInstant("2022-03-11T09:30:00.000 NY"),
                parseInstant("2022-03-11T09:29:00.000 NY"),
                parseInstant("2022-03-11T09:30:00.000 NY"),
                parseInstant("2022-03-11T09:32:00.000 NY"),
                null);

        testThrowsInternal(
                testTable(RowSetFactory.flat(5).toTracking(), ts,
                        byteCol("col", (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4)));

        testThrowsInternal(
                testTable(RowSetFactory.flat(5).toTracking(), ts,
                        shortCol("col", (short) 0, (short) 1, (short) 2, (short) 3, (short) 4)));

        testThrowsInternal(
                testTable(RowSetFactory.flat(5).toTracking(), ts,
                        intCol("col", 0, 1, 2, 3, 4)));

        testThrowsInternal(
                testTable(RowSetFactory.flat(5).toTracking(), ts,
                        longCol("col", 0, 1, 2, 3, 4)));

        testThrowsInternal(
                testTable(RowSetFactory.flat(5).toTracking(), ts,
                        floatCol("col", 0, 1, 2, 3, 4)));

        testThrowsInternal(
                testTable(RowSetFactory.flat(5).toTracking(), ts,
                        doubleCol("col", 0, 1, 2, 3, 4)));

        testThrowsInternal(
                testTable(RowSetFactory.flat(5).toTracking(), ts,
                        col("col", BigInteger.valueOf(0),
                                BigInteger.valueOf(1),
                                BigInteger.valueOf(2),
                                BigInteger.valueOf(3),
                                BigInteger.valueOf(4))));

        testThrowsInternal(
                testTable(RowSetFactory.flat(5).toTracking(), ts,
                        col("col", BigDecimal.valueOf(0),
                                BigDecimal.valueOf(1),
                                BigDecimal.valueOf(2),
                                BigDecimal.valueOf(3),
                                BigDecimal.valueOf(4))));
    }

    private void testThrowsInternal(TableDefaults table) {
        assertThrows(
                "Encountered negative delta time during EMS processing",
                TableDataException.class,
                () -> table.updateBy(UpdateByOperation.Ems(
                        OperationControl.builder().build(), "ts", 100)));
    }

    @Test
    public void testResetBehavior() {
        // Value reset
        final OperationControl dataResetControl = OperationControl.builder()
                .onNullValue(BadDataBehavior.RESET)
                .build();

        final ColumnHolder ts = col("ts",
                parseInstant("2022-03-11T09:30:00.000 NY"),
                parseInstant("2022-03-11T09:31:00.000 NY"),
                parseInstant("2022-03-11T09:32:00.000 NY"),
                parseInstant("2022-03-11T09:33:00.000 NY"),
                parseInstant("2022-03-11T09:34:00.000 NY"),
                parseInstant("2022-03-11T09:35:00.000 NY"));

        Table expected = testTable(RowSetFactory.flat(6).toTracking(), ts,
                doubleCol("col", 0, NULL_DOUBLE, 2, NULL_DOUBLE, 4, NULL_DOUBLE));

        TableDefaults input = testTable(RowSetFactory.flat(6).toTracking(), ts,
                byteCol("col", (byte) 0, NULL_BYTE, (byte) 2, NULL_BYTE, (byte) 4, NULL_BYTE));
        Table result = input.updateBy(UpdateByOperation.Ems(dataResetControl, "ts", 1_000_000_000));
        assertTableEquals(expected, result);

        input = testTable(RowSetFactory.flat(6).toTracking(), ts,
                shortCol("col", (short) 0, NULL_SHORT, (short) 2, NULL_SHORT, (short) 4, NULL_SHORT));
        result = input.updateBy(UpdateByOperation.Ems(dataResetControl, "ts", 1_000_000_000));
        assertTableEquals(expected, result);

        input = testTable(RowSetFactory.flat(6).toTracking(), ts,
                intCol("col", 0, NULL_INT, 2, NULL_INT, 4, NULL_INT));
        result = input.updateBy(UpdateByOperation.Ems(dataResetControl, "ts", 1_000_000_000));
        assertTableEquals(expected, result);

        input = testTable(RowSetFactory.flat(6).toTracking(), ts,
                longCol("col", 0, NULL_LONG, 2, NULL_LONG, 4, NULL_LONG));
        result = input.updateBy(UpdateByOperation.Ems(dataResetControl, "ts", 1_000_000_000));
        assertTableEquals(expected, result);

        input = testTable(RowSetFactory.flat(6).toTracking(), ts,
                floatCol("col", 0, NULL_FLOAT, 2, NULL_FLOAT, 4, NULL_FLOAT));
        result = input.updateBy(UpdateByOperation.Ems(dataResetControl, "ts", 1_000_000_000));
        assertTableEquals(expected, result);

        input = testTable(RowSetFactory.flat(6).toTracking(), ts,
                doubleCol("col", 0, NULL_DOUBLE, 2, NULL_DOUBLE, 4, NULL_DOUBLE));
        result = input.updateBy(UpdateByOperation.Ems(dataResetControl, "ts", 1_000_000_000));
        assertTableEquals(expected, result);

        // BigInteger/BigDecimal

        expected = testTable(RowSetFactory.flat(6).toTracking(), ts,
                col("col", BigDecimal.valueOf(0), null, BigDecimal.valueOf(2), null, BigDecimal.valueOf(4), null));

        input = testTable(RowSetFactory.flat(6).toTracking(), ts,
                col("col", BigInteger.valueOf(0),
                        null,
                        BigInteger.valueOf(2),
                        null,
                        BigInteger.valueOf(4),
                        null));
        result = input.updateBy(UpdateByOperation.Ems(dataResetControl, "ts", 1_000_000_000));
        assertTableEquals(expected, result);

        input = testTable(RowSetFactory.flat(6).toTracking(), ts,
                col("col", BigDecimal.valueOf(0),
                        null,
                        BigDecimal.valueOf(2),
                        null,
                        BigDecimal.valueOf(4),
                        null));
        result = input.updateBy(UpdateByOperation.Ems(dataResetControl, "ts", 1_000_000_000));
        assertTableEquals(expected, result);

        // Test reset for NaN values
        final OperationControl resetControl = OperationControl.builder()
                .onNanValue(BadDataBehavior.RESET)
                .build();

        input = testTable(RowSetFactory.flat(3).toTracking(), doubleCol("col", 0, Double.NaN, 1));
        result = input.updateBy(UpdateByOperation.Ems(resetControl, 100));
        expected = testTable(RowSetFactory.flat(3).toTracking(), doubleCol("col", 0, NULL_DOUBLE, 1));
        assertTableEquals(expected, result);

        input = testTable(RowSetFactory.flat(3).toTracking(), floatCol("col", 0, Float.NaN, 1));
        result = input.updateBy(UpdateByOperation.Ems(resetControl, 100));
        expected = testTable(RowSetFactory.flat(3).toTracking(), doubleCol("col", 0, NULL_DOUBLE, 1));
        assertTableEquals(expected, result);
    }

    @Test
    public void testPoison() {
        final OperationControl nanCtl = OperationControl.builder().onNanValue(BadDataBehavior.POISON)
                .onNullValue(BadDataBehavior.RESET)
                .build();

        Table expected = testTable(RowSetFactory.flat(5).toTracking(),
                doubleCol("col", 0, Double.NaN, NULL_DOUBLE, Double.NaN, Double.NaN));
        TableDefaults input = testTable(RowSetFactory.flat(5).toTracking(),
                doubleCol("col", 0, Double.NaN, NULL_DOUBLE, Double.NaN, 4));
        assertTableEquals(expected, input.updateBy(UpdateByOperation.Ems(nanCtl, 10)));
        input = testTable(RowSetFactory.flat(5).toTracking(), floatCol("col", 0, Float.NaN, NULL_FLOAT, Float.NaN, 4));
        assertTableEquals(expected, input.updateBy(UpdateByOperation.Ems(nanCtl, 10)));

        final ColumnHolder ts = col("ts",
                parseInstant("2022-03-11T09:30:00.000 NY"),
                parseInstant("2022-03-11T09:31:00.000 NY"),
                null,
                parseInstant("2022-03-11T09:33:00.000 NY"),
                parseInstant("2022-03-11T09:34:00.000 NY"),
                null);

        expected = testTable(RowSetFactory.flat(6).toTracking(), ts,
                doubleCol("col", 0, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN));
        input = testTable(RowSetFactory.flat(6).toTracking(), ts,
                doubleCol("col", 0, Double.NaN, 2, Double.NaN, 4, 5));
        Table result = input.updateBy(UpdateByOperation.Ems(nanCtl, "ts", 10));
        assertTableEquals(expected, result);

        input = testTable(RowSetFactory.flat(6).toTracking(), ts,
                floatCol("col", 0, Float.NaN, 2, Float.NaN, 4, 5));
        assertTableEquals(expected, input.updateBy(UpdateByOperation.Ems(nanCtl, "ts", 10)));
    }

    /**
     * This is a hacky, inefficient way to force nulls into the timestamps while maintaining sorted-ness otherwise
     */
    private class SortedIntGeneratorWithNulls extends SortedInstantGenerator {
        final double nullFrac;

        public SortedIntGeneratorWithNulls(Instant minTime, Instant maxTime, double nullFrac) {
            super(minTime, maxTime);
            this.nullFrac = nullFrac;
        }

        @Override
        public Chunk<Values> populateChunk(RowSet toAdd, Random random) {
            Chunk<Values> retChunk = super.populateChunk(toAdd, random);
            if (nullFrac == 0.0) {
                return retChunk;
            }
            ObjectChunk<Instant, Values> srcChunk = retChunk.asObjectChunk();
            Object[] dateArr = new Object[srcChunk.size()];
            srcChunk.copyToArray(0, dateArr, 0, dateArr.length);

            // force some entries to null
            for (int ii = 0; ii < srcChunk.size(); ii++) {
                if (random.nextDouble() < nullFrac) {
                    dateArr[ii] = null;
                }
            }
            return ObjectChunk.chunkWrap(dateArr);
        }
    }

    @Test
    public void testNullTimestamps() {
        final CreateResult timeResult = createTestTable(100, true, false, true, 0x31313131,
                new String[] {"ts"}, new TestDataGenerator[] {new SortedIntGeneratorWithNulls(
                        parseInstant("2022-03-09T09:00:00.000 NY"),
                        parseInstant("2022-03-09T16:30:00.000 NY"),
                        0.25)});

        final OperationControl skipControl = OperationControl.builder()
                .onNullValue(BadDataBehavior.SKIP)
                .onNanValue(BadDataBehavior.SKIP).build();

        final EvalNugget[] timeNuggets = new EvalNugget[] {
                new EvalNugget() {
                    @Override
                    protected Table e() {
                        TableDefaults base = timeResult.t;
                        // short timescale to make sure we trigger all the transition behavior
                        return base.updateBy(UpdateByOperation.Ems(skipControl, "ts", 2 * MINUTE));
                    }
                }
        };
        final Random billy = new Random(0xB177B177);
        for (int ii = 0; ii < 100; ii++) {
            try {
                simulateShiftAwareStep(10, billy, timeResult.t, timeResult.infos, timeNuggets);
            } catch (Throwable t) {
                System.out.println("Crapped out on step " + ii);
                throw t;
            }
        }
    }
    // endregion


    // region Live Tests
    @Test
    public void testZeroKeyAppendOnly() {
        doTestTicking(false, true);
    }

    @Test
    public void testZeroKeyGeneral() {
        doTestTicking(false, false);
    }

    @Test
    public void testBucketedAppendOnly() {
        doTestTicking(true, true);
    }

    @Test
    public void testBucketedGeneral() {
        doTestTicking(true, false);
    }

    private void doTestTicking(boolean bucketed, boolean appendOnly) {
        final CreateResult tickResult = createTestTable(DYNAMIC_TABLE_SIZE, bucketed, false, true, 0x31313131,
                new String[] {"charCol"}, new TestDataGenerator[] {
                        new CharGenerator('A', 'z', 0.1)});
        final CreateResult timeResult = createTestTable(DYNAMIC_TABLE_SIZE, bucketed, false, true, 0x31313131,
                new String[] {"ts", "charCol"}, new TestDataGenerator[] {
                        new SortedInstantGenerator(
                                parseInstant("2022-03-09T09:00:00.000 NY"),
                                parseInstant("2022-03-09T16:30:00.000 NY")),
                        new CharGenerator('A', 'z', 0.1)});

        if (appendOnly) {
            tickResult.t.setAttribute(Table.APPEND_ONLY_TABLE_ATTRIBUTE, Boolean.TRUE);
            timeResult.t.setAttribute(Table.APPEND_ONLY_TABLE_ATTRIBUTE, Boolean.TRUE);
        }

        final OperationControl skipControl = OperationControl.builder()
                .onNullValue(BadDataBehavior.SKIP)
                .onNanValue(BadDataBehavior.SKIP).build();

        final OperationControl resetControl = OperationControl.builder()
                .onNullValue(BadDataBehavior.RESET)
                .onNanValue(BadDataBehavior.RESET).build();

        final EvalNugget[] nuggets = new EvalNugget[] {
                new EvalNugget() {
                    @Override
                    protected Table e() {
                        return bucketed
                                ? tickResult.t.updateBy(UpdateByOperation.Ems(skipControl, 100), "Sym")
                                : tickResult.t.updateBy(UpdateByOperation.Ems(skipControl, 100));
                    }
                },
                new EvalNugget() {
                    @Override
                    protected Table e() {
                        return bucketed
                                ? tickResult.t.updateBy(UpdateByOperation.Ems(resetControl, 100), "Sym")
                                : tickResult.t.updateBy(UpdateByOperation.Ems(resetControl, 100));
                    }
                }
        };

        final EvalNugget[] timeNuggets = new EvalNugget[] {
                new EvalNugget() {
                    @Override
                    protected Table e() {
                        TableDefaults base = timeResult.t;
                        if (!appendOnly) {
                            base = (TableDefaults) base.sort("ts");
                        }
                        return bucketed
                                ? base.updateBy(UpdateByOperation.Ems(skipControl, "ts", 10 * MINUTE), "Sym")
                                : base.updateBy(UpdateByOperation.Ems(skipControl, "ts", 10 * MINUTE));
                    }
                },
                new EvalNugget() {
                    @Override
                    protected Table e() {
                        TableDefaults base = timeResult.t;
                        if (!appendOnly) {
                            base = (TableDefaults) base.sort("ts");
                        }
                        return bucketed
                                ? base.updateBy(UpdateByOperation.Ems(resetControl, "ts", 10 * MINUTE), "Sym")
                                : base.updateBy(UpdateByOperation.Ems(resetControl, "ts", 10 * MINUTE));
                    }
                }
        };

        final Random billy = new Random(0xB177B177);
        for (int ii = 0; ii < DYNAMIC_UPDATE_STEPS; ii++) {
            try {
                if (appendOnly) {
                    ExecutionContext.getContext().getUpdateGraph().<ControlledUpdateGraph>cast().runWithinUnitTestCycle(
                            () -> {
                                generateAppends(DYNAMIC_UPDATE_SIZE, billy, tickResult.t, tickResult.infos);
                                generateAppends(DYNAMIC_UPDATE_SIZE, billy, timeResult.t, timeResult.infos);
                            });
                    validate("Table", nuggets);
                    validate("Table", timeNuggets);
                } else {
                    simulateShiftAwareStep(DYNAMIC_UPDATE_SIZE, billy, tickResult.t, tickResult.infos, nuggets);
                    simulateShiftAwareStep(DYNAMIC_UPDATE_SIZE, billy, timeResult.t, timeResult.infos, timeNuggets);
                }
            } catch (Throwable t) {
                System.out.println("Crapped out on step " + ii);
                throw t;
            }
        }
    }
    // endregion

    // region Special Tests
    @Test
    public void testInterfaces() {
        // This test will verify that the interfaces exposed by the UpdateByOperation class are usable without errors.

        final QueryTable t = createTestTable(100, false, false, false, 0xFFFABBBC,
                new String[] {"ts", "charCol"}, new TestDataGenerator[] {
                        new SortedInstantGenerator(
                                parseInstant("2022-03-09T09:00:00.000 NY"),
                                parseInstant("2022-03-09T16:30:00.000 NY")),
                        new CharGenerator('A', 'z', 0.1)}).t;

        final OperationControl skipControl = OperationControl.builder()
                .onNullValue(BadDataBehavior.SKIP)
                .onNanValue(BadDataBehavior.SKIP).build();

        final OperationControl resetControl = OperationControl.builder()
                .onNullValue(BadDataBehavior.RESET)
                .onNanValue(BadDataBehavior.RESET).build();

        final Instant[] ts = ColumnVectors.ofObject(t, "ts", Instant.class).toArray();
        final long[] timestamps = new long[t.intSize()];
        for (int i = 0; i < t.intSize(); i++) {
            timestamps[i] = epochNanos(ts[i]);
        }

        Table actual = t.updateBy(UpdateByOperation.Ems(100, columns));

        Table actualSkip = t.updateBy(UpdateByOperation.Ems(skipControl, 100, columns));
        Table actualReset = t.updateBy(UpdateByOperation.Ems(resetControl, 100, columns));

        Table actualTime = t.updateBy(UpdateByOperation.Ems("ts", 10 * MINUTE, columns));

        Table actualSkipTime = t.updateBy(UpdateByOperation.Ems(skipControl, "ts", 10 * MINUTE, columns));
        Table actualResetTime = t.updateBy(UpdateByOperation.Ems(resetControl, "ts", 10 * MINUTE, columns));

        actualTime = t.updateBy(UpdateByOperation.Ems("ts", Duration.ofMinutes(10), columns));

        actualSkipTime = t.updateBy(UpdateByOperation.Ems(skipControl, "ts", Duration.ofMinutes(10), columns));
        actualResetTime = t.updateBy(UpdateByOperation.Ems(resetControl, "ts", Duration.ofMinutes(10), columns));
    }
    // endregion

    // region Manual Verification functions
    public static double[] compute_ems_ticks(OperationControl control, long ticks, double[] values) {
        if (values == null) {
            return null;
        }

        if (values.length == 0) {
            return new double[0];
        }

        final double alpha = Math.exp(-1.0 / (double) ticks);

        final double[] result = new double[values.length];
        double runningVal = NULL_DOUBLE;

        for (int i = 0; i < values.length; i++) {
            if (values[i] == NULL_DOUBLE) {
                if (control.onNullValueOrDefault() == BadDataBehavior.RESET) {
                    runningVal = NULL_DOUBLE;
                }
                // if SKIP, keep prev runningVal
            } else if (values[i] == Double.NaN) {
                if (control.onNanValueOrDefault() == BadDataBehavior.POISON) {
                    runningVal = Double.NaN;
                } else if (control.onNanValueOrDefault() == BadDataBehavior.RESET) {
                    runningVal = NULL_DOUBLE;
                }
            } else if (runningVal == NULL_DOUBLE) {
                runningVal = values[i];
            } else {
                final double decayedVal = alpha * runningVal;
                // Compute EM Sum by adding the current value to the decayed previous value.
                runningVal = decayedVal + values[i];
            }
            result[i] = runningVal;
        }

        return result;
    }

    public static BigDecimal[] compute_ems_ticks(OperationControl control, long ticks, BigDecimal[] values) {
        if (values == null) {
            return null;
        }

        if (values.length == 0) {
            return new BigDecimal[0];
        }

        final BigDecimal alpha = BigDecimal.valueOf(Math.exp(-1.0 / (double) ticks));

        final BigDecimal[] result = new BigDecimal[values.length];
        BigDecimal runningVal = null;

        for (int i = 0; i < values.length; i++) {
            if (values[i] == null) {
                if (control.onNullValueOrDefault() == BadDataBehavior.RESET) {
                    runningVal = null;
                }
                // if SKIP, keep prev runningVal
            } else if (runningVal == null) {
                runningVal = values[i];
            } else {
                final BigDecimal decayedVal = alpha.multiply(runningVal, mathContextDefault);
                // Compute EM Sum by adding the current value to the decayed previous value.
                runningVal = decayedVal.add(values[i], mathContextDefault);
            }
            result[i] = runningVal;
        }

        return result;
    }

    public static double[] compute_ems_time(OperationControl control, long nanos, long[] timestamps, double[] values) {
        if (values == null) {
            return null;
        }

        if (values.length == 0) {
            return new double[0];
        }

        final double[] result = new double[values.length];
        double runningVal = NULL_DOUBLE;
        long lastStamp = NULL_LONG;

        for (int i = 0; i < values.length; i++) {
            final long timestamp = timestamps[i];

            if (values[i] == NULL_DOUBLE) {
                if (control.onNullValueOrDefault() == BadDataBehavior.RESET) {
                    runningVal = NULL_DOUBLE;
                }
                // if SKIP, keep prev runningVal
            } else if (values[i] == Double.NaN) {
                if (control.onNanValueOrDefault() == BadDataBehavior.POISON) {
                    runningVal = Double.NaN;
                } else if (control.onNanValueOrDefault() == BadDataBehavior.RESET) {
                    runningVal = NULL_DOUBLE;
                }
            } else if (timestamp == NULL_LONG) {
                // no change to curVal and lastStamp
            } else if (runningVal == NULL_DOUBLE) {
                runningVal = values[i];
                lastStamp = timestamp;
            } else {
                final long dt = timestamp - lastStamp;

                // alpha is dynamic, based on time
                final double alpha = Math.exp(-dt / (double) nanos);
                final double decayedVal = alpha * runningVal;
                // Compute EMSum by adding the current value to the decayed previous value.
                runningVal = decayedVal + values[i];
                lastStamp = timestamp;
            }
            result[i] = runningVal;
        }

        return result;
    }

    public static BigDecimal[] compute_ems_time(OperationControl control, long nanos, long[] timestamps,
            BigDecimal[] values) {
        if (values == null) {
            return null;
        }

        if (values.length == 0) {
            return new BigDecimal[0];
        }


        final BigDecimal[] result = new BigDecimal[values.length];
        BigDecimal runningVal = null;
        long lastStamp = NULL_LONG;

        for (int i = 0; i < values.length; i++) {
            final long timestamp = timestamps[i];

            if (values[i] == null) {
                if (control.onNullValueOrDefault() == BadDataBehavior.RESET) {
                    runningVal = null;
                }
                // if SKIP, keep prev runningVal
            } else if (runningVal == null) {
                runningVal = values[i];
                lastStamp = timestamp;
            } else if (timestamp == NULL_LONG) {
                // no change to curVal and lastStamp
            } else {
                final long dt = timestamp - lastStamp;

                // alpha is dynamic, based on time
                final BigDecimal alpha = BigDecimal.valueOf(Math.exp(-dt / (double) nanos));
                final BigDecimal decayedVal = alpha.multiply(runningVal, mathContextDefault);
                // Compute EM Sum by adding the current value to the decayed previous value.
                runningVal = decayedVal.add(values[i], mathContextDefault);
                lastStamp = timestamp;
            }
            result[i] = runningVal;
        }
        return result;
    }


    final double[] convertArray(byte[] array) {
        final double[] result = new double[array.length];
        for (int ii = 0; ii < array.length; ii++) {
            result[ii] = isNull(array[ii]) ? NULL_DOUBLE : (double) array[ii];
        }
        return result;
    }

    final double[] convertArray(char[] array) {
        final double[] result = new double[array.length];
        for (int ii = 0; ii < array.length; ii++) {
            result[ii] = isNull(array[ii]) ? NULL_DOUBLE : (double) array[ii];
        }
        return result;
    }

    final double[] convertArray(short[] array) {
        final double[] result = new double[array.length];
        for (int ii = 0; ii < array.length; ii++) {
            result[ii] = isNull(array[ii]) ? NULL_DOUBLE : (double) array[ii];
        }
        return result;
    }

    final double[] convertArray(int[] array) {
        final double[] result = new double[array.length];
        for (int ii = 0; ii < array.length; ii++) {
            result[ii] = isNull(array[ii]) ? NULL_DOUBLE : (double) array[ii];
        }
        return result;
    }

    final double[] convertArray(long[] array) {
        final double[] result = new double[array.length];
        for (int ii = 0; ii < array.length; ii++) {
            result[ii] = isNull(array[ii]) ? NULL_DOUBLE : (double) array[ii];
        }
        return result;
    }

    final double[] convertArray(float[] array) {
        final double[] result = new double[array.length];
        for (int ii = 0; ii < array.length; ii++) {
            result[ii] = isNull(array[ii]) ? NULL_DOUBLE : (double) array[ii];
        }
        return result;
    }

    final BigDecimal[] convertArray(BigInteger[] array) {
        final BigDecimal[] result = new BigDecimal[array.length];
        for (int ii = 0; ii < array.length; ii++) {
            result[ii] = isNull(array[ii]) ? null : new BigDecimal(array[ii]);
        }
        return result;
    }

    final boolean fuzzyEquals(BigDecimal actualVal, BigDecimal expectedVal) {
        if (actualVal == null && expectedVal == null) {
            return true;
        } else if (actualVal == null) {
            return false;
        } else if (expectedVal == null) {
            return false;
        }

        BigDecimal diff = actualVal.subtract(expectedVal, mathContextDefault).abs();
        // Equal if the difference is zero or smaller than the allowed delta
        if (diff.equals(BigDecimal.ZERO) || allowableDelta.compareTo(diff) == 1) {
            return true;
        }

        // Equal if the difference is smaller than the allowable fraction.
        BigDecimal diffFraction = diff.divide(actualVal, mathContextDefault).abs();
        return allowableFraction.compareTo(diffFraction) == 1;
    }

    void assertBDArrayEquals(final BigDecimal[] expected,
            final BigDecimal[] actual) {
        if (expected.length != actual.length) {
            fail("Array lengths do not mach");
        }
        for (int ii = 0; ii < expected.length; ii++) {
            if (!fuzzyEquals(expected[ii], actual[ii])) {
                fail("Values do not match, expected: " + expected[ii] + " vs. actual: " + actual[ii]);
            }
        }
    }

    final void assertWithEmsTicks(final OperationControl control,
            final long ticks,
            @NotNull final Object expected,
            @NotNull final Object actual,
            final Class<?> type) {
        if (expected instanceof double[]) {
            assertArrayEquals(compute_ems_ticks(control, ticks, (double[]) expected), (double[]) actual, .001d);
        }
        if (expected instanceof byte[]) {
            assertArrayEquals(compute_ems_ticks(control, ticks, convertArray((byte[]) expected)), (double[]) actual,
                    .001d);
        } else if (expected instanceof char[]) {
            assertArrayEquals(compute_ems_ticks(control, ticks, convertArray((char[]) expected)), (double[]) actual,
                    .001d);
        } else if (expected instanceof short[]) {
            assertArrayEquals(compute_ems_ticks(control, ticks, convertArray((short[]) expected)), (double[]) actual,
                    .001d);
        } else if (expected instanceof int[]) {
            assertArrayEquals(compute_ems_ticks(control, ticks, convertArray((int[]) expected)), (double[]) actual,
                    .001d);
        } else if (expected instanceof long[]) {
            assertArrayEquals(compute_ems_ticks(control, ticks, convertArray((long[]) expected)), (double[]) actual,
                    .001d);
        } else if (expected instanceof float[]) {
            assertArrayEquals(compute_ems_ticks(control, ticks, convertArray((float[]) expected)), (double[]) actual,
                    .001d);
        } else if (expected instanceof double[]) {
            assertArrayEquals(compute_ems_ticks(control, ticks, (double[]) expected), (double[]) actual, .001d);
            // } else if (expected instanceof Boolean[]) {
            // assertArrayEquals(cumsum((Boolean[]) expected), (long[]) actual);
        } else {
            if (type == BigInteger.class) {
                assertBDArrayEquals(compute_ems_ticks(control, ticks, convertArray((BigInteger[]) expected)),
                        (BigDecimal[]) actual);
            } else if (type == BigDecimal.class) {
                assertBDArrayEquals(compute_ems_ticks(control, ticks, (BigDecimal[]) expected), (BigDecimal[]) actual);
            }
        }
    }

    final void assertWithEmsTime(final OperationControl control,
            final long nanos,
            @NotNull final long[] timestamps,
            @NotNull final Object expected,
            @NotNull final Object actual,
            final Class<?> type) {
        if (expected instanceof double[]) {
            assertArrayEquals(compute_ems_time(control, nanos, timestamps, (double[]) expected), (double[]) actual,
                    .001d);
        }
        if (expected instanceof byte[]) {
            assertArrayEquals(compute_ems_time(control, nanos, timestamps, convertArray((byte[]) expected)),
                    (double[]) actual, .001d);
        } else if (expected instanceof short[]) {
            assertArrayEquals(compute_ems_time(control, nanos, timestamps, convertArray((short[]) expected)),
                    (double[]) actual, .001d);
        } else if (expected instanceof char[]) {
            assertArrayEquals(compute_ems_time(control, nanos, timestamps, convertArray((char[]) expected)),
                    (double[]) actual, .001d);
        } else if (expected instanceof int[]) {
            assertArrayEquals(compute_ems_time(control, nanos, timestamps, convertArray((int[]) expected)),
                    (double[]) actual, .001d);
        } else if (expected instanceof long[]) {
            assertArrayEquals(compute_ems_time(control, nanos, timestamps, convertArray((long[]) expected)),
                    (double[]) actual, .001d);
        } else if (expected instanceof float[]) {
            assertArrayEquals(compute_ems_time(control, nanos, timestamps, convertArray((float[]) expected)),
                    (double[]) actual, .001d);
        } else if (expected instanceof double[]) {
            assertArrayEquals(compute_ems_time(control, nanos, timestamps, (double[]) expected), (double[]) actual,
                    .001d);
            // } else if (expected instanceof Boolean[]) {
            // assertArrayEquals(cumsum((Boolean[]) expected), (long[]) actual);
        } else {
            if (type == BigInteger.class) {
                assertBDArrayEquals(compute_ems_time(control, nanos, timestamps, convertArray((BigInteger[]) expected)),
                        (BigDecimal[]) actual);
            } else if (type == BigDecimal.class) {
                assertBDArrayEquals(compute_ems_time(control, nanos, timestamps, (BigDecimal[]) expected),
                        (BigDecimal[]) actual);
            }
        }
    }
    // endregion
}
