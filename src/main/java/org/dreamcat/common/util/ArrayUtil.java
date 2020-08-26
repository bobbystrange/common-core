package org.dreamcat.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dreamcat.common.core.WriteResult;
import org.dreamcat.common.function.IntToByteFunction;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;

/**
 * Create by tuke on 2020/3/3
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArrayUtil {

    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static int[] rangeOf(int size) {
        return rangeOf(0, size);
    }

    public static int[] rangeOf(int start, int end) {
        return rangeOf(start, end, 1);
    }

    // example: (1, 9, 2) or (1, 8, 2) then 1,3,5,7, (9-1)/2 = 4, (8-1)/2 = 3
    public static int[] rangeOf(int start, int end, int step) {
        int size = (end - start) / step + (end - start) % step;
        int[] a = new int[size];
        for (int i = 0; i < size; i++) {
            a[i] = start + i * step;
        }
        return a;
    }

    public static ArrayList<Integer> listRangeOf(int start, int end, int step) {
        int size = (end - start) / step + (end - start) % step;
        ArrayList<Integer> a = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            a.add(start + i * step);
        }
        return a;
    }

    public static int[] randomRangeOf(int size) {
        return randomRangeOf(0, size);
    }

    public static int[] randomRangeOf(int start, int end) {
        return randomRangeOf(start, end, 1);
    }

    public static int[] randomRangeOf(int start, int end, int step) {
        ArrayList<Integer> list = listRangeOf(start, end, step);
        int size = list.size();
        int[] a = new int[size];

        for (int i = size; i >= 1; i--) {
            int r = RandomUtil.randi(i);
            a[i - 1] = list.remove(r);
        }
        return a;
    }

    // random [0, round-1]
    public static int[] randomOrder(int bound) {

        int[] sorted = rangeOf(bound);
        int[] order = new int[bound];
        for (int i = bound; i >= 1; i--) {
            int r = RandomUtil.randi(i);
        }
        return null;
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

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

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

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

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static int[] arrange(int[] a, int[] order) {
        int size = a.length;
        int[] b = new int[size];
        for (int i = 0, len = Math.min(size, order.length); i < len; i++) {
            b[order[i]] = a[i];
        }
        return b;
    }

    public static long[] arrange(long[] a, int[] order) {
        int size = a.length;
        long[] b = new long[size];
        for (int i = 0, len = Math.min(size, order.length); i < len; i++) {
            b[order[i]] = a[i];
        }
        return b;
    }

    public static double[] arrange(double[] a, int[] order) {
        int size = a.length;
        double[] b = new double[size];
        for (int i = 0, len = Math.min(size, order.length); i < len; i++) {
            b[order[i]] = a[i];
        }
        return b;
    }

    public static byte[] arrange(byte[] a, int[] order) {
        int size = a.length;
        byte[] b = new byte[size];
        for (int i = 0, len = Math.min(size, order.length); i < len; i++) {
            b[order[i]] = a[i];
        }
        return b;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] arrange(T[] a, int[] order) {
        int size = a.length;
        T[] b = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
        for (int i = 0, len = Math.min(size, order.length); i < len; i++) {
            b[order[i]] = a[i];
        }
        return b;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static void reverse(int[] a) {
        if (a == null) return;
        int size = a.length;
        if (size < 2) return;
        int halfSize = size / 2;
        for (int i = 0; i < halfSize; i++) {
            SwapUtil.swap(a, i, size - 1 - i);
        }
    }

    public static void reverse(long[] a) {
        if (a == null) return;
        int size = a.length;
        if (size < 2) return;
        int halfSize = size / 2;
        for (int i = 0; i < halfSize; i++) {
            SwapUtil.swap(a, i, size - 1 - i);
        }
    }

    public static void reverse(double[] a) {
        if (a == null) return;
        int size = a.length;
        if (size < 2) return;
        int halfSize = size / 2;
        for (int i = 0; i < halfSize; i++) {
            SwapUtil.swap(a, i, size - 1 - i);
        }
    }

    public static void reverse(byte[] a) {
        if (a == null) return;
        int size = a.length;
        if (size < 2) return;
        int halfSize = size / 2;
        for (int i = 0; i < halfSize; i++) {
            SwapUtil.swap(a, i, size - 1 - i);
        }
    }

    public static <T> void reverse(T[] a) {
        if (a == null) return;
        int size = a.length;
        if (size < 2) return;
        int halfSize = size / 2;
        for (int i = 0; i < halfSize; i++) {
            SwapUtil.swap(a, i, size - 1 - i);
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static int binarySearch(int[] a, int key, WriteResult<Integer> result) {
        return binarySearch(a, 0, a.length, key, result);
    }

    public static int binarySearch(int[] a, int fromIndex, int toIndex, int key, WriteResult<Integer> result) {
        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = a[mid];
            int cmp = midVal - key;
            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else {
                if (result != null) result.update(true);
                return mid; // key found
            }
        }

        // [-1, length - 1]
        if (result != null) {
            result.update(false, high + 1);
        }
        return -1; // key not found
    }

    public static int binarySearch(long[] a, long key, WriteResult<Integer> result) {
        return binarySearch(a, 0, a.length, key, result);
    }

    public static int binarySearch(long[] a, int fromIndex, int toIndex, long key, WriteResult<Integer> result) {
        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            long midVal = a[mid];
            long cmp = midVal - key;
            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else {
                if (result != null) result.update(true);
                return mid; // key found
            }
        }

        // [-1, length - 1]
        if (result != null) {
            result.update(false, high + 1);
        }
        return -1; // key not found
    }

    // Note that result.data = [0, a.length], a[i] <= key when i < a.length
    public static <T> int binarySearch(T[] a, T key, Comparator<? super T> c, WriteResult<Integer> result) {
        return binarySearch(a, 0, a.length, key, c, result);
    }

    public static <T> int binarySearch(T[] a, int fromIndex, int toIndex, T key, Comparator<? super T> c, WriteResult<Integer> result) {
        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            T midVal = a[mid];
            int cmp = c.compare(midVal, key);
            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else {
                if (result != null) result.update(true);
                return mid; // key found
            }

        }

        // [-1, length - 1]
        if (result != null) {
            result.update(false, high + 1);
        }
        return -1; // key not found
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static Boolean[] boxed(boolean[] a) {
        int len = a.length;
        Boolean[] b = new Boolean[len];
        for (int i = 0; i < len; i++) {
            b[i] = a[i];
        }
        return b;
    }

    public static Byte[] boxed(byte[] a) {
        int len = a.length;
        Byte[] b = new Byte[len];
        for (int i = 0; i < len; i++) {
            b[i] = a[i];
        }
        return b;
    }

    public static Short[] boxed(short[] a) {
        int len = a.length;
        Short[] b = new Short[len];
        for (int i = 0; i < len; i++) {
            b[i] = a[i];
        }
        return b;
    }

    public static Character[] boxed(char[] a) {
        int len = a.length;
        Character[] b = new Character[len];
        for (int i = 0; i < len; i++) {
            b[i] = a[i];
        }
        return b;
    }

    public static Integer[] boxed(int[] a) {
        int len = a.length;
        Integer[] b = new Integer[len];
        for (int i = 0; i < len; i++) {
            b[i] = a[i];
        }
        return b;
    }

    public static Long[] boxed(long[] a) {
        int len = a.length;
        Long[] b = new Long[len];
        for (int i = 0; i < len; i++) {
            b[i] = a[i];
        }
        return b;
    }

    public static Float[] boxed(float[] a) {
        int len = a.length;
        Float[] b = new Float[len];
        for (int i = 0; i < len; i++) {
            b[i] = a[i];
        }
        return b;
    }

    public static Double[] boxed(double[] a) {
        int len = a.length;
        Double[] b = new Double[len];
        for (int i = 0; i < len; i++) {
            b[i] = a[i];
        }
        return b;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static <T> T get(T[] a, int index) {
        if (index >= a.length || index < 0) {
            return null;
        }
        return a[index];
    }

    public static <T> T get(List<T> a, int index) {
        if (index >= a.size() || index < 0) {
            return null;
        }
        return a.get(index);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static <T, R> List<List<R>> map(List<List<T>> list, Function<T, R> function) {
        if (list == null) return null;
        if (list.isEmpty()) return new ArrayList<>();

        List<List<R>> result = new ArrayList<>(list.size());
        for (List<T> row : list) {
            if (row == null) {
                result.add(null);
                continue;
            }

            List<R> col = new ArrayList<>(row.size());
            result.add(col);

            for (T column : row) {
                col.add(function.apply(column));
            }
        }
        return result;
    }


}
