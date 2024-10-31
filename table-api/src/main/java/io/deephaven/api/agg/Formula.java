//
// Copyright (c) 2016-2024 Deephaven Data Labs and Patent Pending
//
package io.deephaven.api.agg;

import io.deephaven.annotations.SimpleStyle;
import io.deephaven.api.ColumnName;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

/**
 * An {@link Aggregation aggregation} that provides a single output column that is computed by applying a formula to a
 * set of input columns.
 */
@Immutable
@SimpleStyle
public abstract class Formula implements Aggregation {

    public static Formula parse(String formulaString) {
        final int ix = formulaString.indexOf('=');
        if (ix < 0 || ix + 1 == formulaString.length() || formulaString.charAt(ix + 1) == '=') {
            throw new IllegalArgumentException(String.format(
                    "Unable to parse formula '%s', expected form '<newColumn>=<expression>'", formulaString));
        }
        return of(formulaString.substring(0, ix), formulaString.substring(ix + 1));
    }

    public static Formula of(ColumnName name, String formula) {
        return ImmutableFormula.of(name, formula);
    }

    public static Formula of(String name, String formula) {
        return of(ColumnName.of(name), formula);
    }

    @Parameter
    public abstract ColumnName column();

    @Parameter
    public abstract String formula();

    /**
     * Return this {@link Formula} as a string of the form {@code <newColumn>=<expression>}
     */
    public String formulaString() {
        return column().toString() + "=" + formula();
    }

    @Override
    public final <V extends Visitor> V walk(V visitor) {
        visitor.visit(this);
        return visitor;
    }
}
