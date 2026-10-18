//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
package io.deephaven.benchmark.engine.matchfilter;

import io.deephaven.engine.context.TestExecutionContext;
import io.deephaven.engine.table.ColumnDefinition;
import io.deephaven.engine.table.TableDefinition;
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
 * Measures the build time of a {@link MatchFilter} with {@code values} match values: creating the filter, initializing
 * it, which converts the values to the column type, and building the {@link ChunkFilter} that rows are matched against,
 * which unboxes the values and populates its set. {@link MatchFilterWhereBenchmark}'s {@code filter} measures the
 * filter time without it, and its {@code where} includes it.
 * <p>
 * Reports the time per match value, in nanoseconds, as the secondary result {@code values}.
 * {@link MatchFilterBenchmarkData} describes the values.
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(3)
public class MatchFilterBuildBenchmark {

    /**
     * Counts the match values built, so that JMH reports the time per value as the secondary result {@code values}.
     */
    @State(Scope.Thread)
    @AuxCounters(AuxCounters.Type.OPERATIONS)
    public static class ValueCounter {
        public long values;

        @Setup(Level.Iteration)
        public void reset() {
            values = 0;
        }
    }

    @Param({"int", "long", "float", "double", "Instant", "String", "BigDecimal"})
    private String type;

    @Param({"1", "2", "8", "32", "1024", "4096", "100000"})
    private int values;

    @Param({"sparse"})
    private String density;

    private SafeCloseable executionContext;
    private MatchFilterBenchmarkData data;
    private TableDefinition definition;

    @Setup(Level.Trial)
    public void setupEnv() {
        executionContext = TestExecutionContext.createForUnitTests().open();
        data = new MatchFilterBenchmarkData(type, values, density);
        definition = TableDefinition.of(
                ColumnDefinition.fromGenericType(MatchFilterBenchmarkData.COLUMN, data.columnType()));
    }

    @TearDown(Level.Trial)
    public void tearDownEnv() {
        executionContext.close();
    }

    @Benchmark
    public ChunkFilter build(final ValueCounter counter) {
        counter.values += values;
        final MatchFilter filter = data.newFilter();
        filter.init(definition);
        return filter.chunkFilter().orElseThrow();
    }
}
