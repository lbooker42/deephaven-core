//
// Copyright (c) 2016-2024 Deephaven Data Labs and Patent Pending
//
package io.deephaven.api.updateby.spec;

import io.deephaven.annotations.BuildableStyle;
import io.deephaven.api.agg.util.AggCountType;
import org.immutables.value.Value;
import org.immutables.value.Value.Immutable;

import java.time.Duration;

/**
 * An {@link UpdateBySpec} for performing a windowed rolling count operation
 */
@Immutable
@BuildableStyle
public abstract class RollingCountSpec extends RollingOpSpec {
    public static RollingCountSpec ofTicks(long revTicks, AggCountType countType) {
        return of(WindowScale.ofTicks(revTicks), countType);
    }

    public static RollingCountSpec ofTicks(long revTicks, long fwdTicks, AggCountType countType) {
        return of(WindowScale.ofTicks(revTicks), WindowScale.ofTicks(fwdTicks), countType);
    }

    public static RollingCountSpec ofTime(final String timestampCol, Duration revDuration, AggCountType countType) {
        return of(WindowScale.ofTime(timestampCol, revDuration), countType);
    }

    public static RollingCountSpec ofTime(final String timestampCol, Duration revDuration, Duration fwdDuration,
            AggCountType countType) {
        return of(WindowScale.ofTime(timestampCol, revDuration),
                WindowScale.ofTime(timestampCol, fwdDuration),
                countType);
    }

    public static RollingCountSpec ofTime(final String timestampCol, long revDuration, AggCountType countType) {
        return of(WindowScale.ofTime(timestampCol, revDuration), countType);
    }

    public static RollingCountSpec ofTime(final String timestampCol, long revDuration, long fwdDuration,
            AggCountType countType) {
        return of(WindowScale.ofTime(timestampCol, revDuration),
                WindowScale.ofTime(timestampCol, fwdDuration),
                countType);
    }

    public static RollingCountSpec of(WindowScale revWindowScale, AggCountType countType) {
        return ImmutableRollingCountSpec.builder()
                .revWindowScale(revWindowScale)
                .countType(countType)
                .build();
    }

    public static RollingCountSpec of(WindowScale revWindowScale, WindowScale fwdWindowScale, AggCountType countType) {
        return ImmutableRollingCountSpec.builder()
                .revWindowScale(revWindowScale)
                .fwdWindowScale(fwdWindowScale)
                .countType(countType)
                .build();
    }

    @Value.Parameter
    public abstract AggCountType countType();

    @Override
    public final boolean applicableTo(Class<?> inputType) {
        return true;
    }

    @Override
    public final <T> T walk(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
