package org.dreamcat.common.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NumericUtil {

    // n=4321, pos=1,2,3,4 then return 1,2,3,4
    public static int digitAt(int n, int index) {
        return digitAt(n, index, 10);
    }

    public static int digitAt(long n, int index) {
        return digitAt(n, index, 10);
    }

    public static int digitAt(BigInteger n, int index) {
        return digitAt(n, index, 10);
    }

    public static int digitAt(int n, int index, int base) {
        if (index < 0) return 0;
        int[] digits = digit(n, base);
        if (index >= digits.length) return 0;
        return digits[index];
    }

    public static int digitAt(long n, int index, int base) {
        if (index < 0) return 0;
        int[] digits = digit(n, base);
        if (index >= digits.length) return 0;
        return digits[index];
    }

    public static int digitAt(BigInteger n, int index, int base) {
        if (index < 0) return 0;
        int[] digits = digit(n, base);
        if (index >= digits.length) return 0;
        return digits[index];
    }

    public static int[] digit(int n) {
        return digit(n, 10);
    }

    public static int[] digit(long n) {
        return digit(n, 10);
    }

    public static int[] digit(BigInteger n) {
        return digit(n, 10);
    }

    public static int[] digit(int n, int base) {
        if (base < 2) {
            throw new IllegalArgumentException("base may not < 2");
        }
        int[] digits = new int[32];
        int remainder = n;
        int i = 0;
        while (remainder != 0) {
            digits[i++] = remainder % base;
            remainder = remainder / base;
        }
        return Arrays.copyOf(digits, i);
    }

    public static int[] digit(long n, int base) {
        if (base < 2) {
            throw new IllegalArgumentException("base may not < 2");
        }
        int[] digits = new int[64];
        long remainder = n;
        int i = 0;
        while (remainder != 0) {
            digits[i++] = (int) (remainder % base);
            remainder = remainder / base;
        }
        return Arrays.copyOf(digits, i);
    }

    public static int[] digit(BigInteger n, int base) {
        if (base < 2) {
            throw new IllegalArgumentException("base may not < 2");
        }
        List<Integer> digitList = new ArrayList<>(64);
        BigInteger remainder = n;
        BigInteger b = BigInteger.valueOf(base);
        while (!remainder.equals(BigInteger.ZERO)) {
            digitList.add(remainder.remainder(b).intValue());
            remainder = remainder.divide(b);
        }

        int size = digitList.size();
        int[] digits = new int[size];
        for (int i = 0; i < size; i++) {
            digits[i] = digitList.get(i);
        }
        return digits;
    }

}
