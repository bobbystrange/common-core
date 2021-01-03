package org.dreamcat.common.util.bean;

import static org.dreamcat.common.util.BeanUtil.pretty;
import static org.dreamcat.common.util.FormatUtil.log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.core.Timeit;
import org.dreamcat.common.util.BeanUtil;
import org.dreamcat.common.util.ReflectUtil;
import org.dreamcat.test.BeanData;
import org.dreamcat.test.BeanUnion;
import org.junit.Test;

@Slf4j
public class BeanUtilTest {

    // reflect method
    private static Map<String, Object> toMapByGetter(Object bean) {
        Class<?> clazz = bean.getClass();

        Map<String, Object> map = new HashMap<>();
        List<Method> methodList = ReflectUtil.retrieveMethods(clazz);
        List<String> fieldNames = ReflectUtil.retrieveFieldNames(clazz);

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
    public void box() {
        Object obj = 2;
        log.info(obj.getClass().getName());
    }

    @Test
    public void testNotSupportDeepClone() {
        BeanData.Pojo source = BeanData.ofPojo();
        BeanData.Pojo target = BeanUtil.copy(source, BeanData.Pojo.class);
        System.out.println(pretty(source));
        System.out.println(pretty(target));
        target.getD()[0] = -1;
        assert source.getD()[0] == -1;
    }

    @Test
    public void toMap() {
        Query query = query();
        log("{}", pretty(BeanUtil.toMap(query)));
        log("{}", pretty(BeanUtil.toProps(query)));
    }

    @Test
    public void toMapSpeed() {
        System.out.println("\t \t field\t\t getter");
        for (int i = 1; i < 1024; i *= 2) {
            String ts = Timeit.ofActions()
                    .addUnaryAction(this::query, BeanUtil::toMap)
                    .addUnaryAction(this::query, BeanUtilTest::toMapByGetter)
                    .count(20)
                    .skip(4)
                    .repeat(i)
                    .runAndFormatUs();
            System.out.printf("%4d \t %s\n", i, ts);
        }
    }

    @Test
    public void toList() {
        List<Object> list;
        BeanData.Pojo obj = BeanData.ofPojo();

        list = BeanUtil.toList(obj);
        log.info("\n{}", pretty(list));
        assert list.size() == 6;
    }

    @Test
    public void toList2() {
        List<Object> list;
        BeanData.Pojo obj = BeanData.ofPojo();

        list = BeanUtil.toList(obj, BeanData.Ann.class);
        log.info("\n{}", pretty(list));
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    @Test
    public void toList3() {
        List<Object> list;
        BeanData.Pojo obj = BeanData.ofPojo();

        list = BeanUtil.toList(obj,
                Modifier.PROTECTED | Modifier.VOLATILE, BeanData.Ann.class);
        log.info("\n{}", pretty(list));
    }

    @Test
    public void retrieveExpandedList() {
        List<Object> list;
        BeanUnion obj = BeanUnion.newInstance();
        list = new ArrayList<>();
        BeanUtil.retrieveExpandedList(list, obj, null);
        log.info("\n{}", pretty(list));

        list = new ArrayList<>();
        BeanUtil.retrieveExpandedList(list, obj, null, BeanUnion.BeanBlock1.class);
        log.info("\n\n{}", pretty(list));

        list = new ArrayList<>();
        BeanUtil.retrieveExpandedList(list, obj, null,
                BeanUnion.BeanBlock1.class, BeanUnion.BeanBlock2.class, BeanUnion.BeanBlock3.class);
        log.info("\n\n{}", pretty(list));
    }

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

}
