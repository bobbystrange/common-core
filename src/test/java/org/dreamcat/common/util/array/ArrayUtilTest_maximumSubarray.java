package org.dreamcat.common.util.array;

import java.util.Arrays;
import org.dreamcat.common.util.ArrayUtil;
import org.junit.Test;

/**
 * Create by tuke on 2020/11/15
 */
public class ArrayUtilTest_maximumSubarray {

    @Test
    public void maximumSubarrayTest() {
        int[] a = new int[]{
                13, -3, -25, 20, -3, -16, -23, 18, 20, -7, 12, -5, -22, 15, -4, 7};
        int[] indices = ArrayUtil.maximumSubarray(a);
        System.out.println(Arrays.toString(indices));
    }

}
