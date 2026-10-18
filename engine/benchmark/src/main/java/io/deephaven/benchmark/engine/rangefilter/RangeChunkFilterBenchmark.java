//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
package io.deephaven.benchmark.engine.rangefilter;

import io.deephaven.chunk.Chunk;
import io.deephaven.chunk.ChunkType;
import io.deephaven.chunk.WritableBooleanChunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.context.TestExecutionContext;
import io.deephaven.engine.rowset.WritableRowSet;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.impl.QueryTable;
import io.deephaven.engine.table.impl.chunkfilter.ChunkFilter;
import io.deephaven.util.SafeCloseable;
import org.openjdk.jmh.annotations.AuxCounters;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import java.util.SplittableRandom;
import java.util.concurrent.TimeUnit;

/**
 * Measures a range filter's {@link ChunkFilter} over every row of a table of {@code rows} rows, with one in-memory
 * column, through each of its three methods:
 * <ul>
 * <li>{@link #filter}: {@link ChunkFilter#applyChunkFilter}, which reads the column a chunk at a time and keeps the row
 * keys that pass, as a {@code where} does.</li>
 * <li>{@link #filterBoolean}: the filter that writes a boolean for every value, over chunks that wrap the column's
 * array, so that no read of the column is measured.</li>
 * <li>{@link #filterAnd}: the filter that ands its result into booleans that an earlier filter wrote, as a
 * {@code countWhere} with several filters does, over the same chunks. Before each chunk it copies in booleans of which
 * about {@code prior} are true, a copy that costs the same whatever the filter.</li>
 * </ul>
 * Each method reports its time per row, in nanoseconds, as the secondary result {@code rows}. Setup checks every method
 * against the rows that the {@code *Comparisons} classes pass. {@link RangeFilterBenchmarkData} describes the data and
 * the shapes of range.
 * <p>
 * To narrow a run, pass JMH arguments, which replace the task's own:
 *
 * <pre>
 * ./gradlew :engine-benchmark:jmhRunRangeChunkFilter \
 *     --args='RangeChunkFilterBenchmark.filter -p type=int,double -p shape=between -p selectivity=0.1,0.5'
 * </pre>
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(3)
public class RangeChunkFilterBenchmark {

    /**
     * Counts the rows filtered, so that JMH reports the time per row as the secondary result {@code rows}.
     */
    @State(Scope.Thread)
    @AuxCounters(AuxCounters.Type.OPERATIONS)
    public static class RowCounter {
        public long rows;

        @Setup(Level.Iteration)
        public void reset() {
            rows = 0;
        }
    }

    private static final long PRIOR_SEED = 0x7072696F72L;

    @Param({"byte", "short", "char", "int", "long", "float", "double"})
    private String type;

    @Param({"between", "greater", "less"})
    private String shape;

    // about how much of the values that are not null pass
    @Param({"0.01", "0.1", "0.5", "0.9"})
    private double selectivity;

    @Param({"1000000", "10000000"})
    private int rows;

    @Param({"0.01"})
    private double nullFraction;

    // for filterAnd, about how much of the rows an earlier filter passed
    @Param({"0.5"})
    private double prior;

    private SafeCloseable executionContext;

    private QueryTable table;
    private ColumnSource<?> source;
    private ChunkFilter chunkFilter;
    private Chunk<Values>[] chunks;
    private WritableBooleanChunk<Values>[] priorResults;
    private WritableBooleanChunk<Values> results;

    @Setup(Level.Trial)
    public void setupEnv() {
        executionContext = TestExecutionContext.createForUnitTests().open();

        final RangeFilterBenchmarkData.GeneratedTable generated = RangeFilterBenchmarkData.generate(
                type, RangeFilterBenchmarkData.Shape.valueOf(shape.toUpperCase()), selectivity, nullFraction, rows);
        table = generated.table;
        source = table.getColumnSource(RangeFilterBenchmarkData.COLUMN);
        generated.filter.init(table.getDefinition());
        chunkFilter = generated.filter.chunkFilter().orElseThrow();

        final ChunkType chunkType = ChunkType.fromElementType(source.getType());
        final int chunkCount = (rows + ChunkFilter.FILTER_CHUNK_SIZE - 1) / ChunkFilter.FILTER_CHUNK_SIZE;
        // noinspection unchecked
        chunks = new Chunk[chunkCount];
        // noinspection unchecked
        priorResults = new WritableBooleanChunk[chunkCount];
        final SplittableRandom random = new SplittableRandom(PRIOR_SEED);
        long passingAfterPrior = 0;
        for (int cc = 0; cc < chunkCount; ++cc) {
            final int offset = cc * ChunkFilter.FILTER_CHUNK_SIZE;
            final int size = Math.min(ChunkFilter.FILTER_CHUNK_SIZE, rows - offset);
            chunks[cc] = chunkType.chunkWrap(generated.data, offset, size);
            priorResults[cc] = WritableBooleanChunk.makeWritableChunk(size);
            for (int ii = 0; ii < size; ++ii) {
                final boolean passedPrior = random.nextDouble() < prior;
                priorResults[cc].set(ii, passedPrior);
                passingAfterPrior += passedPrior && generated.passes[offset + ii] ? 1 : 0;
            }
        }
        results = WritableBooleanChunk.makeWritableChunk(ChunkFilter.FILTER_CHUNK_SIZE);

        final long passing = generated.passingRows();
        check("filter", filter(new RowCounter()), passing);
        check("filterBoolean", filterBoolean(new RowCounter()), passing);
        check("filterAnd", filterAnd(new RowCounter()), passingAfterPrior);
    }

    @TearDown(Level.Trial)
    public void tearDownEnv() {
        for (final WritableBooleanChunk<Values> priorResult : priorResults) {
            priorResult.close();
        }
        results.close();
        executionContext.close();
    }

    private static void check(final String method, final long passed, final long expected) {
        if (passed != expected) {
            throw new IllegalStateException(method + " passed " + passed + " rows, expected " + expected);
        }
    }

    @Benchmark
    public long filter(final RowCounter counter) {
        counter.rows += rows;
        try (final WritableRowSet result = ChunkFilter.applyChunkFilter(table.getRowSet(), source, false,
                chunkFilter)) {
            return result.size();
        }
    }

    @Benchmark
    public long filterBoolean(final RowCounter counter) {
        counter.rows += rows;
        long passed = 0;
        for (final Chunk<Values> chunk : chunks) {
            passed += chunkFilter.filter(chunk, results);
        }
        return passed;
    }

    @Benchmark
    public long filterAnd(final RowCounter counter) {
        counter.rows += rows;
        long passed = 0;
        for (int cc = 0; cc < chunks.length; ++cc) {
            final Chunk<Values> chunk = chunks[cc];
            results.copyFromChunk(priorResults[cc], 0, 0, chunk.size());
            passed += chunkFilter.filterAnd(chunk, results);
        }
        return passed;
    }
}
