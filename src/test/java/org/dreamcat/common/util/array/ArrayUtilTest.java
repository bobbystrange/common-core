package org.dreamcat.common.util.array;

import static org.dreamcat.common.bean.BeanFormatUtil.pretty;

import java.util.Arrays;
import org.dreamcat.common.util.ArrayUtil;
import org.junit.Test;

/**
 * Create by tuke on 2020/3/11
 */
public class ArrayUtilTest {

    @Test
    public void concatTest() {
        Integer[] a1 = new Integer[]{1, 2, 3};
        Double[] a2 = new Double[]{3.14, 2.72, 0.618, 1.414};
        Number[] a = ArrayUtil.concat(a1, a2, Number.class);
        System.out.println(pretty(a));
    }

    @Test
    public void concatTest2() {
        Double[] a1 = new Double[]{1.0, 2.0, 3.0};
        Double[] a2 = new Double[]{3.14, 2.72, 0.618, 1.414};
        Double[] a = ArrayUtil.concat(a1, a2, a1, a1, a2);
        System.out.println(pretty(a));
    }

    @Test
    public void reverseTest() {
        int[] a1 = new int[]{1, 2, 3};
        Double[] a2 = new Double[]{3.14, 2.72, 0.618, 1.414};
        ArrayUtil.reverse(a1);
        ArrayUtil.reverse(a2);
        System.out.println(pretty(a1) + "\n" + Arrays.toString(a2));
    }

    @Test
    public void arrangeTest() {
        int[] a1 = new int[]{0, 1, 2, 3, 4, 5};
        a1 = ArrayUtil.arrange(a1, new int[]{3, 2, 0, 4, 1, 5});
        //[2, 4, 1, 0, 3, 5]
        System.out.println(pretty(a1));

        System.out.println();
        System.out.println(Arrays.toString(ArrayUtil.randomRangeOf(6)));
        System.out.println(Arrays.toString(ArrayUtil.randomRangeOf(6)));
        System.out.println(Arrays.toString(ArrayUtil.randomRangeOf(6)));
        System.out.println(Arrays.toString(ArrayUtil.randomRangeOf(6)));
        System.out.println(Arrays.toString(ArrayUtil.randomRangeOf(6)));
        System.out.println(Arrays.toString(ArrayUtil.randomRangeOf(6)));
    }


}

