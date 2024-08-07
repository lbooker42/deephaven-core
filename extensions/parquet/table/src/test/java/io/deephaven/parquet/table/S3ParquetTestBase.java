//
// Copyright (c) 2016-2024 Deephaven Data Labs and Patent Pending
//
package io.deephaven.parquet.table;

import io.deephaven.engine.table.ColumnDefinition;
import io.deephaven.engine.table.Table;
import io.deephaven.engine.table.TableDefinition;
import io.deephaven.engine.table.impl.QueryTable;
import io.deephaven.engine.table.impl.locations.TableDataException;
import io.deephaven.engine.testutil.junit4.EngineCleanup;
import io.deephaven.engine.util.TableTools;
import io.deephaven.extensions.s3.S3Instructions;
import io.deephaven.extensions.s3.testlib.S3SeekableChannelTestSetup;
import io.deephaven.test.types.OutOfBandTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;
import software.amazon.awssdk.core.async.AsyncRequestBody;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static io.deephaven.engine.testutil.TstUtils.assertTableEquals;
import static io.deephaven.engine.util.TableTools.merge;
import static io.deephaven.parquet.table.ParquetTools.writeKeyValuePartitionedTable;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@Category(OutOfBandTest.class)
abstract class S3ParquetTestBase extends S3SeekableChannelTestSetup {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Rule
    public final EngineCleanup framework = new EngineCleanup();

    @Before
    public void setUp() throws ExecutionException, InterruptedException, TimeoutException {
        super.doSetUp();
    }

    @After
    public void tearDown() throws ExecutionException, InterruptedException, TimeoutException {
        super.doTearDown();
    }

    private static Table getTable(final int numRows) {
        return TableTools.emptyTable(numRows).update(
                "someIntColumn = (int) i",
                "someDoubleColumn = (double) i",
                "someStringColumn = String.valueOf(i)",
                "someBooleanColumn = i % 2 == 0",
                "someCharColumn = (char) (i % 26 + 'a')");
    }

    @Test
    public final void readSingleParquetFile()
            throws IOException, ExecutionException, InterruptedException, TimeoutException {
        final Table table = getTable(500_000);
        final File dest = new File(folder.newFolder(), "table.parquet");
        ParquetTools.writeTable(table, dest.getAbsolutePath());
        putObject("table.parquet", AsyncRequestBody.fromFile(dest));

        final URI uri = uri("table.parquet");
        final ParquetInstructions readInstructions = ParquetInstructions.builder()
                .setSpecialInstructions(s3Instructions(
                        S3Instructions.builder()
                                .readTimeout(Duration.ofSeconds(10)))
                        .build())
                .build();
        final Table fromS3 = ParquetTools.readTable(uri.toString(), readInstructions);
        assertTableEquals(table, fromS3);
    }

    @Test
    public final void readFlatPartitionedParquetData()
            throws ExecutionException, InterruptedException, TimeoutException, IOException {
        final Table table = getTable(100_000);
        final String destDirName = "flatPartitionedDataDir";
        final File destDir = new File(folder.newFolder(), destDirName);
        for (int i = 0; i < 3; ++i) {
            final File dest = new File(destDir, "table" + i + ".parquet");
            ParquetTools.writeTable(table, dest.getAbsolutePath());
        }
        final File pqFileToBeIgnored = new File(destDir, "temp/table.parquet");
        ParquetTools.writeTable(TableTools.emptyTable(100).select("someIntColumn = (int) i"),
                pqFileToBeIgnored.getAbsolutePath());
        uploadDirectory(destDir.toPath(), destDirName);
        final URI uri = uri(destDirName);
        final ParquetInstructions readInstructions = ParquetInstructions.builder()
                .setSpecialInstructions(s3Instructions(
                        S3Instructions.builder()
                                .readTimeout(Duration.ofSeconds(10)))
                        .build())
                .build();

        final Table expected = merge(table, table, table);
        final Table fromS3AsFlat = ParquetTools.readTable(uri.toString(),
                readInstructions.withLayout(ParquetInstructions.ParquetFileLayout.FLAT_PARTITIONED));
        assertTableEquals(expected, fromS3AsFlat);
    }

    @Test
    public final void readFlatPartitionedParquetDataAsKVPartitioned()
            throws ExecutionException, InterruptedException, TimeoutException, IOException {
        final Table table = getTable(100_000);
        final String destDirName = "flatPartitionedDataDir";
        final File destDir = new File(folder.newFolder(), destDirName);
        for (int i = 0; i < 3; ++i) {
            final File dest = new File(destDir, "table" + i + ".parquet");
            ParquetTools.writeTable(table, dest.getAbsolutePath());
        }
        uploadDirectory(destDir.toPath(), destDirName);
        final URI uri = uri(destDirName);
        final ParquetInstructions readInstructions = ParquetInstructions.builder()
                .setSpecialInstructions(s3Instructions(
                        S3Instructions.builder()
                                .readTimeout(Duration.ofSeconds(10)))
                        .build())
                .build();

        final Table expected = merge(table, table, table);
        final Table fromS3AsFlat = ParquetTools.readTable(uri.toString(),
                readInstructions.withLayout(ParquetInstructions.ParquetFileLayout.FLAT_PARTITIONED));
        assertTableEquals(expected, fromS3AsFlat);

        final Table fromS3AsKV = ParquetTools.readTable(uri.toString(),
                readInstructions.withLayout(ParquetInstructions.ParquetFileLayout.KV_PARTITIONED));
        assertTableEquals(expected, fromS3AsKV);

        // Read with definition without layout
        final Table fromS3AsFlatWithDefinition = ParquetTools.readTable(uri.toString(),
                readInstructions.withTableDefinition(expected.getDefinition()));
        assertTableEquals(expected, fromS3AsFlatWithDefinition);
    }

    @Test
    public void readKeyValuePartitionedParquetData()
            throws ExecutionException, InterruptedException, TimeoutException, IOException {
        final TableDefinition definition = TableDefinition.of(
                ColumnDefinition.ofInt("PC1").withPartitioning(),
                ColumnDefinition.ofInt("PC2").withPartitioning(),
                ColumnDefinition.ofInt("someIntColumn"),
                ColumnDefinition.ofString("someStringColumn"));
        final Table table = ((QueryTable) TableTools.emptyTable(500_000)
                .updateView("PC1 = (int)(ii%3)",
                        "PC2 = (int)(ii%2)",
                        "someIntColumn = (int) i",
                        "someStringColumn = String.valueOf(i)"))
                .withDefinitionUnsafe(definition);
        final String destDirName = "keyValuePartitionedDataDir";
        final File destDir = new File(folder.newFolder(), destDirName);
        final ParquetInstructions writeInstructions = ParquetInstructions.builder()
                .setBaseNameForPartitionedParquetData("data")
                .build();
        writeKeyValuePartitionedTable(table, destDir.getPath(), writeInstructions);
        uploadDirectory(destDir.toPath(), destDirName);
        final URI uri = uri(destDirName);
        final ParquetInstructions readInstructions = ParquetInstructions.builder()
                .setSpecialInstructions(s3Instructions(
                        S3Instructions.builder()
                                .readTimeout(Duration.ofSeconds(10)))
                        .build())
                .setTableDefinition(definition)
                .build();
        final Table fromS3 = ParquetTools.readTable(uri.toString(), readInstructions);
        final Table fromDisk = ParquetTools.readTable(destDir.getPath());
        readPartitionedParquetTestHelper(fromDisk, fromS3);

        // Failure cases for missing metadata files
        try {
            ParquetTools.readTable(uri.toString(),
                    readInstructions.withTableDefinitionAndLayout(null,
                            ParquetInstructions.ParquetFileLayout.METADATA_PARTITIONED));
            fail("Expected exception because metadata file is not present");
        } catch (final TableDataException expected) {
            assertTrue(expected.getMessage().contains("metadata"));
        }
        final URI metadataFileURI = uri(destDirName + "/_metadata");
        try {
            ParquetTools.readTable(metadataFileURI.toString(), readInstructions.withTableDefinition(null));
            fail("Expected exception because metadata file is not present");
        } catch (final TableDataException expected) {
            assertTrue(expected.getMessage().contains("metadata"));
        }
    }

    @Test
    public void readMetadataPartitionedParquetData()
            throws ExecutionException, InterruptedException, TimeoutException, IOException {
        final TableDefinition definition = TableDefinition.of(
                ColumnDefinition.ofInt("PC1").withPartitioning(),
                ColumnDefinition.ofInt("PC2").withPartitioning(),
                ColumnDefinition.ofInt("someIntColumn"),
                ColumnDefinition.ofString("someStringColumn"));
        final Table table = ((QueryTable) TableTools.emptyTable(500_000)
                .updateView("PC1 = (int)(ii%3)",
                        "PC2 = (int)(ii%2)",
                        "someIntColumn = (int) i",
                        "someStringColumn = String.valueOf(i)"))
                .withDefinitionUnsafe(definition);
        final String destDirName = "metadataPartitionedDataDir";
        final File destDir = new File(folder.newFolder(), destDirName);
        final ParquetInstructions writeInstructions = ParquetInstructions.builder()
                .setBaseNameForPartitionedParquetData("data")
                .setGenerateMetadataFiles(true)
                .build();
        writeKeyValuePartitionedTable(table, destDir.getPath(), writeInstructions);
        assertTrue(new File(destDir, "_metadata").exists());
        assertTrue(new File(destDir, "_common_metadata").exists());
        uploadDirectory(destDir.toPath(), destDirName);
        final ParquetInstructions readInstructions = ParquetInstructions.builder()
                .setSpecialInstructions(s3Instructions(
                        S3Instructions.builder()
                                .readTimeout(Duration.ofSeconds(10)))
                        .build())
                .build();
        final URI directoryURI = uri(destDirName);
        final Table fromS3MetadataPartitioned = ParquetTools.readTable(directoryURI.toString(),
                readInstructions.withLayout(ParquetInstructions.ParquetFileLayout.METADATA_PARTITIONED));
        final Table fromDiskMetadataPartitioned = ParquetTools.readTable(destDir.getPath(),
                ParquetInstructions.EMPTY.withLayout(ParquetInstructions.ParquetFileLayout.METADATA_PARTITIONED));
        readPartitionedParquetTestHelper(fromDiskMetadataPartitioned, fromS3MetadataPartitioned);

        final URI metadataFileURI = uri(destDirName + "/_metadata");
        final Table fromS3WithMetadata = ParquetTools.readTable(metadataFileURI.toString(), readInstructions);
        final Table fromDiskWithMetadata = ParquetTools.readTable(new File(destDir, "_metadata").getPath());
        readPartitionedParquetTestHelper(fromDiskWithMetadata, fromS3WithMetadata);

        final URI commonMetadataFileURI = uri(destDirName + "/_common_metadata");
        final Table fromS3WithCommonMetadata =
                ParquetTools.readTable(commonMetadataFileURI.toString(), readInstructions);
        final Table fromDiskWithCommonMetadata =
                ParquetTools.readTable(new File(destDir, "_common_metadata").getPath());
        readPartitionedParquetTestHelper(fromDiskWithCommonMetadata, fromS3WithCommonMetadata);
    }

    private static void readPartitionedParquetTestHelper(final Table expected, final Table fromS3) {
        assertTrue(fromS3.getDefinition().getColumn("PC1").isPartitioning());
        assertTrue(fromS3.getDefinition().getColumn("PC2").isPartitioning());
        assertTableEquals(expected, fromS3);
    }

    @Test
    public void readMetadataPartitionedParquetWithMissingMetadataFile()
            throws ExecutionException, InterruptedException, TimeoutException, IOException {
        final TableDefinition definition = TableDefinition.of(
                ColumnDefinition.ofInt("PC1").withPartitioning(),
                ColumnDefinition.ofInt("PC2").withPartitioning(),
                ColumnDefinition.ofInt("someIntColumn"),
                ColumnDefinition.ofString("someStringColumn"));
        final Table table = ((QueryTable) TableTools.emptyTable(500_000)
                .updateView("PC1 = (int)(ii%3)",
                        "PC2 = (int)(ii%2)",
                        "someIntColumn = (int) i",
                        "someStringColumn = String.valueOf(i)"))
                .withDefinitionUnsafe(definition);
        final String destDirName = "metadataPartitionedDataDir";
        final File destDir = new File(folder.newFolder(), destDirName);
        final ParquetInstructions writeInstructions = ParquetInstructions.builder()
                .setBaseNameForPartitionedParquetData("data")
                .setGenerateMetadataFiles(true)
                .build();
        writeKeyValuePartitionedTable(table, destDir.getPath(), writeInstructions);

        // Delete the metadata file before uploading
        final File metadataFile = new File(destDir, "_metadata");
        metadataFile.delete();

        uploadDirectory(destDir.toPath(), destDirName);
        final URI directoryURI = uri(destDirName);
        final ParquetInstructions readInstructions = ParquetInstructions.builder()
                .setSpecialInstructions(s3Instructions(
                        S3Instructions.builder()
                                .readTimeout(Duration.ofSeconds(10)))
                        .build())
                .build();
        try {
            ParquetTools.readTable(directoryURI.toString(),
                    readInstructions.withLayout(ParquetInstructions.ParquetFileLayout.METADATA_PARTITIONED));
            fail("Expected exception because metadata file is not present");
        } catch (final TableDataException expected) {
            assertTrue(expected.getMessage().contains("metadata"));
        }
    }
}
