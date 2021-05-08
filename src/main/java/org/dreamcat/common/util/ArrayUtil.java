package org.dreamcat.common.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import org.dreamcat.common.core.Pair;
import org.dreamcat.common.function.IntToByteFunction;

/**
 * Create by tuke on 2020/3/3
 * <p>
 * Note that end is endIndexExclusive since We follow the common rules or JDK
 */
@SuppressWarnings({"unchecked"})
public final class ArrayUtil {

    private ArrayUtil() {
    }

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

    public static List<Integer> listRangeOf(int start, int end, int step) {
        int size = (end - start) / step + (end - start) % step;
        List<Integer> a = new ArrayList<>(size);
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
        List<Integer> list = listRangeOf(start, end, step);
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

    public static int sum(int[] a, int start, int end) {
        // just avoid the infinite loop
        ObjectUtil.requireNotNegative(end - start, "end - start");
        int sum = 0;
        for (int i = start; i < end; i++) sum += a[i];
        return sum;
    }

    public static int sumInclusive(int[] a, int start, int endInclusive) {
        // just avoid the infinite loop
        ObjectUtil.requireNotNegative(endInclusive - start, "endInclusive - start");
        int sum = 0;
        for (int i = start; i <= endInclusive; i++) sum += a[i];
        return sum;
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

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static byte[] trimEnd(byte[] a) {
        return trimEnd(a, (byte) 0);
    }

    public static byte[] trimEnd(byte[] a, byte b) {
        int offset = 0, size = a.length;
        for (int i = size - 1; i >= 0; i--) {
            if (a[i] != b) break;
            offset++;
        }
        if (offset == 0) return a;
        return Arrays.copyOf(a, size - offset);
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

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static int binarySearch(int[] a, int key, Pair<Boolean, Integer> result) {
        return binarySearch(a, 0, a.length, key, result);
    }

    public static int binarySearch(int[] a, int fromIndex, int toIndex, int key,
            Pair<Boolean, Integer> result) {
        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = a[mid];
            int cmp = midVal - key;
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                if (result != null) result.updateFirst(true);
                return mid; // key found
            }
        }

        // [-1, length - 1]
        if (result != null) {
            result.update(false, high + 1);
        }
        return -1; // key not found
    }

    public static int binarySearch(long[] a, long key, Pair<Boolean, Integer> result) {
        return binarySearch(a, 0, a.length, key, result);
    }

    public static int binarySearch(long[] a, int fromIndex, int toIndex, long key,
            Pair<Boolean, Integer> result) {
        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            long midVal = a[mid];
            long cmp = midVal - key;
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                if (result != null) result.updateFirst(true);
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
    public static <T> int binarySearch(T[] a, T key, Comparator<? super T> c,
            Pair<Boolean, Integer> result) {
        return binarySearch(a, 0, a.length, key, c, result);
    }

    public static <T> int binarySearch(T[] a, int fromIndex, int toIndex, T key,
            Comparator<? super T> c, Pair<Boolean, Integer> result) {
        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            T midVal = a[mid];
            int cmp = c.compare(midVal, key);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                if (result != null) result.updateFirst(true);
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

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static <T> T[] removeAt(T[] a, int index) {
        int length = a.length;
        if (index == 0) {
            return Arrays.copyOfRange(a, 1, length);
        }
        if (index == length - 1) {
            return Arrays.copyOf(a, length - 1);
        }
        T[] b = (T[]) Array.newInstance(a.getClass().getComponentType(), length - 1);
        System.arraycopy(a, 0, b, 0, index);
        System.arraycopy(a, index + 1, b, index, length - index - 1);
        return b;
    }

    public static int[] removeAt(int[] a, int index) {
        int length = a.length;
        if (index == 0) {
            return Arrays.copyOfRange(a, 1, length);
        }
        if (index == length - 1) {
            return Arrays.copyOf(a, length - 1);
        }
        int[] b = new int[length - 1];
        System.arraycopy(a, 0, b, 0, index);
        System.arraycopy(a, index + 1, b, index, length - index - 1);
        return b;
    }

    public static long[] removeAt(long[] a, int index) {
        int length = a.length;
        if (index == 0) {
            return Arrays.copyOfRange(a, 1, length);
        }
        if (index == length - 1) {
            return Arrays.copyOf(a, length - 1);
        }
        long[] b = new long[length - 1];
        System.arraycopy(a, 0, b, 0, index);
        System.arraycopy(a, index + 1, b, index, length - index - 1);
        return b;
    }

    public static double[] removeAt(double[] a, int index) {
        int length = a.length;
        if (index == 0) {
            return Arrays.copyOfRange(a, 1, length);
        }
        if (index == length - 1) {
            return Arrays.copyOf(a, length - 1);
        }
        double[] b = new double[length - 1];
        System.arraycopy(a, 0, b, 0, index);
        System.arraycopy(a, index + 1, b, index, length - index - 1);
        return b;
    }

    public static byte[] removeAt(byte[] a, int index) {
        int length = a.length;
        if (index == 0) {
            return Arrays.copyOfRange(a, 1, length);
        }
        if (index == length - 1) {
            return Arrays.copyOf(a, length - 1);
        }
        byte[] b = new byte[length - 1];
        System.arraycopy(a, 0, b, 0, index);
        System.arraycopy(a, index + 1, b, index, length - index - 1);
        return b;
    }

    public static char[] removeAt(char[] a, int index) {
        int length = a.length;
        if (index == 0) {
            return Arrays.copyOfRange(a, 1, length);
        }
        if (index == length - 1) {
            return Arrays.copyOf(a, length - 1);
        }
        char[] b = new char[length - 1];
        System.arraycopy(a, 0, b, 0, index);
        System.arraycopy(a, index + 1, b, index, length - index - 1);
        return b;
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static <T> T[] addAt(T[] a, int index, T elem) {
        int length = a.length;
        T[] b = (T[]) Array.newInstance(a.getClass().getComponentType(), length + 1);
        b[index] = elem;

        if (index > 0) {
            System.arraycopy(a, 0, b, 0, index);
        }
        if (index < length) {
            System.arraycopy(a, index, b, index + 1, length - index);
        }
        return b;
    }

    public static int[] addAt(int[] a, int index, int elem) {
        int length = a.length;
        int[] b = new int[length + 1];
        b[index] = elem;

        if (index > 0) {
            System.arraycopy(a, 0, b, 0, index);
        }
        if (index < length) {
            System.arraycopy(a, index, b, index + 1, length - index);
        }
        return b;
    }

    public static long[] addAt(long[] a, int index, long elem) {
        int length = a.length;
        long[] b = new long[length + 1];
        b[index] = elem;

        if (index > 0) {
            System.arraycopy(a, 0, b, 0, index);
        }
        if (index < length) {
            System.arraycopy(a, index, b, index + 1, length - index);
        }
        return b;
    }

    public static double[] addAt(double[] a, int index, double elem) {
        int length = a.length;
        double[] b = new double[length + 1];
        b[index] = elem;

        if (index > 0) {
            System.arraycopy(a, 0, b, 0, index);
        }
        if (index < length) {
            System.arraycopy(a, index, b, index + 1, length - index);
        }
        return b;
    }

    public static byte[] addAt(byte[] a, int index, byte elem) {
        int length = a.length;
        byte[] b = new byte[length + 1];
        b[index] = elem;

        if (index > 0) {
            System.arraycopy(a, 0, b, 0, index);
        }
        if (index < length) {
            System.arraycopy(a, index, b, index + 1, length - index);
        }
        return b;
    }

    public static char[] addAt(char[] a, int index, char elem) {
        int length = a.length;
        char[] b = new char[length + 1];
        b[index] = elem;

        if (index > 0) {
            System.arraycopy(a, 0, b, 0, index);
        }
        if (index < length) {
            System.arraycopy(a, index, b, index + 1, length - index);
        }
        return b;
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

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    /**
     * If the maximum subarray is a[s:e] for a[0:j],
     * then the maximum subarray is a[s:e] or a[i:j+1] for a[0:j+1].
     * Especially, a[0:1] is the maximum subarray for a[0:1],
     * so the maximum subarray for a[0:2] is a[0:1] or a[i:2].
     *
     * @param a int array
     * @return {@code new int[]{start, end}}} or {@code null}
     */
    public static int[] maximumSubarray(int[] a) {
        int size;
        if (a == null || (size = a.length) == 0) return null;
        if (size == 1) return new int[]{0, 1};

        // a[s:e] is a[0:1] for a[0:1]
        int start = 0, end = 1, sum = a[0], nextSum = 0;
        int nextStart = start;
        boolean nextEnd = false;
        for (int j = 1; j < size; j++) {
            // skip if a[j] is not positive
            if (a[j] <= 0) continue;

            for (int i = j; i >= start; i--) {
                nextSum += a[i];
                if (nextSum > sum) {
                    sum = nextSum;
                    nextStart = i;
                    nextEnd = true;
                }
            }
            nextSum = 0;
            // a[i:j+1] is the maximum, change a[s:e] to a[i:j+1]
            if (nextEnd) {
                start = nextStart;
                end = j + 1;
                nextEnd = false;
            }
        }

        return new int[]{start, end};
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static int[] partitionSort(int[] a, IntPredicate predicate) {
        int length = a.length;
        if (length <= 1) return a;

        // find the first element which cannot pass the test
        int middle = -1;
        for (int i = 0; i < length; i++) {
            if (!predicate.test(a[i])) {
                middle = i;
                break;
            }
        }
        // the first element is the last one in the array, or not found
        if (middle == length - 1 || middle == -1) return a;

        // split three region: _test_, middle, _unsorted_
        for (int i = middle + 1; i < length; i++) {
            if (predicate.test(a[i])) {
                SwapUtil.swap(a, i, middle);
                middle++;
            }
        }
        return a;
    }
}
