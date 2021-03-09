package org.dreamcat.common.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * Create by tuke on 2019-06-04
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Slf4j
public final class BeanUtil {

    private BeanUtil() {
    }

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
        } catch (Exception ignored) {
        }

        return target;
    }

    /**
     * <strong>slower than cglib</strong>, please use it in field-based pojo (no getter/setter) only
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
                targetField.setAccessible(true);
                sourceField.setAccessible(true);
                targetField.set(target, sourceField.get(source));
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.debug("set {} to {}, error is: {}",
                            sourceField, targetField, e.getMessage());
                }
            }
        }

    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    /**
     * Create by tuke on 2018-12-27
     * set the value of some fields to null
     * it maybe useful on xml or json formatting situation
     *
     * @param bean       pojo
     * @param expression a specified bean expression string, such as `c.{m.x,n.{y,z}},b,a`
     * @throws Exception uncaught exception
     */
    public static void lessFiled(Object bean, String expression) throws Exception {
        Class<?> clazz = bean.getClass();
        if (clazz.isPrimitive()) {
            if (log.isDebugEnabled()) {
                log.debug("ignore primitive type class `{}`", clazz);
            }
            return;
        }

        List<String> fieldNames = ReflectUtil.retrieveFieldNames(clazz);
        List<String> keptFiledNames = new ArrayList<>();
        List<String> levelOneFiledNames = new ArrayList<>();
        retrieveExpression(expression, levelOneFiledNames);

        boolean exclude = false;
        for (String levelOneFiledName : levelOneFiledNames) {
            // exclude mode
            if (levelOneFiledName.startsWith("-")) {
                exclude = true;
                String str = levelOneFiledName.substring(1);
                int i = str.indexOf('.');
                if (i < 0) {
                    Field field = clazz.getDeclaredField(str);
                    if (field.getClass().isPrimitive()) {
                        if (log.isDebugEnabled()) {
                            log.debug("ignore primitive type field `{}` of class `{}`", str, clazz);
                        }
                        continue;
                    }
                    field.setAccessible(true);
                    field.set(bean, null);
                    continue;
                }

                checkDotPosition(str, i);

                String name = str.substring(0, i);
                // chars after first '.'
                String rest = str.substring(i + 1);

                if (!fieldNames.contains(name)) {
                    continue;
                }

                Field field = clazz.getDeclaredField(name);
                field.setAccessible(true);
                Object fieldObject = field.get(bean);
                if (fieldObject == null) continue;

                Field fieldField = fieldObject.getClass().getDeclaredField(rest);
                fieldField.setAccessible(true);
                Object fieldFieldObject = fieldField.get(fieldObject);
                if (fieldFieldObject == null) continue;

                Class<?> fieldFieldObjectClass = fieldFieldObject.getClass();
                if (fieldFieldObjectClass.isPrimitive()) {
                    if (log.isDebugEnabled()) {
                        log.debug("ignore primitive type field `{}` of class `{}`",
                                rest, fieldFieldObjectClass);
                    }
                    continue;
                }
                fieldField.set(fieldObject, null);
            } else {
                if (exclude) throw new IllegalArgumentException();

                int i = levelOneFiledName.indexOf('.');
                if (i < 0) {
                    if (!fieldNames.contains(levelOneFiledName)) {
                        if (log.isDebugEnabled()) {
                            log.debug("class {} hasn't field `{}`", clazz, levelOneFiledName);
                        }
                        continue;
                    }
                    keptFiledNames.add(levelOneFiledName);
                    continue;
                }

                checkDotPosition(levelOneFiledName, i);

                String name = levelOneFiledName.substring(0, i);
                // chars after first '.'
                String rest = levelOneFiledName.substring(i + 1);
                Field field = clazz.getDeclaredField(name);
                field.setAccessible(true);
                Object fieldObject = field.get(bean);
                if (fieldObject == null) continue;
                Class<?> fieldClass = fieldObject.getClass();

                if (rest.startsWith("{") && rest.endsWith("}")) {
                    String levelTwoExpression = rest.substring(1, rest.length() - 1);
                    lessFiled(fieldObject, levelTwoExpression);
                } else {
                    try {
                        keepOneField(fieldObject, rest);
                    } catch (Exception e) {
                        // wanna kept field is not exists, so remove the object self
                        if (fieldClass.isPrimitive()) {
                            if (log.isDebugEnabled()) {
                                log.debug("ignore primitive type field `{}` of class `{}`",
                                        name, fieldClass);
                            }
                        } else {
                            field.set(bean, null);
                        }
                    }
                }
                keptFiledNames.add(name);
            }
        }

        if (!exclude) {
            for (String fieldName : fieldNames) {
                if (!keptFiledNames.contains(fieldName)) {
                    Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    ReflectUtil.nullify(bean, field);
                }
            }
        }
    }

    // make all fields' value equal null but the specified one
    private static void keepOneField(Object bean, String fieldName) throws Exception {
        Class<?> clazz = bean.getClass();
        List<Field> fields = ReflectUtil.retrieveFields(clazz);
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) continue;

            field.setAccessible(true);
            Object fieldObject = field.get(bean);
            if (fieldObject.getClass().isPrimitive()) {
                if (log.isDebugEnabled()) {
                    log.debug("ignore primitive type field `{}` of class `{}`", fieldName, clazz);
                }
            }
            field.set(bean, null);
        }
    }

    // first '.' is last char of str
    private static void checkDotPosition(String str, int i) {
        if (i == str.length() - 1) {
            throw new IllegalArgumentException();
        }
    }

    // FIXME there are some known bugs when it accepts a invalid expression
    private static void retrieveExpression(String expression, List<String> levelOneFiledNames) {
        if (expression == null || expression.isEmpty()) return;

        expression = expression.trim();
        int firstComma = expression.indexOf(",");
        int firstLeftBigParentheses = expression.indexOf("{");

        // if not has ',', then treat as single field
        if (firstComma < 0) {
            levelOneFiledNames.add(expression);
            return;
        }
        // if not has '{'
        else if (firstLeftBigParentheses < 0) {
            levelOneFiledNames.addAll(Arrays.stream(expression.trim().split(","))
                    .map(String::trim)
                    .collect(Collectors.toList()));
            return;
        }

        String suffix = expression;
        // if expression is like a,b,c.{m.x,n.{y,z}}
        // in fact, just not is c.{m.x,n.{y,z}},b,a
        if (firstComma < firstLeftBigParentheses) {
            int index = expression.substring(0, firstLeftBigParentheses).lastIndexOf(",");
            String prefix = expression.substring(0, index);
            levelOneFiledNames.addAll(splitString(prefix));
            suffix = expression.substring(index + 1);
        }
        retrieveForFirstLeftBigParentheses(suffix, levelOneFiledNames);
    }

    // work on this case: c.{m.x,n.{y,z}},b,a
    private static void retrieveForFirstLeftBigParentheses(
            String expression, List<String> levelOneFiledNames) {
        int leftPos = expression.indexOf("{");
        int len = expression.length();
        // '{' count 1, '}' count -1, matched until count equal 0
        int count = 1, rightPos = 0;
        boolean matched = false;
        if (leftPos + 1 > len - 1) throw new IllegalArgumentException("not matched '}' for '{'");
        for (int i = leftPos + 1; i < len; i++) {
            char c = expression.charAt(i);
            if (c == '{') count++;
            else if (c == '}') count--;
            if (count == 0) {
                matched = true;
                rightPos = i;
                break;
            }
        }
        if (matched) {
            String subExpr = expression.substring(0, rightPos + 1);
            levelOneFiledNames.add(subExpr.trim());

            // still has some chars after matched '}'
            if (rightPos < len - 1) {
                char ch = expression.charAt(rightPos + 1);
                if (ch != ',') {
                    throw new IllegalArgumentException(
                            String.format("invalid char `%s` after `}`", ch + ""));
                }
                // still has some chars after matched '},'
                if (rightPos + 1 < len - 1) {
                    String restExpr = expression.substring(rightPos + 1);
                    retrieveExpression(restExpr, levelOneFiledNames);
                }
            }
        } else {
            throw new IllegalArgumentException("not matched '}' for '{'");
        }
    }

    private static List<String> splitString(String string) {
        if (string == null || string.isEmpty()) return Collections.emptyList();

        return Arrays.stream(string.trim().split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

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
            sb.append(Arrays.stream((Object[]) o).map(BeanUtil::obj2str)
                    .collect(Collectors.joining(", ")));
            sb.append("]");
        } else if (o instanceof Collection) {
            sb.append("[");
            sb.append(((Collection<?>) o).stream().map(BeanUtil::obj2str)
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
            sb.append(fields.stream().map(field2str(o))
                    .collect(Collectors.joining(", ")));
            sb.append("}");
        }
        return sb.toString();
    }

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

    private static Function<Field, String> field2str(Object o) {
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
