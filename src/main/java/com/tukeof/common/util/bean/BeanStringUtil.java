package com.tukeof.common.util.bean;


import com.tukeof.common.annotation.Nullable;
import com.tukeof.common.util.ObjectUtil;
import com.tukeof.common.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by tuke on 2017/4/1.
 */
public class BeanStringUtil {

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
