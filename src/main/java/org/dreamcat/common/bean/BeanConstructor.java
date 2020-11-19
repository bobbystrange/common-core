package org.dreamcat.common.bean;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import org.dreamcat.common.util.ObjectUtil;

/**
 * Create by tuke on 2018/11/9
 */
public class BeanConstructor {

    @Getter
    @SuppressWarnings("rawtypes")
    private final Map<Class, Function<String, Object>> mapper = new HashMap<>();
    private final List<String> format = new ArrayList<>();

    public BeanConstructor(String... patterns) {
        registerMapper();
        if (ObjectUtil.isEmpty(patterns)) registerFormat();
        else format.addAll(Arrays.asList(patterns));
    }

    @SuppressWarnings("rawtypes")
    public <T> T fromStringList(Class<T> clazz, List<String> list) {
        T bean;
        try {
            bean = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        Field[] fields = clazz.getDeclaredFields();
        List<Class> types = Arrays.stream(fields)
                .map(Field::getType)
                .collect(Collectors.toList());

        int len = list.size();
        for (int i = 0; i < len; i++) {
            Class key = boxClass(types.get(i));

            Object value = mapper.get(key).apply(list.get(i));

            fields[i].setAccessible(true);
            try {
                fields[i].set(bean, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return bean;
    }

    public <T> T fromStringArray(Class<T> clazz, String[] strings) {
        return fromStringList(clazz, Arrays.asList(strings));
    }

    private Class<?> boxClass(Class<?> clazz) {
        String typename = clazz.toString();
        switch (typename) {
            case "boolean":
                return Boolean.class;
            case "char":
                return Character.class;

            case "byte":
                return Byte.class;
            case "short":
                return Short.class;
            case "int":
                return Integer.class;
            case "long":
                return Long.class;

            case "float":
                return Float.class;
            case "double":
                return Double.class;

            default:
                return clazz;

        }
    }

    public Date parseDate(String s) {
        for (String pattern : format) {
            try {
                return new SimpleDateFormat(pattern).parse(s);
            } catch (ParseException ignored) {
            }
        }

        return null;
    }

    private void registerMapper() {
        mapper.put(String.class, (s) -> s);
        mapper.put(StringBuilder.class, StringBuilder::new);
        mapper.put(StringBuffer.class, StringBuffer::new);

        mapper.put(Boolean.class, Boolean::parseBoolean);
        mapper.put(Character.class, (s) -> s.isEmpty() ? '\0' : s.charAt(0));

        mapper.put(Byte.class, Byte::parseByte);
        mapper.put(Short.class, Short::parseShort);
        mapper.put(Integer.class, Integer::parseInt);
        mapper.put(Long.class, Long::parseLong);

        mapper.put(Float.class, Float::parseFloat);
        mapper.put(Double.class, Double::parseDouble);

        mapper.put(Date.class, this::parseDate);
    }

    private void registerFormat() {
        format.add("yyyy-MM-dd HH:mm:ss");
        format.add("yyyy-MM-dd");
    }
}
