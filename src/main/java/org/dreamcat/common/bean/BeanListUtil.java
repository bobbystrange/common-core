package org.dreamcat.common.bean;

import org.dreamcat.common.annotation.Nullable;
import org.dreamcat.common.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2019-01-23
 */
public class BeanListUtil {
    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====
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

        List<Field> fields = new ArrayList<>();
        ReflectUtil.retrieveFields(fields, clazz);

        return fields.stream()
                .filter(field -> {
                    boolean exclude = ReflectUtil.isStaticAndHasExtraModifiersAndAnyAnnotation(field, extraExcludeModifiers, excludeAnnotations);
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

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static void retrieveExpandedList(
            List<Object> list,
            Object bean,
            @Nullable Class<? extends Annotation>[] excludeAnnotations,
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
        if (level < 1 || level > 256) throw new IllegalArgumentException("level must belong [1-256] but got " + level);
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

}
