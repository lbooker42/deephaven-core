//
// Copyright (c) 2016-2025 Deephaven Data Labs and Patent Pending
//
// ****** AUTO-GENERATED CLASS - DO NOT EDIT MANUALLY
// ****** Run ReplicateTypedHashers or ./gradlew replicateTypedHashers to regenerate
//
// @formatter:off
package io.deephaven.engine.table.impl.asofjoin.typed.rightincopen.gen;

import io.deephaven.chunk.ChunkType;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.impl.asofjoin.RightIncrementalAsOfJoinStateManagerTypedBase;
import java.util.Arrays;

/**
 * The TypedHashDispatcher returns a pre-generated and precompiled hasher instance suitable for the provided column sources, or null if there is not a precompiled hasher suitable for the specified sources.
 */
public class TypedHashDispatcher {
    private TypedHashDispatcher() {
        // static use only
    }

    public static RightIncrementalAsOfJoinStateManagerTypedBase dispatch(
            ColumnSource[] tableKeySources, ColumnSource[] originalTableKeySources, int tableSize,
            double maximumLoadFactor, double targetLoadFactor) {
        final ChunkType[] chunkTypes = Arrays.stream(tableKeySources).map(ColumnSource::getChunkType).toArray(ChunkType[]::new);;
        if (chunkTypes.length == 1) {
            return dispatchSingle(chunkTypes[0], tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
        }
        return null;
    }

    private static RightIncrementalAsOfJoinStateManagerTypedBase dispatchSingle(ChunkType chunkType,
            ColumnSource[] tableKeySources, ColumnSource[] originalTableKeySources, int tableSize,
            double maximumLoadFactor, double targetLoadFactor) {
        switch (chunkType) {
            default: throw new UnsupportedOperationException("Invalid chunk type for typed hashers: " + chunkType);
            case Char: return new RightIncrementalAsOfJoinHasherChar(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
            case Byte: return new RightIncrementalAsOfJoinHasherByte(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
            case Short: return new RightIncrementalAsOfJoinHasherShort(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
            case Int: return new RightIncrementalAsOfJoinHasherInt(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
            case Long: return new RightIncrementalAsOfJoinHasherLong(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
            case Float: return new RightIncrementalAsOfJoinHasherFloat(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
            case Double: return new RightIncrementalAsOfJoinHasherDouble(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
            case Object: return new RightIncrementalAsOfJoinHasherObject(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, targetLoadFactor);
        }
    }
}
