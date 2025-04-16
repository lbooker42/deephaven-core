//
// Copyright (c) 2016-2025 Deephaven Data Labs and Patent Pending
//
package io.deephaven.engine.table.impl;

import io.deephaven.base.log.LogOutput;
import io.deephaven.engine.context.ExecutionContext;
import io.deephaven.engine.exceptions.CancellationException;
import io.deephaven.engine.rowset.RowSet;
import io.deephaven.engine.rowset.RowSetFactory;
import io.deephaven.engine.rowset.WritableRowSet;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.ModifiedColumnSet;
import io.deephaven.engine.table.impl.perf.BasePerformanceEntry;
import io.deephaven.engine.table.impl.select.WhereFilter;
import io.deephaven.engine.table.impl.util.JobScheduler;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * The AbstractFilterExecution incorporates the idea that we have an added and modified RowSet to filter and that there
 * are a resulting pair of added and modified rows representing what was filtered. There is also the possibility that we
 * encounter an exception "exceptionResult" in which case the operation should be considered a failure.
 * <p>
 * The strategy that is used to divide the work is that there is some target split (by default the number of threads in
 * the TableMapTransform or LiveTableMonitor update thread pools) that we will divide our operation into. If there is
 * not enough work (defined by the {@link QueryTable#PARALLEL_WHERE_ROWS_PER_SEGMENT}) for more than one thread, we
 * simply run the operation in the calling thread. After each filter, we "reseed" the operation and recursively divide
 * it. For example, you might imagine we have a sequence of filters like "isBusinessTime" followed by a filter on
 * spread. The isBusinessTime filter would produce unequal results, therefore we do an N-way split on each result set to
 * avoid some threads doing inordinately more work than others.
 * <p>
 * After a unit of work is completed, it percolates the result to its parent. Finally we call a completion routine,
 * which will either notify a downstream table (in the listener case) or set the value of a future (in the
 * initialization case).
 */
abstract class AbstractFilterExecution {
    final BasePerformanceEntry basePerformanceEntry = new BasePerformanceEntry();

    final QueryTable sourceTable;
    final WhereFilter[] filters;

    final boolean runModifiedFilters;
    final ModifiedColumnSet sourceModColumns;

    /**
     * The added RowSet we are filtering.
     */
    final RowSet addedInput;

    /**
     * The modified RowSet we are filtering.
     */
    final RowSet modifiedInput;

    /**
     * For initial filtering we may need to usePrev.
     */
    final boolean usePrev;

    AbstractFilterExecution(
            final QueryTable sourceTable,
            final WhereFilter[] filters,
            @NotNull final RowSet addedInput,
            @NotNull final RowSet modifiedInput,
            final boolean usePrev,
            final boolean runModifiedFilters,
            final ModifiedColumnSet sourceModColumns) {
        this.sourceTable = sourceTable;
        this.filters = filters;
        this.addedInput = addedInput;
        this.modifiedInput = modifiedInput;
        this.usePrev = usePrev;
        this.runModifiedFilters = runModifiedFilters;
        this.sourceModColumns = sourceModColumns;
    }

    /**
     * Retrieve the {@link JobScheduler} to use for this operation.
     */
    abstract JobScheduler jobScheduler();

    /**
     * This is called when a filter has been completed successfully.
     */
    @FunctionalInterface
    public interface FilterComplete {
        /**
         * Called when a filter has been completed successfully.
         *
         * @param adds the added rows resulting from the filter
         * @param mods the modified rows resulting from the filter
         */
        void accept(@NotNull RowSet adds, @NotNull RowSet mods);
    }

    /**
     * Run the single filter specified by this AbstractFilterExecution and store the results in addedResult and
     * modifyResult. Allows specification of the start and end positions in the added and modified inputs.
     *
     * @param filter the filter to execute
     * @param input the input to use for this filter
     * @param inputStart the start position in the input
     * @param inputEnd the end position in the input (exclusive)
     * @param onComplete the routine to call after the filter has been successfully executed
     * @param onError the routine to call if a filter raises an exception
     */
    private void doFilter(
            final WhereFilter filter,
            @NotNull final RowSet input,
            final long inputStart,
            final long inputEnd,
            final Consumer<WritableRowSet> onComplete,
            final Consumer<Exception> onError) {
        if (Thread.interrupted()) {
            throw new CancellationException("interrupted while filtering");
        }
        try {
            final WritableRowSet result;
            if (inputStart < inputEnd) {
                try (final RowSet restrictedInput = input.subSetByPositionRange(inputStart, inputEnd)) {
                    result = filter.filter(restrictedInput, sourceTable.getRowSet(), sourceTable, usePrev);
                }
            } else {
                result = RowSetFactory.empty();
            }
            onComplete.accept(result);
        } catch (Exception e) {
            onError.accept(e);
        }
    }

    /**
     * Run the filter specified by this AbstractFilterExecution in parallel
     *
     * @param filter the filter to execute
     * @param input the added input to use for this filter
     * @param onComplete the routine to call after the filter has been successfully executed
     * @param onError the routine to call if a filter raises an exception
     */
    private void doFilterParallel(
            final WhereFilter filter,
            @NotNull final RowSet input,
            final Consumer<WritableRowSet> onComplete,
            final Consumer<Exception> onError) {
        if (Thread.interrupted()) {
            throw new CancellationException("interrupted while filtering");
        }

        final long inputSize = input.size();

        final int targetSegments = (int) Math.min(getTargetSegments(), (inputSize +
                QueryTable.PARALLEL_WHERE_ROWS_PER_SEGMENT - 1) / QueryTable.PARALLEL_WHERE_ROWS_PER_SEGMENT);
        final long targetSize = (inputSize + targetSegments - 1) / targetSegments;

        // noinspection resource
        final WritableRowSet filterResult = RowSetFactory.empty();

        jobScheduler().iterateParallel(
                ExecutionContext.getContext(),
                this::append,
                JobScheduler.DEFAULT_CONTEXT_FACTORY,
                0, targetSegments,
                (localContext, idx, nec, resume) -> {
                    final long startOffSet = idx * targetSize;
                    final long endOffset = startOffSet + targetSize;

                    final Consumer<WritableRowSet> onFilterComplete = (result) -> {
                        // Clean up the row sets created by the filter.
                        try (final RowSet ignored = result) {
                            synchronized (filterResult) {
                                filterResult.insert(result);
                            }
                        }
                        resume.run();
                    };

                    // Filter this segment of the input rows.
                    doFilter(filter,
                            input, startOffSet, endOffset,
                            onFilterComplete, nec);

                }, () -> onComplete.accept(filterResult), onError);
    }

    public LogOutput append(LogOutput output) {
        return output.append("FilterExecution{")
                .append(System.identityHashCode(this)).append(": ");
    }

    /**
     * Simple extensions to hold either stateless or stateful filter.
     */
    private static class FilterCollection extends ArrayList<WhereFilter> {
    }
    private static class StatelessFilterCollection extends FilterCollection {
    }
    private static class StatefulFilterCollection extends FilterCollection {
    }

    /**
     * Transform an array of {@link WhereFilter filters} into collections of stateful and stateless filters.
     *
     * @param filters the filters to collect
     * @return a list of filter collections
     */
    static List<FilterCollection> collectFilters(WhereFilter[] filters) {
        final List<FilterCollection> filterCollections = new ArrayList<>();
        boolean collectionStateless = true;
        for (final WhereFilter filter : filters) {
            final boolean filterStateless = filter.permitParallelization(); // determines if a filter is stateless
            if (filterCollections.isEmpty() || collectionStateless != filterStateless) {
                filterCollections.add(filterStateless
                        ? new StatelessFilterCollection()
                        : new StatefulFilterCollection());
                collectionStateless = filterStateless;
            }
            filterCollections.get(filterCollections.size() - 1).add(filter);
        }
        return filterCollections;
    }

    /**
     * Execute the stateless filter at the provided index. This function re-evaluates the remaining filter costs after
     * the filtering action is complete, then re-sorts the array based on the lowest remaining evaluation cost. A filter
     * is not considered complete until the pushdown and filter are both complete, so this re-ordering may result in a
     * different filter occupying the current index. In this case, the new filter will be evaluated and process will
     * continue until the filter occupying the current index is fully complete.
     */
    private void executeStatelessFilter(
            final FilterResultContext[] filterResultContexts,
            final int filterIdx,
            final WhereFilter filter,
            final MutableObject<RowSet> localInput,
            final Runnable filterComplete,
            final Consumer<Exception> filterNec) {
        // Three initial scenarios are possible:
        // 1. Push-down filter executed, after re-ordering a new filter is at the current index.
        // * Combine the push-down results into a new input RowSet and recurse to the new filter.
        // * This might chain multiple push-down filters together, followed by a single chunk-filter.
        // 2. Push-down filter executed, after re-ordering the current filter is still at the current index.
        // * Optimize by executing the chunk filter against only the push-down "maybe" results.
        // 3. Chunk filter executed.
        // * Store the results and call the results consumer

        // Create the result consumer for chunk filters.
        final Consumer<WritableRowSet> onFilterComplete = (result) -> {
            // Clean up the row sets created by the filter.
            try (final RowSet ignored = localInput.getValue()) {
                // Store the output as the next filter input.
                localInput.setValue(result);
            }
            // This filter is complete, sort the remaining filters and call the consumer.
            if (filterIdx + 1 < filterResultContexts.length) {
                // Recompute the remaining costs to ensure the next filter is the lowest cost.
                FilterResultContext.sortFilterContexts(
                        filterIdx, filterResultContexts, sourceTable, localInput.getValue(), usePrev);
            }
            filterComplete.run();
        };

        final FilterResultContext frc = filterResultContexts[filterIdx];
        final WhereFilter currentFilter = frc.filter();

        if (frc.pushdownNeeded()) {
            // Create the push-down result consumer.
            final Consumer<PushdownResult> onPushdownComplete = (pushdownResult) -> {
                // Record that we are done with the push-down.
                frc.markPushdownComplete();

                if (pushdownResult.match().isEmpty()) {
                    // If there are no remaining `maybe` rows, skip the chunk filter.
                    localInput.setValue(pushdownResult.match().copy());

                    // Maybe recompute the remaining costs to ensure the next filter is the lowest cost.
                    if (filterIdx + 1 < filterResultContexts.length) {
                        FilterResultContext.sortFilterContexts(filterIdx + 1, filterResultContexts, sourceTable,
                                localInput.getValue(), usePrev);
                    }

                    // Cleanup the rowsets and call the consumer.
                    try (final PushdownResult ignored = pushdownResult) {
                        filterComplete.run();
                        return;
                    }
                }

                // Sort the filters again.
                if (filterIdx + 1 < filterResultContexts.length) {
                    // Recompute the remaining costs to ensure the next filter is the lowest cost.
                    FilterResultContext.sortFilterContexts(
                            filterIdx, filterResultContexts, sourceTable, localInput.getValue(), usePrev);
                }

                // If there is a new filter at the current index, need to evaluate it.
                if (!frc.equals(filterResultContexts[filterIdx])) {
                    // Use the union of the match and maybe rows as the input for the next filter.
                    localInput.setValue(pushdownResult.match().union(pushdownResult.maybeMatch()));

                    // Store the result for later use by the companion chunk filter.
                    frc.setPushdownResult(pushdownResult);

                    executeStatelessFilter(
                            filterResultContexts, filterIdx, filterResultContexts[filterIdx].filter(),
                            localInput, filterComplete, filterNec);
                } else {
                    // Leverage push-down results to reduce the chunk filter input.
                    final Consumer<WritableRowSet> localConsumer = (rows) -> {
                        try (final RowSet ignored = rows; final PushdownResult ignored2 = pushdownResult) {
                            onFilterComplete.accept(rows.union(pushdownResult.match()));
                        }
                    };
                    final long inputSize = pushdownResult.maybeMatch().size();

                    if (!shouldParallelizeFilter(filter, inputSize)) {
                        doFilter(filter, pushdownResult.maybeMatch(), 0, inputSize, localConsumer, filterNec);
                    } else {
                        doFilterParallel(filter, pushdownResult.maybeMatch(), localConsumer, filterNec);
                    }
                }
            };

            if (frc.ppm() != null) {
                frc.ppm().pushdownFilter(
                        sourceTable.getColumnSourceMap(),
                        currentFilter, localInput.getValue(), sourceTable.getRowSet(), jobScheduler(),
                        onPushdownComplete, filterNec);
            } else {
                final AbstractColumnSource<?> acs =
                        (AbstractColumnSource<?>) sourceTable
                                .getColumnSource(currentFilter.getColumns().get(0));

                acs.pushdownFilter(
                        currentFilter, localInput.getValue(), sourceTable.getRowSet(), jobScheduler(),
                        onPushdownComplete, filterNec);
            }
            return;
        }

        final RowSet input = localInput.getValue();

        if (frc.pushdownResult() != null) {
            // Leverage push-down results to reduce the chunk filter input.
            final PushdownResult localPushdownResult = frc.pushdownResult();

            final Consumer<WritableRowSet> localConsumer = (rows) -> {
                // Do some cleanup on the rowsets and call the consumer.
                try (final RowSet ignored = rows; final PushdownResult ignored2 = localPushdownResult) {
                    onFilterComplete.accept(rows.union(frc.pushdownResult().match()));
                }
            };

            localPushdownResult.match().retain(input);
            localPushdownResult.maybeMatch().retain(input);

            final long inputSize = localPushdownResult.maybeMatch().size();

            if (!shouldParallelizeFilter(filter, inputSize)) {
                doFilter(filter, localPushdownResult.maybeMatch(), 0, inputSize, localConsumer, filterNec);
            } else {
                doFilterParallel(filter, localPushdownResult.maybeMatch(), localConsumer, filterNec);
            }
            return;
        }
        final long inputSize = input.size();

        // Run serially or parallelized?
        if (!shouldParallelizeFilter(filter, inputSize)) {
            doFilter(filter, input, 0, inputSize, onFilterComplete, filterNec);
        } else {
            doFilterParallel(filter, input, onFilterComplete, filterNec);
        }
    }

    /**
     * Execute all stateless filters in the collection and return a row set that contains the rows that match every
     * filter.
     *
     * @param filters the filters to execute
     * @param localInput the input to use for this filter, also stores the result after the filters are executed
     * @param collectionResume the routine to call after the filter has been completely executed
     * @param collectionNec the routine to call if the filter experiences an exception
     */
    private void filterStatelessCollection(
            final List<WhereFilter> filters,
            final MutableObject<RowSet> localInput,
            final Runnable collectionResume,
            final Consumer<Exception> collectionNec) {

        // Create an array of FilterResultContext objects for the filters in this collection.
        final FilterResultContext[] filterResultContexts = filters.stream().map(filter -> {
            final PushdownPredicateManager ppm;
            if (filter.getColumns().size() > 1) {
                final Collection<ColumnSource<?>> columnSources = filter.getColumns().stream()
                        .map(sourceTable::getColumnSource)
                        .collect(Collectors.toList());
                ppm = PushdownPredicateManager.getPushdownPredicateManager(columnSources);
            } else {
                ppm = null;
            }
            return new FilterResultContext(filter, ppm);
        }).toArray(FilterResultContext[]::new);

        // Sort the filters by cost, with the lowest cost first.
        FilterResultContext.sortFilterContexts(
                0, filterResultContexts, sourceTable, localInput.getValue(), usePrev);

        // Iterate serially through the stateless filters in this set. Each filter will successively
        // restrict the input to the next filter, until we reach the end of the filter chain.
        jobScheduler().iterateSerial(
                ExecutionContext.getContext(),
                this::append,
                JobScheduler.DEFAULT_CONTEXT_FACTORY,
                0, filterResultContexts.length,
                (filterContext, filterIdx, filterNec, filterResume) -> {
                    executeStatelessFilter(
                            filterResultContexts, filterIdx, filterResultContexts[filterIdx].filter(),
                            localInput, filterResume, filterNec);
                }, collectionResume, collectionNec);
    }

    /**
     * Execute all stateful filters in the collection and return a row set that contains the rows that match every
     * filter.
     *
     * @param filters the filters to execute
     * @param localInput the input to use for this filter, also stores the result after the filters are executed
     * @param collectionResume the routine to call after the filter has been completely executed
     * @param collectionNec the routine to call if the filter experiences an exception
     */
    private void filterStatefulCollection(
            final List<WhereFilter> filters,
            final MutableObject<RowSet> localInput,
            final Runnable collectionResume,
            final Consumer<Exception> collectionNec) {
        // Iterate serially through the stateful filters in this set. Each filter will successively
        // restrict the input to the next filter, until we reach the end of the filter chain.
        jobScheduler().iterateSerial(
                ExecutionContext.getContext(),
                this::append,
                JobScheduler.DEFAULT_CONTEXT_FACTORY,
                0, filters.size(),
                (filterContext, filterIdx, filterNec, filterResume) -> {
                    final WhereFilter filter = filters.get(filterIdx);
                    // Use the restricted output for the next filter (if this is not the first invocation)
                    final RowSet input = localInput.getValue();
                    final long inputSize = input.size();

                    final Consumer<WritableRowSet> onFilterComplete = (result) -> {
                        // Clean up the row sets created by the filter.
                        try (final RowSet ignored = localInput.getValue()) {
                            // Store the output as the next filter input.
                            localInput.setValue(result);
                        }
                        filterResume.run();
                    };

                    // Run serially or parallelized?
                    if (!shouldParallelizeFilter(filter, inputSize)) {
                        doFilter(filter, input, 0, inputSize, onFilterComplete, filterNec);
                    } else {
                        doFilterParallel(filter, input, onFilterComplete, filterNec);
                    }
                }, collectionResume, collectionNec);
    }

    /**
     * Execute all filters; this may execute some filters in parallel when appropriate.
     *
     * @param onComplete the routine to call after the filter has been completely executed.
     * @param onError the routine to call if the filter experiences an exception.
     */
    public void scheduleCompletion(
            @NotNull final AbstractFilterExecution.FilterComplete onComplete,
            @NotNull final Consumer<Exception> onError) {

        // Union the added and modified inputs (if needed).
        final WritableRowSet input = runModifiedFilters ? addedInput.union(modifiedInput) : addedInput.copy();

        // Short-circuit if there is no input to filter.
        if (input == null || input.isEmpty()) {
            onComplete.accept(RowSetFactory.empty(), RowSetFactory.empty());
            return;
        }

        // Start with the input row sets and narrow with each filter.
        final MutableObject<RowSet> localInput = new MutableObject<>(input);

        // Divide the filters into stateful and stateless filter sets.
        final List<FilterCollection> filterCollections = collectFilters(filters);

        // Iterate serially through the filter collections.
        jobScheduler().iterateSerial(
                ExecutionContext.getContext(),
                this::append,
                JobScheduler.DEFAULT_CONTEXT_FACTORY,
                0, filterCollections.size(),
                (collectionCtx, collectionIdx, collectionNec, collectionResume) -> {
                    final FilterCollection filterCollection = filterCollections.get(collectionIdx);
                    if (filterCollection instanceof StatelessFilterCollection) {
                        filterStatelessCollection(filterCollection, localInput, collectionResume, collectionNec);
                    } else {
                        filterStatefulCollection(filterCollection, localInput, collectionResume, collectionNec);
                    }
                }, () -> {
                    // Return empty RowSets instead of null.
                    final RowSet result = localInput.getValue();
                    final BasePerformanceEntry baseEntry = jobScheduler().getAccumulatedPerformance();
                    if (baseEntry != null) {
                        basePerformanceEntry.accumulate(baseEntry);
                    }

                    // Separate the added and modified result if necessary.
                    if (runModifiedFilters) {
                        final WritableRowSet writableResult = result.writableCast();
                        final WritableRowSet addedResult = writableResult.extract(addedInput);
                        onComplete.accept(addedResult, result);
                    } else {
                        onComplete.accept(result, RowSetFactory.empty());
                    }
                }, onError);
    }

    /**
     * @return how many ways should we spit execution
     */
    abstract int getTargetSegments();

    /**
     * Should this operation be allowed to run parallelized?
     */
    abstract boolean permitParallelization();

    /**
     * Should a filter of the given size be parallelized or executed within this thread?
     */
    boolean shouldParallelizeFilter(WhereFilter filter, long numberOfRows) {
        return permitParallelization()
                && numberOfRows != 0
                && (QueryTable.FORCE_PARALLEL_WHERE || numberOfRows / 2 > QueryTable.PARALLEL_WHERE_ROWS_PER_SEGMENT)
                && filter.permitParallelization();
    }

    /**
     * Should parallelization be allowed for this operation.
     *
     * @param filters the filters that we are operating on
     *
     * @return true if we should permit parallelization (if any filters can be parallelized)
     */
    static boolean permitParallelization(WhereFilter[] filters) {
        final Boolean threadLocal = QueryTable.isParallelWhereDisabledForThread();
        if (threadLocal != null) {
            return !threadLocal;
        }
        if (QueryTable.DISABLE_PARALLEL_WHERE) {
            return false;
        }
        return Arrays.stream(filters).anyMatch(WhereFilter::permitParallelization);
    }
}
