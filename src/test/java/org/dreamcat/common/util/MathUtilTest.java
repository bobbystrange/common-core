package org.dreamcat.common.util;

import org.junit.Test;

import static org.dreamcat.common.util.PrintUtil.println;

/**
 * Create by tuke on 2020/4/24
 */
public class MathUtilTest {

    @Test
    public void fibTest() {
        long fib10 = MathUtil.fibonacci(10);
        long fib11 = MathUtil.fibonacci(11);
        println(fib10, fib11);
    }
}
