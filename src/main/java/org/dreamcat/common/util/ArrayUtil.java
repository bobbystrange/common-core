package org.dreamcat.common.util;

import org.dreamcat.common.function.IntToByteFunction;

import java.lang.reflect.Array;
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

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    @SuppressWarnings("unchecked")
    public static <T, T1 extends T, T2 extends T> T[] concat(T1[] a1, T2[] a2, Class<T> newType) {
        if (a1 == null && a2 == null) return null;
        if (a1 == null) {
            return Arrays.copyOf(a2, a2.length);
        }
        if (a2 == null) {
            return Arrays.copyOf(a1, a1.length);
        }

        T[] a = (T[]) Array.newInstance(newType, a1.length + a2.length);
        System.arraycopy(a1, 0, a, 0, a1.length);
        System.arraycopy(a2, 0, a, a1.length, a2.length);
        return a;
    }

    public static <T> T[] concat(T[] a1, T[] a2) {
        if (a1 == null && a2 == null) return null;
        if (a1 == null) {
            return Arrays.copyOf(a2, a2.length);
        }
        if (a2 == null) {
            return Arrays.copyOf(a1, a1.length);
        }

        T[] a = Arrays.copyOf(a1, a1.length + a2.length);
        System.arraycopy(a2, 0, a, a1.length, a2.length);
        return a;
    }

    public static <T> T[] concat(T[] a1, T[] a2, T[] a3) {
        return concat(concat(a1, a2), a3);
    }

    public static <T> T[] concat(T[] a1, T[] a2, T[] a3, T[] a4) {
        return concat(concat(a1, a2, a3), a4);

    }

    public static <T> T[] concat(T[] a1, T[] a2, T[] a3, T[] a4, T[] a5) {
        return concat(concat(a1, a2, a3, a4), a5);
    }

}
