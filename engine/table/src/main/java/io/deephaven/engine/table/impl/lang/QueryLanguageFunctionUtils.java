//
// Copyright (c) 2016-2025 Deephaven Data Labs and Patent Pending
//
package io.deephaven.engine.table.impl.lang;

import io.deephaven.configuration.Configuration;
import io.deephaven.util.QueryConstants;
import org.jpy.PyObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static java.lang.Math.*;

@SuppressWarnings({"unused", "WeakerAccess", "SimplifiableIfStatement"})
public final class QueryLanguageFunctionUtils {

    private static final String DEFAULT_SCALE_PROPERTY = "defaultScale";
    public static final int DEFAULT_SCALE = Configuration.getInstance()
            .getIntegerForClassWithDefault(QueryLanguageFunctionUtils.class, DEFAULT_SCALE_PROPERTY, 8);

    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    public static boolean eq(Object obj1, Object obj2) {
        // noinspection SimplifiableBooleanExpression
        return obj1 == obj2 || (!(obj1 == null ^ obj2 == null) && obj1.equals(obj2));
    }

    @SuppressWarnings({"unchecked"})
    public static int compareTo(Comparable obj1, Comparable obj2) {
        if (obj1 == null) {
            return (obj2 == null) ? 0 : -1;
        }

        if (obj2 == null) {
            return 1;
        }

        return obj1.compareTo(obj2);
    }

    public static boolean not(boolean a) {
        return !a;
    }

    public static Boolean not(Boolean a) {
        return a == QueryConstants.NULL_BOOLEAN ? QueryConstants.NULL_BOOLEAN : Boolean.valueOf(!a);
    }

    public static int plus(int a, int b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_INT ? QueryConstants.NULL_INT : a + b;
    }

    public static int[] plusArray(int a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (int, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] plusArray(int a[], int b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static int[] plusArray(int a, int b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static double plus(int a, double b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a + b;
    }

    public static double[] plusArray(int a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (int, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] plusArray(int a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static double[] plusArray(int a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static long plus(int a, long b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : a + b;
    }

    public static long[] plusArray(int a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (int, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static long[] plusArray(int a[], long b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static long[] plusArray(int a, long b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static float plus(int a, float b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a + b;
    }

    public static float[] plusArray(int a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (int, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static float[] plusArray(int a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static float[] plusArray(int a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static int plus(int a, char b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_INT : a + b;
    }

    public static int[] plusArray(int a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (int, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] plusArray(int a[], char b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static int[] plusArray(int a, char b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static int plus(int a, byte b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_INT : a + b;
    }

    public static int[] plusArray(int a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (int, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] plusArray(int a[], byte b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static int[] plusArray(int a, byte b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static int plus(int a, short b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_INT : a + b;
    }

    public static int[] plusArray(int a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (int, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] plusArray(int a[], short b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static int[] plusArray(int a, short b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static double plus(double a, int b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_INT ? QueryConstants.NULL_DOUBLE : a + b;
    }

    public static double[] plusArray(double a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (double, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] plusArray(double a[], int b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static double[] plusArray(double a, int b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static double plus(double a, double b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a + b;
    }

    public static double[] plusArray(double a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (double, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] plusArray(double a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static double[] plusArray(double a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static double plus(double a, long b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_DOUBLE : a + b;
    }

    public static double[] plusArray(double a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (double, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] plusArray(double a[], long b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static double[] plusArray(double a, long b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static double plus(double a, float b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_DOUBLE : a + b;
    }

    public static double[] plusArray(double a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (double, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] plusArray(double a[], float b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static double[] plusArray(double a, float b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static double plus(double a, char b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_DOUBLE : a + b;
    }

    public static double[] plusArray(double a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (double, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] plusArray(double a[], char b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static double[] plusArray(double a, char b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static double plus(double a, byte b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_DOUBLE : a + b;
    }

    public static double[] plusArray(double a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (double, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] plusArray(double a[], byte b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static double[] plusArray(double a, byte b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static double plus(double a, short b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_DOUBLE : a + b;
    }

    public static double[] plusArray(double a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (double, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] plusArray(double a[], short b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static double[] plusArray(double a, short b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static long plus(long a, int b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_INT ? QueryConstants.NULL_LONG : a + b;
    }

    public static long[] plusArray(long a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (long, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static long[] plusArray(long a[], int b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static long[] plusArray(long a, int b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static double plus(long a, double b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a + b;
    }

    public static double[] plusArray(long a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (long, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] plusArray(long a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static double[] plusArray(long a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static long plus(long a, long b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : a + b;
    }

    public static long[] plusArray(long a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (long, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static long[] plusArray(long a[], long b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static long[] plusArray(long a, long b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static float plus(long a, float b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a + b;
    }

    public static float[] plusArray(long a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (long, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static float[] plusArray(long a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static float[] plusArray(long a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static long plus(long a, char b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_LONG : a + b;
    }

    public static long[] plusArray(long a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (long, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static long[] plusArray(long a[], char b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static long[] plusArray(long a, char b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static long plus(long a, byte b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_LONG : a + b;
    }

    public static long[] plusArray(long a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (long, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static long[] plusArray(long a[], byte b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static long[] plusArray(long a, byte b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static long plus(long a, short b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_LONG : a + b;
    }

    public static long[] plusArray(long a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (long, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static long[] plusArray(long a[], short b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static long[] plusArray(long a, short b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static float plus(float a, int b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_INT ? QueryConstants.NULL_FLOAT : a + b;
    }

    public static float[] plusArray(float a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (float, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static float[] plusArray(float a[], int b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static float[] plusArray(float a, int b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static double plus(float a, double b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a + b;
    }

    public static double[] plusArray(float a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (float, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] plusArray(float a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static double[] plusArray(float a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static float plus(float a, long b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_FLOAT : a + b;
    }

    public static float[] plusArray(float a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (float, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static float[] plusArray(float a[], long b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static float[] plusArray(float a, long b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static float plus(float a, float b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a + b;
    }

    public static float[] plusArray(float a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (float, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static float[] plusArray(float a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static float[] plusArray(float a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static float plus(float a, char b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_FLOAT : a + b;
    }

    public static float[] plusArray(float a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (float, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static float[] plusArray(float a[], char b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static float[] plusArray(float a, char b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static float plus(float a, byte b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_FLOAT : a + b;
    }

    public static float[] plusArray(float a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (float, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static float[] plusArray(float a[], byte b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static float[] plusArray(float a, byte b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static float plus(float a, short b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_FLOAT : a + b;
    }

    public static float[] plusArray(float a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (float, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static float[] plusArray(float a[], short b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static float[] plusArray(float a, short b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static int plus(char a, int b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_INT ? QueryConstants.NULL_INT : a + b;
    }

    public static int[] plusArray(char a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (char, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] plusArray(char a[], int b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static int[] plusArray(char a, int b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static double plus(char a, double b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a + b;
    }

    public static double[] plusArray(char a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (char, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] plusArray(char a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static double[] plusArray(char a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static long plus(char a, long b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : a + b;
    }

    public static long[] plusArray(char a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (char, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static long[] plusArray(char a[], long b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static long[] plusArray(char a, long b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static float plus(char a, float b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a + b;
    }

    public static float[] plusArray(char a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (char, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static float[] plusArray(char a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static float[] plusArray(char a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static int plus(char a, char b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_INT : a + b;
    }

    public static int[] plusArray(char a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (char, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] plusArray(char a[], char b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static int[] plusArray(char a, char b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static int plus(char a, byte b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_INT : a + b;
    }

    public static int[] plusArray(char a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (char, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] plusArray(char a[], byte b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static int[] plusArray(char a, byte b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static int plus(char a, short b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_INT : a + b;
    }

    public static int[] plusArray(char a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (char, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] plusArray(char a[], short b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static int[] plusArray(char a, short b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static int plus(byte a, int b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_INT ? QueryConstants.NULL_INT : a + b;
    }

    public static int[] plusArray(byte a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (byte, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] plusArray(byte a[], int b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static int[] plusArray(byte a, int b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static double plus(byte a, double b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a + b;
    }

    public static double[] plusArray(byte a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (byte, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] plusArray(byte a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static double[] plusArray(byte a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static long plus(byte a, long b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : a + b;
    }

    public static long[] plusArray(byte a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (byte, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static long[] plusArray(byte a[], long b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static long[] plusArray(byte a, long b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static float plus(byte a, float b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a + b;
    }

    public static float[] plusArray(byte a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (byte, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static float[] plusArray(byte a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static float[] plusArray(byte a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static int plus(byte a, char b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_INT : a + b;
    }

    public static int[] plusArray(byte a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (byte, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] plusArray(byte a[], char b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static int[] plusArray(byte a, char b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static int plus(byte a, byte b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_INT : a + b;
    }

    public static int[] plusArray(byte a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (byte, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] plusArray(byte a[], byte b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static int[] plusArray(byte a, byte b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static int plus(byte a, short b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_INT : a + b;
    }

    public static int[] plusArray(byte a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (byte, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] plusArray(byte a[], short b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static int[] plusArray(byte a, short b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static int plus(short a, int b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_INT ? QueryConstants.NULL_INT : a + b;
    }

    public static int[] plusArray(short a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (short, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] plusArray(short a[], int b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static int[] plusArray(short a, int b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static double plus(short a, double b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a + b;
    }

    public static double[] plusArray(short a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (short, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] plusArray(short a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static double[] plusArray(short a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static long plus(short a, long b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : a + b;
    }

    public static long[] plusArray(short a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (short, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static long[] plusArray(short a[], long b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static long[] plusArray(short a, long b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static float plus(short a, float b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a + b;
    }

    public static float[] plusArray(short a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (short, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static float[] plusArray(short a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static float[] plusArray(short a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static int plus(short a, char b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_INT : a + b;
    }

    public static int[] plusArray(short a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (short, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] plusArray(short a[], char b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static int[] plusArray(short a, char b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static int plus(short a, byte b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_INT : a + b;
    }

    public static int[] plusArray(short a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (short, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] plusArray(short a[], byte b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static int[] plusArray(short a, byte b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static int plus(short a, short b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_INT : a + b;
    }

    public static int[] plusArray(short a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to add two arrays (short, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] plusArray(short a[], short b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = plus(a[i], b);
        }

        return ret;
    }

    public static int[] plusArray(short a, short b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = plus(a, b[i]);
        }

        return ret;
    }

    public static int minus(int a, int b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_INT ? QueryConstants.NULL_INT : a - b;
    }

    public static int[] minusArray(int a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (int, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] minusArray(int a[], int b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static int[] minusArray(int a, int b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static double minus(int a, double b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a - b;
    }

    public static double[] minusArray(int a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (int, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] minusArray(int a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static double[] minusArray(int a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static long minus(int a, long b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : a - b;
    }

    public static long[] minusArray(int a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (int, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static long[] minusArray(int a[], long b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static long[] minusArray(int a, long b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static float minus(int a, float b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a - b;
    }

    public static float[] minusArray(int a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (int, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static float[] minusArray(int a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static float[] minusArray(int a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static int minus(int a, char b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_INT : a - b;
    }

    public static int[] minusArray(int a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (int, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] minusArray(int a[], char b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static int[] minusArray(int a, char b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static int minus(int a, byte b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_INT : a - b;
    }

    public static int[] minusArray(int a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (int, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] minusArray(int a[], byte b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static int[] minusArray(int a, byte b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static int minus(int a, short b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_INT : a - b;
    }

    public static int[] minusArray(int a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (int, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] minusArray(int a[], short b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static int[] minusArray(int a, short b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static double minus(double a, int b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_INT ? QueryConstants.NULL_DOUBLE : a - b;
    }

    public static double[] minusArray(double a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (double, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] minusArray(double a[], int b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static double[] minusArray(double a, int b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static double minus(double a, double b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a - b;
    }

    public static double[] minusArray(double a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (double, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] minusArray(double a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static double[] minusArray(double a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static double minus(double a, long b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_DOUBLE : a - b;
    }

    public static double[] minusArray(double a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (double, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] minusArray(double a[], long b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static double[] minusArray(double a, long b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static double minus(double a, float b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_DOUBLE : a - b;
    }

    public static double[] minusArray(double a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (double, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] minusArray(double a[], float b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static double[] minusArray(double a, float b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static double minus(double a, char b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_DOUBLE : a - b;
    }

    public static double[] minusArray(double a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (double, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] minusArray(double a[], char b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static double[] minusArray(double a, char b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static double minus(double a, byte b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_DOUBLE : a - b;
    }

    public static double[] minusArray(double a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (double, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] minusArray(double a[], byte b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static double[] minusArray(double a, byte b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static double minus(double a, short b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_DOUBLE : a - b;
    }

    public static double[] minusArray(double a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (double, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] minusArray(double a[], short b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static double[] minusArray(double a, short b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static long minus(long a, int b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_INT ? QueryConstants.NULL_LONG : a - b;
    }

    public static long[] minusArray(long a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (long, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static long[] minusArray(long a[], int b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static long[] minusArray(long a, int b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static double minus(long a, double b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a - b;
    }

    public static double[] minusArray(long a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (long, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] minusArray(long a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static double[] minusArray(long a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static long minus(long a, long b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : a - b;
    }

    public static long[] minusArray(long a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (long, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static long[] minusArray(long a[], long b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static long[] minusArray(long a, long b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static float minus(long a, float b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a - b;
    }

    public static float[] minusArray(long a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (long, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static float[] minusArray(long a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static float[] minusArray(long a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static long minus(long a, char b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_LONG : a - b;
    }

    public static long[] minusArray(long a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (long, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static long[] minusArray(long a[], char b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static long[] minusArray(long a, char b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static long minus(long a, byte b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_LONG : a - b;
    }

    public static long[] minusArray(long a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (long, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static long[] minusArray(long a[], byte b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static long[] minusArray(long a, byte b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static long minus(long a, short b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_LONG : a - b;
    }

    public static long[] minusArray(long a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (long, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static long[] minusArray(long a[], short b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static long[] minusArray(long a, short b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static float minus(float a, int b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_INT ? QueryConstants.NULL_FLOAT : a - b;
    }

    public static float[] minusArray(float a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (float, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static float[] minusArray(float a[], int b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static float[] minusArray(float a, int b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static double minus(float a, double b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a - b;
    }

    public static double[] minusArray(float a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (float, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] minusArray(float a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static double[] minusArray(float a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static float minus(float a, long b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_FLOAT : a - b;
    }

    public static float[] minusArray(float a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (float, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static float[] minusArray(float a[], long b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static float[] minusArray(float a, long b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static float minus(float a, float b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a - b;
    }

    public static float[] minusArray(float a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (float, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static float[] minusArray(float a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static float[] minusArray(float a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static float minus(float a, char b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_FLOAT : a - b;
    }

    public static float[] minusArray(float a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (float, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static float[] minusArray(float a[], char b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static float[] minusArray(float a, char b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static float minus(float a, byte b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_FLOAT : a - b;
    }

    public static float[] minusArray(float a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (float, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static float[] minusArray(float a[], byte b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static float[] minusArray(float a, byte b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static float minus(float a, short b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_FLOAT : a - b;
    }

    public static float[] minusArray(float a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (float, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static float[] minusArray(float a[], short b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static float[] minusArray(float a, short b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static int minus(char a, int b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_INT ? QueryConstants.NULL_INT : a - b;
    }

    public static int[] minusArray(char a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (char, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] minusArray(char a[], int b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static int[] minusArray(char a, int b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static double minus(char a, double b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a - b;
    }

    public static double[] minusArray(char a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (char, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] minusArray(char a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static double[] minusArray(char a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static long minus(char a, long b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : a - b;
    }

    public static long[] minusArray(char a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (char, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static long[] minusArray(char a[], long b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static long[] minusArray(char a, long b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static float minus(char a, float b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a - b;
    }

    public static float[] minusArray(char a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (char, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static float[] minusArray(char a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static float[] minusArray(char a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static int minus(char a, char b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_INT : a - b;
    }

    public static int[] minusArray(char a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (char, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] minusArray(char a[], char b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static int[] minusArray(char a, char b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static int minus(char a, byte b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_INT : a - b;
    }

    public static int[] minusArray(char a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (char, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] minusArray(char a[], byte b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static int[] minusArray(char a, byte b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static int minus(char a, short b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_INT : a - b;
    }

    public static int[] minusArray(char a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (char, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] minusArray(char a[], short b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static int[] minusArray(char a, short b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static int minus(byte a, int b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_INT ? QueryConstants.NULL_INT : a - b;
    }

    public static int[] minusArray(byte a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (byte, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] minusArray(byte a[], int b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static int[] minusArray(byte a, int b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static double minus(byte a, double b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a - b;
    }

    public static double[] minusArray(byte a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (byte, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] minusArray(byte a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static double[] minusArray(byte a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static long minus(byte a, long b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : a - b;
    }

    public static long[] minusArray(byte a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (byte, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static long[] minusArray(byte a[], long b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static long[] minusArray(byte a, long b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static float minus(byte a, float b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a - b;
    }

    public static float[] minusArray(byte a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (byte, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static float[] minusArray(byte a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static float[] minusArray(byte a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static int minus(byte a, char b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_INT : a - b;
    }

    public static int[] minusArray(byte a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (byte, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] minusArray(byte a[], char b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static int[] minusArray(byte a, char b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static int minus(byte a, byte b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_INT : a - b;
    }

    public static int[] minusArray(byte a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (byte, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] minusArray(byte a[], byte b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static int[] minusArray(byte a, byte b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static int minus(byte a, short b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_INT : a - b;
    }

    public static int[] minusArray(byte a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (byte, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] minusArray(byte a[], short b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static int[] minusArray(byte a, short b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static int minus(short a, int b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_INT ? QueryConstants.NULL_INT : a - b;
    }

    public static int[] minusArray(short a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (short, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] minusArray(short a[], int b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static int[] minusArray(short a, int b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static double minus(short a, double b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a - b;
    }

    public static double[] minusArray(short a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (short, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static double[] minusArray(short a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static double[] minusArray(short a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static long minus(short a, long b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : a - b;
    }

    public static long[] minusArray(short a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (short, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static long[] minusArray(short a[], long b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static long[] minusArray(short a, long b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static float minus(short a, float b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a - b;
    }

    public static float[] minusArray(short a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (short, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static float[] minusArray(short a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static float[] minusArray(short a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static int minus(short a, char b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_INT : a - b;
    }

    public static int[] minusArray(short a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (short, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] minusArray(short a[], char b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static int[] minusArray(short a, char b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static int minus(short a, byte b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_INT : a - b;
    }

    public static int[] minusArray(short a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (short, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] minusArray(short a[], byte b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static int[] minusArray(short a, byte b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static int minus(short a, short b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_INT : a - b;
    }

    public static int[] minusArray(short a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to subtract two arrays (short, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b[i]);
        }

        return ret;
    }

    public static int[] minusArray(short a[], short b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = minus(a[i], b);
        }

        return ret;
    }

    public static int[] minusArray(short a, short b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = minus(a, b[i]);
        }

        return ret;
    }

    public static int multiply(int a, int b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_INT ? QueryConstants.NULL_INT : a * b;
    }

    public static int[] multiplyArray(int a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (int, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static int[] multiplyArray(int a[], int b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static int[] multiplyArray(int a, int b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static double multiply(int a, double b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a * b;
    }

    public static double[] multiplyArray(int a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (int, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static double[] multiplyArray(int a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static double[] multiplyArray(int a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static long multiply(int a, long b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : a * b;
    }

    public static long[] multiplyArray(int a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (int, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static long[] multiplyArray(int a[], long b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static long[] multiplyArray(int a, long b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static float multiply(int a, float b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a * b;
    }

    public static float[] multiplyArray(int a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (int, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static float[] multiplyArray(int a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static float[] multiplyArray(int a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static int multiply(int a, char b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_INT : a * b;
    }

    public static int[] multiplyArray(int a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (int, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static int[] multiplyArray(int a[], char b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static int[] multiplyArray(int a, char b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static int multiply(int a, byte b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_INT : a * b;
    }

    public static int[] multiplyArray(int a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (int, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static int[] multiplyArray(int a[], byte b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static int[] multiplyArray(int a, byte b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static int multiply(int a, short b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_INT : a * b;
    }

    public static int[] multiplyArray(int a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (int, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static int[] multiplyArray(int a[], short b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static int[] multiplyArray(int a, short b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static double multiply(double a, int b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_INT ? QueryConstants.NULL_DOUBLE : a * b;
    }

    public static double[] multiplyArray(double a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (double, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static double[] multiplyArray(double a[], int b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static double[] multiplyArray(double a, int b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static double multiply(double a, double b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a * b;
    }

    public static double[] multiplyArray(double a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (double, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static double[] multiplyArray(double a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static double[] multiplyArray(double a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static double multiply(double a, long b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_DOUBLE : a * b;
    }

    public static double[] multiplyArray(double a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (double, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static double[] multiplyArray(double a[], long b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static double[] multiplyArray(double a, long b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static double multiply(double a, float b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_DOUBLE : a * b;
    }

    public static double[] multiplyArray(double a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (double, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static double[] multiplyArray(double a[], float b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static double[] multiplyArray(double a, float b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static double multiply(double a, char b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_DOUBLE : a * b;
    }

    public static double[] multiplyArray(double a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (double, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static double[] multiplyArray(double a[], char b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static double[] multiplyArray(double a, char b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static double multiply(double a, byte b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_DOUBLE : a * b;
    }

    public static double[] multiplyArray(double a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (double, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static double[] multiplyArray(double a[], byte b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static double[] multiplyArray(double a, byte b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static double multiply(double a, short b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_DOUBLE : a * b;
    }

    public static double[] multiplyArray(double a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (double, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static double[] multiplyArray(double a[], short b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static double[] multiplyArray(double a, short b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static long multiply(long a, int b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_INT ? QueryConstants.NULL_LONG : a * b;
    }

    public static long[] multiplyArray(long a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (long, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static long[] multiplyArray(long a[], int b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static long[] multiplyArray(long a, int b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static double multiply(long a, double b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a * b;
    }

    public static double[] multiplyArray(long a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (long, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static double[] multiplyArray(long a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static double[] multiplyArray(long a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static long multiply(long a, long b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : a * b;
    }

    public static long[] multiplyArray(long a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (long, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static long[] multiplyArray(long a[], long b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static long[] multiplyArray(long a, long b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static float multiply(long a, float b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a * b;
    }

    public static float[] multiplyArray(long a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (long, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static float[] multiplyArray(long a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static float[] multiplyArray(long a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static long multiply(long a, char b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_LONG : a * b;
    }

    public static long[] multiplyArray(long a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (long, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static long[] multiplyArray(long a[], char b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static long[] multiplyArray(long a, char b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static long multiply(long a, byte b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_LONG : a * b;
    }

    public static long[] multiplyArray(long a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (long, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static long[] multiplyArray(long a[], byte b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static long[] multiplyArray(long a, byte b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static long multiply(long a, short b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_LONG : a * b;
    }

    public static long[] multiplyArray(long a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (long, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static long[] multiplyArray(long a[], short b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static long[] multiplyArray(long a, short b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static float multiply(float a, int b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_INT ? QueryConstants.NULL_FLOAT : a * b;
    }

    public static float[] multiplyArray(float a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (float, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static float[] multiplyArray(float a[], int b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static float[] multiplyArray(float a, int b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static double multiply(float a, double b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a * b;
    }

    public static double[] multiplyArray(float a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (float, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static double[] multiplyArray(float a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static double[] multiplyArray(float a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static float multiply(float a, long b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_FLOAT : a * b;
    }

    public static float[] multiplyArray(float a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (float, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static float[] multiplyArray(float a[], long b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static float[] multiplyArray(float a, long b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static float multiply(float a, float b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a * b;
    }

    public static float[] multiplyArray(float a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (float, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static float[] multiplyArray(float a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static float[] multiplyArray(float a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static float multiply(float a, char b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_FLOAT : a * b;
    }

    public static float[] multiplyArray(float a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (float, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static float[] multiplyArray(float a[], char b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static float[] multiplyArray(float a, char b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static float multiply(float a, byte b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_FLOAT : a * b;
    }

    public static float[] multiplyArray(float a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (float, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static float[] multiplyArray(float a[], byte b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static float[] multiplyArray(float a, byte b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static float multiply(float a, short b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_FLOAT : a * b;
    }

    public static float[] multiplyArray(float a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (float, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static float[] multiplyArray(float a[], short b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static float[] multiplyArray(float a, short b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static int multiply(char a, int b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_INT ? QueryConstants.NULL_INT : a * b;
    }

    public static int[] multiplyArray(char a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (char, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static int[] multiplyArray(char a[], int b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static int[] multiplyArray(char a, int b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static double multiply(char a, double b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a * b;
    }

    public static double[] multiplyArray(char a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (char, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static double[] multiplyArray(char a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static double[] multiplyArray(char a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static long multiply(char a, long b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : a * b;
    }

    public static long[] multiplyArray(char a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (char, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static long[] multiplyArray(char a[], long b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static long[] multiplyArray(char a, long b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static float multiply(char a, float b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a * b;
    }

    public static float[] multiplyArray(char a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (char, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static float[] multiplyArray(char a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static float[] multiplyArray(char a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static int multiply(char a, char b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_INT : a * b;
    }

    public static int[] multiplyArray(char a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (char, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static int[] multiplyArray(char a[], char b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static int[] multiplyArray(char a, char b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static int multiply(char a, byte b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_INT : a * b;
    }

    public static int[] multiplyArray(char a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (char, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static int[] multiplyArray(char a[], byte b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static int[] multiplyArray(char a, byte b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static int multiply(char a, short b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_INT : a * b;
    }

    public static int[] multiplyArray(char a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (char, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static int[] multiplyArray(char a[], short b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static int[] multiplyArray(char a, short b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static int multiply(byte a, int b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_INT ? QueryConstants.NULL_INT : a * b;
    }

    public static int[] multiplyArray(byte a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (byte, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static int[] multiplyArray(byte a[], int b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static int[] multiplyArray(byte a, int b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static double multiply(byte a, double b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a * b;
    }

    public static double[] multiplyArray(byte a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (byte, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static double[] multiplyArray(byte a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static double[] multiplyArray(byte a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static long multiply(byte a, long b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : a * b;
    }

    public static long[] multiplyArray(byte a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (byte, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static long[] multiplyArray(byte a[], long b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static long[] multiplyArray(byte a, long b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static float multiply(byte a, float b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a * b;
    }

    public static float[] multiplyArray(byte a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (byte, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static float[] multiplyArray(byte a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static float[] multiplyArray(byte a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static int multiply(byte a, char b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_INT : a * b;
    }

    public static int[] multiplyArray(byte a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (byte, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static int[] multiplyArray(byte a[], char b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static int[] multiplyArray(byte a, char b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static int multiply(byte a, byte b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_INT : a * b;
    }

    public static int[] multiplyArray(byte a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (byte, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static int[] multiplyArray(byte a[], byte b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static int[] multiplyArray(byte a, byte b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static int multiply(byte a, short b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_INT : a * b;
    }

    public static int[] multiplyArray(byte a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (byte, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static int[] multiplyArray(byte a[], short b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static int[] multiplyArray(byte a, short b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static int multiply(short a, int b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_INT ? QueryConstants.NULL_INT : a * b;
    }

    public static int[] multiplyArray(short a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (short, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static int[] multiplyArray(short a[], int b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static int[] multiplyArray(short a, int b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static double multiply(short a, double b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a * b;
    }

    public static double[] multiplyArray(short a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (short, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static double[] multiplyArray(short a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static double[] multiplyArray(short a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static long multiply(short a, long b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : a * b;
    }

    public static long[] multiplyArray(short a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (short, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static long[] multiplyArray(short a[], long b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static long[] multiplyArray(short a, long b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static float multiply(short a, float b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a * b;
    }

    public static float[] multiplyArray(short a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (short, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static float[] multiplyArray(short a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static float[] multiplyArray(short a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static int multiply(short a, char b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_INT : a * b;
    }

    public static int[] multiplyArray(short a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (short, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static int[] multiplyArray(short a[], char b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static int[] multiplyArray(short a, char b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static int multiply(short a, byte b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_INT : a * b;
    }

    public static int[] multiplyArray(short a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (short, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static int[] multiplyArray(short a[], byte b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static int[] multiplyArray(short a, byte b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static int multiply(short a, short b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_INT : a * b;
    }

    public static int[] multiplyArray(short a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to multiply two arrays (short, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b[i]);
        }

        return ret;
    }

    public static int[] multiplyArray(short a[], short b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = multiply(a[i], b);
        }

        return ret;
    }

    public static int[] multiplyArray(short a, short b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = multiply(a, b[i]);
        }

        return ret;
    }

    public static double divide(int a, int b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_INT ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(int a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (int, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(int a[], int b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(int a, int b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(int a, double b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a / b;
    }

    public static double[] divideArray(int a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (int, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(int a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(int a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(int a, long b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(int a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (int, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(int a[], long b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(int a, long b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static float divide(int a, float b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a / b;
    }

    public static float[] divideArray(int a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (int, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static float[] divideArray(int a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static float[] divideArray(int a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(int a, char b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(int a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (int, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(int a[], char b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(int a, char b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(int a, byte b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(int a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (int, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(int a[], byte b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(int a, byte b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(int a, short b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(int a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (int, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(int a[], short b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(int a, short b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(double a, int b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_INT ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(double a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (double, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(double a[], int b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(double a, int b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(double a, double b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a / b;
    }

    public static double[] divideArray(double a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (double, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(double a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(double a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(double a, long b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(double a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (double, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(double a[], long b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(double a, long b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(double a, float b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_DOUBLE : a / b;
    }

    public static double[] divideArray(double a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (double, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(double a[], float b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(double a, float b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(double a, char b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(double a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (double, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(double a[], char b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(double a, char b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(double a, byte b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(double a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (double, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(double a[], byte b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(double a, byte b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(double a, short b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(double a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (double, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(double a[], short b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(double a, short b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(long a, int b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_INT ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(long a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (long, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(long a[], int b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(long a, int b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(long a, double b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a / b;
    }

    public static double[] divideArray(long a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (long, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(long a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(long a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(long a, long b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(long a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (long, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(long a[], long b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(long a, long b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static float divide(long a, float b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a / b;
    }

    public static float[] divideArray(long a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (long, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static float[] divideArray(long a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static float[] divideArray(long a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(long a, char b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(long a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (long, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(long a[], char b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(long a, char b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(long a, byte b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(long a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (long, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(long a[], byte b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(long a, byte b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(long a, short b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(long a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (long, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(long a[], short b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(long a, short b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(float a, int b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_INT ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(float a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (float, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(float a[], int b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(float a, int b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(float a, double b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a / b;
    }

    public static double[] divideArray(float a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (float, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(float a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(float a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(float a, long b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(float a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (float, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(float a[], long b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(float a, long b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static float divide(float a, float b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a / b;
    }

    public static float[] divideArray(float a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (float, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static float[] divideArray(float a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static float[] divideArray(float a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(float a, char b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(float a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (float, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(float a[], char b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(float a, char b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(float a, byte b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(float a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (float, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(float a[], byte b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(float a, byte b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(float a, short b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(float a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (float, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(float a[], short b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(float a, short b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(char a, int b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_INT ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(char a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (char, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(char a[], int b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(char a, int b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(char a, double b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a / b;
    }

    public static double[] divideArray(char a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (char, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(char a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(char a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(char a, long b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(char a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (char, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(char a[], long b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(char a, long b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static float divide(char a, float b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a / b;
    }

    public static float[] divideArray(char a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (char, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static float[] divideArray(char a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static float[] divideArray(char a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(char a, char b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(char a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (char, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(char a[], char b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(char a, char b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(char a, byte b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(char a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (char, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(char a[], byte b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(char a, byte b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(char a, short b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(char a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (char, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(char a[], short b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(char a, short b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(byte a, int b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_INT ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(byte a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (byte, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(byte a[], int b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(byte a, int b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(byte a, double b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a / b;
    }

    public static double[] divideArray(byte a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (byte, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(byte a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(byte a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(byte a, long b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(byte a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (byte, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(byte a[], long b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(byte a, long b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static float divide(byte a, float b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a / b;
    }

    public static float[] divideArray(byte a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (byte, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static float[] divideArray(byte a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static float[] divideArray(byte a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(byte a, char b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(byte a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (byte, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(byte a[], char b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(byte a, char b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(byte a, byte b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(byte a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (byte, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(byte a[], byte b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(byte a, byte b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(byte a, short b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(byte a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (byte, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(byte a[], short b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(byte a, short b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(short a, int b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_INT ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(short a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (short, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(short a[], int b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(short a, int b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(short a, double b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a / b;
    }

    public static double[] divideArray(short a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (short, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(short a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(short a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(short a, long b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(short a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (short, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(short a[], long b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(short a, long b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static float divide(short a, float b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a / b;
    }

    public static float[] divideArray(short a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (short, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static float[] divideArray(short a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static float[] divideArray(short a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(short a, char b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(short a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (short, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(short a[], char b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(short a, char b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(short a, byte b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(short a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (short, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(short a[], byte b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(short a, byte b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static double divide(short a, short b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_DOUBLE
                : a / (double) b;
    }

    public static double[] divideArray(short a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to divide two arrays (short, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b[i]);
        }

        return ret;
    }

    public static double[] divideArray(short a[], short b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = divide(a[i], b);
        }

        return ret;
    }

    public static double[] divideArray(short a, short b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = divide(a, b[i]);
        }

        return ret;
    }

    public static int remainder(int a, int b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_INT ? QueryConstants.NULL_INT : a % b;
    }

    public static int[] remainderArray(int a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (int, int) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static int[] remainderArray(int a[], int b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static int[] remainderArray(int a, int b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static double remainder(int a, double b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a % b;
    }

    public static double[] remainderArray(int a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (int, double) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static double[] remainderArray(int a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static double[] remainderArray(int a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static long remainder(int a, long b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : a % b;
    }

    public static long[] remainderArray(int a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (int, long) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static long[] remainderArray(int a[], long b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static long[] remainderArray(int a, long b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static float remainder(int a, float b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a % b;
    }

    public static float[] remainderArray(int a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (int, float) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static float[] remainderArray(int a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static float[] remainderArray(int a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static int remainder(int a, char b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_INT : a % b;
    }

    public static int[] remainderArray(int a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (int, char) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static int[] remainderArray(int a[], char b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static int[] remainderArray(int a, char b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static int remainder(int a, byte b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_INT : a % b;
    }

    public static int[] remainderArray(int a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (int, byte) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static int[] remainderArray(int a[], byte b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static int[] remainderArray(int a, byte b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static int remainder(int a, short b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_INT : a % b;
    }

    public static int[] remainderArray(int a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (int, short) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static int[] remainderArray(int a[], short b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static int[] remainderArray(int a, short b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static double remainder(double a, int b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_INT ? QueryConstants.NULL_DOUBLE : a % b;
    }

    public static double[] remainderArray(double a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (double, int) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static double[] remainderArray(double a[], int b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static double[] remainderArray(double a, int b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static double remainder(double a, double b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a % b;
    }

    public static double[] remainderArray(double a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (double, double) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static double[] remainderArray(double a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static double[] remainderArray(double a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static double remainder(double a, long b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_DOUBLE : a % b;
    }

    public static double[] remainderArray(double a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (double, long) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static double[] remainderArray(double a[], long b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static double[] remainderArray(double a, long b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static double remainder(double a, float b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_DOUBLE : a % b;
    }

    public static double[] remainderArray(double a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (double, float) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static double[] remainderArray(double a[], float b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static double[] remainderArray(double a, float b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static double remainder(double a, char b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_DOUBLE : a % b;
    }

    public static double[] remainderArray(double a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (double, char) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static double[] remainderArray(double a[], char b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static double[] remainderArray(double a, char b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static double remainder(double a, byte b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_DOUBLE : a % b;
    }

    public static double[] remainderArray(double a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (double, byte) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static double[] remainderArray(double a[], byte b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static double[] remainderArray(double a, byte b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static double remainder(double a, short b) {
        return a == QueryConstants.NULL_DOUBLE || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_DOUBLE : a % b;
    }

    public static double[] remainderArray(double a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (double, short) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static double[] remainderArray(double a[], short b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static double[] remainderArray(double a, short b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static long remainder(long a, int b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_INT ? QueryConstants.NULL_LONG : a % b;
    }

    public static long[] remainderArray(long a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (long, int) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static long[] remainderArray(long a[], int b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static long[] remainderArray(long a, int b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static double remainder(long a, double b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a % b;
    }

    public static double[] remainderArray(long a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (long, double) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static double[] remainderArray(long a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static double[] remainderArray(long a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static long remainder(long a, long b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : a % b;
    }

    public static long[] remainderArray(long a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (long, long) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static long[] remainderArray(long a[], long b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static long[] remainderArray(long a, long b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static float remainder(long a, float b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a % b;
    }

    public static float[] remainderArray(long a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (long, float) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static float[] remainderArray(long a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static float[] remainderArray(long a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static long remainder(long a, char b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_LONG : a % b;
    }

    public static long[] remainderArray(long a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (long, char) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static long[] remainderArray(long a[], char b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static long[] remainderArray(long a, char b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static long remainder(long a, byte b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_LONG : a % b;
    }

    public static long[] remainderArray(long a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (long, byte) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static long[] remainderArray(long a[], byte b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static long[] remainderArray(long a, byte b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static long remainder(long a, short b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_LONG : a % b;
    }

    public static long[] remainderArray(long a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (long, short) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static long[] remainderArray(long a[], short b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static long[] remainderArray(long a, short b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static float remainder(float a, int b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_INT ? QueryConstants.NULL_FLOAT : a % b;
    }

    public static float[] remainderArray(float a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (float, int) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static float[] remainderArray(float a[], int b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static float[] remainderArray(float a, int b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static double remainder(float a, double b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a % b;
    }

    public static double[] remainderArray(float a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (float, double) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static double[] remainderArray(float a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static double[] remainderArray(float a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static float remainder(float a, long b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_FLOAT : a % b;
    }

    public static float[] remainderArray(float a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (float, long) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static float[] remainderArray(float a[], long b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static float[] remainderArray(float a, long b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static float remainder(float a, float b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a % b;
    }

    public static float[] remainderArray(float a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (float, float) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static float[] remainderArray(float a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static float[] remainderArray(float a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static float remainder(float a, char b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_FLOAT : a % b;
    }

    public static float[] remainderArray(float a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (float, char) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static float[] remainderArray(float a[], char b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static float[] remainderArray(float a, char b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static float remainder(float a, byte b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_FLOAT : a % b;
    }

    public static float[] remainderArray(float a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (float, byte) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static float[] remainderArray(float a[], byte b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static float[] remainderArray(float a, byte b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static float remainder(float a, short b) {
        return a == QueryConstants.NULL_FLOAT || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_FLOAT : a % b;
    }

    public static float[] remainderArray(float a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (float, short) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static float[] remainderArray(float a[], short b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static float[] remainderArray(float a, short b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static int remainder(char a, int b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_INT ? QueryConstants.NULL_INT : a % b;
    }

    public static int[] remainderArray(char a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (char, int) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static int[] remainderArray(char a[], int b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static int[] remainderArray(char a, int b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static double remainder(char a, double b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a % b;
    }

    public static double[] remainderArray(char a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (char, double) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static double[] remainderArray(char a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static double[] remainderArray(char a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static long remainder(char a, long b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : a % b;
    }

    public static long[] remainderArray(char a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (char, long) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static long[] remainderArray(char a[], long b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static long[] remainderArray(char a, long b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static float remainder(char a, float b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a % b;
    }

    public static float[] remainderArray(char a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (char, float) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static float[] remainderArray(char a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static float[] remainderArray(char a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static int remainder(char a, char b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_INT : a % b;
    }

    public static int[] remainderArray(char a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (char, char) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static int[] remainderArray(char a[], char b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static int[] remainderArray(char a, char b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static int remainder(char a, byte b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_INT : a % b;
    }

    public static int[] remainderArray(char a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (char, byte) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static int[] remainderArray(char a[], byte b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static int[] remainderArray(char a, byte b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static int remainder(char a, short b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_INT : a % b;
    }

    public static int[] remainderArray(char a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (char, short) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static int[] remainderArray(char a[], short b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static int[] remainderArray(char a, short b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static int remainder(byte a, int b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_INT ? QueryConstants.NULL_INT : a % b;
    }

    public static int[] remainderArray(byte a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (byte, int) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static int[] remainderArray(byte a[], int b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static int[] remainderArray(byte a, int b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static double remainder(byte a, double b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a % b;
    }

    public static double[] remainderArray(byte a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (byte, double) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static double[] remainderArray(byte a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static double[] remainderArray(byte a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static long remainder(byte a, long b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : a % b;
    }

    public static long[] remainderArray(byte a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (byte, long) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static long[] remainderArray(byte a[], long b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static long[] remainderArray(byte a, long b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static float remainder(byte a, float b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a % b;
    }

    public static float[] remainderArray(byte a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (byte, float) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static float[] remainderArray(byte a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static float[] remainderArray(byte a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static int remainder(byte a, char b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_INT : a % b;
    }

    public static int[] remainderArray(byte a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (byte, char) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static int[] remainderArray(byte a[], char b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static int[] remainderArray(byte a, char b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static int remainder(byte a, byte b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_INT : a % b;
    }

    public static int[] remainderArray(byte a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (byte, byte) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static int[] remainderArray(byte a[], byte b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static int[] remainderArray(byte a, byte b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static int remainder(byte a, short b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_INT : a % b;
    }

    public static int[] remainderArray(byte a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (byte, short) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static int[] remainderArray(byte a[], short b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static int[] remainderArray(byte a, short b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static int remainder(short a, int b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_INT ? QueryConstants.NULL_INT : a % b;
    }

    public static int[] remainderArray(short a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (short, int) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static int[] remainderArray(short a[], int b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static int[] remainderArray(short a, int b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static double remainder(short a, double b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : a % b;
    }

    public static double[] remainderArray(short a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (short, double) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static double[] remainderArray(short a[], double b) {
        double[] ret = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static double[] remainderArray(short a, double b[]) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static long remainder(short a, long b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : a % b;
    }

    public static long[] remainderArray(short a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (short, long) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static long[] remainderArray(short a[], long b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static long[] remainderArray(short a, long b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static float remainder(short a, float b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : a % b;
    }

    public static float[] remainderArray(short a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (short, float) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static float[] remainderArray(short a[], float b) {
        float[] ret = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static float[] remainderArray(short a, float b[]) {
        float[] ret = new float[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static int remainder(short a, char b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_INT : a % b;
    }

    public static int[] remainderArray(short a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (short, char) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static int[] remainderArray(short a[], char b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static int[] remainderArray(short a, char b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static int remainder(short a, byte b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_INT : a % b;
    }

    public static int[] remainderArray(short a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (short, byte) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static int[] remainderArray(short a[], byte b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static int[] remainderArray(short a, byte b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static int remainder(short a, short b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_INT : a % b;
    }

    public static int[] remainderArray(short a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to calculate remainder of two arrays (short, short) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b[i]);
        }

        return ret;
    }

    public static int[] remainderArray(short a[], short b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = remainder(a[i], b);
        }

        return ret;
    }

    public static int[] remainderArray(short a, short b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = remainder(a, b[i]);
        }

        return ret;
    }

    public static int binaryOr(int a, int b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_INT ? QueryConstants.NULL_INT : a | b;
    }

    public static int[] binaryOrArray(int a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to BINARY_OR two arrays (int, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = binaryOr(a[i], b[i]);
        }

        return ret;
    }

    public static int[] binaryOrArray(int a[], int b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = binaryOr(a[i], b);
        }

        return ret;
    }

    public static int[] binaryOrArray(int a, int b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = binaryOr(a, b[i]);
        }

        return ret;
    }

    public static long binaryOr(long a, long b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : a | b;
    }

    public static long[] binaryOrArray(long a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to BINARY_OR two arrays (long, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = binaryOr(a[i], b[i]);
        }

        return ret;
    }

    public static long[] binaryOrArray(long a[], long b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = binaryOr(a[i], b);
        }

        return ret;
    }

    public static long[] binaryOrArray(long a, long b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = binaryOr(a, b[i]);
        }

        return ret;
    }

    public static int binaryOr(char a, char b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_INT : a | b;
    }

    public static int[] binaryOrArray(char a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to BINARY_OR two arrays (char, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = binaryOr(a[i], b[i]);
        }

        return ret;
    }

    public static int[] binaryOrArray(char a[], char b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = binaryOr(a[i], b);
        }

        return ret;
    }

    public static int[] binaryOrArray(char a, char b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = binaryOr(a, b[i]);
        }

        return ret;
    }

    public static int binaryOr(byte a, byte b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_INT : a | b;
    }

    public static int[] binaryOrArray(byte a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to BINARY_OR two arrays (byte, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = binaryOr(a[i], b[i]);
        }

        return ret;
    }

    public static int[] binaryOrArray(byte a[], byte b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = binaryOr(a[i], b);
        }

        return ret;
    }

    public static int[] binaryOrArray(byte a, byte b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = binaryOr(a, b[i]);
        }

        return ret;
    }

    public static int binaryOr(short a, short b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_INT : a | b;
    }

    public static int[] binaryOrArray(short a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to BINARY_OR two arrays (short, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = binaryOr(a[i], b[i]);
        }

        return ret;
    }

    public static int[] binaryOrArray(short a[], short b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = binaryOr(a[i], b);
        }

        return ret;
    }

    public static int[] binaryOrArray(short a, short b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = binaryOr(a, b[i]);
        }

        return ret;
    }

    public static int xor(int a, int b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_INT ? QueryConstants.NULL_INT : a ^ b;
    }

    public static int[] xorArray(int a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to XOR two arrays (int, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = xor(a[i], b[i]);
        }

        return ret;
    }

    public static int[] xorArray(int a[], int b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = xor(a[i], b);
        }

        return ret;
    }

    public static int[] xorArray(int a, int b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = xor(a, b[i]);
        }

        return ret;
    }

    public static long xor(long a, long b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : a ^ b;
    }

    public static long[] xorArray(long a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to XOR two arrays (long, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = xor(a[i], b[i]);
        }

        return ret;
    }

    public static long[] xorArray(long a[], long b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = xor(a[i], b);
        }

        return ret;
    }

    public static long[] xorArray(long a, long b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = xor(a, b[i]);
        }

        return ret;
    }

    public static int xor(char a, char b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_INT : a ^ b;
    }

    public static int[] xorArray(char a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to XOR two arrays (char, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = xor(a[i], b[i]);
        }

        return ret;
    }

    public static int[] xorArray(char a[], char b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = xor(a[i], b);
        }

        return ret;
    }

    public static int[] xorArray(char a, char b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = xor(a, b[i]);
        }

        return ret;
    }

    public static int xor(byte a, byte b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_INT : a ^ b;
    }

    public static int[] xorArray(byte a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to XOR two arrays (byte, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = xor(a[i], b[i]);
        }

        return ret;
    }

    public static int[] xorArray(byte a[], byte b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = xor(a[i], b);
        }

        return ret;
    }

    public static int[] xorArray(byte a, byte b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = xor(a, b[i]);
        }

        return ret;
    }

    public static int xor(short a, short b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_INT : a ^ b;
    }

    public static int[] xorArray(short a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to XOR two arrays (short, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = xor(a[i], b[i]);
        }

        return ret;
    }

    public static int[] xorArray(short a[], short b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = xor(a[i], b);
        }

        return ret;
    }

    public static int[] xorArray(short a, short b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = xor(a, b[i]);
        }

        return ret;
    }

    public static int binaryAnd(int a, int b) {
        return a == QueryConstants.NULL_INT || b == QueryConstants.NULL_INT ? QueryConstants.NULL_INT : a & b;
    }

    public static int[] binaryAndArray(int a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to BINARY_AND two arrays (int, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = binaryAnd(a[i], b[i]);
        }

        return ret;
    }

    public static int[] binaryAndArray(int a[], int b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = binaryAnd(a[i], b);
        }

        return ret;
    }

    public static int[] binaryAndArray(int a, int b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = binaryAnd(a, b[i]);
        }

        return ret;
    }

    public static long binaryAnd(long a, long b) {
        return a == QueryConstants.NULL_LONG || b == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : a & b;
    }

    public static long[] binaryAndArray(long a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to BINARY_AND two arrays (long, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = binaryAnd(a[i], b[i]);
        }

        return ret;
    }

    public static long[] binaryAndArray(long a[], long b) {
        long[] ret = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = binaryAnd(a[i], b);
        }

        return ret;
    }

    public static long[] binaryAndArray(long a, long b[]) {
        long[] ret = new long[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = binaryAnd(a, b[i]);
        }

        return ret;
    }

    public static int binaryAnd(char a, char b) {
        return a == QueryConstants.NULL_CHAR || b == QueryConstants.NULL_CHAR ? QueryConstants.NULL_INT : a & b;
    }

    public static int[] binaryAndArray(char a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to BINARY_AND two arrays (char, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = binaryAnd(a[i], b[i]);
        }

        return ret;
    }

    public static int[] binaryAndArray(char a[], char b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = binaryAnd(a[i], b);
        }

        return ret;
    }

    public static int[] binaryAndArray(char a, char b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = binaryAnd(a, b[i]);
        }

        return ret;
    }

    public static int binaryAnd(byte a, byte b) {
        return a == QueryConstants.NULL_BYTE || b == QueryConstants.NULL_BYTE ? QueryConstants.NULL_INT : a & b;
    }

    public static int[] binaryAndArray(byte a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to BINARY_AND two arrays (byte, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = binaryAnd(a[i], b[i]);
        }

        return ret;
    }

    public static int[] binaryAndArray(byte a[], byte b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = binaryAnd(a[i], b);
        }

        return ret;
    }

    public static int[] binaryAndArray(byte a, byte b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = binaryAnd(a, b[i]);
        }

        return ret;
    }

    public static int binaryAnd(short a, short b) {
        return a == QueryConstants.NULL_SHORT || b == QueryConstants.NULL_SHORT ? QueryConstants.NULL_INT : a & b;
    }

    public static int[] binaryAndArray(short a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to BINARY_AND two arrays (short, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = binaryAnd(a[i], b[i]);
        }

        return ret;
    }

    public static int[] binaryAndArray(short a[], short b) {
        int[] ret = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = binaryAnd(a[i], b);
        }

        return ret;
    }

    public static int[] binaryAndArray(short a, short b[]) {
        int[] ret = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = binaryAnd(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(int a, int b) {
        if (a == QueryConstants.NULL_INT) {
            return (b == QueryConstants.NULL_INT) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_INT) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(int a, int b) {
        if (a == QueryConstants.NULL_INT) {
            return (b == QueryConstants.NULL_INT);
        }

        if (b == QueryConstants.NULL_INT) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(int a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (int, int) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(int a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(int a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(int a, double b) {
        if (a == QueryConstants.NULL_INT) {
            return (b == QueryConstants.NULL_DOUBLE) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_DOUBLE) {
            return 1;
        }

        return Double.compare(a, b);
    }

    public static boolean eq(int a, double b) {
        if (a == QueryConstants.NULL_INT) {
            return (b == QueryConstants.NULL_DOUBLE);
        }

        if (b == QueryConstants.NULL_DOUBLE) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(int a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (int, double) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(int a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(int a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(int a, long b) {
        if (a == QueryConstants.NULL_INT) {
            return (b == QueryConstants.NULL_LONG) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_LONG) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(int a, long b) {
        if (a == QueryConstants.NULL_INT) {
            return (b == QueryConstants.NULL_LONG);
        }

        if (b == QueryConstants.NULL_LONG) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(int a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (int, long) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(int a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(int a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(int a, float b) {
        if (a == QueryConstants.NULL_INT) {
            return (b == QueryConstants.NULL_FLOAT) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_FLOAT) {
            return 1;
        }

        return Double.compare(a, b);
    }

    public static boolean eq(int a, float b) {
        if (a == QueryConstants.NULL_INT) {
            return (b == QueryConstants.NULL_FLOAT);
        }

        if (b == QueryConstants.NULL_FLOAT) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(int a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (int, float) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(int a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(int a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(int a, char b) {
        if (a == QueryConstants.NULL_INT) {
            return (b == QueryConstants.NULL_CHAR) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_CHAR) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(int a, char b) {
        if (a == QueryConstants.NULL_INT) {
            return (b == QueryConstants.NULL_CHAR);
        }

        if (b == QueryConstants.NULL_CHAR) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(int a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (int, char) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(int a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(int a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(int a, byte b) {
        if (a == QueryConstants.NULL_INT) {
            return (b == QueryConstants.NULL_BYTE) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_BYTE) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(int a, byte b) {
        if (a == QueryConstants.NULL_INT) {
            return (b == QueryConstants.NULL_BYTE);
        }

        if (b == QueryConstants.NULL_BYTE) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(int a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (int, byte) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(int a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(int a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(int a, short b) {
        if (a == QueryConstants.NULL_INT) {
            return (b == QueryConstants.NULL_SHORT) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_SHORT) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(int a, short b) {
        if (a == QueryConstants.NULL_INT) {
            return (b == QueryConstants.NULL_SHORT);
        }

        if (b == QueryConstants.NULL_SHORT) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(int a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (int, short) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(int a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(int a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(double a, int b) {
        if (a == QueryConstants.NULL_DOUBLE) {
            return (b == QueryConstants.NULL_INT) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_INT) {
            return 1;
        }

        return Double.compare(a, b);
    }

    public static boolean eq(double a, int b) {
        if (a == QueryConstants.NULL_DOUBLE) {
            return (b == QueryConstants.NULL_INT);
        }

        if (b == QueryConstants.NULL_INT) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(double a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (double, int) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(double a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(double a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(double a, double b) {
        if (a == QueryConstants.NULL_DOUBLE) {
            return (b == QueryConstants.NULL_DOUBLE) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_DOUBLE) {
            return 1;
        }

        return Double.compare(a, b);
    }

    public static boolean eq(double a, double b) {
        if (a == QueryConstants.NULL_DOUBLE) {
            return (b == QueryConstants.NULL_DOUBLE);
        }

        if (b == QueryConstants.NULL_DOUBLE) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(double a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (double, double) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(double a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(double a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(double a, long b) {
        return -compareTo(b, a);
    }

    public static boolean eq(double a, long b) {
        if (a == QueryConstants.NULL_DOUBLE) {
            return (b == QueryConstants.NULL_LONG);
        }

        if (b == QueryConstants.NULL_LONG) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(double a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (double, long) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(double a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(double a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(double a, float b) {
        if (a == QueryConstants.NULL_DOUBLE) {
            return (b == QueryConstants.NULL_FLOAT) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_FLOAT) {
            return 1;
        }

        return Double.compare(a, b);
    }

    public static boolean eq(double a, float b) {
        if (a == QueryConstants.NULL_DOUBLE) {
            return (b == QueryConstants.NULL_FLOAT);
        }

        if (b == QueryConstants.NULL_FLOAT) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(double a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (double, float) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(double a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(double a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(double a, char b) {
        if (a == QueryConstants.NULL_DOUBLE) {
            return (b == QueryConstants.NULL_CHAR) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_CHAR) {
            return 1;
        }

        return Double.compare(a, b);
    }

    public static boolean eq(double a, char b) {
        if (a == QueryConstants.NULL_DOUBLE) {
            return (b == QueryConstants.NULL_CHAR);
        }

        if (b == QueryConstants.NULL_CHAR) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(double a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (double, char) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(double a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(double a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(double a, byte b) {
        if (a == QueryConstants.NULL_DOUBLE) {
            return (b == QueryConstants.NULL_BYTE) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_BYTE) {
            return 1;
        }

        return Double.compare(a, b);
    }

    public static boolean eq(double a, byte b) {
        if (a == QueryConstants.NULL_DOUBLE) {
            return (b == QueryConstants.NULL_BYTE);
        }

        if (b == QueryConstants.NULL_BYTE) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(double a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (double, byte) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(double a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(double a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(double a, short b) {
        if (a == QueryConstants.NULL_DOUBLE) {
            return (b == QueryConstants.NULL_SHORT) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_SHORT) {
            return 1;
        }

        return Double.compare(a, b);
    }

    public static boolean eq(double a, short b) {
        if (a == QueryConstants.NULL_DOUBLE) {
            return (b == QueryConstants.NULL_SHORT);
        }

        if (b == QueryConstants.NULL_SHORT) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(double a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (double, short) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(double a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(double a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(long a, int b) {
        if (a == QueryConstants.NULL_LONG) {
            return (b == QueryConstants.NULL_INT) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_INT) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(long a, int b) {
        if (a == QueryConstants.NULL_LONG) {
            return (b == QueryConstants.NULL_INT);
        }

        if (b == QueryConstants.NULL_INT) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(long a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (long, int) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(long a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(long a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(long a, double b) {
        if (a == QueryConstants.NULL_LONG) {
            return (b == QueryConstants.NULL_DOUBLE) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_DOUBLE) {
            return 1;
        }

        if (Double.isNaN(b)) {
            return -1;
        }
        if (b > Long.MAX_VALUE) {
            return -1;
        } else if (b < Long.MIN_VALUE) {
            return 1;
        } else {
            final long longValue = (long) b;
            if (longValue > a) {
                return -1;
            } else if (longValue == a) {
                if (b - longValue == 0d) {
                    return 0;
                } else if (b - longValue > 0d) {
                    return -1;
                }
            }
            return 1;
        }
    }

    public static boolean eq(long a, double b) {
        if (a == QueryConstants.NULL_LONG) {
            return (b == QueryConstants.NULL_DOUBLE);
        }

        if (b == QueryConstants.NULL_DOUBLE) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(long a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (long, double) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(long a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(long a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(long a, long b) {
        if (a == QueryConstants.NULL_LONG) {
            return (b == QueryConstants.NULL_LONG) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_LONG) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(long a, long b) {
        if (a == QueryConstants.NULL_LONG) {
            return (b == QueryConstants.NULL_LONG);
        }

        if (b == QueryConstants.NULL_LONG) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(long a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (long, long) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(long a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(long a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(long a, float b) {
        if (a == QueryConstants.NULL_LONG) {
            return (b == QueryConstants.NULL_FLOAT) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_FLOAT) {
            return 1;
        }

        if (Double.isNaN(b)) {
            return -1;
        }
        if (b > Long.MAX_VALUE) {
            return -1;
        } else if (b < Long.MIN_VALUE) {
            return 1;
        } else {
            final long longValue = (long) b;
            if (longValue > a) {
                return -1;
            } else if (longValue == a) {
                if (b - longValue == 0d) {
                    return 0;
                } else if (b - longValue > 0d) {
                    return -1;
                }
            }
            return 1;
        }
    }

    public static boolean eq(long a, float b) {
        if (a == QueryConstants.NULL_LONG) {
            return (b == QueryConstants.NULL_FLOAT);
        }

        if (b == QueryConstants.NULL_FLOAT) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(long a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (long, float) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(long a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(long a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(long a, char b) {
        if (a == QueryConstants.NULL_LONG) {
            return (b == QueryConstants.NULL_CHAR) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_CHAR) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(long a, char b) {
        if (a == QueryConstants.NULL_LONG) {
            return (b == QueryConstants.NULL_CHAR);
        }

        if (b == QueryConstants.NULL_CHAR) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(long a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (long, char) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(long a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(long a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(long a, byte b) {
        if (a == QueryConstants.NULL_LONG) {
            return (b == QueryConstants.NULL_BYTE) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_BYTE) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(long a, byte b) {
        if (a == QueryConstants.NULL_LONG) {
            return (b == QueryConstants.NULL_BYTE);
        }

        if (b == QueryConstants.NULL_BYTE) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(long a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (long, byte) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(long a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(long a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(long a, short b) {
        if (a == QueryConstants.NULL_LONG) {
            return (b == QueryConstants.NULL_SHORT) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_SHORT) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(long a, short b) {
        if (a == QueryConstants.NULL_LONG) {
            return (b == QueryConstants.NULL_SHORT);
        }

        if (b == QueryConstants.NULL_SHORT) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(long a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (long, short) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(long a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(long a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(float a, int b) {
        if (a == QueryConstants.NULL_FLOAT) {
            return (b == QueryConstants.NULL_INT) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_INT) {
            return 1;
        }

        return Double.compare(a, b);
    }

    public static boolean eq(float a, int b) {
        if (a == QueryConstants.NULL_FLOAT) {
            return (b == QueryConstants.NULL_INT);
        }

        if (b == QueryConstants.NULL_INT) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(float a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (float, int) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(float a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(float a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(float a, double b) {
        if (a == QueryConstants.NULL_FLOAT) {
            return (b == QueryConstants.NULL_DOUBLE) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_DOUBLE) {
            return 1;
        }

        return Double.compare(a, b);
    }

    public static boolean eq(float a, double b) {
        if (a == QueryConstants.NULL_FLOAT) {
            return (b == QueryConstants.NULL_DOUBLE);
        }

        if (b == QueryConstants.NULL_DOUBLE) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(float a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (float, double) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(float a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(float a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(float a, long b) {
        return -compareTo(b, a);
    }

    public static boolean eq(float a, long b) {
        if (a == QueryConstants.NULL_FLOAT) {
            return (b == QueryConstants.NULL_LONG);
        }

        if (b == QueryConstants.NULL_LONG) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(float a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (float, long) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(float a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(float a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(float a, float b) {
        if (a == QueryConstants.NULL_FLOAT) {
            return (b == QueryConstants.NULL_FLOAT) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_FLOAT) {
            return 1;
        }

        return Float.compare(a, b);
    }

    public static boolean eq(float a, float b) {
        if (a == QueryConstants.NULL_FLOAT) {
            return (b == QueryConstants.NULL_FLOAT);
        }

        if (b == QueryConstants.NULL_FLOAT) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(float a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (float, float) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(float a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(float a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(float a, char b) {
        if (a == QueryConstants.NULL_FLOAT) {
            return (b == QueryConstants.NULL_CHAR) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_CHAR) {
            return 1;
        }

        return Double.compare(a, b);
    }

    public static boolean eq(float a, char b) {
        if (a == QueryConstants.NULL_FLOAT) {
            return (b == QueryConstants.NULL_CHAR);
        }

        if (b == QueryConstants.NULL_CHAR) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(float a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (float, char) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(float a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(float a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(float a, byte b) {
        if (a == QueryConstants.NULL_FLOAT) {
            return (b == QueryConstants.NULL_BYTE) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_BYTE) {
            return 1;
        }

        return Double.compare(a, b);
    }

    public static boolean eq(float a, byte b) {
        if (a == QueryConstants.NULL_FLOAT) {
            return (b == QueryConstants.NULL_BYTE);
        }

        if (b == QueryConstants.NULL_BYTE) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(float a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (float, byte) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(float a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(float a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(float a, short b) {
        if (a == QueryConstants.NULL_FLOAT) {
            return (b == QueryConstants.NULL_SHORT) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_SHORT) {
            return 1;
        }

        return Double.compare(a, b);
    }

    public static boolean eq(float a, short b) {
        if (a == QueryConstants.NULL_FLOAT) {
            return (b == QueryConstants.NULL_SHORT);
        }

        if (b == QueryConstants.NULL_SHORT) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(float a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (float, short) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(float a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(float a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(char a, int b) {
        if (a == QueryConstants.NULL_CHAR) {
            return (b == QueryConstants.NULL_INT) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_INT) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(char a, int b) {
        if (a == QueryConstants.NULL_CHAR) {
            return (b == QueryConstants.NULL_INT);
        }

        if (b == QueryConstants.NULL_INT) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(char a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (char, int) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(char a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(char a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(char a, double b) {
        if (a == QueryConstants.NULL_CHAR) {
            return (b == QueryConstants.NULL_DOUBLE) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_DOUBLE) {
            return 1;
        }

        return Double.compare(a, b);
    }

    public static boolean eq(char a, double b) {
        if (a == QueryConstants.NULL_CHAR) {
            return (b == QueryConstants.NULL_DOUBLE);
        }

        if (b == QueryConstants.NULL_DOUBLE) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(char a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (char, double) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(char a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(char a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(char a, long b) {
        if (a == QueryConstants.NULL_CHAR) {
            return (b == QueryConstants.NULL_LONG) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_LONG) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(char a, long b) {
        if (a == QueryConstants.NULL_CHAR) {
            return (b == QueryConstants.NULL_LONG);
        }

        if (b == QueryConstants.NULL_LONG) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(char a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (char, long) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(char a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(char a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(char a, float b) {
        if (a == QueryConstants.NULL_CHAR) {
            return (b == QueryConstants.NULL_FLOAT) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_FLOAT) {
            return 1;
        }

        return Double.compare(a, b);
    }

    public static boolean eq(char a, float b) {
        if (a == QueryConstants.NULL_CHAR) {
            return (b == QueryConstants.NULL_FLOAT);
        }

        if (b == QueryConstants.NULL_FLOAT) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(char a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (char, float) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(char a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(char a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(char a, char b) {
        if (a == QueryConstants.NULL_CHAR) {
            return (b == QueryConstants.NULL_CHAR) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_CHAR) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(char a, char b) {
        if (a == QueryConstants.NULL_CHAR) {
            return (b == QueryConstants.NULL_CHAR);
        }

        if (b == QueryConstants.NULL_CHAR) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(char a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (char, char) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(char a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(char a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(char a, byte b) {
        if (a == QueryConstants.NULL_CHAR) {
            return (b == QueryConstants.NULL_BYTE) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_BYTE) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(char a, byte b) {
        if (a == QueryConstants.NULL_CHAR) {
            return (b == QueryConstants.NULL_BYTE);
        }

        if (b == QueryConstants.NULL_BYTE) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(char a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (char, byte) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(char a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(char a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(char a, short b) {
        if (a == QueryConstants.NULL_CHAR) {
            return (b == QueryConstants.NULL_SHORT) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_SHORT) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(char a, short b) {
        if (a == QueryConstants.NULL_CHAR) {
            return (b == QueryConstants.NULL_SHORT);
        }

        if (b == QueryConstants.NULL_SHORT) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(char a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (char, short) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(char a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(char a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(byte a, int b) {
        if (a == QueryConstants.NULL_BYTE) {
            return (b == QueryConstants.NULL_INT) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_INT) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(byte a, int b) {
        if (a == QueryConstants.NULL_BYTE) {
            return (b == QueryConstants.NULL_INT);
        }

        if (b == QueryConstants.NULL_INT) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(byte a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (byte, int) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(byte a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(byte a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(byte a, double b) {
        if (a == QueryConstants.NULL_BYTE) {
            return (b == QueryConstants.NULL_DOUBLE) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_DOUBLE) {
            return 1;
        }

        return Double.compare(a, b);
    }

    public static boolean eq(byte a, double b) {
        if (a == QueryConstants.NULL_BYTE) {
            return (b == QueryConstants.NULL_DOUBLE);
        }

        if (b == QueryConstants.NULL_DOUBLE) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(byte a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (byte, double) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(byte a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(byte a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(byte a, long b) {
        if (a == QueryConstants.NULL_BYTE) {
            return (b == QueryConstants.NULL_LONG) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_LONG) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(byte a, long b) {
        if (a == QueryConstants.NULL_BYTE) {
            return (b == QueryConstants.NULL_LONG);
        }

        if (b == QueryConstants.NULL_LONG) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(byte a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (byte, long) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(byte a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(byte a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(byte a, float b) {
        if (a == QueryConstants.NULL_BYTE) {
            return (b == QueryConstants.NULL_FLOAT) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_FLOAT) {
            return 1;
        }

        return Double.compare(a, b);
    }

    public static boolean eq(byte a, float b) {
        if (a == QueryConstants.NULL_BYTE) {
            return (b == QueryConstants.NULL_FLOAT);
        }

        if (b == QueryConstants.NULL_FLOAT) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(byte a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (byte, float) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(byte a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(byte a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(byte a, char b) {
        if (a == QueryConstants.NULL_BYTE) {
            return (b == QueryConstants.NULL_CHAR) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_CHAR) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(byte a, char b) {
        if (a == QueryConstants.NULL_BYTE) {
            return (b == QueryConstants.NULL_CHAR);
        }

        if (b == QueryConstants.NULL_CHAR) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(byte a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (byte, char) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(byte a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(byte a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(byte a, byte b) {
        if (a == QueryConstants.NULL_BYTE) {
            return (b == QueryConstants.NULL_BYTE) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_BYTE) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(byte a, byte b) {
        if (a == QueryConstants.NULL_BYTE) {
            return (b == QueryConstants.NULL_BYTE);
        }

        if (b == QueryConstants.NULL_BYTE) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(byte a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (byte, byte) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(byte a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(byte a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(byte a, short b) {
        if (a == QueryConstants.NULL_BYTE) {
            return (b == QueryConstants.NULL_SHORT) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_SHORT) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(byte a, short b) {
        if (a == QueryConstants.NULL_BYTE) {
            return (b == QueryConstants.NULL_SHORT);
        }

        if (b == QueryConstants.NULL_SHORT) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(byte a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (byte, short) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(byte a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(byte a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(short a, int b) {
        if (a == QueryConstants.NULL_SHORT) {
            return (b == QueryConstants.NULL_INT) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_INT) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(short a, int b) {
        if (a == QueryConstants.NULL_SHORT) {
            return (b == QueryConstants.NULL_INT);
        }

        if (b == QueryConstants.NULL_INT) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(short a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (short, int) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(short a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(short a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(short a, double b) {
        if (a == QueryConstants.NULL_SHORT) {
            return (b == QueryConstants.NULL_DOUBLE) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_DOUBLE) {
            return 1;
        }

        return Double.compare(a, b);
    }

    public static boolean eq(short a, double b) {
        if (a == QueryConstants.NULL_SHORT) {
            return (b == QueryConstants.NULL_DOUBLE);
        }

        if (b == QueryConstants.NULL_DOUBLE) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(short a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (short, double) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(short a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(short a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(short a, long b) {
        if (a == QueryConstants.NULL_SHORT) {
            return (b == QueryConstants.NULL_LONG) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_LONG) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(short a, long b) {
        if (a == QueryConstants.NULL_SHORT) {
            return (b == QueryConstants.NULL_LONG);
        }

        if (b == QueryConstants.NULL_LONG) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(short a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (short, long) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(short a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(short a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(short a, float b) {
        if (a == QueryConstants.NULL_SHORT) {
            return (b == QueryConstants.NULL_FLOAT) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_FLOAT) {
            return 1;
        }

        return Double.compare(a, b);
    }

    public static boolean eq(short a, float b) {
        if (a == QueryConstants.NULL_SHORT) {
            return (b == QueryConstants.NULL_FLOAT);
        }

        if (b == QueryConstants.NULL_FLOAT) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(short a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (short, float) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(short a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(short a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(short a, char b) {
        if (a == QueryConstants.NULL_SHORT) {
            return (b == QueryConstants.NULL_CHAR) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_CHAR) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(short a, char b) {
        if (a == QueryConstants.NULL_SHORT) {
            return (b == QueryConstants.NULL_CHAR);
        }

        if (b == QueryConstants.NULL_CHAR) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(short a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (short, char) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(short a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(short a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(short a, byte b) {
        if (a == QueryConstants.NULL_SHORT) {
            return (b == QueryConstants.NULL_BYTE) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_BYTE) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(short a, byte b) {
        if (a == QueryConstants.NULL_SHORT) {
            return (b == QueryConstants.NULL_BYTE);
        }

        if (b == QueryConstants.NULL_BYTE) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(short a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (short, byte) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(short a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(short a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static int compareTo(short a, short b) {
        if (a == QueryConstants.NULL_SHORT) {
            return (b == QueryConstants.NULL_SHORT) ? 0 : -1;
        }

        if (b == QueryConstants.NULL_SHORT) {
            return 1;
        }

        return a < b ? -1 : (a == b ? 0 : 1);
    }

    public static boolean eq(short a, short b) {
        if (a == QueryConstants.NULL_SHORT) {
            return (b == QueryConstants.NULL_SHORT);
        }

        if (b == QueryConstants.NULL_SHORT) {
            return false;
        }

        return a == b;
    }

    public static boolean[] eqArray(short a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (short, short) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(short a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(short a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static boolean less(int a, int b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(int a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(int a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(int a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(int a, double b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(int a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(int a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(int a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(int a, long b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(int a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(int a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(int a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(int a, float b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(int a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(int a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(int a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(int a, char b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(int a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(int a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(int a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(int a, byte b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(int a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(int a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(int a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(int a, short b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(int a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(int a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(int a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(double a, int b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(double a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(double a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(double a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(double a, double b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(double a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(double a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(double a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(double a, long b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(double a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(double a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(double a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(double a, float b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(double a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(double a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(double a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(double a, char b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(double a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(double a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(double a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(double a, byte b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(double a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(double a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(double a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(double a, short b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(double a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(double a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(double a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(long a, int b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(long a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(long a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(long a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(long a, double b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(long a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(long a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(long a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(long a, long b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(long a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(long a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(long a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(long a, float b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(long a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(long a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(long a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(long a, char b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(long a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(long a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(long a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(long a, byte b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(long a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(long a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(long a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(long a, short b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(long a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(long a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(long a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(float a, int b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(float a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(float a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(float a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(float a, double b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(float a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(float a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(float a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(float a, long b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(float a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(float a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(float a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(float a, float b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(float a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(float a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(float a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(float a, char b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(float a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(float a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(float a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(float a, byte b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(float a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(float a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(float a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(float a, short b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(float a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(float a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(float a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(char a, int b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(char a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(char a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(char a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(char a, double b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(char a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(char a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(char a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(char a, long b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(char a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(char a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(char a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(char a, float b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(char a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(char a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(char a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(char a, char b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(char a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(char a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(char a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(char a, byte b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(char a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(char a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(char a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(char a, short b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(char a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(char a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(char a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(byte a, int b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(byte a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(byte a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(byte a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(byte a, double b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(byte a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(byte a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(byte a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(byte a, long b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(byte a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(byte a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(byte a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(byte a, float b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(byte a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(byte a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(byte a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(byte a, char b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(byte a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(byte a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(byte a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(byte a, byte b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(byte a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(byte a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(byte a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(byte a, short b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(byte a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(byte a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(byte a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(short a, int b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(short a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(short a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(short a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(short a, double b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(short a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(short a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(short a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(short a, long b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(short a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(short a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(short a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(short a, float b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(short a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(short a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(short a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(short a, char b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(short a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(short a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(short a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(short a, byte b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(short a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(short a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(short a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean less(short a, short b) {
        return compareTo(a, b) < 0;
    }

    public static boolean[] lessArray(short a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(short a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(short a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(int a, int b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(int a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(int a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(int a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(int a, double b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(int a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(int a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(int a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(int a, long b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(int a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(int a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(int a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(int a, float b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(int a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(int a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(int a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(int a, char b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(int a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(int a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(int a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(int a, byte b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(int a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(int a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(int a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(int a, short b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(int a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(int a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(int a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(double a, int b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(double a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(double a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(double a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(double a, double b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(double a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(double a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(double a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(double a, long b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(double a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(double a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(double a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(double a, float b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(double a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(double a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(double a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(double a, char b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(double a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(double a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(double a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(double a, byte b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(double a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(double a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(double a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(double a, short b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(double a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(double a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(double a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(long a, int b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(long a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(long a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(long a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(long a, double b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(long a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(long a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(long a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(long a, long b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(long a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(long a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(long a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(long a, float b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(long a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(long a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(long a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(long a, char b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(long a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(long a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(long a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(long a, byte b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(long a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(long a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(long a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(long a, short b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(long a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(long a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(long a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(float a, int b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(float a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(float a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(float a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(float a, double b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(float a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(float a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(float a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(float a, long b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(float a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(float a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(float a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(float a, float b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(float a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(float a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(float a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(float a, char b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(float a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(float a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(float a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(float a, byte b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(float a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(float a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(float a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(float a, short b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(float a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(float a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(float a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(char a, int b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(char a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(char a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(char a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(char a, double b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(char a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(char a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(char a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(char a, long b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(char a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(char a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(char a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(char a, float b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(char a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(char a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(char a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(char a, char b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(char a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(char a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(char a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(char a, byte b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(char a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(char a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(char a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(char a, short b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(char a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(char a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(char a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(byte a, int b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(byte a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(byte a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(byte a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(byte a, double b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(byte a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(byte a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(byte a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(byte a, long b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(byte a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(byte a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(byte a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(byte a, float b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(byte a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(byte a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(byte a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(byte a, char b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(byte a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(byte a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(byte a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(byte a, byte b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(byte a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(byte a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(byte a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(byte a, short b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(byte a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(byte a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(byte a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(short a, int b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(short a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(short a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(short a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(short a, double b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(short a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(short a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(short a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(short a, long b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(short a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(short a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(short a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(short a, float b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(short a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(short a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(short a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(short a, char b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(short a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(short a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(short a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(short a, byte b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(short a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(short a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(short a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean greater(short a, short b) {
        return compareTo(a, b) > 0;
    }

    public static boolean[] greaterArray(short a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(short a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(short a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(int a, int b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(int a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(int a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(int a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(int a, double b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(int a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(int a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(int a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(int a, long b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(int a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(int a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(int a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(int a, float b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(int a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(int a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(int a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(int a, char b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(int a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(int a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(int a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(int a, byte b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(int a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(int a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(int a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(int a, short b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(int a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(int a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(int a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(double a, int b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(double a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(double a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(double a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(double a, double b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(double a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(double a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(double a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(double a, long b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(double a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(double a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(double a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(double a, float b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(double a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(double a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(double a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(double a, char b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(double a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(double a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(double a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(double a, byte b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(double a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(double a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(double a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(double a, short b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(double a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(double a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(double a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(long a, int b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(long a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(long a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(long a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(long a, double b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(long a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(long a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(long a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(long a, long b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(long a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(long a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(long a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(long a, float b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(long a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(long a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(long a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(long a, char b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(long a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(long a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(long a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(long a, byte b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(long a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(long a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(long a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(long a, short b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(long a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(long a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(long a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(float a, int b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(float a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(float a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(float a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(float a, double b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(float a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(float a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(float a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(float a, long b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(float a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(float a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(float a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(float a, float b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(float a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(float a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(float a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(float a, char b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(float a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(float a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(float a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(float a, byte b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(float a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(float a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(float a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(float a, short b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(float a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(float a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(float a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(char a, int b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(char a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(char a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(char a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(char a, double b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(char a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(char a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(char a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(char a, long b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(char a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(char a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(char a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(char a, float b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(char a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(char a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(char a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(char a, char b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(char a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(char a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(char a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(char a, byte b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(char a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(char a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(char a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(char a, short b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(char a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(char a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(char a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(byte a, int b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(byte a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(byte a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(byte a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(byte a, double b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(byte a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(byte a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(byte a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(byte a, long b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(byte a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(byte a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(byte a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(byte a, float b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(byte a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(byte a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(byte a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(byte a, char b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(byte a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(byte a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(byte a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(byte a, byte b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(byte a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(byte a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(byte a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(byte a, short b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(byte a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(byte a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(byte a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(short a, int b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(short a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(short a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(short a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(short a, double b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(short a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(short a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(short a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(short a, long b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(short a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(short a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(short a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(short a, float b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(short a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(short a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(short a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(short a, char b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(short a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(short a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(short a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(short a, byte b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(short a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(short a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(short a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean lessEquals(short a, short b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean[] lessEqualsArray(short a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(short a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(short a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(int a, int b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(int a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(int a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(int a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(int a, double b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(int a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(int a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(int a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(int a, long b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(int a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(int a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(int a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(int a, float b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(int a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(int a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(int a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(int a, char b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(int a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(int a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(int a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(int a, byte b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(int a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(int a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(int a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(int a, short b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(int a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (int, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(int a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(int a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(double a, int b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(double a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(double a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(double a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(double a, double b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(double a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(double a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(double a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(double a, long b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(double a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(double a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(double a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(double a, float b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(double a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(double a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(double a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(double a, char b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(double a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(double a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(double a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(double a, byte b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(double a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(double a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(double a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(double a, short b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(double a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (double, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(double a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(double a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(long a, int b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(long a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(long a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(long a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(long a, double b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(long a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(long a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(long a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(long a, long b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(long a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(long a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(long a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(long a, float b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(long a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(long a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(long a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(long a, char b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(long a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(long a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(long a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(long a, byte b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(long a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(long a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(long a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(long a, short b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(long a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (long, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(long a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(long a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(float a, int b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(float a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(float a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(float a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(float a, double b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(float a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(float a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(float a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(float a, long b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(float a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(float a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(float a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(float a, float b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(float a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(float a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(float a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(float a, char b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(float a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(float a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(float a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(float a, byte b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(float a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(float a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(float a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(float a, short b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(float a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (float, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(float a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(float a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(char a, int b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(char a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(char a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(char a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(char a, double b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(char a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(char a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(char a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(char a, long b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(char a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(char a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(char a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(char a, float b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(char a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(char a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(char a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(char a, char b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(char a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(char a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(char a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(char a, byte b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(char a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(char a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(char a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(char a, short b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(char a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (char, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(char a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(char a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(byte a, int b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(byte a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(byte a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(byte a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(byte a, double b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(byte a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(byte a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(byte a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(byte a, long b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(byte a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(byte a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(byte a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(byte a, float b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(byte a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(byte a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(byte a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(byte a, char b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(byte a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(byte a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(byte a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(byte a, byte b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(byte a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(byte a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(byte a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(byte a, short b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(byte a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (byte, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(byte a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(byte a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(short a, int b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(short a[], int b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, int) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(short a[], int b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(short a, int b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(short a, double b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(short a[], double b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, double) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(short a[], double b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(short a, double b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(short a, long b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(short a[], long b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, long) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(short a[], long b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(short a, long b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(short a, float b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(short a[], float b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, float) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(short a[], float b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(short a, float b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(short a, char b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(short a[], char b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, char) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(short a[], char b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(short a, char b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(short a, byte b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(short a[], byte b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, byte) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(short a[], byte b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(short a, byte b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean greaterEquals(short a, short b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean[] greaterEqualsArray(short a[], short b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Attempt to compare two arrays (short, short) of different length" +
                    " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(short a[], short b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(short a, short b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean less(Comparable a, Comparable b) {
        return compareTo(a, b) < 0;
    }

    public static boolean greater(Comparable a, Comparable b) {
        return compareTo(a, b) > 0;
    }

    public static boolean lessEquals(Comparable a, Comparable b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean greaterEquals(Comparable a, Comparable b) {
        return compareTo(a, b) >= 0;
    }

    public static Boolean binaryOr(Boolean a, Boolean b) {
        return a == QueryConstants.NULL_BOOLEAN || b == QueryConstants.NULL_BOOLEAN ? QueryConstants.NULL_BOOLEAN
                : Boolean.valueOf(a | b);
    }

    public static Boolean xor(Boolean a, Boolean b) {
        return a == QueryConstants.NULL_BOOLEAN || b == QueryConstants.NULL_BOOLEAN ? QueryConstants.NULL_BOOLEAN
                : Boolean.valueOf(a ^ b);
    }

    public static Boolean binaryAnd(Boolean a, Boolean b) {
        return a == QueryConstants.NULL_BOOLEAN || b == QueryConstants.NULL_BOOLEAN ? QueryConstants.NULL_BOOLEAN
                : Boolean.valueOf(a & b);
    }

    public static boolean[] eqArray(Boolean a[], boolean b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (Boolean, boolean) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(boolean a[], Boolean b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (boolean, Boolean) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(boolean a[], boolean b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (boolean, boolean) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(Object a[], Object b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to check equality of two arrays (Object, Object) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(boolean a[], Boolean b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(Object a[], Object b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = eq(a[i], b);
        }

        return ret;
    }

    public static boolean[] eqArray(Boolean a, boolean b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static boolean[] eqArray(Object a, Object b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = eq(a, b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(Comparable a[], Comparable b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to compare two arrays (Comparable, Comparable) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessArray(Comparable a[], Comparable b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = less(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessArray(Comparable a, Comparable b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = less(a, b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(Comparable a[], Comparable b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to compare two arrays (Comparable, Comparable) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterArray(Comparable a[], Comparable b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greater(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterArray(Comparable a, Comparable b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greater(a, b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(Comparable a[], Comparable b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to compare two arrays (Comparable, Comparable) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(Comparable a[], Comparable b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = lessEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] lessEqualsArray(Comparable a, Comparable b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = lessEquals(a, b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(Comparable a[], Comparable b[]) {
        if (a.length != b.length)
            throw new IllegalArgumentException(
                    "Attempt to compare two arrays (Comparable, Comparable) of different length" +
                            " (a.length=" + a.length + ", b.length=" + b.length + ')');

        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b[i]);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(Comparable a[], Comparable b) {
        boolean[] ret = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = greaterEquals(a[i], b);
        }

        return ret;
    }

    public static boolean[] greaterEqualsArray(Comparable a, Comparable b[]) {
        boolean[] ret = new boolean[b.length];
        for (int i = 0; i < b.length; i++) {
            ret[i] = greaterEquals(a, b[i]);
        }

        return ret;
    }

    public static double doubleCast(int a) {
        return a == QueryConstants.NULL_INT ? QueryConstants.NULL_DOUBLE : (double) a;
    }

    public static long longCast(int a) {
        return a == QueryConstants.NULL_INT ? QueryConstants.NULL_LONG : (long) a;
    }

    public static float floatCast(int a) {
        return a == QueryConstants.NULL_INT ? QueryConstants.NULL_FLOAT : (float) a;
    }

    public static char charCast(int a) {
        return a == QueryConstants.NULL_INT ? QueryConstants.NULL_CHAR : (char) a;
    }

    public static byte byteCast(int a) {
        return a == QueryConstants.NULL_INT ? QueryConstants.NULL_BYTE : (byte) a;
    }

    public static short shortCast(int a) {
        return a == QueryConstants.NULL_INT ? QueryConstants.NULL_SHORT : (short) a;
    }

    public static int intCast(double a) {
        return a == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_INT : (int) a;
    }

    public static long longCast(double a) {
        return a == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_LONG : (long) a;
    }

    public static float floatCast(double a) {
        return a == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_FLOAT : (float) a;
    }

    public static char charCast(double a) {
        return a == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_CHAR : (char) a;
    }

    public static byte byteCast(double a) {
        return a == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_BYTE : (byte) a;
    }

    public static short shortCast(double a) {
        return a == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_SHORT : (short) a;
    }

    public static int intCast(long a) {
        return a == QueryConstants.NULL_LONG ? QueryConstants.NULL_INT : (int) a;
    }

    public static double doubleCast(long a) {
        return a == QueryConstants.NULL_LONG ? QueryConstants.NULL_DOUBLE : (double) a;
    }

    public static float floatCast(long a) {
        return a == QueryConstants.NULL_LONG ? QueryConstants.NULL_FLOAT : (float) a;
    }

    public static char charCast(long a) {
        return a == QueryConstants.NULL_LONG ? QueryConstants.NULL_CHAR : (char) a;
    }

    public static byte byteCast(long a) {
        return a == QueryConstants.NULL_LONG ? QueryConstants.NULL_BYTE : (byte) a;
    }

    public static short shortCast(long a) {
        return a == QueryConstants.NULL_LONG ? QueryConstants.NULL_SHORT : (short) a;
    }

    public static int intCast(float a) {
        return a == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_INT : (int) a;
    }

    public static double doubleCast(float a) {
        return a == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_DOUBLE : (double) a;
    }

    public static long longCast(float a) {
        return a == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_LONG : (long) a;
    }

    public static char charCast(float a) {
        return a == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_CHAR : (char) a;
    }

    public static byte byteCast(float a) {
        return a == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_BYTE : (byte) a;
    }

    public static short shortCast(float a) {
        return a == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_SHORT : (short) a;
    }

    public static int intCast(char a) {
        return a == QueryConstants.NULL_CHAR ? QueryConstants.NULL_INT : (int) a;
    }

    public static double doubleCast(char a) {
        return a == QueryConstants.NULL_CHAR ? QueryConstants.NULL_DOUBLE : (double) a;
    }

    public static long longCast(char a) {
        return a == QueryConstants.NULL_CHAR ? QueryConstants.NULL_LONG : (long) a;
    }

    public static float floatCast(char a) {
        return a == QueryConstants.NULL_CHAR ? QueryConstants.NULL_FLOAT : (float) a;
    }

    public static byte byteCast(char a) {
        return a == QueryConstants.NULL_CHAR ? QueryConstants.NULL_BYTE : (byte) a;
    }

    public static short shortCast(char a) {
        return a == QueryConstants.NULL_CHAR ? QueryConstants.NULL_SHORT : (short) a;
    }

    public static int intCast(byte a) {
        return a == QueryConstants.NULL_BYTE ? QueryConstants.NULL_INT : (int) a;
    }

    public static double doubleCast(byte a) {
        return a == QueryConstants.NULL_BYTE ? QueryConstants.NULL_DOUBLE : (double) a;
    }

    public static long longCast(byte a) {
        return a == QueryConstants.NULL_BYTE ? QueryConstants.NULL_LONG : (long) a;
    }

    public static float floatCast(byte a) {
        return a == QueryConstants.NULL_BYTE ? QueryConstants.NULL_FLOAT : (float) a;
    }

    public static char charCast(byte a) {
        return a == QueryConstants.NULL_BYTE ? QueryConstants.NULL_CHAR : (char) a;
    }

    public static short shortCast(byte a) {
        return a == QueryConstants.NULL_BYTE ? QueryConstants.NULL_SHORT : (short) a;
    }

    public static int intCast(short a) {
        return a == QueryConstants.NULL_SHORT ? QueryConstants.NULL_INT : (int) a;
    }

    public static double doubleCast(short a) {
        return a == QueryConstants.NULL_SHORT ? QueryConstants.NULL_DOUBLE : (double) a;
    }

    public static long longCast(short a) {
        return a == QueryConstants.NULL_SHORT ? QueryConstants.NULL_LONG : (long) a;
    }

    public static float floatCast(short a) {
        return a == QueryConstants.NULL_SHORT ? QueryConstants.NULL_FLOAT : (float) a;
    }

    public static char charCast(short a) {
        return a == QueryConstants.NULL_SHORT ? QueryConstants.NULL_CHAR : (char) a;
    }

    public static byte byteCast(short a) {
        return a == QueryConstants.NULL_SHORT ? QueryConstants.NULL_BYTE : (byte) a;
    }

    public static int intCast(Object a) {
        // TODO: #3262 this change is not included in QueryLanguageFunctionGenerator.
        // TODO: #3264 change 8900c427 causes `intCast(NULL_DOUBLE)` provides an incorrect value instead of throwing.
        return a == null ? QueryConstants.NULL_INT : ((Number) a).intValue();
    }

    public static double doubleCast(Object a) {
        return a == null ? QueryConstants.NULL_DOUBLE : ((Number) a).doubleValue();
    }

    public static long longCast(Object a) {
        return a == null ? QueryConstants.NULL_LONG : ((Number) a).longValue();
    }

    public static float floatCast(Object a) {
        return a == null ? QueryConstants.NULL_FLOAT : ((Number) a).floatValue();
    }

    public static char charCast(Object a) {
        return a == null ? QueryConstants.NULL_CHAR : (char) a;
    }

    public static byte byteCast(Object a) {
        return a == null ? QueryConstants.NULL_BYTE : ((Number) a).byteValue();
    }

    public static short shortCast(Object a) {
        return a == null ? QueryConstants.NULL_SHORT : ((Number) a).shortValue();
    }

    public static int intPyCast(Object a) {
        if (a != null && !(a instanceof PyObject)) {
            throw new IllegalArgumentException("Provided value is not a PyObject");
        }
        PyObject o = (PyObject) a;
        if (o == null || o.isNone()) {
            return QueryConstants.NULL_INT;
        }
        return o.getIntValue();
    }

    public static double doublePyCast(Object a) {
        if (a != null && !(a instanceof PyObject)) {
            throw new IllegalArgumentException("Provided value is not a PyObject");
        }
        PyObject o = (PyObject) a;
        if (o == null || o.isNone()) {
            return QueryConstants.NULL_DOUBLE;
        }
        return o.getDoubleValue();
    }

    public static long longPyCast(Object a) {
        if (a != null && !(a instanceof PyObject)) {
            throw new IllegalArgumentException("Provided value is not a PyObject");
        }
        PyObject o = (PyObject) a;
        if (o == null || o.isNone()) {
            return QueryConstants.NULL_LONG;
        }
        return o.getLongValue();
    }

    public static float floatPyCast(Object a) {
        if (a != null && !(a instanceof PyObject)) {
            throw new IllegalArgumentException("Provided value is not a PyObject");
        }
        PyObject o = (PyObject) a;
        if (o == null || o.isNone()) {
            return QueryConstants.NULL_FLOAT;
        }
        return (float) o.getDoubleValue();
    }

    public static char charPyCast(Object a) {
        if (a != null && !(a instanceof PyObject)) {
            throw new IllegalArgumentException("Provided value is not a PyObject");
        }
        PyObject o = (PyObject) a;
        if (o == null || o.isNone()) {
            return QueryConstants.NULL_CHAR;
        }
        return (char) o.getIntValue();
    }

    public static byte bytePyCast(Object a) {
        if (a != null && !(a instanceof PyObject)) {
            throw new IllegalArgumentException("Provided value is not a PyObject");
        }
        PyObject o = (PyObject) a;
        if (o == null || o.isNone()) {
            return QueryConstants.NULL_BYTE;
        }
        return (byte) o.getIntValue();
    }

    public static short shortPyCast(Object a) {
        if (a != null && !(a instanceof PyObject)) {
            throw new IllegalArgumentException("Provided value is not a PyObject");
        }
        PyObject o = (PyObject) a;
        if (o == null || o.isNone()) {
            return QueryConstants.NULL_SHORT;
        }
        return (short) o.getIntValue();
    }

    public static String doStringPyCast(Object a) {
        if (a != null && !(a instanceof PyObject)) {
            throw new IllegalArgumentException("Provided value is not a PyObject");
        }
        PyObject o = (PyObject) a;
        if (o == null || o.isNone()) {
            return null;
        }
        return o.getStringValue();
    }

    public static boolean booleanPyCast(Object a) {
        if (a != null && !(a instanceof PyObject)) {
            throw new IllegalArgumentException("Provided value is not a PyObject");
        }
        PyObject o = (PyObject) a;
        if (o == null || o.isNone()) {
            throw new NullPointerException("Provided value is unexpectedly null; cannot cast to boolean");
        }
        return o.getBooleanValue();
    }

    public static Boolean doBooleanPyCast(Object a) {
        if (a != null && !(a instanceof PyObject)) {
            throw new IllegalArgumentException("Provided value is not a PyObject");
        }
        PyObject o = (PyObject) a;
        if (o == null || o.isNone()) {
            return null;
        }
        return o.getBooleanValue();
    }

    public static int negate(int a) {
        return a == QueryConstants.NULL_INT ? QueryConstants.NULL_INT : -a;
    }

    public static double negate(double a) {
        return a == QueryConstants.NULL_DOUBLE ? QueryConstants.NULL_DOUBLE : -a;
    }

    public static long negate(long a) {
        return a == QueryConstants.NULL_LONG ? QueryConstants.NULL_LONG : -a;
    }

    public static float negate(float a) {
        return a == QueryConstants.NULL_FLOAT ? QueryConstants.NULL_FLOAT : -a;
    }

    public static int negate(char a) {
        return a == QueryConstants.NULL_CHAR ? QueryConstants.NULL_INT : -a;
    }

    public static int negate(byte a) {
        return a == QueryConstants.NULL_BYTE ? QueryConstants.NULL_INT : -a;
    }

    public static int negate(short a) {
        return a == QueryConstants.NULL_SHORT ? QueryConstants.NULL_INT : -a;
    }

    //
    // BigDecimal ops
    //

    public static BigDecimal plus(BigDecimal a, BigDecimal b) {
        if (a == null || b == null) {
            return null;
        }
        return a.add(b);
    }

    public static BigDecimal plus(BigDecimal a, long b) {
        if (a == null || b == QueryConstants.NULL_LONG) {
            return null;
        }
        return a.add(BigDecimal.valueOf(b));
    }

    public static BigDecimal plus(long a, BigDecimal b) {
        return plus(b, a);
    }

    public static BigDecimal plus(BigDecimal a, int b) {
        if (a == null || b == QueryConstants.NULL_INT) {
            return null;
        }
        return a.add(BigDecimal.valueOf(b));
    }

    public static BigDecimal plus(int a, BigDecimal b) {
        return plus(b, a);
    }

    public static BigDecimal plus(BigDecimal a, short b) {
        if (a == null || b == QueryConstants.NULL_SHORT) {
            return null;
        }
        return a.add(BigDecimal.valueOf(b));
    }

    public static BigDecimal plus(short a, BigDecimal b) {
        return plus(b, a);
    }

    public static BigDecimal plus(BigDecimal a, byte b) {
        if (a == null || b == QueryConstants.NULL_BYTE) {
            return null;
        }
        return a.add(BigDecimal.valueOf(b));
    }

    public static BigDecimal plus(byte a, BigDecimal b) {
        return plus(b, a);
    }

    public static BigDecimal plus(BigDecimal a, double b) {
        if (a == null || b == QueryConstants.NULL_DOUBLE) {
            return null;
        }
        return a.add(BigDecimal.valueOf(b));
    }

    public static BigDecimal plus(double a, BigDecimal b) {
        return plus(b, a);
    }

    public static BigDecimal plus(BigDecimal a, float b) {
        if (a == null || b == QueryConstants.NULL_FLOAT) {
            return null;
        }
        return a.add(BigDecimal.valueOf(b));
    }

    public static BigDecimal plus(float a, BigDecimal b) {
        return plus(b, a);
    }

    public static BigDecimal minus(BigDecimal a, BigDecimal b) {
        if (a == null || b == null) {
            return null;
        }
        return a.subtract(b);
    }

    public static BigDecimal minus(BigDecimal a, long b) {
        if (a == null || b == QueryConstants.NULL_LONG) {
            return null;
        }
        return a.subtract(BigDecimal.valueOf(b));
    }

    public static BigDecimal minus(long a, BigDecimal b) {
        if (a == QueryConstants.NULL_LONG || b == null) {
            return null;
        }
        return BigDecimal.valueOf(a).subtract(b);
    }

    public static BigDecimal minus(BigDecimal a, int b) {
        if (a == null || b == QueryConstants.NULL_INT) {
            return null;
        }
        return a.subtract(BigDecimal.valueOf(b));
    }

    public static BigDecimal minus(int a, BigDecimal b) {
        if (a == QueryConstants.NULL_INT || b == null) {
            return null;
        }
        return BigDecimal.valueOf(a).subtract(b);
    }

    public static BigDecimal minus(BigDecimal a, short b) {
        if (a == null || b == QueryConstants.NULL_SHORT) {
            return null;
        }
        return a.subtract(BigDecimal.valueOf(b));
    }

    public static BigDecimal minus(short a, BigDecimal b) {
        if (a == QueryConstants.NULL_SHORT || b == null) {
            return null;
        }
        return BigDecimal.valueOf(a).subtract(b);
    }

    public static BigDecimal minus(BigDecimal a, byte b) {
        if (a == null || b == QueryConstants.NULL_BYTE) {
            return null;
        }
        return a.subtract(BigDecimal.valueOf(b));
    }

    public static BigDecimal minus(byte a, BigDecimal b) {
        if (a == QueryConstants.NULL_BYTE || b == null) {
            return null;
        }
        return BigDecimal.valueOf(a).subtract(b);
    }

    public static BigDecimal minus(BigDecimal a, double b) {
        if (a == null || b == QueryConstants.NULL_DOUBLE) {
            return null;
        }
        return a.subtract(BigDecimal.valueOf(b));
    }

    public static BigDecimal minus(double a, BigDecimal b) {
        if (a == QueryConstants.NULL_DOUBLE || b == null) {
            return null;
        }
        return BigDecimal.valueOf(a).subtract(b);
    }

    public static BigDecimal minus(BigDecimal a, float b) {
        if (a == null || b == QueryConstants.NULL_FLOAT) {
            return null;
        }
        return a.subtract(BigDecimal.valueOf(b));
    }

    public static BigDecimal minus(float a, BigDecimal b) {
        if (a == QueryConstants.NULL_FLOAT || b == null) {
            return null;
        }
        return BigDecimal.valueOf(a).subtract(b);
    }

    public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
        if (a == null || b == null) {
            return null;
        }
        return a.multiply(b);
    }

    public static BigDecimal multiply(BigDecimal a, long b) {
        if (a == null || b == QueryConstants.NULL_LONG) {
            return null;
        }
        return a.multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal multiply(long a, BigDecimal b) {
        return multiply(b, a);
    }

    public static BigDecimal multiply(BigDecimal a, int b) {
        if (a == null || b == QueryConstants.NULL_INT) {
            return null;
        }
        return a.multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal multiply(int a, BigDecimal b) {
        return multiply(b, a);
    }

    public static BigDecimal multiply(BigDecimal a, short b) {
        if (a == null || b == QueryConstants.NULL_SHORT) {
            return null;
        }
        return a.multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal multiply(short a, BigDecimal b) {
        return multiply(b, a);
    }

    public static BigDecimal multiply(BigDecimal a, byte b) {
        if (a == null || b == QueryConstants.NULL_BYTE) {
            return null;
        }
        return a.multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal multiply(byte a, BigDecimal b) {
        return multiply(b, a);
    }

    public static BigDecimal multiply(BigDecimal a, double b) {
        if (a == null || b == QueryConstants.NULL_DOUBLE) {
            return null;
        }
        return a.multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal multiply(double a, BigDecimal b) {
        return multiply(b, a);
    }

    public static BigDecimal multiply(BigDecimal a, float b) {
        if (a == null || b == QueryConstants.NULL_FLOAT) {
            return null;
        }
        return a.multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal multiply(float a, BigDecimal b) {
        return multiply(b, a);
    }

    public static BigDecimal divide(BigDecimal a, BigDecimal b) {
        if (a == null || b == null) {
            return null;
        }
        final int scale = max(max(a.scale(), b.scale()), DEFAULT_SCALE);
        return a.divide(b, scale, ROUNDING_MODE);
    }

    private static BigDecimal divideNoNull(BigDecimal a, long b) {
        return a.divide(
                BigDecimal.valueOf(b),
                max(a.scale(), DEFAULT_SCALE),
                ROUNDING_MODE);
    }

    private static BigDecimal divideNoNull(long a, BigDecimal b) {
        return BigDecimal.valueOf(a)
                .divide(b, max(b.scale(), DEFAULT_SCALE), ROUNDING_MODE);
    }

    public static BigDecimal divide(BigDecimal a, long b) {
        if (a == null || b == QueryConstants.NULL_LONG) {
            return null;
        }
        return divideNoNull(a, b);
    }

    public static BigDecimal divide(long a, BigDecimal b) {
        if (a == QueryConstants.NULL_LONG || b == null) {
            return null;
        }
        return divideNoNull(a, b);
    }

    public static BigDecimal divide(BigDecimal a, int b) {
        if (a == null || b == QueryConstants.NULL_INT) {
            return null;
        }
        return divideNoNull(a, b);
    }

    public static BigDecimal divide(int a, BigDecimal b) {
        if (a == QueryConstants.NULL_INT || b == null) {
            return null;
        }
        return divideNoNull(a, b);
    }

    public static BigDecimal divide(BigDecimal a, short b) {
        if (a == null || b == QueryConstants.NULL_SHORT) {
            return null;
        }
        return divideNoNull(a, b);
    }

    public static BigDecimal divide(short a, BigDecimal b) {
        if (a == QueryConstants.NULL_SHORT || b == null) {
            return null;
        }
        return divideNoNull(a, b);
    }

    public static BigDecimal divide(BigDecimal a, byte b) {
        if (a == null || b == QueryConstants.NULL_BYTE) {
            return null;
        }
        return divideNoNull(a, b);
    }

    public static BigDecimal divide(byte a, BigDecimal b) {
        if (a == QueryConstants.NULL_BYTE || b == null) {
            return null;
        }
        return divideNoNull(a, b);
    }

    public static BigDecimal divide(BigDecimal a, double b) {
        if (a == null || b == QueryConstants.NULL_DOUBLE) {
            return null;
        }
        final BigDecimal bdb = BigDecimal.valueOf(b);
        final int scale = max(max(a.scale(), bdb.scale()), DEFAULT_SCALE);
        return a.divide(bdb, scale, ROUNDING_MODE);
    }

    public static BigDecimal divide(double a, BigDecimal b) {
        if (a == QueryConstants.NULL_DOUBLE || b == null) {
            return null;
        }
        final BigDecimal bda = BigDecimal.valueOf(a);
        final int scale = max(max(bda.scale(), b.scale()), DEFAULT_SCALE);
        return bda.divide(b, scale, ROUNDING_MODE);
    }

    public static BigDecimal divide(BigDecimal a, float b) {
        if (a == null || b == QueryConstants.NULL_FLOAT) {
            return null;
        }
        final BigDecimal bdb = BigDecimal.valueOf(b);
        final int scale = max(max(a.scale(), bdb.scale()), DEFAULT_SCALE);
        return a.divide(bdb, scale, ROUNDING_MODE);
    }

    public static BigDecimal divide(float a, BigDecimal b) {
        if (a == QueryConstants.NULL_FLOAT || b == null) {
            return null;
        }
        final BigDecimal bda = BigDecimal.valueOf(a);
        final int scale = max(max(bda.scale(), b.scale()), DEFAULT_SCALE);
        return bda.divide(b, scale, ROUNDING_MODE);
    }

    public static boolean eq(BigDecimal a, BigDecimal b) {
        if (a == null) {
            return (b == null);
        }
        if (b == null) {
            return false;
        }
        return a.compareTo(b) == 0;
    }

    public static boolean eq(BigDecimal a, long b) {
        if (a == null) {
            return (b == QueryConstants.NULL_LONG);
        }
        if (b == QueryConstants.NULL_LONG) {
            return false;
        }
        return a.compareTo(BigDecimal.valueOf(b)) == 0;
    }

    public static boolean eq(long a, BigDecimal b) {
        if (a == QueryConstants.NULL_LONG) {
            return (b == null);
        }
        if (b == null) {
            return false;
        }
        return BigDecimal.valueOf(a).compareTo(b) == 0;
    }

    public static boolean eq(BigDecimal a, int b) {
        if (a == null) {
            return (b == QueryConstants.NULL_INT);
        }
        if (b == QueryConstants.NULL_INT) {
            return false;
        }
        return a.compareTo(BigDecimal.valueOf(b)) == 0;
    }

    public static boolean eq(int a, BigDecimal b) {
        if (a == QueryConstants.NULL_INT) {
            return (b == null);
        }
        if (b == null) {
            return false;
        }
        return BigDecimal.valueOf(a).compareTo(b) == 0;
    }

    public static boolean eq(BigDecimal a, short b) {
        if (a == null) {
            return (b == QueryConstants.NULL_SHORT);
        }
        if (b == QueryConstants.NULL_SHORT) {
            return false;
        }
        return a.compareTo(BigDecimal.valueOf(b)) == 0;
    }

    public static boolean eq(short a, BigDecimal b) {
        if (a == QueryConstants.NULL_SHORT) {
            return (b == null);
        }
        if (b == null) {
            return false;
        }
        return BigDecimal.valueOf(a).compareTo(b) == 0;
    }

    public static boolean eq(BigDecimal a, byte b) {
        if (a == null) {
            return (b == QueryConstants.NULL_BYTE);
        }
        if (b == QueryConstants.NULL_BYTE) {
            return false;
        }
        return a.compareTo(BigDecimal.valueOf(b)) == 0;
    }

    public static boolean eq(byte a, BigDecimal b) {
        if (a == QueryConstants.NULL_BYTE) {
            return (b == null);
        }
        if (b == null) {
            return false;
        }
        return BigDecimal.valueOf(a).compareTo(b) == 0;
    }

    public static boolean eq(BigDecimal a, double b) {
        if (Double.isNaN(b)) {
            return false;
        }
        if (a == null) {
            return (b == QueryConstants.NULL_DOUBLE);
        }
        if (b == QueryConstants.NULL_DOUBLE) {
            return false;
        }
        return a.compareTo(BigDecimal.valueOf(b)) == 0;
    }

    public static boolean eq(double a, BigDecimal b) {
        if (Double.isNaN(a)) {
            return false;
        }
        if (a == QueryConstants.NULL_DOUBLE) {
            return (b == null);
        }
        if (b == null) {
            return false;
        }
        return BigDecimal.valueOf(a).compareTo(b) == 0;
    }

    public static boolean eq(BigDecimal a, float b) {
        if (Float.isNaN(b)) {
            return false;
        }
        if (a == null) {
            return (b == QueryConstants.NULL_FLOAT);
        }
        if (b == QueryConstants.NULL_FLOAT) {
            return false;
        }
        return a.compareTo(BigDecimal.valueOf(b)) == 0;
    }

    public static boolean eq(float a, BigDecimal b) {
        if (Float.isNaN(a)) {
            return false;
        }
        if (a == QueryConstants.NULL_FLOAT) {
            return (b == null);
        }
        if (b == null) {
            return false;
        }
        return BigDecimal.valueOf(a).compareTo(b) == 0;
    }

    public static int compareTo(BigDecimal a, BigDecimal b) {
        if (a == null) {
            return (b == null) ? 0 : -1;
        }
        if (b == null) {
            return 1;
        }
        return a.compareTo(b);
    }

    public static int compareTo(BigDecimal a, long b) {
        if (a == null) {
            return (b == QueryConstants.NULL_LONG) ? 0 : -1;
        }
        if (b == QueryConstants.NULL_LONG) {
            return 1;
        }
        return a.compareTo(BigDecimal.valueOf(b));
    }

    public static int compareTo(long a, BigDecimal b) {
        if (a == QueryConstants.NULL_LONG) {
            return (b == null) ? 0 : -1;
        }
        if (b == null) {
            return 1;
        }
        return BigDecimal.valueOf(a).compareTo(b);
    }

    public static int compareTo(BigDecimal a, int b) {
        if (a == null) {
            return (b == QueryConstants.NULL_INT) ? 0 : -1;
        }
        if (b == QueryConstants.NULL_INT) {
            return 1;
        }
        return a.compareTo(BigDecimal.valueOf(b));
    }

    public static int compareTo(int a, BigDecimal b) {
        if (a == QueryConstants.NULL_INT) {
            return (b == null) ? 0 : -1;
        }
        if (b == null) {
            return 1;
        }
        return BigDecimal.valueOf(a).compareTo(b);
    }

    public static int compareTo(BigDecimal a, short b) {
        if (a == null) {
            return (b == QueryConstants.NULL_SHORT) ? 0 : -1;
        }
        if (b == QueryConstants.NULL_SHORT) {
            return 1;
        }
        return a.compareTo(BigDecimal.valueOf(b));
    }

    public static int compareTo(short a, BigDecimal b) {
        if (a == QueryConstants.NULL_SHORT) {
            return (b == null) ? 0 : -1;
        }
        if (b == null) {
            return 1;
        }
        return BigDecimal.valueOf(a).compareTo(b);
    }

    public static int compareTo(BigDecimal a, byte b) {
        if (a == null) {
            return (b == QueryConstants.NULL_BYTE) ? 0 : -1;
        }
        if (b == QueryConstants.NULL_BYTE) {
            return 1;
        }
        return a.compareTo(BigDecimal.valueOf(b));
    }

    public static int compareTo(byte a, BigDecimal b) {
        if (a == QueryConstants.NULL_BYTE) {
            return (b == null) ? 0 : -1;
        }
        if (b == null) {
            return 1;
        }
        return BigDecimal.valueOf(a).compareTo(b);
    }

    public static int compareTo(BigDecimal a, double b) {
        if (Double.isNaN(b)) {
            return -1; // even if a == null.
        }
        if (a == null) {
            return (b == QueryConstants.NULL_DOUBLE) ? 0 : -1;
        }
        if (b == QueryConstants.NULL_DOUBLE) {
            return 1;
        }
        return a.compareTo(BigDecimal.valueOf(b));
    }

    public static int compareTo(double a, BigDecimal b) {
        if (Double.isNaN(a)) {
            return 1; // even if b == null.
        }
        if (a == QueryConstants.NULL_DOUBLE) {
            return (b == null) ? 0 : -1;
        }
        if (b == null) {
            return 1;
        }
        return BigDecimal.valueOf(a).compareTo(b);
    }

    public static int compareTo(BigDecimal a, float b) {
        if (Float.isNaN(b)) {
            return -1; // even if a == null.
        }
        if (a == null) {
            return (b == QueryConstants.NULL_FLOAT) ? 0 : -1;
        }
        if (b == QueryConstants.NULL_FLOAT) {
            return 1;
        }
        return a.compareTo(BigDecimal.valueOf(b));
    }

    public static int compareTo(float a, BigDecimal b) {
        if (Float.isNaN(a)) {
            return 1; // even if b == null
        }
        if (a == QueryConstants.NULL_FLOAT) {
            return (b == null) ? 0 : -1;
        }
        if (b == null) {
            return 1;
        }
        return BigDecimal.valueOf(a).compareTo(b);
    }

    public static boolean less(BigDecimal a, BigDecimal b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(BigDecimal a, long b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(long a, BigDecimal b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(BigDecimal a, int b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(int a, BigDecimal b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(BigDecimal a, short b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(short a, BigDecimal b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(BigDecimal a, byte b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(byte a, BigDecimal b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(BigDecimal a, double b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(double a, BigDecimal b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(BigDecimal a, float b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(float a, BigDecimal b) {
        return compareTo(a, b) < 0;
    }

    public static boolean lessEquals(BigDecimal a, BigDecimal b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(BigDecimal a, long b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(long a, BigDecimal b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(BigDecimal a, int b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(int a, BigDecimal b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(BigDecimal a, short b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(short a, BigDecimal b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(BigDecimal a, byte b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(byte a, BigDecimal b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(BigDecimal a, double b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(double a, BigDecimal b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(BigDecimal a, float b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(float a, BigDecimal b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean greater(BigDecimal a, BigDecimal b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(BigDecimal a, long b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(long a, BigDecimal b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(BigDecimal a, int b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(int a, BigDecimal b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(BigDecimal a, short b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(short a, BigDecimal b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(BigDecimal a, byte b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(byte a, BigDecimal b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(BigDecimal a, double b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(double a, BigDecimal b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(BigDecimal a, float b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(float a, BigDecimal b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greaterEquals(BigDecimal a, BigDecimal b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(BigDecimal a, long b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(long a, BigDecimal b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(BigDecimal a, int b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(int a, BigDecimal b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(BigDecimal a, short b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(short a, BigDecimal b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(BigDecimal a, byte b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(byte a, BigDecimal b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(BigDecimal a, double b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(double a, BigDecimal b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(BigDecimal a, float b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(float a, BigDecimal b) {
        return compareTo(a, b) >= 0;
    }

    //
    // BigInteger ops
    //

    public static BigInteger plus(BigInteger a, BigInteger b) {
        if (a == null || b == null) {
            return null;
        }
        return a.add(b);
    }

    public static BigInteger plus(BigInteger a, long b) {
        if (a == null || b == QueryConstants.NULL_LONG) {
            return null;
        }
        return a.add(BigInteger.valueOf(b));
    }

    public static BigInteger plus(long a, BigInteger b) {
        return plus(b, a);
    }

    public static BigInteger plus(BigInteger a, int b) {
        if (a == null || b == QueryConstants.NULL_INT) {
            return null;
        }
        return a.add(BigInteger.valueOf(b));
    }

    public static BigInteger plus(int a, BigInteger b) {
        return plus(b, a);
    }

    public static BigInteger plus(BigInteger a, short b) {
        if (a == null || b == QueryConstants.NULL_SHORT) {
            return null;
        }
        return a.add(BigInteger.valueOf(b));
    }

    public static BigInteger plus(short a, BigInteger b) {
        return plus(b, a);
    }

    public static BigInteger plus(BigInteger a, byte b) {
        if (a == null || b == QueryConstants.NULL_BYTE) {
            return null;
        }
        return a.add(BigInteger.valueOf(b));
    }

    public static BigInteger plus(byte a, BigInteger b) {
        return plus(b, a);
    }

    public static BigDecimal plus(BigInteger a, double b) {
        if (a == null || b == QueryConstants.NULL_DOUBLE) {
            return null;
        }
        return new BigDecimal(a).add(BigDecimal.valueOf(b));
    }

    public static BigDecimal plus(double a, BigInteger b) {
        return plus(b, a);
    }

    public static BigDecimal plus(BigInteger a, float b) {
        if (a == null || b == QueryConstants.NULL_FLOAT) {
            return null;
        }
        return new BigDecimal(a).add(BigDecimal.valueOf(b));
    }

    public static BigDecimal plus(float a, BigInteger b) {
        return plus(b, a);
    }

    public static BigInteger minus(BigInteger a, BigInteger b) {
        if (a == null || b == null) {
            return null;
        }
        return a.subtract(b);
    }

    public static BigInteger minus(BigInteger a, long b) {
        if (a == null || b == QueryConstants.NULL_LONG) {
            return null;
        }
        return a.subtract(BigInteger.valueOf(b));
    }

    public static BigInteger minus(long a, BigInteger b) {
        if (a == QueryConstants.NULL_LONG || b == null) {
            return null;
        }
        return BigInteger.valueOf(a).subtract(b);
    }

    public static BigInteger minus(BigInteger a, int b) {
        if (a == null || b == QueryConstants.NULL_INT) {
            return null;
        }
        return a.subtract(BigInteger.valueOf(b));
    }

    public static BigInteger minus(int a, BigInteger b) {
        if (a == QueryConstants.NULL_INT || b == null) {
            return null;
        }
        return BigInteger.valueOf(a).subtract(b);
    }

    public static BigInteger minus(BigInteger a, short b) {
        if (a == null || b == QueryConstants.NULL_SHORT) {
            return null;
        }
        return a.subtract(BigInteger.valueOf(b));
    }

    public static BigInteger minus(short a, BigInteger b) {
        if (a == QueryConstants.NULL_SHORT || b == null) {
            return null;
        }
        return BigInteger.valueOf(a).subtract(b);
    }

    public static BigInteger minus(BigInteger a, byte b) {
        if (a == null || b == QueryConstants.NULL_BYTE) {
            return null;
        }
        return a.subtract(BigInteger.valueOf(b));
    }

    public static BigInteger minus(byte a, BigInteger b) {
        if (a == QueryConstants.NULL_BYTE || b == null) {
            return null;
        }
        return BigInteger.valueOf(a).subtract(b);
    }

    public static BigDecimal minus(BigInteger a, double b) {
        if (a == null || b == QueryConstants.NULL_DOUBLE) {
            return null;
        }
        return new BigDecimal(a).subtract(BigDecimal.valueOf(b));
    }

    public static BigDecimal minus(double a, BigInteger b) {
        if (a == QueryConstants.NULL_DOUBLE || b == null) {
            return null;
        }
        return BigDecimal.valueOf(a).subtract(new BigDecimal(b));
    }

    public static BigDecimal minus(BigInteger a, float b) {
        if (a == null || b == QueryConstants.NULL_FLOAT) {
            return null;
        }
        return new BigDecimal(a).subtract(BigDecimal.valueOf(b));
    }

    public static BigDecimal minus(float a, BigInteger b) {
        if (a == QueryConstants.NULL_FLOAT || b == null) {
            return null;
        }
        return BigDecimal.valueOf(a).subtract(new BigDecimal(b));
    }

    public static BigInteger multiply(BigInteger a, BigInteger b) {
        if (a == null || b == null) {
            return null;
        }
        return a.multiply(b);
    }

    public static BigInteger multiply(BigInteger a, long b) {
        if (a == null || b == QueryConstants.NULL_LONG) {
            return null;
        }
        return a.multiply(BigInteger.valueOf(b));
    }

    public static BigInteger multiply(long a, BigInteger b) {
        return multiply(b, a);
    }

    public static BigInteger multiply(BigInteger a, int b) {
        if (a == null || b == QueryConstants.NULL_INT) {
            return null;
        }
        return a.multiply(BigInteger.valueOf(b));
    }

    public static BigInteger multiply(int a, BigInteger b) {
        return multiply(b, a);
    }

    public static BigInteger multiply(BigInteger a, short b) {
        if (a == null || b == QueryConstants.NULL_SHORT) {
            return null;
        }
        return a.multiply(BigInteger.valueOf(b));
    }

    public static BigInteger multiply(short a, BigInteger b) {
        return multiply(b, a);
    }

    public static BigInteger multiply(BigInteger a, byte b) {
        if (a == null || b == QueryConstants.NULL_BYTE) {
            return null;
        }
        return a.multiply(BigInteger.valueOf(b));
    }

    public static BigInteger multiply(byte a, BigInteger b) {
        return multiply(b, a);
    }

    public static BigDecimal multiply(BigInteger a, double b) {
        if (a == null || b == QueryConstants.NULL_DOUBLE) {
            return null;
        }
        return new BigDecimal(a).multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal multiply(double a, BigInteger b) {
        return multiply(b, a);
    }

    public static BigDecimal multiply(BigInteger a, float b) {
        if (a == null || b == QueryConstants.NULL_FLOAT) {
            return null;
        }
        return new BigDecimal(a).multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal multiply(float a, BigInteger b) {
        return multiply(b, a);
    }

    private static BigDecimal divideNoNull(final BigInteger a, final long b) {
        return new BigDecimal(a).divide(BigDecimal.valueOf(b), DEFAULT_SCALE, ROUNDING_MODE);
    }

    private static BigDecimal divideNoNull(final long a, final BigInteger b) {
        return BigDecimal.valueOf(a)
                .divide(new BigDecimal(b), DEFAULT_SCALE, ROUNDING_MODE);
    }

    public static BigDecimal divide(BigInteger a, BigInteger b) {
        if (a == null || b == null) {
            return null;
        }
        return new BigDecimal(a)
                .divide(new BigDecimal(b), DEFAULT_SCALE, ROUNDING_MODE);
    }

    public static BigDecimal divide(BigInteger a, long b) {
        if (a == null || b == QueryConstants.NULL_LONG) {
            return null;
        }
        return divideNoNull(a, b);
    }

    public static BigDecimal divide(long a, BigInteger b) {
        if (a == QueryConstants.NULL_LONG || b == null) {
            return null;
        }
        return divideNoNull(a, b);
    }

    public static BigDecimal divide(BigInteger a, int b) {
        if (a == null || b == QueryConstants.NULL_INT) {
            return null;
        }
        return divideNoNull(a, b);
    }

    public static BigDecimal divide(int a, BigInteger b) {
        if (a == QueryConstants.NULL_INT || b == null) {
            return null;
        }
        return divideNoNull(a, b);
    }

    public static BigDecimal divide(BigInteger a, short b) {
        if (a == null || b == QueryConstants.NULL_SHORT) {
            return null;
        }
        return divideNoNull(a, b);
    }

    public static BigDecimal divide(short a, BigInteger b) {
        if (a == QueryConstants.NULL_SHORT || b == null) {
            return null;
        }
        return divideNoNull(a, b);
    }

    public static BigDecimal divide(BigInteger a, byte b) {
        if (a == null || b == QueryConstants.NULL_BYTE) {
            return null;
        }
        return divideNoNull(a, b);
    }

    public static BigDecimal divide(byte a, BigInteger b) {
        if (a == QueryConstants.NULL_BYTE || b == null) {
            return null;
        }
        return divideNoNull(a, b);
    }

    public static BigDecimal divide(BigInteger a, double b) {
        if (a == null || b == QueryConstants.NULL_DOUBLE) {
            return null;
        }
        final BigDecimal bbd = BigDecimal.valueOf(b);
        return new BigDecimal(a)
                .divide(bbd, max(bbd.scale(), DEFAULT_SCALE), ROUNDING_MODE);
    }

    public static BigDecimal divide(double a, BigInteger b) {
        if (a == QueryConstants.NULL_DOUBLE || b == null) {
            return null;
        }
        BigDecimal bba = BigDecimal.valueOf(a);
        return bba.divide(
                new BigDecimal(b),
                max(bba.scale(), DEFAULT_SCALE),
                ROUNDING_MODE);
    }

    public static BigDecimal divide(BigInteger a, float b) {
        if (a == null || b == QueryConstants.NULL_FLOAT) {
            return null;
        }
        final BigDecimal bbd = BigDecimal.valueOf(b);
        return new BigDecimal(a)
                .divide(bbd, max(bbd.scale(), DEFAULT_SCALE), ROUNDING_MODE);
    }

    public static BigDecimal divide(float a, BigInteger b) {
        if (a == QueryConstants.NULL_FLOAT || b == null) {
            return null;
        }
        BigDecimal bba = BigDecimal.valueOf(a);
        return bba.divide(
                new BigDecimal(b),
                max(bba.scale(), DEFAULT_SCALE),
                ROUNDING_MODE);
    }

    public static boolean eq(BigInteger a, BigInteger b) {
        if (a == null) {
            return (b == null);
        }
        if (b == null) {
            return false;
        }
        return a.compareTo(b) == 0;
    }

    public static boolean eq(BigInteger a, long b) {
        if (a == null) {
            return (b == QueryConstants.NULL_LONG);
        }
        if (b == QueryConstants.NULL_LONG) {
            return false;
        }
        return a.compareTo(BigInteger.valueOf(b)) == 0;
    }

    public static boolean eq(long a, BigInteger b) {
        if (a == QueryConstants.NULL_LONG) {
            return (b == null);
        }
        if (b == null) {
            return false;
        }
        return BigInteger.valueOf(a).compareTo(b) == 0;
    }

    public static boolean eq(BigInteger a, int b) {
        if (a == null) {
            return (b == QueryConstants.NULL_INT);
        }
        if (b == QueryConstants.NULL_INT) {
            return false;
        }
        return a.compareTo(BigInteger.valueOf(b)) == 0;
    }

    public static boolean eq(int a, BigInteger b) {
        if (a == QueryConstants.NULL_INT) {
            return (b == null);
        }
        if (b == null) {
            return false;
        }
        return BigInteger.valueOf(a).compareTo(b) == 0;
    }

    public static boolean eq(BigInteger a, short b) {
        if (a == null) {
            return (b == QueryConstants.NULL_SHORT);
        }
        if (b == QueryConstants.NULL_SHORT) {
            return false;
        }
        return a.compareTo(BigInteger.valueOf(b)) == 0;
    }

    public static boolean eq(short a, BigInteger b) {
        if (a == QueryConstants.NULL_SHORT) {
            return (b == null);
        }
        if (b == null) {
            return false;
        }
        return BigInteger.valueOf(a).compareTo(b) == 0;
    }

    public static boolean eq(BigInteger a, byte b) {
        if (a == null) {
            return (b == QueryConstants.NULL_BYTE);
        }
        if (b == QueryConstants.NULL_BYTE) {
            return false;
        }
        return a.compareTo(BigInteger.valueOf(b)) == 0;
    }

    public static boolean eq(byte a, BigInteger b) {
        if (a == QueryConstants.NULL_BYTE) {
            return (b == null);
        }
        if (b == null) {
            return false;
        }
        return BigInteger.valueOf(a).compareTo(b) == 0;
    }

    public static boolean eq(BigInteger a, double b) {
        if (Double.isNaN(b)) {
            return false;
        }
        if (a == null) {
            return (b == QueryConstants.NULL_DOUBLE);
        }
        if (b == QueryConstants.NULL_DOUBLE) {
            return false;
        }
        return new BigDecimal(a).compareTo(BigDecimal.valueOf(b)) == 0;
    }

    public static boolean eq(double a, BigInteger b) {
        if (Double.isNaN(a)) {
            return false;
        }
        if (a == QueryConstants.NULL_DOUBLE) {
            return (b == null);
        }
        if (b == null) {
            return false;
        }
        return BigDecimal.valueOf(a).compareTo(new BigDecimal(b)) == 0;
    }

    public static boolean eq(BigInteger a, float b) {
        if (Float.isNaN(b)) {
            return false;
        }
        if (a == null) {
            return (b == QueryConstants.NULL_FLOAT);
        }
        if (b == QueryConstants.NULL_FLOAT) {
            return false;
        }
        return new BigDecimal(a).compareTo(BigDecimal.valueOf(b)) == 0;
    }

    public static boolean eq(float a, BigInteger b) {
        if (Float.isNaN(a)) {
            return false;
        }
        if (a == QueryConstants.NULL_FLOAT) {
            return (b == null);
        }
        if (b == null) {
            return false;
        }
        return BigDecimal.valueOf(a).compareTo(new BigDecimal(b)) == 0;
    }

    public static int compareTo(BigInteger a, BigInteger b) {
        if (a == null) {
            return (b == null) ? 0 : -1;
        }
        if (b == null) {
            return 1;
        }
        return a.compareTo(b);
    }

    public static int compareTo(BigInteger a, long b) {
        if (a == null) {
            return (b == QueryConstants.NULL_LONG) ? 0 : -1;
        }
        if (b == QueryConstants.NULL_LONG) {
            return 1;
        }
        return a.compareTo(BigInteger.valueOf(b));
    }

    public static int compareTo(long a, BigInteger b) {
        if (a == QueryConstants.NULL_LONG) {
            return (b == null) ? 0 : -1;
        }
        if (b == null) {
            return 1;
        }
        return BigInteger.valueOf(a).compareTo(b);
    }

    public static int compareTo(BigInteger a, int b) {
        if (a == null) {
            return (b == QueryConstants.NULL_INT) ? 0 : -1;
        }
        if (b == QueryConstants.NULL_INT) {
            return 1;
        }
        return a.compareTo(BigInteger.valueOf(b));
    }

    public static int compareTo(int a, BigInteger b) {
        if (a == QueryConstants.NULL_INT) {
            return (b == null) ? 0 : -1;
        }
        if (b == null) {
            return 1;
        }
        return BigInteger.valueOf(a).compareTo(b);
    }

    public static int compareTo(BigInteger a, short b) {
        if (a == null) {
            return (b == QueryConstants.NULL_SHORT) ? 0 : -1;
        }
        if (b == QueryConstants.NULL_SHORT) {
            return 1;
        }
        return a.compareTo(BigInteger.valueOf(b));
    }

    public static int compareTo(short a, BigInteger b) {
        if (a == QueryConstants.NULL_SHORT) {
            return (b == null) ? 0 : -1;
        }
        if (b == null) {
            return 1;
        }
        return BigInteger.valueOf(a).compareTo(b);
    }

    public static int compareTo(BigInteger a, byte b) {
        if (a == null) {
            return (b == QueryConstants.NULL_BYTE) ? 0 : -1;
        }
        if (b == QueryConstants.NULL_BYTE) {
            return 1;
        }
        return a.compareTo(BigInteger.valueOf(b));
    }

    public static int compareTo(byte a, BigInteger b) {
        if (a == QueryConstants.NULL_BYTE) {
            return (b == null) ? 0 : -1;
        }
        if (b == null) {
            return 1;
        }
        return BigInteger.valueOf(a).compareTo(b);
    }

    public static int compareTo(BigInteger a, double b) {
        if (Double.isNaN(b)) {
            return -1; // even if a == null.
        }
        if (a == null) {
            return (b == QueryConstants.NULL_DOUBLE) ? 0 : -1;
        }
        if (b == QueryConstants.NULL_DOUBLE) {
            return 1;
        }
        return new BigDecimal(a).compareTo(BigDecimal.valueOf(b));
    }

    public static int compareTo(double a, BigInteger b) {
        if (Double.isNaN(a)) {
            return 1; // even if b == null.
        }
        if (a == QueryConstants.NULL_DOUBLE) {
            return (b == null) ? 0 : -1;
        }
        if (b == null) {
            return 1;
        }
        return BigDecimal.valueOf(a).compareTo(new BigDecimal(b));
    }

    public static int compareTo(BigInteger a, float b) {
        if (Float.isNaN(b)) {
            return -1; // even if a == null.
        }
        if (a == null) {
            return (b == QueryConstants.NULL_FLOAT) ? 0 : -1;
        }
        if (b == QueryConstants.NULL_FLOAT) {
            return 1;
        }
        return new BigDecimal(a).compareTo(BigDecimal.valueOf(b));
    }

    public static int compareTo(float a, BigInteger b) {
        if (Float.isNaN(a)) {
            return 1; // even if b == null
        }
        if (a == QueryConstants.NULL_FLOAT) {
            return (b == null) ? 0 : -1;
        }
        if (b == null) {
            return 1;
        }
        return BigDecimal.valueOf(a).compareTo(new BigDecimal(b));
    }

    public static boolean less(BigInteger a, BigInteger b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(BigInteger a, long b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(long a, BigInteger b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(BigInteger a, int b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(int a, BigInteger b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(BigInteger a, short b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(short a, BigInteger b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(BigInteger a, byte b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(byte a, BigInteger b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(BigInteger a, double b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(double a, BigInteger b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(BigInteger a, float b) {
        return compareTo(a, b) < 0;
    }

    public static boolean less(float a, BigInteger b) {
        return compareTo(a, b) < 0;
    }

    public static boolean lessEquals(BigInteger a, BigInteger b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(BigInteger a, long b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(long a, BigInteger b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(BigInteger a, int b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(int a, BigInteger b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(BigInteger a, short b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(short a, BigInteger b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(BigInteger a, byte b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(byte a, BigInteger b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(BigInteger a, double b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(double a, BigInteger b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(BigInteger a, float b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean lessEquals(float a, BigInteger b) {
        return compareTo(a, b) <= 0;
    }

    public static boolean greater(BigInteger a, BigInteger b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(BigInteger a, long b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(long a, BigInteger b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(BigInteger a, int b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(int a, BigInteger b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(BigInteger a, short b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(short a, BigInteger b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(BigInteger a, byte b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(byte a, BigInteger b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(BigInteger a, double b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(double a, BigInteger b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(BigInteger a, float b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greater(float a, BigInteger b) {
        return compareTo(a, b) > 0;
    }

    public static boolean greaterEquals(BigInteger a, BigInteger b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(BigInteger a, long b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(long a, BigInteger b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(BigInteger a, int b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(int a, BigInteger b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(BigInteger a, short b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(short a, BigInteger b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(BigInteger a, byte b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(byte a, BigInteger b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(BigInteger a, double b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(double a, BigInteger b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(BigInteger a, float b) {
        return compareTo(a, b) >= 0;
    }

    public static boolean greaterEquals(float a, BigInteger b) {
        return compareTo(a, b) >= 0;
    }
}
