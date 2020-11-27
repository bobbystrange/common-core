package org.dreamcat.common.core.legacy;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.dreamcat.common.util.ObjectUtil;
import org.dreamcat.common.util.ReflectUtil;

/**
 * Create by tuke on 2020/3/3
 */
public class BeanFormatUtil {

    /**
     * @param o pojo
     * @return inline string
     * @see java.util.Arrays#deepToString(Object[])
     */
    public static String inline(Object o) {
        if (o == null) {
            return "null";
        }

        Class<?> clazz = o.getClass();
        if (ReflectUtil.isBoxedClass(clazz) || o instanceof CharSequence) {
            return o.toString();
        }

        if (clazz.isArray()) {
            Class<?> componentType = clazz.getComponentType();
            if (componentType.isPrimitive()) {
                return pa2str(o);
            }
        }

        StringBuilder sb = new StringBuilder(clazz.getName());
        if (clazz.isArray()) {
            sb.append("[");
            sb.append(Arrays.stream((Object[]) o).map(BeanFormatUtil::obj2str)
                    .collect(Collectors.joining(", ")));
            sb.append("]");
        } else if (o instanceof Collection) {
            sb.append("[");
            sb.append(((Collection<?>) o).stream().map(BeanFormatUtil::obj2str)
                    .collect(Collectors.joining(", ")));
            sb.append("]");
        } else if (o instanceof Map) {
            sb.append("{");
            sb.append(((Map<?, ?>) o).entrySet().stream().map(entry ->
                    obj2str(entry.getKey()) + " => " + obj2str(entry.getValue()))
                    .collect(Collectors.joining(", ")));
            sb.append("}");
        } else {
            sb.append(clazz.getSimpleName());
            sb.append("{");
            List<Field> fields = ReflectUtil.retrieveFields(clazz);
            sb.append(fields.stream().map(_field2str(o))
                    .collect(Collectors.joining(", ")));
            sb.append("}");
        }
        return sb.toString();
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static String pretty(Object o) {
        if (o == null) {
            return "null";
        }

        Class<?> clazz = o.getClass();
        if (ReflectUtil.isBoxedClass(clazz) || o instanceof CharSequence) {
            return o.toString();
        }

        if (clazz.isArray()) {
            Class<?> componentType = clazz.getComponentType();
            if (componentType.isPrimitive()) {
                return pa2str(o);
            }

            return prettyFor((Object[]) o);
        }
        if (o instanceof Collection) {
            return prettyFor((Collection<?>) o);
        }
        if (o instanceof Map) {
            return prettyFor((Map<?, ?>) o);
        }

        StringBuilder sb = new StringBuilder("Object");
        sb.append("<").append(clazz.getName()).append(">")
                .append("{\n");
        List<Field> fields = ReflectUtil.retrieveFields(clazz);
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(o);
                String valueString = obj2str(value);
                sb.append("\t[").append(field.getName()).append("]")
                        .append('\t').append(valueString).append('\n');
            } catch (IllegalAccessException ignored) {
            }
        }
        sb.append("}");
        return sb.toString();
    }

    private static String prettyFor(Object[] array) {
        StringBuilder sb = new StringBuilder("Array");
        sb.append(" {\n");
        int seq = 0;
        for (Object element : array) {
            String elementType = element == null ? "?" : element.getClass().getName();
            String elementString = obj2str(element);

            sb.append("\t[").append(seq++).append("]")
                    .append("<").append(elementType).append(">")
                    .append('\t').append(elementString).append('\n');
        }
        sb.append("}");
        return sb.toString();
    }

    private static String prettyFor(Collection<?> collection) {
        StringBuilder sb = new StringBuilder("Collection");
        Type[] types = collection.getClass().getGenericInterfaces();
        String typeName = ObjectUtil.isEmpty(types) ? "?" : types[0].getTypeName();
        sb.append("<").append(typeName).append(">")
                .append(" {\n");
        int seq = 0;
        for (Object element : collection) {
            String elementType = element == null ? "?" : element.getClass().getName();
            String elementString = obj2str(element);

            sb.append("\t[").append(seq++).append("]")
                    .append("<").append(elementType).append(">")
                    .append('\t').append(elementString).append('\n');
        }
        sb.append("}\n");
        return sb.toString();
    }

    @SuppressWarnings("rawtypes")
    private static String prettyFor(Map<?, ?> map) {
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
            String valueString = obj2str(value);

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

    private static String obj2str(Object obj) {
        if (obj == null) {
            return "null";
        } else {
            if (obj instanceof Date) {
                Date date = (Date) obj;
                return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss SSS, ")
                        .format(date) + date.getTime();
            } else if (obj instanceof LocalDateTime) {
                LocalDateTime dateTime = (LocalDateTime) obj;
                return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss SSS, ")) +
                        Date.from(dateTime.toInstant(ZoneOffset.UTC)).getTime();
            } else if (obj instanceof LocalDate) {
                LocalDate date = (LocalDate) obj;
                return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } else if (obj instanceof LocalTime) {
                LocalTime time = (LocalTime) obj;
                return time.format(DateTimeFormatter.ofPattern("hh:mm:ss SSS"));
            } else {
                Class<?> clazz = obj.getClass();
                if (clazz.isArray()) {
                    Class<?> componentType = clazz.getComponentType();
                    if (componentType.isPrimitive()) {
                        return pa2str(obj);
                    } else {
                        return Arrays.deepToString((Object[]) obj);
                    }
                }

                return obj.toString();
            }
        }
    }

    private static Function<Field, String> _field2str(Object o) {
        return field -> {
            field.setAccessible(true);
            try {
                Object value = field.get(o);
                String valueString = obj2str(value);
                return field.getName() + "=" + valueString;
            } catch (IllegalAccessException ignored) {
                return field.getName() + ": IllegalAccessException was thrown";
            }
        };
    }

    private static String pa2str(Object a) {
        Class<?> clazz = a.getClass();
        if (clazz == boolean[].class) {
            return Arrays.toString((boolean[]) a);
        } else if (clazz == byte[].class) {
            return Arrays.toString((byte[]) a);
        } else if (clazz == short[].class) {
            return Arrays.toString((short[]) a);
        } else if (clazz == char[].class) {
            return Arrays.toString((char[]) a);
        } else if (clazz == int[].class) {
            return Arrays.toString((int[]) a);
        } else if (clazz == long[].class) {
            return Arrays.toString((long[]) a);
        } else if (clazz == float[].class) {
            return Arrays.toString((float[]) a);
        } else if (clazz == double[].class) {
            return Arrays.toString((double[]) a);
        } else throw new IllegalArgumentException("require a primitive array, but got" + clazz);
    }

}
