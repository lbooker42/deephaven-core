//
// Copyright (c) 2016-2025 Deephaven Data Labs and Patent Pending
//
package io.deephaven.engine.table.impl.locations.impl;

import io.deephaven.base.FileUtils;
import io.deephaven.base.verify.Assert;
import io.deephaven.engine.table.ColumnDefinition;
import io.deephaven.engine.table.Table;
import io.deephaven.engine.table.TableDefinition;
import io.deephaven.engine.table.impl.indexer.DataIndexer;
import io.deephaven.parquet.table.ParquetTools;
import io.deephaven.engine.util.TableTools;
import io.deephaven.engine.util.file.TrackedFileHandleFactory;
import io.deephaven.engine.testutil.TstUtils;
import io.deephaven.parquet.table.layout.DeephavenNestedPartitionLayout;
import io.deephaven.parquet.table.ParquetInstructions;
import io.deephaven.engine.testutil.junit4.EngineCleanup;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.deephaven.parquet.table.layout.DeephavenNestedPartitionLayout.PARQUET_FILE_NAME;

/**
 * Unit tests for {@code PartitioningColumnDataIndex} and {@code MergedDataIndex}.
 */
public class TestSourceTableDataIndexes {

    @Rule
    public final EngineCleanup base = new EngineCleanup();

    private File dataDirectory;

    @Before
    public void setUp() throws Exception {
        dataDirectory = Files.createTempDirectory(Paths.get(""), "TestChunkedRegionedOperations-").toFile();
        dataDirectory.deleteOnExit();
    }

    @After
    public void tearDown() throws Exception {
        if (dataDirectory.exists()) {
            TrackedFileHandleFactory.getInstance().closeAll();
            int tries = 0;
            boolean success = false;
            do {
                try {
                    FileUtils.deleteRecursively(dataDirectory);
                    success = true;
                } catch (Exception e) {
                    System.gc();
                    tries++;
                }
            } while (!success && tries < 10);
            TestCase.assertTrue(success);
        }
    }

    @Test
    public void testParallelOrdering() {
        doTest(false);
    }

    @Test
    public void testParallelMissingIndexes() {
        doTest(true);
    }

    private void doTest(final boolean missingIndexes) {
        final Table raw = TableTools.emptyTable(26 * 10 * 1000).update("Part=String.format(`%04d`, (long)(ii/1000))",
                "Sym=(char)('A' + ii % 26)", "Other=ii");
        final Table[] partitions = raw.partitionBy("Part")
                .transform(null, rp -> rp.groupBy("Sym").ungroup(), false)
                .constituents();

        // Create a local index for each partition
        for (final Table t : partitions) {
            DataIndexer.getOrCreateDataIndex(t, "Sym");
        }

        if (missingIndexes) {
            // Create a pair of partitions without the indexing column
            partitions[2] = partitions[2].dropColumns("Sym");
            partitions[3] = partitions[3].dropColumns("Sym");
        }

        final TableDefinition partitionedDataDefinition = TableDefinition.of(
                ColumnDefinition.ofString("Part").withPartitioning(),
                ColumnDefinition.ofChar("Sym"),
                ColumnDefinition.ofLong("Other"));

        final TableDefinition partitionedMissingDataDefinition = TableDefinition.of(
                ColumnDefinition.ofString("Part").withPartitioning(),
                ColumnDefinition.ofLong("Other"));

        final String tableName = "TestTable";

        // @formatter:off
        ParquetTools.writeTable(
                partitions[0],
                new File(dataDirectory,
                        "IP" + File.separator + "0000" + File.separator + tableName + File.separator
                                + PARQUET_FILE_NAME).getPath(),
                ParquetInstructions.EMPTY.withTableDefinition(partitionedDataDefinition));
        ParquetTools.writeTable(
                partitions[1],
                new File(dataDirectory,
                        "IP" + File.separator + "0001" + File.separator + tableName + File.separator
                                + PARQUET_FILE_NAME).getPath(),
                ParquetInstructions.EMPTY.withTableDefinition(partitionedDataDefinition));
        ParquetTools.writeTable(
                partitions[2],
                new File(dataDirectory,
                        "IP" + File.separator + "0002" + File.separator + tableName + File.separator
                                + PARQUET_FILE_NAME).getPath(),
                ParquetInstructions.EMPTY.withTableDefinition(
                        missingIndexes ? partitionedMissingDataDefinition : partitionedDataDefinition));
        ParquetTools.writeTable(
                partitions[3],
                new File(dataDirectory,
                        "IP" + File.separator + "0003" + File.separator + tableName + File.separator
                                + PARQUET_FILE_NAME).getPath(),
                ParquetInstructions.EMPTY.withTableDefinition(
                        missingIndexes ? partitionedMissingDataDefinition : partitionedDataDefinition));
        ParquetTools.writeTables(
                Arrays.copyOfRange(partitions, 4, partitions.length),
                IntStream.range(4, 260)
                        .mapToObj(pcv -> new File(dataDirectory,
                                "IP" + File.separator + String.format("%04d", pcv) + File.separator + tableName
                                        + File.separator + PARQUET_FILE_NAME))
                        .map(File::getPath).toArray(String[]::new),
                ParquetInstructions.EMPTY.withTableDefinition(partitionedDataDefinition));
        // TODO (deephaven/deephaven-core/issues/321): Re-add this part of the test when the parquet bug is fixed
        ParquetTools.writeTable(
                TableTools.emptyTable(0).updateView("Sym=NULL_CHAR", "Other=NULL_LONG"),
                new File(dataDirectory,
                        "IP" + File.separator + "XXXX" + File.separator + tableName + File.separator
                                + PARQUET_FILE_NAME).getPath(),
                    ParquetInstructions.EMPTY.withTableDefinition(partitionedDataDefinition));
        // @formatter:on

        if (missingIndexes) {
            // Put Sym back on for the partitions that dropped it.
            partitions[2] = partitions[2].updateView("Sym = NULL_CHAR");
            partitions[3] = partitions[3].updateView("Sym = NULL_CHAR");
        }
        // Column ordering was changed by groupBy()/ungroup() above, restore it here.
        final Table expected = TableTools.merge(partitions).view("Part", "Sym", "Other");

        final Table actual = ParquetTools.readTable(
                DeephavenNestedPartitionLayout.forParquet(dataDirectory, tableName, "Part", ipn -> ipn.equals("IP"),
                        ParquetInstructions.EMPTY),
                ParquetInstructions.EMPTY.withTableDefinition(partitionedDataDefinition)).coalesce();

        TstUtils.assertTableEquals(expected, actual);

        // Make sure we have the partitioning column index
        Assert.eqTrue(DataIndexer.hasDataIndex(actual, "Part"),
                "DataIndexer.hasDataIndex(actual, \"Part\")");

        // Without
        TestCase.assertEquals(!missingIndexes, DataIndexer.hasDataIndex(actual, "Sym"));

        TstUtils.assertTableEquals(expected.groupBy("Sym").ungroup(), actual.groupBy("Sym").ungroup());
    }

    @Test
    public void testDroppedIndexColumn() {
        final Table raw = TableTools.emptyTable(26 * 10 * 1000).update("Part=String.format(`%04d`, (long)(ii/1000))",
                "Sym=(char)('A' + ii % 26)", "Other=ii");
        DataIndexer.getOrCreateDataIndex(raw, "Sym");

        final String path =
                dataDirectory.getAbsolutePath() + File.separator + "TestTable2" + File.separator + PARQUET_FILE_NAME;

        ParquetTools.writeTable(raw, path);

        TestCase.assertFalse(DataIndexer.hasDataIndex(
                ParquetTools.readTable(path).dropColumns("Sym").coalesce(), "Sym"));
    }

    @Test
    public void testParallelCollection() {
        final List<Integer> observedOrder = Collections.synchronizedList(new ArrayList<>());
        final int[] intArray = IntStream.range(0, 10000).parallel().peek(observedOrder::add).toArray();
        for (int ii = 1; ii < intArray.length; ++ii) {
            TestCase.assertTrue(intArray[ii - 1] < intArray[ii]);
        }
        System.out.println("Out of order observed: " + IntStream.range(1, intArray.length)
                .anyMatch(ii -> observedOrder.get(ii - 1) > observedOrder.get(ii)));
        observedOrder.clear();

        final List<Integer> integerList = Arrays.stream(intArray).boxed().parallel().peek(observedOrder::add)
                .collect(Collectors.toList());
        for (int ii = 0; ii < integerList.size(); ++ii) {
            TestCase.assertEquals(intArray[ii], integerList.get(ii).intValue());
        }
        System.out.println("Out of order observed: " + IntStream.range(1, intArray.length)
                .anyMatch(ii -> observedOrder.get(ii - 1) > observedOrder.get(ii)));
        observedOrder.clear();

        final LinkedHashMap<Integer, Integer> integerMap = integerList.parallelStream().peek(observedOrder::add)
                .collect(Collectors.toMap(Function.identity(), Function.identity(), Assert::neverInvoked,
                        LinkedHashMap::new));
        System.out.println("Out of order observed: " + IntStream.range(1, intArray.length)
                .anyMatch(ii -> observedOrder.get(ii - 1) > observedOrder.get(ii)));
        observedOrder.clear();

        final LinkedHashMap<String, String> stringMap =
                integerMap.entrySet().parallelStream().peek(e -> observedOrder.add(e.getKey()))
                        .collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue().toString(),
                                Assert::neverInvoked, LinkedHashMap::new));
        System.out.println("Out of order observed: " + IntStream.range(1, intArray.length)
                .anyMatch(ii -> observedOrder.get(ii - 1) > observedOrder.get(ii)));
        observedOrder.clear();

        final int[] outputArray = stringMap.values().parallelStream().mapToInt(Integer::parseInt).toArray();
        for (int ii = 0; ii < outputArray.length; ++ii) {
            TestCase.assertEquals(intArray[ii], outputArray[ii]);
        }
    }
}
