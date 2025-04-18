//
// Copyright (c) 2016-2025 Deephaven Data Labs and Patent Pending
//
package io.deephaven.plot.errors;

/**
 * Cause of a plotting exception. Has information about the plot.
 */
public interface PlotExceptionCause {

    PlotInfo getPlotInfo();
}
