package org.dreamcat.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public class NumberUtil {

    public static int[] digit(int decimal, int base) {
        List<Integer> list = new ArrayList<>();
        int remainder = decimal;
        while (remainder != 0) {
            list.add(remainder % base);
            remainder = remainder / base;
        }
        Collections.reverse(list);
        int size = list.size();
        int[] digits = new int[size];
        for (int i = 0; i < size; i++) {
            digits[i] = list.get(i);
        }
        return digits;
    }

    public static <T extends Number> T multiply(Number lhs, Number rhs, Class<T> clazz) {
        return computer(lhs, rhs, clazz, BigDecimal::multiply);
    }

    public static <T extends Number> T divide(Number lhs, Number rhs, Class<T> clazz) {
        return computer(lhs, rhs, clazz, BigDecimal::divide);
    }

    public static <T extends Number> T subtract(Number lhs, Number rhs, Class<T> clazz) {
        return computer(lhs, rhs, clazz, BigDecimal::subtract);
    }

    public static <T extends Number> T add(Number lhs, Number rhs, Class<T> clazz) {
        return computer(lhs, rhs, clazz, BigDecimal::add);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Number> T toNumber(BigDecimal bigDecimal, Class<T> clazz)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (clazz.equals(Integer.class)) {
            Method method = clazz.getMethod("valueOf", int.class);
            return (T) method.invoke(null, bigDecimal.intValue());
        } else if (clazz.equals(Double.class)) {
            Method method = clazz.getMethod("valueOf", double.class);
            return (T) method.invoke(null, bigDecimal.doubleValue());
        } else if (clazz.equals(Long.class)) {
            Method method = clazz.getMethod("valueOf", long.class);
            return (T) method.invoke(null, bigDecimal.longValue());
        } else if (clazz.equals(Float.class)) {
            Method method = clazz.getMethod("valueOf", float.class);
            return (T) method.invoke(null, bigDecimal.floatValue());
        } else {
            throw new IllegalArgumentException("args clazz is wrong");
        }
    }

    private static <T extends Number> T computer(Number lhs, Number rhs, Class<T> clazz, BiFunction<BigDecimal, BigDecimal, BigDecimal> function) {
        BigDecimal bd1 = new BigDecimal(lhs.toString());
        BigDecimal bd2 = new BigDecimal(rhs.toString());
        BigDecimal result = function.apply(bd1, bd2);

        try {
            return toNumber(result, clazz);
        } catch (Throwable t) {
            throw new RuntimeException(t.getMessage(), t);
        }
    }
}
