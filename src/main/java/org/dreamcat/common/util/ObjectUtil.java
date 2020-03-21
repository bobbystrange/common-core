package org.dreamcat.common.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class ObjectUtil {

    public static <T> boolean isEmpty(String o) {
        return !isNotEmpty(o);
    }

    public static boolean isEmpty(Collection<?> o) {
        return !isNotEmpty(o);
    }

    public static boolean isEmpty(Map<?, ?> o) {
        return !isNotEmpty(o);
    }

    public static <T> boolean isEmpty(T[] o) {
        return !isNotEmpty(o);
    }

    public static boolean isEmpty(int[] o) {
        return !isNotEmpty(o);
    }

    public static boolean isEmpty(long[] o) {
        return !isNotEmpty(o);
    }

    public static boolean isEmpty(double[] o) {
        return !isNotEmpty(o);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static boolean isNotEmpty(String o) {
        return o != null && !o.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> o) {
        return o != null && !o.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> o) {
        return o != null && !o.isEmpty();
    }

    public static <T> boolean isNotEmpty(T[] o) {
        return o != null && o.length != 0;
    }

    public static boolean isNotEmpty(int[] o) {
        return o != null && o.length != 0;
    }

    public static boolean isNotEmpty(long[] o) {
        return o != null && o.length != 0;
    }

    public static boolean isNotEmpty(double[] o) {
        return o != null && o.length != 0;
    }

    public static boolean isBlank(String string) {
        return !isNotBlank(string);
    }

    public static boolean isNotBlank(String string) {
        return string != null && !string.trim().isEmpty();
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static <T> T requireNotNull(T o, String name) {
        if (o == null) {
            throw new IllegalArgumentException(name + " is null");
        }
        return o;
    }

    public static <T> String requireNotEmpty(String o, String name) {
        if (isEmpty(o)) {
            throw new IllegalArgumentException(name + " is empty");
        }
        return o;
    }

    public static <T extends Collection<?>> T requireNotEmpty(T o, String name) {
        if (isEmpty(o)) {
            throw new IllegalArgumentException(name + " is empty");
        }
        return o;
    }

    public static <T extends Map<?, ?>> T requireNotEmpty(T o, String name) {
        if (isEmpty(o)) {
            throw new IllegalArgumentException(name + " is empty");
        }
        return o;
    }

    public static <T> T[] requireNotEmpty(T[] o, String name) {
        if (isEmpty(o)) {
            throw new IllegalArgumentException(name + " is empty");
        }
        return o;
    }

    public static <T> int[] requireNotEmpty(int[] o, String name) {
        if (isEmpty(o)) {
            throw new IllegalArgumentException(name + " is empty");
        }
        return o;
    }

    public static <T> long[] requireNotEmpty(long[] o, String name) {
        if (isEmpty(o)) {
            throw new IllegalArgumentException(name + " is empty");
        }
        return o;
    }

    public static <T> double[] requireNotEmpty(double[] o, String name) {
        if (isEmpty(o)) {
            throw new IllegalArgumentException(name + " is empty");
        }
        return o;
    }

    public static String requireNotBlank(String o, String name) {
        if (isBlank(o)) {
            throw new IllegalArgumentException(name + " is blank");
        }
        return o;
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static int requirePositive(int o, String name) {
        if (o <= 0) {
            throw new IllegalArgumentException(name + " must be a positive number");
        }
        return o;
    }

    public static long requirePositive(long o, String name) {
        if (o <= 0) {
            throw new IllegalArgumentException(name + " must be a positive number");
        }
        return o;
    }

    public static double requirePositive(double o, String name) {
        if (o <= 0) {
            throw new IllegalArgumentException(name + " must be a positive number");
        }
        return o;
    }

    public static int requireNotNegative(int o, String name) {
        if (o < 0) {
            throw new IllegalArgumentException(name + " must be a positive number or zero");
        }
        return o;
    }

    public static long requireNotNegative(long o, String name) {
        if (o < 0) {
            throw new IllegalArgumentException(name + " must be a positive number or zero");
        }
        return o;
    }

    public static double requireNotNegative(double o, String name) {
        if (o < 0) {
            throw new IllegalArgumentException(name + " must be a positive number or zero");
        }
        return o;
    }

    public static int requireNegative(int o, String name) {
        if (o >= 0) {
            throw new IllegalArgumentException(name + " must be a negative number");
        }
        return o;
    }

    public static long requireNegative(long o, String name) {
        if (o >= 0) {
            throw new IllegalArgumentException(name + " must be a negative number");
        }
        return o;
    }

    public static double requireNegative(double o, String name) {
        if (o >= 0) {
            throw new IllegalArgumentException(name + " must be a negative number");
        }
        return o;
    }

    public static int requireNotPositive(int o, String name) {
        if (o >= 0) {
            throw new IllegalArgumentException(name + " must be a negative number or zero");
        }
        return o;
    }

    public static long requireNotPositive(long o, String name) {
        if (o >= 0) {
            throw new IllegalArgumentException(name + " must be a negative number or zero");
        }
        return o;
    }

    public static double requireNotPositive(double o, String name) {
        if (o >= 0) {
            throw new IllegalArgumentException(name + " must be a negative number or zero");
        }
        return o;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static <T> T requireBelong(T elem, Collection<T> collection) {
        if (isEmpty(collection)) return elem;
        if (!collection.contains(elem)) {

            throw new IllegalArgumentException("require , but got "
                    + elem);
        }
        return elem;
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static int requireEqualSize(int[] xData, int[] yData) {
        if (xData.length != yData.length) {
            throw new IllegalArgumentException("require same size for xData and yData, but got "
                    + xData.length + " and " + yData.length);
        }
        return xData.length;
    }

    public static <T> int requireEqualSize(T[] xData, int[] yData) {
        if (xData.length != yData.length) {
            throw new IllegalArgumentException("require same size for xData and yData, but got "
                    + xData.length + " and " + yData.length);
        }
        return xData.length;
    }

    public static int requireEqualSize(long[] xData, long[] yData) {
        if (xData.length != yData.length) {
            throw new IllegalArgumentException("require same size for xData and yData, but got "
                    + xData.length + " and " + yData.length);
        }
        return xData.length;
    }

    public static <T> int requireEqualSize(T[] xData, long[] yData) {
        if (xData.length != yData.length) {
            throw new IllegalArgumentException("require same size for xData and yData, but got "
                    + xData.length + " and " + yData.length);
        }
        return xData.length;
    }

    public static int requireEqualSize(double[] xData, double[] yData) {
        if (xData.length != yData.length) {
            throw new IllegalArgumentException("require same size for xData and yData, but got "
                    + xData.length + " and " + yData.length);
        }
        return xData.length;
    }

    public static <T> int requireEqualSize(T[] xData, double[] yData) {
        if (xData.length != yData.length) {
            throw new IllegalArgumentException("require same size for xData and yData, but got "
                    + xData.length + " and " + yData.length);
        }
        return xData.length;
    }

    public static <T> int requireEqualSize(T[] xData, T[] yData) {
        if (xData.length != yData.length) {
            throw new IllegalArgumentException("require same size for xData and yData, but got "
                    + xData.length + " and " + yData.length);
        }
        return xData.length;
    }

    public static <T1, T2> int requireEqualSize(Collection<T1> xData, Collection<T2> yData) {
        if (xData.size() != yData.size()) {
            throw new IllegalArgumentException("require same size for xData and yData, but got "
                    + xData.size() + " and " + yData.size());
        }
        return xData.size();
    }

    public static <T1, T2> int requireEqualSize(Collection<T1> xData, T2[] yData) {
        if (xData.size() != yData.length) {
            throw new IllegalArgumentException("require same size for xData and yData, but got "
                    + xData.size() + " and " + yData.length);
        }
        return xData.size();
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static <T> T firstElement(List<T> o) {
        return elementAt(o, 0);
    }

    public static <T> T firstElement(T[] o) {
        return elementAt(o, 0);
    }

    public static <T> T lastElement(List<T> o) {
        if (isEmpty(o)) return null;
        return o.get(o.size() - 1);
    }

    public static <T> T lastElement(T[] o) {
        if (isEmpty(o)) return null;
        return o[o.length - 1];
    }

    public static <T> T elementAt(List<T> o, int index) {
        if (isEmpty(o)) return null;
        if (index < 0 || index >= o.size()) return null;
        return o.get(index);
    }

    public static <T> T elementAt(T[] o, int index) {
        if (isEmpty(o)) return null;
        if (index < 0 || index >= o.length) return null;
        return o[index];
    }
}
