package org.dreamcat.common.util;

import org.dreamcat.common.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by tuke on 2019-01-23
 */
public class BeanMapUtil {
    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====
    // bean to map
    public static List<String> toList(Object bean) {
        return new ArrayList<>(toProp(bean).values());
    }

    public static Map<String, String> toProp(Object bean) {
        Map<String, Object> map = toMap(bean);
        return map2prop(map);
    }

    public static Map<String, String> toProp(
            Object bean, Class<? extends Annotation> aliasAnnotation) {
        Map<String, Object> map = toMap(bean, aliasAnnotation);
        return map2prop(map);
    }

    public static Map<String, String> toProp(
            Object bean, Class<? extends Annotation> aliasAnnotation,
            Class<? extends Annotation> excludeAnnotation) {
        Map<String, Object> map = toMap(bean, aliasAnnotation, excludeAnnotation);
        return map2prop(map);
    }

    public static Map<String, String> toProp(
            Object bean, Class<? extends Annotation> aliasAnnotation,
            Class<? extends Annotation> excludeAnnotation, int extraExcludeModifiers) {
        Map<String, Object> map = toMap(bean, aliasAnnotation, excludeAnnotation, extraExcludeModifiers);
        return map2prop(map);
    }

    public static Map<String, Object> toMap(
            Object bean) {
        return toMap(bean, null);
    }

    public static Map<String, Object> toMap(
            Object bean, Class<? extends Annotation> aliasAnnotation) {
        return toMap(bean, aliasAnnotation, null);
    }

    public static Map<String, Object> toMap(
            Object bean, Class<? extends Annotation> aliasAnnotation,
            Class<? extends Annotation> excludeAnnotation) {
        return toMap(bean, aliasAnnotation, excludeAnnotation,
                Modifier.TRANSIENT | Modifier.VOLATILE);
    }

    // reflect field
    public static Map<String, Object> toMap(
            Object bean, Class<? extends Annotation> aliasAnnotation,
            Class<? extends Annotation> excludeAnnotation, int extraExcludeModifiers) {
        Class<?> clazz = bean.getClass();
        Map<String, Object> map = new HashMap<>();

        Map<String, Field> fieldMap = new HashMap<>();
        ReflectUtil.retrieveFields(clazz, fieldMap, aliasAnnotation);

        fieldMap.entrySet().stream()
                .filter(entry -> {
                    Field field = entry.getValue();
                    boolean exclude = ReflectUtil.isStaticAndHasExtraModifiersAndAnyAnnotation(field, extraExcludeModifiers, excludeAnnotation);
                    return !exclude;
                })
                .forEach(entry -> {
                    String name = entry.getKey();
                    Field field = entry.getValue();
                    field.setAccessible(true);
                    Object value = null;
                    try {
                        value = field.get(bean);
                    } catch (IllegalAccessException ignored) {
                    }
                    if (value != null) {
                        map.put(name, value);
                    }
                });
        return map;
    }

    // reflect method
    public static Map<String, Object> toMapByGetter(
            Object bean, Class<? extends Annotation> aliasAnnotation) {
        Class<?> clazz = bean.getClass();

        Map<String, Object> map = new HashMap<>();
        List<Method> methodList = new ArrayList<>();
        ReflectUtil.retrieveMethods(methodList, clazz);
        List<String> fieldNames = new ArrayList<>();
        ReflectUtil.retrieveFieldNames(clazz, fieldNames, aliasAnnotation);

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

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

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

    private static Map<String, String> map2prop(Map<String, Object> map) {
        Map<String, String> prop = new HashMap<>();
        for (String key : map.keySet()) {
            Object value = map.get(key);
            if (value == null) {
                continue;
            }
            prop.put(key, value.toString());
        }
        return prop;
    }

}
