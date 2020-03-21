package org.dreamcat.common.util;

import org.junit.Test;

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
}

