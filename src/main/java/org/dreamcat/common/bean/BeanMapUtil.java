package org.dreamcat.common.bean;

import org.dreamcat.common.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Create by tuke on 2019-01-23
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class BeanMapUtil {

    public static Map<String, String> toProps(Object bean) {
        Map<String, Object> map = toMap(bean);
        return map2props(map);
    }

    public static Map<String, String> toProps(
            Object bean, Class<? extends Annotation> aliasAnnotation) {
        Map<String, Object> map = toMap(bean, aliasAnnotation);
        return map2props(map);
    }

    public static Map<String, String> toProps(
            Object bean, Class<? extends Annotation> aliasAnnotation,
            Class<? extends Annotation> excludeAnnotation) {
        Map<String, Object> map = toMap(bean, aliasAnnotation, excludeAnnotation);
        return map2props(map);
    }

    public static Map<String, String> toProps(
            Object bean, Class<? extends Annotation> aliasAnnotation,
            Class<? extends Annotation> excludeAnnotation, int extraExcludeModifiers) {
        Map<String, Object> map = toMap(bean, aliasAnnotation, excludeAnnotation, extraExcludeModifiers);
        return map2props(map);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

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

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static <T> T fromMap(Map<String, ?> map, Class<T> clazz, Class... aliasAnnotationClasses) {
        T bean;
        try {
            bean = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        Map<String, Field> fieldMap = new HashMap<>();
        ReflectUtil.retrieveFields(clazz, fieldMap, aliasAnnotationClasses);

        Set<Map.Entry<String, Field>> entrySet = fieldMap.entrySet();
        for (Map.Entry<String, Field> entry : entrySet) {
            String name = entry.getKey();
            Field field = entry.getValue();
            field.setAccessible(true);

            Object value = map.get(name);
            if (value == null) continue;

            Class<?> valueType = value.getClass();
            Class<?> type = field.getType();

            if (type.isAssignableFrom(valueType)) {
                try {
                    field.set(bean, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

            } else if (Map.class.isAssignableFrom(valueType)) {
                Map<String, Object> valueMap = (Map<String, Object>) value;
                Object valueBean = fromMap(valueMap, type, aliasAnnotationClasses);
                try {
                    field.set(bean, valueBean);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Object castValue = ReflectUtil.cast(value, type);
                try {
                    field.set(bean, castValue);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        return bean;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private static Map<String, String> map2props(Map<String, Object> map) {
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
