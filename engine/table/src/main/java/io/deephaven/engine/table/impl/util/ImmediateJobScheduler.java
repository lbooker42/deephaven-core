package io.deephaven.engine.table.impl.util;

import io.deephaven.base.log.LogOutputAppendable;
import io.deephaven.engine.context.ExecutionContext;
import io.deephaven.engine.table.impl.perf.BasePerformanceEntry;
import io.deephaven.util.SafeCloseable;
import io.deephaven.util.process.ProcessEnvironment;

import java.util.function.Consumer;

public class ImmediateJobScheduler implements JobScheduler {
    public static final ImmediateJobScheduler INSTANCE = new ImmediateJobScheduler();

    @Override
    public void submit(
            final ExecutionContext executionContext,
            final Runnable runnable,
            final LogOutputAppendable description,
            final Consumer<Exception> onError) {
        try (SafeCloseable ignored = executionContext != null ? executionContext.open() : null) {
            runnable.run();
        } catch (Exception e) {
            onError.accept(e);
        } catch (Error e) {
            ProcessEnvironment.getGlobalFatalErrorReporter().report("SelectAndView Error", e);
            throw e;
        }
    }

    @Override
    public BasePerformanceEntry getAccumulatedPerformance() {
        return null;
    }

    @Override
    public int threadCount() {
        return 1;
    }
}