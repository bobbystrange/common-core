package org.dreamcat.common.util;

import java.util.List;

/**
 * Create by tuke on 2020/4/4
 */
public class SwapUtil {

    public static void swap(int[] data, int i, int j) {
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    public static void swap(int[][] data, int i1, int j1, int i2, int j2) {
        int tmp = data[i1][j1];
        data[i1][j1] = data[i2][j2];
        data[i2][j2] = tmp;
    }

    public static void swap(long[] data, int i, int j) {
        long tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    public static void swap(long[][] data, int i1, int j1, int i2, int j2) {
        long tmp = data[i1][j1];
        data[i1][j1] = data[i2][j2];
        data[i2][j2] = tmp;
    }

    public static void swap(double[] data, int i, int j) {
        double tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    public static void swap(double[][] data, int i1, int j1, int i2, int j2) {
        double tmp = data[i1][j1];
        data[i1][j1] = data[i2][j2];
        data[i2][j2] = tmp;
    }

    public static <T> void swap(T[] data, int i, int j) {
        T tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    public static <T> void swap(T[][] data, int i1, int j1, int i2, int j2) {
        T tmp = data[i1][j1];
        data[i1][j1] = data[i2][j2];
        data[i2][j2] = tmp;
    }

    public static <T> void swap(List<T> data, int i, int j) {
        T tmp = data.get(i);
        data.set(i, data.get(j));
        data.set(j, tmp);
    }
}
