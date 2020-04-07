package org.dreamcat.common.util;

import org.junit.Test;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Arrays;

import static org.dreamcat.common.util.ConsoleUtil.println;

/**
 * Create by tuke on 2020/4/4
 */
public class NumericUtilTest {

    @Test
    public void test() {
        for (int i = 0; i<100; i++) {
            long v = i*i;
            BigInteger bi = BigInteger.valueOf(v);
            println(v, Arrays.toString(NumericUtil.digit(v)), Arrays.toString(NumericUtil.digit(bi)));
        }

        // ob01_10_01
        int[] digits = NumericUtil.digit(0b11_00_11 ^ 0b10_10_10, 2);
        ArrayUtil.reverse(digits);
        println(Arrays.toString(digits));
    }
}
