package org.dreamcat.common.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.CastUtil;
import org.dreamcat.common.util.ObjectUtil;
import org.dreamcat.common.util.ReflectUtil;

/**
 * Create by tuke on 2019-06-04
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Slf4j
public final class BeanUtil {

    private BeanUtil() {
    }

    public static <T> T fromStringList(Class<T> clazz, List<String> list) {
        T bean;
        try {
            bean = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        Field[] fields = clazz.getDeclaredFields();
        List<Class<?>> types = Arrays.stream(fields)
                .map(Field::getType)
                .collect(Collectors.toList());

        int len = list.size();
        for (int i = 0; i < len; i++) {
            Class<?> targetClass = ReflectUtil.getBoxedClass(types.get(i));
            Object value = CastUtil.parse(list.get(i), targetClass);
            ReflectUtil.setValue(bean, fields[i], value);
        }
        return bean;
    }

    public static <T> T fromStringArray(Class<T> clazz, String[] strings) {
        return fromStringList(clazz, Arrays.asList(strings));
    }

    //bean to string[]
    public static String[] toStringArray(
            Object bean,
            Class... expandClasses) {
        return toStringArray(bean, null, expandClasses);
    }

    public static String[] toStringArray(
            Object bean,
            Class<? extends Annotation>[] excludeAnnotations,
            Class... expandClasses) {
        List<Object> list = new ArrayList<>();
        retrieveExpandedList(list, bean, excludeAnnotations, expandClasses);
        return list.stream().map(Object::toString).toArray(String[]::new);
    }

    public static List<String> toStringList(
            Object bean, Class... excludeAnnotations) {
        return toStringList(bean,
                0
                , excludeAnnotations);
    }

    public static List<String> toStringList(
            Object bean, int extraExcludeModifiers
            , Class... excludeAnnotations) {
        Class<?> clazz = bean.getClass();

        List<Field> fields = ReflectUtil.retrieveFields(clazz);

        return fields.stream()
                .filter(field -> {
                    boolean exclude = ReflectUtil
                            .isStaticAndHasExtraModifiersAndAnyAnnotation(
                                    field, extraExcludeModifiers, excludeAnnotations);
                    return !exclude;
                })
                .map(field -> {
                    Object value = ReflectUtil.getValue(bean, field);
                    return String.valueOf(value);
                }).collect(Collectors.toList());
    }

    public static List<String> toStringList(
            Object bean,
            Class<? extends Annotation>[] excludeAnnotations,
            Class... expandClasses) {
        List<Object> list = new ArrayList<>();
        retrieveExpandedList(list, bean, excludeAnnotations, expandClasses);
        return list.stream().map(it -> {
            if (it == null) return "";
            else return it.toString();
        }).collect(Collectors.toList());
    }

    public static List<Object> toList(
            Object bean, Class... excludeAnnotations) {
        return toList(bean,
                0
                , excludeAnnotations);
    }

    /**
     * get each field value from bean, and create a list
     *
     * @param bean                  source
     * @param extraExcludeModifiers beside `static`,
     *                              field which has other modifiers need be excluded
     * @param excludeAnnotations    field which has annotations need be excluded
     * @return list which consists of field values
     */
    public static List<Object> toList(
            Object bean, int extraExcludeModifiers
            , Class... excludeAnnotations) {
        Class<?> clazz = bean.getClass();

        List<Field> fields = ReflectUtil.retrieveFields(clazz);

        return fields.stream()
                .filter(field -> {
                    boolean exclude = ReflectUtil
                            .isStaticAndHasExtraModifiersAndAnyAnnotation(
                                    field, extraExcludeModifiers, excludeAnnotations);
                    return !exclude;
                })
                .map(field -> ReflectUtil.getValue(bean, field))
                .collect(Collectors.toList());
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static void retrieveExpandedList(
            List<Object> list,
            Object bean,
            Class<? extends Annotation>[] excludeAnnotations,
            Class... expandClasses) {
        retrieveExpandedList(list, bean, 0, excludeAnnotations, 2, expandClasses);
    }

    /**
     * expand a object to list
     *
     * @param list                  target
     * @param bean                  source
     * @param extraExcludeModifiers excluded modifiers
     * @param excludeAnnotations    excluded field
     * @param level                 max expanded level
     * @param expandClasses         classes which need be expanded
     * @throws IllegalArgumentException if level lt 1 || level gt 256
     */
    public static void retrieveExpandedList(
            List<Object> list,
            Object bean, int extraExcludeModifiers,
            Class<? extends Annotation>[] excludeAnnotations, int level,
            Class... expandClasses) {
        if (level < 1 || level > 256) {
            throw new IllegalArgumentException("level must belong [1-256] but got " + level);
        }
        List<Object> values = toList(bean, extraExcludeModifiers, excludeAnnotations);

        for (Object value : values) {
            if (level == 1 || value == null) {
                list.add(value);
                continue;
            }

            boolean expand = ReflectUtil.hasAnySuperClass(value.getClass(), expandClasses);
            if (!expand) {
                list.add(value);
                continue;
            }
            retrieveExpandedList(list, value, extraExcludeModifiers,
                    excludeAnnotations, level - 1, expandClasses);
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static Map<String, String> toProps(Object bean) {
        return map2props(toMap(bean));
    }

    public static Map<String, String> toProps(
            Object bean, Class<? extends Annotation> excludeAnnotation) {
        return map2props(toMap(bean, excludeAnnotation));
    }

    public static <A extends Annotation> Map<String, String> toProps(
            Object bean, Class<A> aliasClass,
            Function<A, String> aliasFunction) {
        return map2props(toMap(bean, aliasClass, aliasFunction));
    }

    public static <A extends Annotation> Map<String, String> toProps(
            Object bean,
            Class<? extends Annotation> excludeAnnotation,
            Class<A> aliasClass, Function<A, String> aliasFunction) {
        return map2props(toMap(bean, excludeAnnotation, aliasClass, aliasFunction));
    }

    private static Map<String, String> map2props(Map<String, Object> map) {
        Map<String, String> prop = new HashMap<>();
        map.forEach((k, v) -> {
            if (v == null) {
                return;
            }
            prop.put(k, v.toString());
        });
        return prop;
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static Map<String, Object> toMap(
            Object bean) {
        return toMap(bean, null);
    }

    public static Map<String, Object> toMap(
            Object bean, Class<? extends Annotation> excludeAnnotation) {
        return toMap(bean, excludeAnnotation, null, null);
    }

    public static <A extends Annotation> Map<String, Object> toMap(
            Object bean, Class<A> aliasClass,
            Function<A, String> aliasFunction) {
        return toMap(bean, null, aliasClass, aliasFunction);
    }

    // reflect field
    public static <A extends Annotation> Map<String, Object> toMap(
            Object bean,
            Class<? extends Annotation> excludeAnnotation,
            Class<A> aliasClass, Function<A, String> aliasFunction) {
        Class<?> clazz = bean.getClass();
        Map<String, Object> map = new HashMap<>();
        Map<String, Field> fieldMap = ReflectUtil.retrieveFieldMap(
                clazz, aliasClass, aliasFunction);

        Set<Map.Entry<String, Field>> entrySet = fieldMap.entrySet();
        for (Map.Entry<String, Field> entry : entrySet) {
            Field field = entry.getValue();
            if (ReflectUtil.hasAnyAnnotation(field, excludeAnnotation)) continue;

            String name = entry.getKey();
            Object value = ReflectUtil.getValue(bean, field);
            map.put(name, value);
        }
        return map;
    }

    public static <T> T fromMap(Map<String, ?> map, Class<T> clazz) {
        return fromMap(map, clazz, null, null);
    }

    public static <T, A extends Annotation> T fromMap(
            Map<String, ?> map, Class<T> clazz,
            Class<A> aliasClass, Function<A, String> aliasFunction) {
        T bean = ReflectUtil.newInstance(clazz);
        Map<String, Field> fieldMap = ReflectUtil.retrieveFieldMap(
                clazz, aliasClass, aliasFunction);

        Set<Map.Entry<String, Field>> entrySet = fieldMap.entrySet();
        for (Map.Entry<String, Field> entry : entrySet) {
            String name = entry.getKey();
            Field field = entry.getValue();

            Object value = map.get(name);
            if (value == null) continue;

            Class<?> valueType = value.getClass();
            Class<?> type = field.getType();

            if (type.isAssignableFrom(valueType)) {
                ReflectUtil.setValue(bean, field, value);
            } else if (Map.class.isAssignableFrom(valueType)) {
                Map<String, ?> valueMap = (Map<String, ?>) value;
                Object valueBean = fromMap(valueMap, type, aliasClass, aliasFunction);
                ReflectUtil.setValue(bean, field, valueBean);
            } else {
                Object castValue = CastUtil.cast(value, type);
                ReflectUtil.setValue(bean, field, castValue);
            }
        }
        return bean;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    // bean field-based copy
    public static <T> T copy(Object source, Class<T> clazz) {
        ObjectUtil.requireNotNull(source, "source");
        try {
            T target = clazz.newInstance();
            copy(source, target);
            return target;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * note that <strong>slower than cglib</strong>
     * please use it in field-based pojo (no getter/setter) only
     *
     * @param source source object
     * @param target target object
     */
    public static void copy(Object source, Object target) {
        Map<String, Field> sourceFieldMap = ReflectUtil.retrieveFieldMap(source.getClass());
        Map<String, Field> targetFieldMap = ReflectUtil.retrieveFieldMap(target.getClass());

        Set<String> commonFieldNames = new HashSet<>(sourceFieldMap.keySet());
        commonFieldNames.retainAll(targetFieldMap.keySet());
        for (String commonFieldName : commonFieldNames) {
            Field targetField = targetFieldMap.get(commonFieldName);
            Field sourceField = sourceFieldMap.get(commonFieldName);
            // must is assignable
            if (!targetField.getType().isAssignableFrom(sourceField.getType())) continue;

            try {
                Object sourceFieldValue = ReflectUtil.getValue(source, sourceField);
                ReflectUtil.setValue(target, targetField, sourceFieldValue);
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.debug("set {} to {}, error is: {}",
                            sourceField, targetField, e.getMessage());
                }
            }
        }

    }

}
