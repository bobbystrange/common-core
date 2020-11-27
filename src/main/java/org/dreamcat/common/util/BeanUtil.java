package org.dreamcat.common.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2019-06-04
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class BeanUtil {

    @SuppressWarnings({"unchecked"})
    public static <T> T parse(String value, Class<?> targetClass) {
        Class<?> boxedTargetClass = ReflectUtil.getBoxedClass(targetClass);
        if (boxedTargetClass.equals(String.class)) {
            return (T) value;
        } else if (boxedTargetClass.equals(Boolean.class)) {
            return (T) Boolean.valueOf(value);
        } else if (boxedTargetClass.equals(Character.class)) {
            return (T) Character.valueOf(value.isEmpty() ? '\0' : value.charAt(0));
        } else if (boxedTargetClass.equals(Byte.class)) {
            return (T) Byte.valueOf(value);
        } else if (boxedTargetClass.equals(Short.class)) {
            return (T) Short.valueOf(value);
        } else if (boxedTargetClass.equals(Integer.class)) {
            return (T) Integer.valueOf(value);
        } else if (boxedTargetClass.equals(Long.class)) {
            return (T) Long.valueOf(value);
        } else if (boxedTargetClass.equals(Float.class)) {
            return (T) Float.valueOf(value);
        } else if (boxedTargetClass.equals(Double.class)) {
            return (T) Double.valueOf(value);
        } else if (boxedTargetClass.equals(Date.class)) {
            return (T) DateUtil.parseDate(value);
        } else if (boxedTargetClass.equals(LocalDate.class)) {
            return (T) DateUtil.parseLocalDate(value);
        } else if (boxedTargetClass.equals(LocalTime.class)) {
            return (T) DateUtil.parseLocalTime(value);
        } else if (boxedTargetClass.equals(LocalDateTime.class)) {
            return (T) DateUtil.parseLocalDateTime(value);
        } else {
            return null;
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

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

            Object value = parse(list.get(i), targetClass);

            fields[i].setAccessible(true);
            try {
                fields[i].set(bean, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
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
                    field.setAccessible(true);
                    Object value = null;
                    try {
                        value = field.get(bean);
                    } catch (IllegalAccessException ignored) {
                    }
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
                .map(field -> {
                    field.setAccessible(true);
                    Object value = null;
                    try {
                        value = field.get(bean);
                    } catch (IllegalAccessException ignored) {
                    }
                    return value;
                }).collect(Collectors.toList());
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
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(bean);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            map.put(name, value);
        }
        return map;
    }

    public static <T, A extends Annotation> T fromMap(Map<String, ?> map, Class<T> clazz) {
        return fromMap(map, clazz, null, null);
    }

    public static <T, A extends Annotation> T fromMap(
            Map<String, ?> map, Class<T> clazz,
            Class<A> aliasClass, Function<A, String> aliasFunction) {
        T bean;
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            if (!constructor.isAccessible()) constructor.setAccessible(true);
            bean = constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Map<String, Field> fieldMap = ReflectUtil.retrieveFieldMap(
                clazz, aliasClass, aliasFunction);

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
                Map<String, ?> valueMap = (Map<String, ?>) value;
                Object valueBean = fromMap(valueMap, type, aliasClass, aliasFunction);
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

    // bean field-based copy
    public static <T> T copy(Object source, Class<T> clazz) {
        ObjectUtil.requireNotNull(source, "source");
        T target = null;
        try {
            target = clazz.newInstance();
            copy(source, target);
        } catch (Throwable ignored) {
        }

        return target;
    }

    // slower than cglib, use it in field-based pojo (no getter/setter) only
    public static void copy(Object source, Object target) {
        Map<String, Field> sourceFieldMap = ReflectUtil.retrieveFieldMap(source.getClass());
        Map<String, Field> targetFieldMap = ReflectUtil.retrieveFieldMap(target.getClass());

        Set<String> targetFields = targetFieldMap.keySet();
        for (String sourceFieldName : sourceFieldMap.keySet()) {
            if (targetFields.contains(sourceFieldName)) {

                Field targetField = targetFieldMap.get(sourceFieldName);
                Field sourceField = sourceFieldMap.get(sourceFieldName);

                targetField.setAccessible(true);
                sourceField.setAccessible(true);
                try {
                    targetField.set(target, sourceField.get(source));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

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
