package com.tukeof.common.util;

import java.util.Collection;
import java.util.Map;

public final class ObjectUtil {

    public static <T> void checkNotNull(T o, String name) {
        if (o == null) {
            throw new RuntimeException(name + " is null");
        }
    }

    public static <T extends Collection<?>> void checkNotEmpty(T o, String name) {
        checkNotNull(o, name);
        if (o.isEmpty()) {
            throw new RuntimeException(name + " is empty");
        }
    }

    public static <T> void checkNotEmpty(T[] o, String name) {
        checkNotNull(o, name);
        if (o.length == 0) {
            throw new RuntimeException(name + " is empty");
        }
    }

    public static void checkNotBlank(String o, String name) {
        checkNotNull(o, name);
        if (o.trim().isEmpty()) {
            throw new RuntimeException(name + " is blank");
        }
    }

    public static <T> T requireNotNull(T o, String name) {
        checkNotNull(o, name);
        return o;
    }

    public static <T extends Collection<?>> T requireNotEmpty(T o, String name) {
        checkNotEmpty(o, name);
        return o;
    }

    public static <T> T[] requireNotEmpty(T[] o, String name) {
        checkNotEmpty(o, name);
        return o;
    }

    public static String requireNotBlank(String o, String name) {
        checkNotBlank(o, name);
        return o;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static <T> boolean isEmpty(Collection<T> o) {
        return !isNotEmpty(o);
    }

    public static boolean isEmpty(Map<?, ?> o) {
        return !isNotEmpty(o);
    }

    public static <T> boolean isEmpty(T[] o) {
        return !isNotEmpty(o);
    }

    public static <T> boolean isEmpty(int[] o) {
        return !isNotEmpty(o);
    }

    public static <T> boolean isEmpty(long[] o) {
        return !isNotEmpty(o);
    }

    public static <T> boolean isEmpty(double[] o) {
        return !isNotEmpty(o);
    }

    public static <T> boolean isNotEmpty(Collection<T> o) {
        return o != null && !o.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> o) {
        return o != null && !o.isEmpty();
    }

    public static <T> boolean isNotEmpty(T[] o) {
        return o != null && o.length != 0;
    }

    public static <T> boolean isNotEmpty(int[] o) {
        return o != null && o.length != 0;
    }

    public static <T> boolean isNotEmpty(long[] o) {
        return o != null && o.length != 0;
    }

    public static <T> boolean isNotEmpty(double[] o) {
        return o != null && o.length != 0;
    }

    public static boolean isBlank(String string) {
        return !isNotBlank(string);
    }

    public static boolean isNotBlank(String string) {
        return string != null && !string.trim().isEmpty();
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

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

}
