package org.dreamcat.common.util;

import org.dreamcat.common.function.IntToByteFunction;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;

/**
 * Create by tuke on 2020/3/3
 */
public class ArrayUtil {

    public static int[] rangeOf(int size) {
        return rangeOf(0, size);
    }

    public static int[] rangeOf(int start, int end) {
        return rangeOf(start, end, 1);
    }

    // example: (1, 9, 2) or (1, 8, 2) thne 1,3,5,7
    // (9-1)/2 = 4, (8-1)/2 = 3
    public static int[] rangeOf(int start, int end, int step) {
        int size = (end - start) / step + (end - start) % step;
        int[] a = new int[size];
        for (int i = 0; i < size; i++) {
            a[i] = start + i * step;
        }
        return a;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static <T> T[] fromMapper(int size, IntFunction<T> mapper, IntFunction<T[]> generator) {
        return Arrays.stream(rangeOf(size))
                .mapToObj(mapper).toArray(generator);
    }

    public static int[] fromMapperAsInt(int size, IntUnaryOperator mapper) {
        return Arrays.stream(rangeOf(size))
                .map(mapper).toArray();
    }

    public static long[] fromMapperAsLong(int size, IntToLongFunction mapper) {
        return Arrays.stream(rangeOf(size))
                .mapToLong(mapper).toArray();
    }

    public static double[] fromMapperAsDouble(int size, IntToDoubleFunction mapper) {
        return Arrays.stream(rangeOf(size))
                .mapToDouble(mapper).toArray();
    }

    public static byte[] fromMapperAsByte(int size, IntToByteFunction mapper) {
        int[] range = rangeOf(size);
        byte[] a = new byte[size];
        for (int i = 0; i < size; i++) {
            a[i] = mapper.applyAsByte(i);
        }
        return a;
    }

}
