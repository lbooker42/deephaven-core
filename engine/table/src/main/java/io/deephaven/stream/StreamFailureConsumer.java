//
// Copyright (c) 2016-2025 Deephaven Data Labs and Patent Pending
//
package io.deephaven.stream;

import org.jetbrains.annotations.NotNull;

/**
 * An interface for accepting failures from an incoming stream in order to propagate them to downstream tables.
 */
@FunctionalInterface
public interface StreamFailureConsumer {

    /**
     * Report an error while processing the stream.
     *
     * @param cause the cause of the error
     */
    void acceptFailure(@NotNull Throwable cause);
}
