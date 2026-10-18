//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
package io.deephaven.replicators;

import io.deephaven.replication.ReplicationUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static io.deephaven.replication.ReplicatePrimitiveCode.*;

public class ReplicateChunkFilters {
    private static final String TASK = "replicateChunkFilters";

    private static final String CHUNK_FILTER_PATH =
            "engine/table/src/main/java/io/deephaven/engine/table/impl/chunkfilter/";
    private static final String CHAR_CHUNK_FILTER = CHUNK_FILTER_PATH + "CharChunkFilter.java";
    private static final String CHAR_RANGE_COMPARATOR = CHUNK_FILTER_PATH + "CharRangeComparator.java";
    private static final String FLOAT_RANGE_COMPARATOR = CHUNK_FILTER_PATH + "FloatRangeComparator.java";
    private static final String CHAR_CHUNK_MATCH_FILTER_FACTORY =
            CHUNK_FILTER_PATH + "CharChunkMatchFilterFactory.java";
    private static final String FLOAT_CHUNK_MATCH_FILTER_FACTORY =
            CHUNK_FILTER_PATH + "FloatChunkMatchFilterFactory.java";

    private static final String RANGE_FILTER_PATH =
            "engine/table/src/main/java/io/deephaven/engine/table/impl/select/";
    private static final String CHAR_RANGE_FILTER = RANGE_FILTER_PATH + "CharRangeFilter.java";
    private static final String FLOAT_RANGE_FILTER = RANGE_FILTER_PATH + "FloatRangeFilter.java";

    public static void main(String[] args) throws IOException {
        // *ChunkFilter.java
        charToAllButBoolean(TASK, CHAR_CHUNK_FILTER);

        // *RangeComparator.java. The other integral types order their null value first, as numeric order does, so their
        // single ended filters compare values as they are, where char's move NULL_CHAR first. A long's offset and limit
        // are longs.
        useNumericOrder(charToByte(TASK, CHAR_RANGE_COMPARATOR));
        useNumericOrder(charToShort(TASK, CHAR_RANGE_COMPARATOR, null));
        useNumericOrder(charToInteger(TASK, CHAR_RANGE_COMPARATOR, null));
        useNumericOrder(charToLong(TASK, CHAR_RANGE_COMPARATOR),
                "Integer\\.MIN_VALUE", "Long.MIN_VALUE",
                "final int offset", "final long offset",
                "final int limit", "final long limit");
        floatToAllFloatingPoints(TASK, FLOAT_RANGE_COMPARATOR);
        // a double's bits are a long
        final File doubleRangeComparator = new File(CHUNK_FILTER_PATH + "DoubleRangeComparator.java");
        FileUtils.writeLines(doubleRangeComparator, ReplicationUtils.globalReplacements(
                FileUtils.readLines(doubleRangeComparator, Charset.defaultCharset()),
                "final int offset", "final long offset",
                "final int limit", "final long limit",
                "final int lowerKey", "final long lowerKey",
                "final int upperKey", "final long upperKey",
                "private static int sortable", "private static long sortable",
                "final int bits = Double\\.doubleToRawIntBits", "final long bits = Double.doubleToRawLongBits",
                "bits >> 31", "bits >> 63",
                "as signed ints", "as signed longs",
                "Integer\\.MIN_VALUE", "Long.MIN_VALUE",
                "Integer\\.MAX_VALUE", "Long.MAX_VALUE",
                "0\\.0f", "0.0d"));

        // *ChunkMatchFilterFactory.java. Byte and short keep the template's bitmap over every value of the type. Int
        // and
        // long match four or more values with the structures in IntMultiValueChunkFilters and
        // LongMultiValueChunkFilters instead, which are maintained by hand.
        charToByte(TASK, CHAR_CHUNK_MATCH_FILTER_FACTORY);
        charToShort(TASK, CHAR_CHUNK_MATCH_FILTER_FACTORY, null);
        useHandMaintainedMultiValueFilters(charToInteger(TASK, CHAR_CHUNK_MATCH_FILTER_FACTORY, null), "Int");
        useHandMaintainedMultiValueFilters(charToLong(TASK, CHAR_CHUNK_MATCH_FILTER_FACTORY), "Long");
        floatToAllFloatingPoints(TASK, FLOAT_CHUNK_MATCH_FILTER_FACTORY);

        final File objectFile = new File(CHUNK_FILTER_PATH + "DoubleChunkMatchFilterFactory.java");
        List<String> lines = FileUtils.readLines(objectFile, Charset.defaultCharset());
        lines = ReplicationUtils.replaceRegion(lines, "getBits", List.of("" +
                "    public static long getBits(double value) {\n" +
                "        return Double.doubleToLongBits(value == 0.0d ? 0.0d : value);\n" +
                "    }\n"));
        // a double's canonical bits are a long, where a float's are an int
        lines = ReplicationUtils.globalReplacements(lines,
                "int valueBits", "long valueBits",
                "int\\[\\]", "long[]",
                "new int\\[", "new long[",
                "IntCuckooSet", "LongCuckooSet",
                "IntOpenHashSet", "LongOpenHashSet",
                "it\\.unimi\\.dsi\\.fastutil\\.ints", "it.unimi.dsi.fastutil.longs",
                "doubleToIntBits", "doubleToLongBits",
                "0\\.0f", "0.0d");
        FileUtils.writeLines(objectFile, lines);

        // *RangeFilter.java
        charToShortAndByte(TASK, CHAR_RANGE_FILTER);
        charToIntegers(TASK, CHAR_RANGE_FILTER);
        charToLong(TASK, CHAR_RANGE_FILTER);
        floatToAllFloatingPoints(TASK, FLOAT_RANGE_FILTER);
    }

    /**
     * Replace the template's single ended comparisons, which move NULL_CHAR first, with plain ones, then make
     * {@code replacements}.
     */
    private static void useNumericOrder(final String comparatorPath, final String... replacements)
            throws IOException {
        final File comparatorFile = new File(comparatorPath);
        List<String> lines = FileUtils.readLines(comparatorFile, Charset.defaultCharset());
        lines = ReplicationUtils.replaceRegion(lines, "atLeastMatches",
                List.of("            return value >= lower;"));
        lines = ReplicationUtils.replaceRegion(lines, "atMostMatches",
                List.of("            return value <= upper;"));
        if (replacements.length > 0) {
            lines = ReplicationUtils.globalReplacements(lines, replacements);
        }
        FileUtils.writeLines(comparatorFile, lines);
    }

    /**
     * Replace the template's bitmap over every value, which suits only the 8 and 16 bit types, with a call to the hand
     * maintained {@code <type>MultiValueChunkFilters}.
     */
    private static void useHandMaintainedMultiValueFilters(final String factoryPath, final String type)
            throws IOException {
        final File factoryFile = new File(factoryPath);
        List<String> lines = FileUtils.readLines(factoryFile, Charset.defaultCharset());
        lines = ReplicationUtils.replaceRegion(lines, "makeMultiValueFilter", List.of(
                "        return " + type + "MultiValueChunkFilters.makeFilter(matchOptions.inverted(), values);"));
        lines = ReplicationUtils.removeRegion(lines, "multiValueFilters");
        FileUtils.writeLines(factoryFile, lines);
    }
}
