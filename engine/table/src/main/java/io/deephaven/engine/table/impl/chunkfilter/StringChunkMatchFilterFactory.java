//
// Copyright (c) 2016-2026 Deephaven Data Labs and Patent Pending
//
package io.deephaven.engine.table.impl.chunkfilter;

import io.deephaven.base.verify.Assert;
import io.deephaven.engine.table.MatchOptions;
import io.deephaven.hash.KeyedObjectHashSet;
import io.deephaven.hash.KeyedObjectKey;
import io.deephaven.base.string.cache.CharSequenceUtils;

import java.util.Arrays;
import java.util.HashSet;

class StringChunkMatchFilterFactory {
    private static final class CIStringKey implements KeyedObjectKey<String, String> {
        @Override
        public String getKey(String s) {
            return s;
        }

        @Override
        public int hashKey(String s) {
            return (s == null) ? 0 : CharSequenceUtils.caseInsensitiveHashCode(s);
        }

        @Override
        public boolean equalKey(String s, String s2) {
            return (s == null) ? s2 == null : s.equalsIgnoreCase(s2);
        }
    }

    private final static CIStringKey CASE_INSENSITIVE_KEY_INSTANCE = new CIStringKey();

    /**
     * Case-insensitive equality that, like the case-sensitive match filters, holds {@code null} equal to {@code null}
     * and to nothing else.
     */
    private static boolean equalsIgnoreCase(final String a, final String b) {
        return CASE_INSENSITIVE_KEY_INSTANCE.equalKey(a, b);
    }

    private StringChunkMatchFilterFactory() {} // static use only

    /**
     * The most values that are scanned rather than looked up in a {@link StringCuckooSet}. Match filters usually match
     * few rows, so this favors low hit rates. Measured with {@code MatchFilterWhereBenchmark} at 10% hits, the scan
     * wins up to six values and the cuckoo table (about 7.8 ns per row) from eight; at 50% hits the scan would win
     * through 24.
     */
    static final int SCAN_THRESHOLD = 6;

    /**
     * The longest list of values whose repeats are removed before choosing between the scan and the cuckoo table.
     */
    private static final int DEDUPLICATED_LIMIT = 4 * SCAN_THRESHOLD;


    /**
     * Create a case-sensitive filter for the provided values. A value that is neither null nor a {@link String} is left
     * out: no String equals it.
     * <p>
     * Up to {@link #SCAN_THRESHOLD} distinct values, the filter compares each row with the values in turn, comparing
     * hash codes first: a String caches its hash code, so most values are ruled out without {@code equals}. Beyond it,
     * the filter looks rows up in a {@link StringCuckooSet}, or, if one cannot be built within its limits, in a hash
     * set.
     */
    @SuppressWarnings("rawtypes")
    static ObjectChunkFilter makeFilter(final MatchOptions matchOptions, final Object... values) {
        Assert.eqFalse(matchOptions.caseInsensitive(), "matchOptions.caseInsensitive()");
        boolean matchesNull = false;
        String[] strings = new String[values.length];
        int stringCount = 0;
        for (final Object value : values) {
            if (value == null) {
                matchesNull = true;
            } else if (value instanceof String) {
                strings[stringCount++] = (String) value;
            }
        }
        strings = Arrays.copyOf(strings, stringCount);
        if (stringCount <= DEDUPLICATED_LIMIT) {
            // a short list: the scan counts distinct values, so that repeats do not push it past SCAN_THRESHOLD
            strings = new HashSet<>(Arrays.asList(strings)).toArray(new String[0]);
        }
        // a long list goes to the cuckoo table, which skips repeats itself
        if (strings.length <= SCAN_THRESHOLD) {
            return matchOptions.inverted()
                    ? new InverseScanStringChunkFilter(matchesNull, strings)
                    : new ScanStringChunkFilter(matchesNull, strings);
        }
        final StringCuckooSet set = StringCuckooSet.build(strings);
        if (set == null) {
            // no cuckoo table could be built within its limits
            return ObjectChunkMatchFilterFactory.makeFilter(matchOptions, values);
        }
        return matchOptions.inverted()
                ? new InverseCuckooStringChunkFilter(matchesNull, set)
                : new CuckooStringChunkFilter(matchesNull, set);
    }

    /**
     * Non-null Strings and their hash codes, compared in turn, hash code first.
     */
    private static abstract class ScannedStrings extends ObjectChunkFilter<String> {
        private final boolean matchesNull;
        private final String[] values;
        private final int[] hashes;

        private ScannedStrings(final boolean matchesNull, final String[] values) {
            this.matchesNull = matchesNull;
            this.values = values;
            this.hashes = Arrays.stream(values).mapToInt(String::hashCode).toArray();
        }

        final boolean contains(final String value) {
            if (value == null) {
                return matchesNull;
            }
            final int hash = value.hashCode();
            for (int ii = 0; ii < hashes.length; ++ii) {
                if (hashes[ii] == hash && value.equals(values[ii])) {
                    return true;
                }
            }
            return false;
        }
    }

    private final static class ScanStringChunkFilter extends ScannedStrings {
        private ScanStringChunkFilter(final boolean matchesNull, final String[] values) {
            super(matchesNull, values);
        }

        @Override
        public boolean matches(final String value) {
            return contains(value);
        }
    }

    private final static class InverseScanStringChunkFilter extends ScannedStrings {
        private InverseScanStringChunkFilter(final boolean matchesNull, final String[] values) {
            super(matchesNull, values);
        }

        @Override
        public boolean matches(final String value) {
            return !contains(value);
        }
    }

    private final static class CuckooStringChunkFilter extends ObjectChunkFilter<String> {
        private final boolean matchesNull;
        private final StringCuckooSet set;

        private CuckooStringChunkFilter(final boolean matchesNull, final StringCuckooSet set) {
            this.matchesNull = matchesNull;
            this.set = set;
        }

        @Override
        public boolean matches(final String value) {
            return value == null ? matchesNull : set.contains(value);
        }
    }

    private final static class InverseCuckooStringChunkFilter extends ObjectChunkFilter<String> {
        private final boolean matchesNull;
        private final StringCuckooSet set;

        private InverseCuckooStringChunkFilter(final boolean matchesNull, final StringCuckooSet set) {
            this.matchesNull = matchesNull;
            this.set = set;
        }

        @Override
        public boolean matches(final String value) {
            return value == null ? !matchesNull : !set.contains(value);
        }
    }

    /**
     * Create a case-insensitive filter for the provided values. Assumes that matchOptions.caseInsensitive() is true and
     * all provided values are {@link String}.
     */
    @SuppressWarnings("rawtypes")
    static ObjectChunkFilter makeCaseInsensitiveFilter(final MatchOptions matchOptions, final Object... values) {
        Assert.eqTrue(matchOptions.caseInsensitive(), "matchOptions.caseInsensitive()");
        if (matchOptions.inverted()) {
            if (values.length == 1) {
                return new InverseSingleValueStringChunkFilter((String) values[0]);
            }
            if (values.length == 2) {
                return new InverseTwoValueStringChunkFilter((String) values[0], (String) values[1]);
            }
            if (values.length == 3) {
                return new InverseThreeValueStringChunkFilter((String) values[0], (String) values[1],
                        (String) values[2]);
            }
            return new InverseMultiValueStringChunkFilter(values);
        } else {
            if (values.length == 1) {
                return new SingleValueStringChunkFilter((String) values[0]);
            }
            if (values.length == 2) {
                return new TwoValueStringChunkFilter((String) values[0], (String) values[1]);
            }
            if (values.length == 3) {
                return new ThreeValueStringChunkFilter((String) values[0], (String) values[1], (String) values[2]);
            }
            return new MultiValueStringChunkFilter(values);
        }
    }

    private final static class SingleValueStringChunkFilter extends ObjectChunkFilter<String> {
        private final String value;

        private SingleValueStringChunkFilter(String value) {
            this.value = value;
        }

        @Override
        public boolean matches(String value) {
            return equalsIgnoreCase(this.value, value);
        }
    }

    private static class InverseSingleValueStringChunkFilter extends ObjectChunkFilter<String> {
        private final String value;

        private InverseSingleValueStringChunkFilter(String value) {
            this.value = value;
        }

        @Override
        public boolean matches(String value) {
            return !equalsIgnoreCase(this.value, value);
        }
    }

    private static class TwoValueStringChunkFilter extends ObjectChunkFilter<String> {
        private final String value1;
        private final String value2;

        private TwoValueStringChunkFilter(String value1, String value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        @Override
        public boolean matches(String value) {
            return equalsIgnoreCase(value1, value) || equalsIgnoreCase(value2, value);
        }
    }

    private static class InverseTwoValueStringChunkFilter extends ObjectChunkFilter<String> {
        private final String value1;
        private final String value2;

        private InverseTwoValueStringChunkFilter(String value1, String value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        @Override
        public boolean matches(String value) {
            return !equalsIgnoreCase(value1, value) && !equalsIgnoreCase(value2, value);
        }
    }

    private static class ThreeValueStringChunkFilter extends ObjectChunkFilter<String> {
        private final String value1;
        private final String value2;
        private final String value3;

        private ThreeValueStringChunkFilter(String value1, String value2, String value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
        }

        @Override
        public boolean matches(String value) {
            return equalsIgnoreCase(value1, value) || equalsIgnoreCase(value2, value)
                    || equalsIgnoreCase(value3, value);
        }
    }

    private static class InverseThreeValueStringChunkFilter extends ObjectChunkFilter<String> {
        private final String value1;
        private final String value2;
        private final String value3;

        private InverseThreeValueStringChunkFilter(String value1, String value2, String value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
        }

        @Override
        public boolean matches(String value) {
            return !equalsIgnoreCase(value1, value) && !equalsIgnoreCase(value2, value)
                    && !equalsIgnoreCase(value3, value);
        }
    }

    private static class MultiValueStringChunkFilter extends ObjectChunkFilter<String> {
        private final KeyedObjectHashSet<String, String> values;
        private final boolean containsNull;

        private MultiValueStringChunkFilter(Object... values) {
            this.values = new KeyedObjectHashSet<>(CASE_INSENSITIVE_KEY_INSTANCE);
            boolean containsNull = false;
            for (Object value : values) {
                if (value == null) {
                    // KeyedObjectHashSet does not hold null
                    containsNull = true;
                } else {
                    this.values.add((String) value);
                }
            }
            this.containsNull = containsNull;
        }

        @Override
        public boolean matches(String value) {
            return value == null ? containsNull : this.values.containsKey(value);
        }
    }

    private static class InverseMultiValueStringChunkFilter extends ObjectChunkFilter<String> {
        private final KeyedObjectHashSet<String, String> values;
        private final boolean containsNull;

        private InverseMultiValueStringChunkFilter(Object... values) {
            this.values = new KeyedObjectHashSet<>(CASE_INSENSITIVE_KEY_INSTANCE);
            boolean containsNull = false;
            for (Object value : values) {
                if (value == null) {
                    // KeyedObjectHashSet does not hold null
                    containsNull = true;
                } else {
                    this.values.add((String) value);
                }
            }
            this.containsNull = containsNull;
        }

        @Override
        public boolean matches(String value) {
            return value == null ? !containsNull : !this.values.containsKey(value);
        }
    }
}
