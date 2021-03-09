package org.dreamcat.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class NumericUtil {

    private NumericUtil() {
    }

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

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static boolean equalTo(Number a, Number b) {
        return equalTo(a, b, false);
    }

    public static boolean equalTo(Number a, Number b, boolean truncated) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        // not integer/float, then just equals
        if ((!isInteger(a) && !isFloat(a)) || (!isInteger(b) && !isFloat(b))) return a.equals(b);
        if (isFloat(a) && isInteger(b)) return equalTo(b, a, truncated);

        if (isInteger(a)) {
            if (a instanceof BigInteger) {
                if (isInteger(b)) {
                    if (b instanceof BigInteger) {
                        return a.equals(b);
                    } else {
                        return a.equals(BigInteger.valueOf(b.longValue()));
                    }
                } else {
                    if (!truncated) return false;
                    if (b instanceof BigDecimal) {
                        return ((BigDecimal) b).toBigInteger().equals(a);
                    } else {
                        return a.equals(BigInteger.valueOf(b.longValue()));
                    }
                }
            } else {
                if (isInteger(b)) {
                    if (b instanceof BigInteger) {
                        return b.equals(BigInteger.valueOf(a.longValue()));
                    } else {
                        return b.longValue() == a.longValue();
                    }
                } else {
                    if (!truncated) return false;
                    if (b instanceof BigDecimal) {
                        return ((BigDecimal) b).toBigInteger()
                                .equals(BigInteger.valueOf(a.longValue()));
                    } else {
                        return b.longValue() == a.longValue();
                    }
                }
            }
        } else {
            if (a instanceof BigDecimal) {
                if (b instanceof BigDecimal) {
                    return a.equals(b);
                } else {
                    return a.equals(BigDecimal.valueOf(b.doubleValue()));
                }
            } else {
                if (b instanceof BigDecimal) {
                    return b.equals(BigDecimal.valueOf(a.doubleValue()));
                } else {
                    return a.doubleValue() == b.doubleValue();
                }
            }
        }
    }

    public static boolean isInteger(Number n) {
        return n instanceof Byte || n instanceof Short ||
                n instanceof Integer || n instanceof Long || n instanceof BigInteger;
    }

    public static boolean isFloat(Number n) {
        return n instanceof Float || n instanceof Double || n instanceof BigDecimal;
    }

    public static Number toBigNumber(Number n) {
        if (n instanceof BigInteger || n instanceof BigDecimal) return n;
        if (isInteger(n)) {
            return BigInteger.valueOf(n.longValue());
        } else {
            return BigDecimal.valueOf(n.doubleValue());
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static String format(BigDecimal n, int scale) {
        if (n == null) return null;
        n = n.setScale(scale, RoundingMode.HALF_EVEN);
        return n.stripTrailingZeros().toPlainString();
    }
}
