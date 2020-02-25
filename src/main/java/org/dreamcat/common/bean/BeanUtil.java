package org.dreamcat.common.bean;

import org.dreamcat.common.annotation.Nullable;
import org.dreamcat.common.util.ObjectUtil;
import org.dreamcat.common.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2019-06-04
 */
public class BeanUtil {

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====
    // bean copy
    public static <T> T copy(Object source, Class<T> clazz) {
        ObjectUtil.checkNotNull(source, "source");
        T target = null;
        try {
            target = clazz.newInstance();
            copyProperties(source, target);
        } catch (Throwable ignored) {
        }

        return target;
    }

    public static void copyProperties(Object source, Object target) {
        Map<String, Field> sourceFieldMap = new HashMap<>();
        ReflectUtil.retrieveFields(source.getClass(), sourceFieldMap);

        Map<String, Field> targetFieldMap = new HashMap<>();
        ReflectUtil.retrieveFields(target.getClass(), targetFieldMap);

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
    // bean op
    public static <T> T nullify(T bean) {
        Class<?> clazz = bean.getClass();
        List<Field> fields = new ArrayList<>();
        ReflectUtil.retrieveFields(fields, clazz);
        if (fields.isEmpty()) return bean;
        for (Field field : fields) {
            nullifyField(bean, field);
        }
        return bean;
    }

    public static void nullifyField(Object bean, Field field) {
        if (ReflectUtil.isFinalOrStatic(field)) {
            return;
        }

        field.setAccessible(true);
        Class<?> type = field.getType();
        try {
            if (!type.isPrimitive()) {
                field.set(bean, null);
            } else {
                if (type.equals(boolean.class)) {
                    field.setBoolean(bean, false);
                } else if (type.equals(byte.class)) {
                    field.setByte(bean, (byte) 0);
                } else if (type.equals(short.class)) {
                    field.setShort(bean, (short) 0);
                } else if (type.equals(char.class)) {
                    field.setChar(bean, (char) 0);
                } else if (type.equals(int.class)) {
                    field.setInt(bean, 0);
                } else if (type.equals(long.class)) {
                    field.setLong(bean, 0L);
                } else if (type.equals(float.class)) {
                    field.setFloat(bean, 0.F);
                } else if (type.equals(double.class)) {
                    field.setDouble(bean, 0.);
                }
            }
        } catch (IllegalAccessException ignored) {
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====
    //bean to strings
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
        BeanListUtil.retrieveExpandedList(list, bean, excludeAnnotations, expandClasses);
        return list.stream().map(Object::toString).toArray(String[]::new);
    }

    public static List<String> toStringList(
            Object bean) {
        return toStringList(bean, null);
    }

    public static List<String> toStringList(
            Object bean,
            @Nullable Class<? extends Annotation>[] excludeAnnotations,
            Class... expandClasses) {
        List<Object> list = new ArrayList<>();
        BeanListUtil.retrieveExpandedList(list, bean, excludeAnnotations, expandClasses);
        return list.stream().map(it -> {
            if (it == null) return "";
            else return it.toString();
        }).collect(Collectors.toList());
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static String toPrettyString(@Nullable Object object) {
        if (object == null) {
            return "null";
        }

        Class<?> clazz = object.getClass();
        if (ReflectUtil.isBoxedClass(clazz) || object instanceof CharSequence) {
            return object.toString();
        }

        if (clazz.isArray()) {
            Class<?> componentType = clazz.getComponentType();
            if (componentType.isPrimitive()) {
                return object.toString();
            }

            return prettyString((Object[]) object);
        }
        if (object instanceof Collection) {
            return prettyString((Collection<?>) object);
        }
        if (object instanceof Map) {
            return prettyString((Map<?, ?>) object);
        }

        StringBuilder sb = new StringBuilder("Object");
        sb.append("<").append(clazz.getName()).append(">")
                .append("{\n");
        List<Field> fields = new ArrayList<>();
        ReflectUtil.retrieveFields(fields, clazz);
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                String valueString;
                if (value == null) {
                    valueString = "null";
                } else {
                    if (value instanceof Date) {
                        Date date = (Date) value;
                        valueString = new SimpleDateFormat(
                                "yyyy-MM-dd hh:mm:ss, ").format(date) + date.getTime();
                    } else {
                        valueString = value.toString();
                    }
                }
                sb.append("\t[").append(field.getName()).append("]")
                        .append('\t').append(valueString).append('\n');
            } catch (IllegalAccessException ignored) {
            }
        }
        sb.append("}");
        return sb.toString();
    }

    private static String prettyString(Object[] array) {
        StringBuilder sb = new StringBuilder("Array");
        sb.append(" {\n");
        int seq = 0;
        for (Object element : array) {
            String elementType = element == null ? "?" : element.getClass().getName();
            String elementString = element == null ? "null" : element.toString();

            sb.append("\t[").append(seq++).append("]")
                    .append("<").append(elementType).append(">")
                    .append('\t').append(elementString).append('\n');
        }
        sb.append("}");
        return sb.toString();
    }

    private static String prettyString(Collection<?> collection) {
        StringBuilder sb = new StringBuilder("Collection");
        Type[] types = collection.getClass().getGenericInterfaces();
        String typeName = ObjectUtil.isEmpty(types) ? "?" : types[0].getTypeName();
        sb.append("<").append(typeName).append(">")
                .append(" {\n");
        int seq = 0;
        for (Object element : collection) {
            String elementType = element == null ? "?" : element.getClass().getName();
            String elementString = element == null ? "null" : element.toString();

            sb.append("\t[").append(seq++).append("]")
                    .append("<").append(elementType).append(">")
                    .append('\t').append(elementString).append('\n');
        }
        sb.append("}\n");
        return sb.toString();
    }

    private static String prettyString(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder("Map");
        Type[] types = map.getClass().getGenericInterfaces();

        String typeName;
        if (ObjectUtil.isEmpty(types) || types.length != 2) {
            typeName = "?, ?";
        } else {
            typeName = types[0].getTypeName() + ", " + types[1].getTypeName();
        }
        sb.append("<").append(typeName).append(">")
                .append(" {\n");
        int seq = 0;
        for (Map.Entry entry : map.entrySet()) {
            Object key = entry.getKey();
            String keyType = key.getClass().getName();
            Object value = entry.getValue();
            String valueType = value == null ? "?" : value.getClass().getName();
            String valueString = value == null ? "null" : value.toString();

            sb.append("\t[").append(seq++)
                    .append(", ").append(entry.hashCode()).append("]")
                    .append("<").append(keyType)
                    .append(", ").append(valueType).append(">")
                    .append("\t`").append(key.toString()).append("` --> `")
                    .append(valueString).append("`\n");
        }
        sb.append("}");
        return sb.toString();
    }

}
