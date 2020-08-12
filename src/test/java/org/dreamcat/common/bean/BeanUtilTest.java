package org.dreamcat.common.bean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.core.Pair;
import org.dreamcat.common.core.Timeit;
import org.dreamcat.common.core.Triple;
import org.dreamcat.common.util.ReflectUtil;
import org.dreamcat.test.BeanData;
import org.dreamcat.test.BeanUnion;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.dreamcat.common.bean.BeanFormatUtil.pretty;
import static org.dreamcat.common.util.FormatUtil.*;

@Slf4j
public class BeanUtilTest {

    @Test
    public void box() {
        Object obj = 2;
        log.info(obj.getClass().getName());
    }

    @Test
    public void nullify() {
        BeanData.Pojo obj = BeanData.ofPojo();
        BeanUtil.nullify(obj);
        println(pretty(obj));
    }

    @Test
    public void testNotSupportDeepClone() {
        BeanData.Pojo source = BeanData.ofPojo();
        BeanData.Pojo target = BeanUtil.copy(source, BeanData.Pojo.class);
        println(pretty(source));
        println(pretty(target));
        target.getD()[0] = -1;
        assert source.getD()[0] == -1;
    }

    @Test
    public void toMap() {
        Query query = query();
        log("{}", BeanFormatUtil.pretty(BeanUtil.toMap(query)));
        log("{}", BeanFormatUtil.pretty(BeanUtil.toProps(query)));
    }

    @Test
    public void toMapSpeed() {
        println("\t \t field\t\t getter");
        for (int i = 1; i < 1024; i *= 2) {
            String ts = Timeit.ofActions()
                    .addUnaryAction(this::query, BeanUtil::toMap)
                    .addUnaryAction(this::query, BeanUtilTest::toMapByGetter)
                    .count(20)
                    .skip(4)
                    .repeat(i)
                    .runAndFormatUs();
            printf("%4d \t %s\n", i, ts);
        }
    }

    @Test
    public void toList() {
        List<Object> list;
        BeanData.Pojo obj = BeanData.ofPojo();

        list = BeanUtil.toList(obj);
        log.info("\n{}", BeanFormatUtil.pretty(list));
        assert list.size() == 6;
    }

    @Test
    public void toList2() {
        List<Object> list;
        BeanData.Pojo obj = BeanData.ofPojo();

        list = BeanUtil.toList(obj, BeanData.Ann.class);
        log.info("\n{}", BeanFormatUtil.pretty(list));
    }

    @Test
    public void toList3() {
        List<Object> list;
        BeanData.Pojo obj = BeanData.ofPojo();

        list = BeanUtil.toList(obj,
                Modifier.PROTECTED | Modifier.VOLATILE, BeanData.Ann.class);
        log.info("\n{}", BeanFormatUtil.pretty(list));
    }

    @Test
    public void retrieveExpandedList() {
        List<Object> list;
        BeanUnion obj = BeanUnion.newInstance();
        list = new ArrayList<>();
        BeanUtil.retrieveExpandedList(list, obj, null);
        log.info("\n{}", BeanFormatUtil.pretty(list));

        list = new ArrayList<>();
        BeanUtil.retrieveExpandedList(list, obj, null, BeanUnion.BeanBlock1.class);
        log.info("\n\n{}", BeanFormatUtil.pretty(list));

        list = new ArrayList<>();
        BeanUtil.retrieveExpandedList(list, obj, null,
                BeanUnion.BeanBlock1.class, BeanUnion.BeanBlock2.class, BeanUnion.BeanBlock3.class);
        log.info("\n\n{}", BeanFormatUtil.pretty(list));
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private Query query() {
        Query query = new Query();
        query.setDigest("A");
        query.setNonce(1);
        query.setTimestamp(System.currentTimeMillis());
        return query;
    }

    @Data
    private static class Query {
        private String digest;
        private Integer nonce;
        private Long timestamp;
        private String sign;
    }

    // reflect method
    private static Map<String, Object> toMapByGetter(Object bean) {
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

}
