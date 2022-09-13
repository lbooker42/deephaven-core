package io.deephaven.engine.table.impl;

import io.deephaven.api.updateby.UpdateByControl;
import io.deephaven.chunk.Chunk;
import io.deephaven.chunk.IntChunk;
import io.deephaven.chunk.LongChunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.rowset.RowSequence;
import io.deephaven.engine.rowset.RowSet;
import io.deephaven.engine.rowset.RowSetShiftData;
import io.deephaven.engine.rowset.TrackingRowSet;
import io.deephaven.engine.rowset.chunkattributes.OrderedRowKeys;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.Table;
import io.deephaven.engine.table.TableUpdate;
import io.deephaven.engine.table.impl.ssa.LongSegmentedSortedArray;
import io.deephaven.engine.table.impl.updateby.internal.BaseCharUpdateByOperator;
import io.deephaven.util.SafeCloseable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

/**
 * An operator that performs a specific computation for {@link Table#updateBy}. When adding implementations of this
 * interface, the pattern of calls will be as follows.
 *
 * <ol>
 * <li>Reprocess
 * <ul>
 * <li>{@link #resetForProcess(UpdateContext, RowSet, long)}</li>
 * <li>{@link #initializeFor(UpdateContext, RowSet, UpdateBy.UpdateType)}</li>
 * <li>{@link #reprocessChunkBucketed(UpdateContext, RowSequence, Chunk, LongChunk, IntChunk, IntChunk, IntChunk)}</li>
 * <li>{@link #finishFor(UpdateContext, UpdateBy.UpdateType)}</li>
 * </ul>
 * </li>
 * </ol>
 *
 * <p>
 * Additionally, implementations are responsible for notifying the update model if any rows have been modified beyond
 * what was passed through in the upstream update via the {@link #anyModified(UpdateContext)} and
 * {@link #getAdditionalModifications(UpdateContext)} methods
 * </p>
 */
public interface UpdateByOperator {
    UpdateByOperator[] ZERO_LENGTH_OP_ARRAY = new UpdateByOperator[0];

    /**
     * Check if the specified {@link TableUpdate} was append-only given the last known key within some other index.
     *
     * @param update the update to check
     * @param lastKnownKey the last known key from some other index.
     * @return if the update was append-only given the last known key
     */
    static boolean isAppendOnly(@NotNull final TableUpdate update, final long lastKnownKey) {
        return update.removed().isEmpty() &&
                update.modified().isEmpty() &&
                update.shifted().empty() &&
                update.added().firstRowKey() > lastKnownKey;
    }

    /**
     * A context item for use with {@link Table#updateBy(UpdateByControl, Collection, String...)} for non-bucketed
     * updates.
     */
    interface UpdateContext extends SafeCloseable {
        /**
         * Determine all the rows affected by the {@link TableUpdate} that need to be reprocessed
         *
         * @param upstream the update
         * @param source the rowset of the parent table (affected rows will be a subset)
         */
        RowSet determineAffectedRows(@NotNull final TableUpdate upstream, @NotNull final TrackingRowSet source,
                                           final boolean initialStep);

        /**
         * Return the affected rows computed by the {@Code determineAffectedRows()}
         */
        RowSet getAffectedRows();

        /**
         * Return the influencer rows computed by the {@Code determineAffectedRows()}
         */
        RowSet getInfluencerRows();

        LongSegmentedSortedArray getTimestampSsa();
    }

    /**
     * Get the name of the input column this operator depends on.
     *
     * @return the name of the input column
     */
    @NotNull
    String getInputColumnName();

    /**
     * Get the name of the timestamp column this operator depends on.
     *
     * @return the name of the input column
     */
    @Nullable
    String getTimestampColumnName();

    /**
     * Get an array of column names that, when modified, affect the result of this computation.
     *
     * @return an array of column names that affect this operator.
     */
    @NotNull
    String[] getAffectingColumnNames();

    /**
     * Get an array of the output column names.
     *
     * @return the output column names.
     */
    @NotNull
    String[] getOutputColumnNames();

    /**
     * Get a map of outputName to output {@link ColumnSource} for this operation.
     *
     * @return a map of output column name to output column source
     */
    @NotNull
    Map<String, ColumnSource<?>> getOutputColumns();

    /**
     * Indicate that the operation should start tracking previous values for ticking updates.
     */
    void startTrackingPrev();

    /**
     * Make an {@link UpdateContext} suitable for use with non-bucketed updates.
     *
     * @param chunkSize The expected size of chunks that will be provided during the update,
     * @param timestampSsa The timestamp SSA to use for time-based operations (null if using ticks)
     * @return a new context
     */
    @NotNull
    UpdateContext makeUpdateContext(final int chunkSize, final LongSegmentedSortedArray timestampSsa);

    /**
     * <p>
     * Initialize the context for the specified stage of the update process. This will always be followed by a call to
     * {@link #finishFor(UpdateContext)} at the end of each successful update.
     * </p>
     *
     * @param context the context object
     * @param updateRowSet the index of rows associated with the update.
     */
    void initializeFor(@NotNull final UpdateContext context,
            @NotNull final RowSet updateRowSet);

    /**
     * Perform and bookkeeping required at the end of a single part of the update. This is always preceded with a call
     * to
     *
     * {@link #initializeFor(UpdateContext, RowSet)}
     *
     * @param context the context object
     */
    void finishFor(@NotNull final UpdateContext context);

    /**
     * Get an index of rows that were modified beyond the input set of modifications from the upstream. This is invoked
     * once at the end of a complete update cycle (that is, after all adds, removes, modifies and shifts have been
     * processed) if {@link #anyModified(UpdateContext)} has returned true.
     *
     * @param context the context object
     * @return a {@link RowSet index} of additional rows that were modified
     */
    @NotNull
    RowSet getAdditionalModifications(@NotNull final UpdateContext context);

    /**
     * Check if the update has modified any rows for this operator. This is invoked once at the end of a complete update
     * cycle (that is, after all adds, removes, modifies and shifts have been processed).
     *
     * @param context the context object
     * @return true if the update modified any rows.
     */
    boolean anyModified(@NotNull final UpdateContext context);

    /**
     * Query if the operator requires key values for the current stage. This method will always be invoked after an
     * appropriate invocation of {@link #initializeFor(UpdateContext, RowSet)}
     *
     * @return true if the operator requires keys for this operation
     */
    boolean requiresKeys();

    /**
     * Query if the operator requires position values for the current stage. This method will always be invoked after an
     * appropriate invocation of {@link #initializeFor(UpdateContext, RowSet)}
     *
     * @return true if the operator requires position indices for this operation
     */
    boolean requiresPositions();

    /**
     * Query if the operator requires values for the current stage.
     *
     * @param context the context object
     * @return true if values are required for compuitation
     */
    boolean requiresValues(@NotNull final UpdateContext context);

    /**
     * Set the chunk size to be used for operations. This is used during the processing phase
     * when the chunks allocated during the normal processing phase may not be large enough.
     *
     * @param context the context object
     * @param chunkSize the new chunk size
     */
    void setChunkSize(@NotNull final UpdateContext context, final int chunkSize);

    /**
     * Apply a shift to the operation.
     *
     */
    void applyOutputShift(@NotNull final UpdateContext context,
            @NotNull final RowSet subIndexToShift,
            final long delta);

    /**
     * Process a chunk of data for an updateBy table.
     *
     * @param context the context object
     * @param inputKeys the keys contained in the chunk
     * @param keyChunk a {@link LongChunk} containing the keys if requested by {@link #requiresKeys()} or null.
     * @param posChunk a {@link LongChunk} containing the positions if requested by {@link #requiresPositions()} or null.
     * @param valuesChunk the current chunk of working values.
     * @param postUpdateSourceIndex the resulting source index af
     */
    void processChunk(@NotNull final UpdateContext context,
                      @NotNull final RowSequence inputKeys,
                      @Nullable final LongChunk<OrderedRowKeys> keyChunk,
                      @Nullable final LongChunk<OrderedRowKeys> posChunk,
                      @Nullable final Chunk<Values> valuesChunk,
                      @NotNull final RowSet postUpdateSourceIndex);

    /**
     * Reset the operator to the state at the `firstModifiedKey` for non-bucketed operation. This is invoked immediately
     * prior to calls to {@link #resetForProcess(UpdateContext, RowSet, long)}. <br>
     * <br>
     * A `firstUnmodifiedKey` of {@link RowSet#NULL_ROW_KEY} indicates that the entire table needs to be recomputed.
     *
     * @param context the context object
     * @param sourceIndex the current index of the source table
     * @param firstUnmodifiedKey the first unmodified key after which we will reprocess rows.
     */
    void resetForProcess(@NotNull final UpdateContext context,
                         @NotNull final RowSet sourceIndex,
                         final long firstUnmodifiedKey);
}
