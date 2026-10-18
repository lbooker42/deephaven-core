//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
package io.deephaven.benchmark.engine.matchfilter;

import io.deephaven.engine.context.TestExecutionContext;
import io.deephaven.engine.rowset.WritableRowSet;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.impl.QueryTable;
import io.deephaven.engine.table.impl.chunkfilter.ChunkFilter;
import io.deephaven.engine.table.impl.select.MatchFilter;
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

import java.util.concurrent.TimeUnit;

/**
 * Measures matching every row of a table of {@code rows} rows, with one in-memory column, against a
 * {@link MatchFilter}:
 * <ul>
 * <li>{@link #filter}: the filter time alone. The filter is built in setup, so this applies its {@link ChunkFilter} to
 * every row: it reads the column, matches each value and builds the result, on one thread. Its time per row should
 * agree for 1M and 10M rows.</li>
 * <li>{@link #where}: a static {@code where} with a new MatchFilter, which pays the build time
 * ({@link MatchFilterBuildBenchmark}) and the filter time, and makes the result table. Parallel {@code where} and
 * memoization are turned off, so it builds the filter once and filters every row once, on one thread.</li>
 * </ul>
 * Each method reports its time per row, in nanoseconds, as the secondary result {@code rows}. Setup checks that both
 * match exactly the rows that hold a match value. {@link MatchFilterBenchmarkData} describes the data.
 * <p>
 * The default types are those that can hold 100,000 match values. byte, short and char cannot, so
 * {@code jmhRunMatchFilterByte} and {@code jmhRunMatchFilterShortChar} run them with the default numbers of match
 * values they can hold. To narrow a run, pass JMH arguments, which replace the task's own:
 *
 * <pre>
 * ./gradlew :engine-benchmark:jmhRunMatchFilter \
 *     --args='MatchFilterWhereBenchmark.filter -p type=int,long -p values=4,8,16 -p density=sparse,dense'
 * </pre>
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(3)
public class MatchFilterWhereBenchmark {

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

    @Param({"int", "long", "float", "double", "Instant", "String", "BigDecimal"})
    private String type;

    @Param({"1", "2", "8", "32", "1024", "4096", "100000"})
    private int values;

    @Param({"sparse"})
    private String density;

    // match filters usually match few rows; 1% to 90% shows how a structure's branches behave across them
    @Param({"0.01", "0.1", "0.5", "0.9"})
    private double hitRate;

    @Param({"1000000", "10000000"})
    private int rows;

    // for String, how many match values share one hash code
    @Param({"0"})
    private int collisionGroup;

    private SafeCloseable executionContext;
    private boolean oldMemoizeResults;
    private SafeCloseable noParallelWhere;

    private MatchFilterBenchmarkData data;
    private QueryTable table;
    private ColumnSource<?> source;
    private ChunkFilter chunkFilter;

    @Setup(Level.Trial)
    public void setupEnv() {
        executionContext = TestExecutionContext.createForUnitTests().open();
        // MatchFilter is memoizable, and equal for equal values, so a memoized where would only filter once
        oldMemoizeResults = QueryTable.setMemoizeResults(false);

        data = new MatchFilterBenchmarkData(type, values, density, collisionGroup);
        final MatchFilterBenchmarkData.GeneratedTable generated = data.table(rows, hitRate);
        table = generated.table;
        source = table.getColumnSource(MatchFilterBenchmarkData.COLUMN);
        final MatchFilter matchFilter = data.newFilter();
        matchFilter.init(table.getDefinition());
        chunkFilter = matchFilter.chunkFilter().orElseThrow();

        check("filter", filter(new RowCounter()), generated.matchingRows);
        check("where", where(new RowCounter()), generated.matchingRows);
    }

    @TearDown(Level.Trial)
    public void tearDownEnv() {
        QueryTable.setMemoizeResults(oldMemoizeResults);
        executionContext.close();
    }

    @Setup(Level.Iteration)
    public void setupIteration() {
        // the flag is per thread, and iteration setup runs on the thread that runs the benchmark
        noParallelWhere = QueryTable.disableParallelWhereForThread();
    }

    @TearDown(Level.Iteration)
    public void tearDownIteration() {
        noParallelWhere.close();
    }

    private static void check(final String method, final long matched, final long expected) {
        if (matched != expected) {
            throw new IllegalStateException(method + " matched " + matched + " rows, expected " + expected);
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
    public long where(final RowCounter counter) {
        counter.rows += rows;
        return table.where(data.newFilter()).size();
    }
}
