package org.dreamcat.common.util;

import java.util.Map;
import java.util.function.DoubleUnaryOperator;

/**
 * @author tuke
 */
public class MathUtil {

    private static volatile Map<Integer, Long> fibonacciCache;

    //difference quotient
    public static double diffq(double[] a, double[] b) {
        int n = a.length;
        if (n == 2) return (b[0] - b[1]) / (a[0] - a[1]);
        if (n == 1) return b[0] / a[0];

        double[] c = new double[n - 1];
        System.arraycopy(a, 0, c, 0, n - 1);
        double[] d = new double[n - 1];
        System.arraycopy(b, 0, d, 0, n - 1);
        return diffq(c, d);
    }

    public static double ploy(double[] v) {
        double sum = 0;
        int n = v.length;
        for (int k = 0; k < n; k++) {
            sum += v[k] * Math.pow(v[k], n - 1.0 - k);
        }
        return sum;
    }

    // f(x) = x, such as cos(x) = x
    public static double fxeqx(double x, DoubleUnaryOperator fx) {
        double xq = fx.applyAsDouble(x);
        if (xq == x) return x;
        return fxeqx(xq, fx);
    }

    public static long fibonacci(int n) {
        ObjectUtil.requirePositive(n, "n");
        if (fibonacciCache == null) {
            synchronized (MathUtil.class) {
                if (fibonacciCache == null) {
                    fibonacciCache = CollectionUtil.concurrentMapOf(
                            1, 1L,
                            2, 1L,
                            3, 2L,
                            4, 3L,
                            5, 5L,
                            6, 8L,
                            7, 13L,
                            8, 21L,
                            9, 34L,
                            10, 55L
                    );
                }
            }
        }

        return fibonacciCache.computeIfAbsent(n,
                k -> fibonacci(n - 1) + fibonacci(n - 2));
    }

}
