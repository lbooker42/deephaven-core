//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
package io.deephaven.benchmark.engine.matchfilter;

import io.deephaven.engine.rowset.RowSetFactory;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.MatchOptions;
import io.deephaven.engine.table.impl.QueryTable;
import io.deephaven.engine.table.impl.select.MatchFilter;
import io.deephaven.engine.table.impl.sources.InMemoryColumnSource;
import io.deephaven.time.DateTimeUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.SplittableRandom;

/**
 * The match values and the one-column table that the MatchFilter benchmarks filter.
 * <p>
 * Every value comes from a {@code long} key, which {@link ValueType} maps to the column type. The match values are
 * distinct keys. With {@code sparse} density they are drawn from the type's whole range of keys. With {@code dense}
 * density they are drawn from a run of consecutive keys ten times their number, so that a bitmap over their min..max
 * range is small. Each row holds a match value with probability {@code hitRate}, and otherwise a key from the same
 * range that is not a match value. The number of match values, their density, the hit rate and the number of rows are
 * therefore independent of each other.
 * <p>
 * An object column holds a new object in every row, as a column read from a file would, so no row is the same object as
 * a match value. Half the BigDecimal rows that match carry an extra trailing zero, which {@code compareTo} ignores and
 * {@code equals} does not. No value is null, and no floating point value is NaN or zero.
 * <p>
 * With a {@code collisionGroup} of G, a String column's match values include G Strings that share one hash code, and
 * half the rows that miss hold one of G other Strings with that hash code. Hash code collisions are easy to construct,
 * so a list of match values may hold many of them.
 */
final class MatchFilterBenchmarkData {

    /**
     * The name of the generated table's only column.
     */
    static final String COLUMN = "X";

    private static final long VALUES_SEED = 0x6D61746368L;
    private static final long ROWS_SEED = 0x726F7773L;

    /**
     * The column types, each with the range of keys it maps from. Each range leaves out the type's null value.
     */
    private enum ValueType {
        BYTE(byte.class, -Byte.MAX_VALUE, Byte.MAX_VALUE), SHORT(short.class, -Short.MAX_VALUE, Short.MAX_VALUE), CHAR(
                char.class, 0, Character.MAX_VALUE - 1), INT(int.class, -Integer.MAX_VALUE,
                        Integer.MAX_VALUE), LONG(long.class, -Long.MAX_VALUE, Long.MAX_VALUE),
        // the bits of the positive, finite floating point values, leaving out zero and MAX_VALUE
        FLOAT(float.class, 1, Float.floatToIntBits(Float.MAX_VALUE) - 1), DOUBLE(double.class, 1,
                Double.doubleToLongBits(Double.MAX_VALUE) - 1),
        // epoch nanoseconds
        INSTANT(Instant.class, -Long.MAX_VALUE, Long.MAX_VALUE),
        // written in base 36
        STRING(String.class, -Long.MAX_VALUE, Long.MAX_VALUE),
        // the unscaled value, at scale 2
        BIG_DECIMAL(BigDecimal.class, -1_000_000_000_000_000L, 1_000_000_000_000_000L);

        private final Class<?> columnType;
        private final long firstKey;
        private final long lastKey;

        ValueType(final Class<?> columnType, final long firstKey, final long lastKey) {
            this.columnType = columnType;
            this.firstKey = firstKey;
            this.lastKey = lastKey;
        }

        private static ValueType of(final String name) {
            switch (name) {
                case "byte":
                    return BYTE;
                case "short":
                    return SHORT;
                case "char":
                    return CHAR;
                case "int":
                    return INT;
                case "long":
                    return LONG;
                case "float":
                    return FLOAT;
                case "double":
                    return DOUBLE;
                case "Instant":
                    return INSTANT;
                case "String":
                    return STRING;
                case "BigDecimal":
                    return BIG_DECIMAL;
                default:
                    throw new IllegalArgumentException("Unknown type " + name);
            }
        }
    }

    /**
     * A generated table, and how many of its rows hold a match value.
     */
    static final class GeneratedTable {
        final QueryTable table;
        final long matchingRows;

        private GeneratedTable(final QueryTable table, final long matchingRows) {
            this.table = table;
            this.matchingRows = matchingRows;
        }
    }

    private final ValueType type;
    // rows and match values are drawn from the keys firstKey..lastKey
    private final long firstKey;
    private final long lastKey;
    private final long[] sortedMatchKeys;
    private final Object[] values;
    // with a collision group, the Strings that share a hash code with some of the values but are not values
    private final String[] collidingMisses;

    /**
     * Draw the match values.
     *
     * @param typeName the column type: byte, short, char, int, long, float, double, Instant, String or BigDecimal
     * @param valueCount the number of match values
     * @param density {@code sparse} or {@code dense}
     */
    MatchFilterBenchmarkData(final String typeName, final int valueCount, final String density) {
        this(typeName, valueCount, density, 0);
    }

    /**
     * Draw the match values.
     *
     * @param typeName the column type: byte, short, char, int, long, float, double, Instant, String or BigDecimal
     * @param valueCount the number of match values
     * @param density {@code sparse} or {@code dense}
     * @param collisionGroup for String, how many of the match values share one hash code; zero for none
     */
    MatchFilterBenchmarkData(final String typeName, final int valueCount, final String density,
            final int collisionGroup) {
        type = ValueType.of(typeName);
        if (collisionGroup != 0 && (type != ValueType.STRING || collisionGroup > valueCount)) {
            throw new IllegalArgumentException("A collision group needs a String column and at most valueCount values");
        }
        final long typeSpan = span(type.firstKey, type.lastKey);
        // at least one key must be left over for the rows that do not match
        if (valueCount < 1 || valueCount > typeSpan) {
            throw new IllegalArgumentException(
                    typeName + " cannot hold " + valueCount + " match values and a value that misses them");
        }

        final SplittableRandom random = new SplittableRandom(VALUES_SEED);
        switch (density) {
            case "sparse":
                firstKey = type.firstKey;
                lastKey = type.lastKey;
                break;
            case "dense":
                final long span = Math.min(10L * valueCount - 1, typeSpan);
                firstKey = nextKey(random, type.firstKey, type.lastKey - span);
                lastKey = firstKey + span;
                break;
            default:
                throw new IllegalArgumentException("Unknown density " + density + ", expected sparse or dense");
        }

        // keep the order the keys were drawn in, so the values are not passed in sorted order
        final LinkedHashSet<Long> matchKeys = new LinkedHashSet<>();
        while (matchKeys.size() < valueCount) {
            matchKeys.add(nextKey(random, firstKey, lastKey));
        }
        values = matchKeys.stream().map(this::value).toArray();
        sortedMatchKeys = matchKeys.stream().mapToLong(Long::longValue).sorted().toArray();
        if (collisionGroup == 0) {
            collidingMisses = null;
        } else {
            final String[] family = collidingFamily(2 * collisionGroup);
            System.arraycopy(family, 0, values, valueCount - collisionGroup, collisionGroup);
            collidingMisses = Arrays.copyOfRange(family, collisionGroup, 2 * collisionGroup);
        }
    }

    /**
     * @return {@code size} Strings that share a hash code: the same number of two-character pieces, each "Aa" or "BB",
     *         which have the same hash code
     */
    private static String[] collidingFamily(final int size) {
        final int pieces = Integer.SIZE - Integer.numberOfLeadingZeros(size - 1);
        final String[] family = new String[size];
        for (int ii = 0; ii < size; ++ii) {
            final StringBuilder builder = new StringBuilder("c");
            for (int piece = 0; piece < pieces; ++piece) {
                builder.append((ii >>> piece & 1) == 0 ? "Aa" : "BB");
            }
            family[ii] = builder.toString();
        }
        return family;
    }

    /**
     * @return the column's data type
     */
    Class<?> columnType() {
        return type.columnType;
    }

    /**
     * @return a new, uninitialized filter on {@link #COLUMN} for the match values
     */
    MatchFilter newFilter() {
        return new MatchFilter(MatchOptions.REGULAR, COLUMN, values);
    }

    /**
     * Generate a table with one column, {@link #COLUMN}, held in an immutable in-memory array.
     *
     * @param rows the number of rows
     * @param hitRate the probability that a row holds a match value
     * @return the table, and how many of its rows hold a match value
     */
    GeneratedTable table(final int rows, final double hitRate) {
        if (!(hitRate >= 0 && hitRate <= 1)) {
            throw new IllegalArgumentException("hitRate must be from 0 to 1, not " + hitRate);
        }
        if (collidingMisses != null) {
            return collidingTable(rows, hitRate);
        }
        final SplittableRandom random = new SplittableRandom(ROWS_SEED);
        final long[] keys = new long[rows];
        long matchingRows = 0;
        for (int ii = 0; ii < rows; ++ii) {
            if (random.nextDouble() < hitRate) {
                keys[ii] = sortedMatchKeys[random.nextInt(sortedMatchKeys.length)];
                ++matchingRows;
            } else {
                long key;
                do {
                    key = nextKey(random, firstKey, lastKey);
                } while (isMatchKey(key));
                keys[ii] = key;
            }
        }
        final QueryTable table = new QueryTable(RowSetFactory.flat(rows).toTracking(),
                Map.of(COLUMN, column(keys, random)));
        return new GeneratedTable(table, matchingRows);
    }

    /**
     * @return a String table whose rows hold a match value with probability {@code hitRate}, and otherwise, with equal
     *         probability, one of {@link #collidingMisses} or a String that shares no hash code with a value
     */
    private GeneratedTable collidingTable(final int rows, final double hitRate) {
        final SplittableRandom random = new SplittableRandom(ROWS_SEED);
        final Set<Object> valueSet = new HashSet<>(Arrays.asList(values));
        final String[] data = new String[rows];
        long matchingRows = 0;
        for (int ii = 0; ii < rows; ++ii) {
            final String row;
            if (random.nextDouble() < hitRate) {
                row = (String) values[random.nextInt(values.length)];
                ++matchingRows;
            } else if (random.nextBoolean()) {
                row = collidingMisses[random.nextInt(collidingMisses.length)];
            } else {
                String candidate;
                do {
                    candidate = Long.toString(nextKey(random, firstKey, lastKey), 36);
                } while (valueSet.contains(candidate));
                row = candidate;
            }
            // a new object, so that no row is the same object as a value
            data[ii] = new String(row.toCharArray());
        }
        final QueryTable table = new QueryTable(RowSetFactory.flat(rows).toTracking(),
                Map.of(COLUMN, InMemoryColumnSource.getImmutableMemoryColumnSource(data, String.class, null)));
        return new GeneratedTable(table, matchingRows);
    }

    private boolean isMatchKey(final long key) {
        return Arrays.binarySearch(sortedMatchKeys, key) >= 0;
    }

    private Object value(final long key) {
        switch (type) {
            case BYTE:
                return (byte) key;
            case SHORT:
                return (short) key;
            case CHAR:
                return (char) key;
            case INT:
                return (int) key;
            case LONG:
                return key;
            case FLOAT:
                return Float.intBitsToFloat((int) key);
            case DOUBLE:
                return Double.longBitsToDouble(key);
            case INSTANT:
                return DateTimeUtils.epochNanosToInstant(key);
            case STRING:
                return Long.toString(key, 36);
            case BIG_DECIMAL:
                return BigDecimal.valueOf(key, 2);
            default:
                throw new IllegalStateException("Unexpected type " + type);
        }
    }

    private ColumnSource<?> column(final long[] keys, final SplittableRandom random) {
        final int rows = keys.length;
        final Object data;
        switch (type) {
            case BYTE: {
                final byte[] array = new byte[rows];
                for (int ii = 0; ii < rows; ++ii) {
                    array[ii] = (byte) keys[ii];
                }
                data = array;
                break;
            }
            case SHORT: {
                final short[] array = new short[rows];
                for (int ii = 0; ii < rows; ++ii) {
                    array[ii] = (short) keys[ii];
                }
                data = array;
                break;
            }
            case CHAR: {
                final char[] array = new char[rows];
                for (int ii = 0; ii < rows; ++ii) {
                    array[ii] = (char) keys[ii];
                }
                data = array;
                break;
            }
            case INT: {
                final int[] array = new int[rows];
                for (int ii = 0; ii < rows; ++ii) {
                    array[ii] = (int) keys[ii];
                }
                data = array;
                break;
            }
            case FLOAT: {
                final float[] array = new float[rows];
                for (int ii = 0; ii < rows; ++ii) {
                    array[ii] = Float.intBitsToFloat((int) keys[ii]);
                }
                data = array;
                break;
            }
            case DOUBLE: {
                final double[] array = new double[rows];
                for (int ii = 0; ii < rows; ++ii) {
                    array[ii] = Double.longBitsToDouble(keys[ii]);
                }
                data = array;
                break;
            }
            case LONG:
            case INSTANT:
                // an Instant column stores epoch nanoseconds
                data = keys;
                break;
            case STRING: {
                final String[] array = new String[rows];
                for (int ii = 0; ii < rows; ++ii) {
                    array[ii] = Long.toString(keys[ii], 36);
                }
                data = array;
                break;
            }
            case BIG_DECIMAL: {
                final BigDecimal[] array = new BigDecimal[rows];
                for (int ii = 0; ii < rows; ++ii) {
                    final BigDecimal value = BigDecimal.valueOf(keys[ii], 2);
                    array[ii] = isMatchKey(keys[ii]) && random.nextBoolean() ? value.setScale(3) : value;
                }
                data = array;
                break;
            }
            default:
                throw new IllegalStateException("Unexpected type " + type);
        }
        return InMemoryColumnSource.getImmutableMemoryColumnSource(data, type.columnType, null);
    }

    /**
     * @return one less than the number of keys from {@code first} to {@code last}, or {@code Long.MAX_VALUE} when that
     *         does not fit in a long
     */
    private static long span(final long first, final long last) {
        final long span = last - first;
        return span < 0 ? Long.MAX_VALUE : span;
    }

    /**
     * @return a key from {@code first} to {@code last}, inclusive, where {@code first} is greater than
     *         {@code Long.MIN_VALUE}
     */
    private static long nextKey(final SplittableRandom random, final long first, final long last) {
        if (last < Long.MAX_VALUE) {
            return random.nextLong(first, last + 1);
        }
        return random.nextLong(first - 1, last) + 1;
    }
}
