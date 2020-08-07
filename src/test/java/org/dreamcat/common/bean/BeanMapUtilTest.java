package org.dreamcat.common.bean;

import lombok.Data;
import org.dreamcat.common.core.Timeit;
import org.dreamcat.common.util.ReflectUtil;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.dreamcat.common.util.FormatUtil.*;

/**
 * Create by tuke on 2020/4/13
 */
public class BeanMapUtilTest {

    // reflect method
    public static Map<String, Object> toMapByGetter(Object bean) {
        Class<?> clazz = bean.getClass();

        Map<String, Object> map = new HashMap<>();
        List<Method> methodList = new ArrayList<>();
        ReflectUtil.retrieveMethods(clazz, methodList);
        List<String> fieldNames = new ArrayList<>();
        ReflectUtil.retrieveFieldNames(clazz, fieldNames);

        for (Method method : methodList) {
            Class<?> returnType = method.getReturnType();
            int modifiers = method.getModifiers();
            boolean isNotPublish = (modifiers & Modifier.PUBLIC) == 0;
            boolean isStatic = (modifiers & Modifier.STATIC) != 0;
            if (isNotPublish || isStatic || returnType.equals(Void.class)) continue;

            if (returnType.equals(Boolean.class) || returnType.equals(boolean.class)) {
                putByPrefix(map, method, bean, fieldNames, "is");
            } else {
                putByPrefix(map, method, bean, fieldNames, "get");
            }
        }
        return map;
    }

    private static void putByPrefix(
            Map<String, Object> map, Method method,
            Object bean, List<String> fieldNames, String prefix) {
        String methodName = method.getName();
        if (!methodName.startsWith(prefix)) return;

        String fieldName = methodName.substring(methodName.indexOf(prefix) + prefix.length());
        fieldName = fieldName.toLowerCase().charAt(0) + fieldName.substring(1);
        if (!fieldNames.contains(fieldName)) return;

        try {
            map.put(methodName, method.invoke(bean));
        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }

    }

    @Test
    public void test() {
        Query query = query();
        log("{}", BeanFormatUtil.pretty(BeanMapUtil.toMap(query)));
        log("{}", BeanFormatUtil.pretty(BeanMapUtil.toProps(query)));

    }

    @Test
    public void testSpeed() {
        println("\t \t field\t\t getter");
        for (int i = 1; i < 1024; i *= 2) {
            String ts = Timeit.ofActions()
                    .addUnaryAction(this::query, BeanMapUtil::toMap)
                    .addUnaryAction(this::query, BeanMapUtilTest::toMapByGetter)
                    .count(20)
                    .skip(4)
                    .repeat(i)
                    .runAndFormatUs();
            printf("%4d \t %s\n", i, ts);
        }
    }

    private Query query() {
        Query query = new Query();
        query.setDigest("A");
        query.setNonce(1);
        query.setTimestamp(System.currentTimeMillis());
        return query;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    @Data
    private static class Query {
        private String digest;
        private Integer nonce;
        private Long timestamp;
        private String sign;
    }

}
