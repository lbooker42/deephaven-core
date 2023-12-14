package io.deephaven.engine.table.impl.dataindex;

import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.table.ChunkSource;
import io.deephaven.engine.table.ColumnSource;
import org.jetbrains.annotations.NotNull;

/**
 * Tools for working with {@link io.deephaven.engine.table.DataIndex data indices}.
 */
public class DataIndexUtils {

    /**
     * Make a {@link ChunkSource} that produces data index {@link io.deephaven.engine.table.DataIndex.RowSetLookup row
     * set lookup} keys from {@code keySources}.
     * 
     * @param keySources The individual key sources
     * @return The boxed key source
     */
    public static ChunkSource.WithPrev<Values> makeBoxedKeySource(@NotNull final ColumnSource<?>... keySources) {
        switch (keySources.length) {
            case 0:
                throw new IllegalArgumentException("Data index must have at least one key column");
            case 1:
                return new DataIndexBoxedKeySourceSingle(keySources[0]);
            default:
                return new DataIndexBoxedKeySourceCompound(keySources);
        }
    }

    /**
     * Make a {@link DataIndexKeySet} that stores data index {@link io.deephaven.engine.table.DataIndex.RowSetLookup row
     * set lookup} keys that have {@code keyColumnCount} components.
     *
     * @param keyColumnCount The number of key components
     * @return The key set
     */
    public static DataIndexKeySet makeKeySet(final int keyColumnCount) {
        if (keyColumnCount == 1) {
            return new DataIndexKeySetSingle();
        }
        if (keyColumnCount > 1) {
            return new DataIndexKeySetCompound();
        }
        throw new IllegalArgumentException("Data index must have at least one key column");
    }

    /**
     * Make a {@link DataIndexKeySet} that stores data index {@link io.deephaven.engine.table.DataIndex.RowSetLookup row
     * set lookup} keys that have {@code keyColumnCount} components.
     *
     * @param keyColumnCount The number of key components
     * @param initialCapacity The initial capacity
     * @return The key set
     */
    public static DataIndexKeySet makeKeySet(final int keyColumnCount, final int initialCapacity) {
        if (keyColumnCount == 1) {
            return new DataIndexKeySetSingle(initialCapacity);
        }
        if (keyColumnCount > 1) {
            return new DataIndexKeySetCompound(initialCapacity);
        }
        throw new IllegalArgumentException("Data index must have at least one key column");
    }
}