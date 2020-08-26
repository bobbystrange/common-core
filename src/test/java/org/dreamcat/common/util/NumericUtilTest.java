package org.dreamcat.common.util;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Create by tuke on 2020/4/4
 */
public class NumericUtilTest {

    @Test
    public void test() {
        for (int i = 0; i < 100; i++) {
            long v = i * i;
            BigInteger bi = BigInteger.valueOf(v);
            System.out.println(v);
            System.out.println(Arrays.toString(NumericUtil.digit(v)));
            System.out.println(Arrays.toString(NumericUtil.digit(bi)));
        }

        // ob01_10_01
        int[] digits = NumericUtil.digit(0b11_00_11 ^ 0b10_10_10, 2);
        ArrayUtil.reverse(digits);
        System.out.println(Arrays.toString(digits));
    }
}
