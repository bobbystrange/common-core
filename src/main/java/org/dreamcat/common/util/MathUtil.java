package org.dreamcat.common.util;

/**
 * @author tuke
 */
public class MathUtil {

    //difference quotient
    public static double diffq(double[] a, double[] b) {
        int n = a.length;
        if (n == 2) {
            return (b[0] - b[1]) / (a[0] - a[1]);
        }
        if (n == 1) {
            return b[0] / a[0];
        }

        double[] c = new double[n - 1];
        System.arraycopy(a, 0, c, 0, n - 1);
        double[] d = new double[n - 1];
        System.arraycopy(b, 0, d, 0, n - 1);
        return diffq(c, d);
    }

}
