package org.dreamcat.common.util;

import org.dreamcat.common.core.WriteResult;
import org.junit.Test;

import java.util.Arrays;

import static org.dreamcat.common.bean.BeanFormatUtil.pretty;
import static org.dreamcat.common.util.PrintUtil.println;

/**
 * Create by tuke on 2020/3/11
 */
public class ArrayUtilTest {
    @Test
    public void concatTest() {
        Integer[] a1 = new Integer[]{1, 2, 3};
        Double[] a2 = new Double[]{3.14, 2.72, 0.618, 1.414};
        Number[] a = ArrayUtil.concat(a1, a2, Number.class);
        println(pretty(a));
    }

    @Test
    public void concatTest2() {
        Double[] a1 = new Double[]{1.0, 2.0, 3.0};
        Double[] a2 = new Double[]{3.14, 2.72, 0.618, 1.414};
        Double[] a = ArrayUtil.concat(a1, a2, a1, a1, a2);
        println(pretty(a));
    }

    @Test
    public void reverseTest() {
        int[] a1 = new int[]{1, 2, 3};
        Double[] a2 = new Double[]{3.14, 2.72, 0.618, 1.414};
        ArrayUtil.reverse(a1);
        ArrayUtil.reverse(a2);
        println(pretty(a1), Arrays.toString(a2));
    }

    @Test
    public void binSearchTest() {
        int i;
        // 14
        long[] a = new long[]{0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130};

        WriteResult<Integer> result = WriteResult.empty();
        i = ArrayUtil.binarySearch(a, 14, result);
        assert i < 0 && result.data == 2 && 14 <= a[2];

        result = WriteResult.empty();
        i = ArrayUtil.binarySearch(a, 20, result);
        assert i == 2 && 14 <= a[2];

        result = WriteResult.empty();
        ArrayUtil.binarySearch(a, 21, result);
        assert result.data == 3 && 21 <= a[3];

        result = WriteResult.empty();
        i = ArrayUtil.binarySearch(a, 130, result);
        assert i == 13 && 130 <= a[13];

        result = WriteResult.empty();
        ArrayUtil.binarySearch(a, 131, result);
        // 14
        assert result.data == a.length;

        int[] b = new int[]{0, 1};
        result = WriteResult.empty();
        i = ArrayUtil.binarySearch(b, 2, result);
        assert i < 0 && result.data == b.length;

        result = WriteResult.empty();
        i = ArrayUtil.binarySearch(b, -1, result);
        assert i < 0 && result.data == 0;

        b = new int[]{0};
        result = WriteResult.empty();
        i = ArrayUtil.binarySearch(b, 1, result);
        assert i < 0 && result.data == b.length;

        result = WriteResult.empty();
        i = ArrayUtil.binarySearch(b, -1, result);
        assert i < 0 && result.data == 0;
    }
}

