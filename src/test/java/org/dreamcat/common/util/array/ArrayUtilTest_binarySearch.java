package org.dreamcat.common.util.array;

import org.dreamcat.common.Pair;
import org.dreamcat.common.util.ArrayUtil;
import org.junit.Test;

/**
 * Create by tuke on 2020/11/15
 */
public class ArrayUtilTest_binarySearch {

    @Test
    public void binSearchTest() {
        int i;
        // 14
        long[] a = new long[]{0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130};

        Pair<Boolean, Integer> result = Pair.empty();
        i = ArrayUtil.binarySearch(a, 14, result);
        assert i < 0 && result.second() == 2 && 14 <= a[2];

        result = Pair.empty();
        i = ArrayUtil.binarySearch(a, 20, result);
        assert i == 2 && 14 <= a[2];

        result = Pair.empty();
        ArrayUtil.binarySearch(a, 21, result);
        assert result.second() == 3 && 21 <= a[3];

        result = Pair.empty();
        i = ArrayUtil.binarySearch(a, 130, result);
        assert i == 13 && 130 <= a[13];

        result = Pair.empty();
        ArrayUtil.binarySearch(a, 131, result);
        // 14
        assert result.second() == a.length;

        int[] b = new int[]{0, 1};
        result = Pair.empty();
        i = ArrayUtil.binarySearch(b, 2, result);
        assert i < 0 && result.second() == b.length;

        result = Pair.empty();
        i = ArrayUtil.binarySearch(b, -1, result);
        assert i < 0 && result.second() == 0;

        b = new int[]{0};
        result = Pair.empty();
        i = ArrayUtil.binarySearch(b, 1, result);
        assert i < 0 && result.second() == b.length;

        result = Pair.empty();
        i = ArrayUtil.binarySearch(b, -1, result);
        assert i < 0 && result.second() == 0;
    }

}
