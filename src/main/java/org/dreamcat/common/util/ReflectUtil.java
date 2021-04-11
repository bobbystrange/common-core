package org.dreamcat.common.util;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings({"unchecked", "rawtypes"})
public final class ReflectUtil {

    private ReflectUtil() {
    }

    public static List<Class<?>> retrieveSuperClasses(Class<?> clazz) {
        List<Class<?>> classList = new ArrayList<>();
        while (!clazz.equals(Object.class)) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) break;

            classList.add(clazz);

            clazz = superClass;
        }
        return classList;
    }

    public static <T> List<Class<? extends T>> retrieveSubClasses(Class<T> clazz,
            String... classPaths) {
        return retrieveSubClasses(clazz, Arrays.asList(classPaths));
    }

    public static <T> List<Class<? extends T>> retrieveSubClasses(Class<T> clazz,
            Collection<String> classPaths) {
        if (ObjectUtil.isEmpty(classPaths)) {
            return retrieveSubClasses(clazz, "");
        }

        List<Class<? extends T>> classList = new ArrayList<>();
        for (String classPath : classPaths) {
            String name = classPath.replace(".", "/");
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            URL url = loader.getResource(name);
            if (url == null) {
                throw new IllegalArgumentException(
                        "the resource under the path " + name + " is not found");
            }
            File classFile = new File(url.getFile());
            String prefix = classFile.getPath() + "/";
            findSubClasses(classFile, prefix, clazz, classList);
        }

        return classList;
    }

    public static List<Method> retrieveNoStaticMethods(Class<?> clazz) {
        List<Method> methods = retrieveMethods(clazz);
        methods.removeIf(method -> Modifier.isStatic(method.getModifiers()));
        return methods;
    }

    public static List<Method> retrieveMethods(Class<?> clazz) {
        List<Method> methodList = new ArrayList<>();
        while (!clazz.equals(Object.class)) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) break;

            Method[] methods = clazz.getDeclaredMethods();
            for (int size = methods.length, i = size - 1; i >= 0; i--) {
                methodList.add(methods[i]);
            }

            clazz = superClass;
        }
        Collections.reverse(methodList);
        return methodList;
    }

    public static List<Field> retrieveNoStaticFields(Class<?> clazz) {
        List<Field> fields = retrieveFields(clazz);
        fields.removeIf(field -> Modifier.isStatic(field.getModifiers()));
        return fields;
    }

    public static List<Field> retrieveFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (!clazz.equals(Object.class)) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) break;

            Field[] fields = clazz.getDeclaredFields();
            for (int size = fields.length, i = size - 1; i >= 0; i--) {
                fieldList.add(fields[i]);
            }

            clazz = superClass;
        }
        Collections.reverse(fieldList);
        return fieldList;
    }

    public static Map<String, Field> retrieveFieldMap(Class<?> clazz) {
        return retrieveFieldMap(clazz, null, null);
    }

    public static <A extends Annotation> Map<String, Field> retrieveFieldMap(Class<?> clazz,
            Class<A> aliasClass, Function<A, String> aliasFunction) {
        Map<String, Field> fieldMap = new HashMap<>();
        while (!clazz.equals(Object.class)) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) break;

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String name = retrieveFieldName(field, aliasClass, aliasFunction);
                fieldMap.put(name, field);
            }

            clazz = superClass;
        }
        return fieldMap;
    }

    public static List<String> retrieveFieldNames(Class<?> clazz) {
        return retrieveFieldNames(clazz, null, null);
    }

    // only no static field
    public static <A extends Annotation> List<String> retrieveFieldNames(Class<?> clazz,
            Class<A> aliasClass, Function<A, String> aliasFunction) {
        List<String> fieldNameList = new ArrayList<>();
        while (!clazz.equals(Object.class)) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) break;

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) continue;
                String fieldName = retrieveFieldName(field, aliasClass, aliasFunction);
                fieldNameList.add(fieldName);
            }
            clazz = superClass;
        }
        Collections.reverse(fieldNameList);
        return fieldNameList;
    }

    public static <A extends Annotation> String retrieveFieldName(Field field, Class<A> aliasClass,
            Function<A, String> aliasFunction) {
        String fieldName = field.getName();
        Annotation[] annotations = field.getAnnotations();

        if (annotations == null || annotations.length == 0
                || aliasClass == null) {
            return fieldName;
        }

        for (Annotation annotation : annotations) {
            if (!aliasClass.isInstance(annotation)) continue;
            return aliasFunction.apply(aliasClass.cast(annotation));
        }
        return fieldName;
    }

    public static List<Annotation> retrieveAnnotations(Class<?> clazz) {
        List<Annotation> annotationList = new ArrayList<>();
        while (!clazz.equals(Object.class)) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) break;

            Annotation[] annotations = clazz.getDeclaredAnnotations();
            for (int size = annotations.length, i = size - 1; i >= 0; i--) {
                annotationList.add(annotations[i]);
            }

            clazz = superClass;
        }
        Collections.reverse(annotationList);
        return annotationList;
    }

    public static <A extends Annotation> A retrieveAnnotation(Class<?> clazz,
            Class<A> annotationClass) {
        List<A> annotationList = retrieveAnnotations(clazz, annotationClass);
        if (annotationList.isEmpty()) return null;
        return annotationList.get(annotationList.size() - 1);
    }

    public static <A extends Annotation> List<A> retrieveAnnotations(Class<?> clazz,
            Class<A> annotationClass) {
        List<A> annotationList = new ArrayList<>();
        while (!clazz.equals(Object.class)) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) break;

            A[] annotations = clazz.getDeclaredAnnotationsByType(annotationClass);
            for (int size = annotations.length, i = size - 1; i >= 0; i--) {
                annotationList.add(annotations[i]);
            }

            clazz = superClass;
        }
        Collections.reverse(annotationList);
        return annotationList;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static boolean hasModifiersAndAnyAnnotation(
            Field field, int modifiers, Class... annotationClasses) {
        boolean exclude = hasAnyAnnotation(field, annotationClasses);
        int fieldModifiers = field.getModifiers();
        return exclude || (fieldModifiers & modifiers) > 0;
    }

    public static boolean isStaticAndHasExtraModifiersAndAnyAnnotation(
            Field field, int extraModifiers, Class... annotationClasses) {
        int modifiers = extraModifiers | Modifier.STATIC;
        return hasModifiersAndAnyAnnotation(field, modifiers, annotationClasses);
    }

    public static boolean hasAnyAnnotation(Field field, Class... annotationClasses) {
        if (ObjectUtil.isEmpty(annotationClasses)) return false;

        Annotation[] annotations = field.getAnnotations();
        if (ObjectUtil.isEmpty(annotations)) return false;

        for (Class<? extends Annotation> annotationClass : annotationClasses) {
            if (annotationClass == null) continue;
            Annotation annotation = field.getAnnotation(annotationClass);
            if (annotation != null) return true;
        }

        return false;
    }

    public static boolean hasAnySuperClass(Class<?> clazz, Class... classes) {
        if (ObjectUtil.isEmpty(classes)) return false;

        for (Class<?> i : classes) {
            // i is same as clazz or superclass
            if (i.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasAnyModifiers(Field field, int... modifiersSet) {
        return hasAnyModifiers(field.getModifiers(), modifiersSet);
    }

    public static boolean hasAnyModifiers(int modifiers, int... modifiersSet) {
        if (ObjectUtil.isEmpty(modifiersSet)) return false;
        for (int m : modifiersSet) {
            boolean hasModifiers = (m & modifiers) != 0;
            if (hasModifiers) return true;
        }
        return false;
    }

    public static boolean isFinalOrStatic(Field field) {
        int modifiers = field.getModifiers();
        return Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers);
    }

    public static boolean isBoxedClass(Class<?> clazz) {
        return clazz.equals(Integer.class) ||
                clazz.equals(Long.class) ||
                clazz.equals(Double.class) ||
                clazz.equals(Boolean.class) ||
                clazz.equals(Character.class) ||
                clazz.equals(Float.class) ||
                clazz.equals(Byte.class) ||
                clazz.equals(Short.class);
    }

    public static boolean isCollectionOrArray(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz) ||
                Map.class.isAssignableFrom(clazz) ||
                clazz.isArray();
    }

    // Note that assert new long[0][0] instanceof Object[]
    public static boolean isPrimitiveArray(Class<?> clazz) {
        return clazz.isArray() && !Object[].class.isAssignableFrom(clazz);
    }

    public static boolean isFlat(Class<?> clazz) {
        return clazz.isPrimitive() ||
                isBoxedClass(clazz) ||
                // map
                Map.class.isAssignableFrom(clazz) ||
                // string
                CharSequence.class.isAssignableFrom(clazz) ||
                // BigDecimal, BigInteger
                Number.class.isAssignableFrom(clazz) ||
                // Date
                Date.class.isAssignableFrom(clazz) ||
                // LocalDateTime
                Temporal.class.isAssignableFrom(clazz) ||
                // enum
                Enum.class.isAssignableFrom(clazz);
    }

    /**
     * following the jackson's rules
     *
     * @param bean java bean
     * @return is a json type or not
     */
    public static boolean isJsonType(Object bean) {
        // null
        if (bean == null) return true;
        Class<?> clazz = bean.getClass();
        // boolean
        if (clazz.equals(Boolean.class) ||
                // number
                clazz.equals(Integer.class) || clazz.equals(Long.class) || clazz
                .equals(Double.class) ||
                // string
                clazz.equals(String.class) || Enum.class.isAssignableFrom(clazz)) {
            return true;
        }
        // array
        if (List.class.isAssignableFrom(clazz)) {
            return isJsonType((List<?>) bean);
        }
        // object
        if (Map.class.isAssignableFrom(clazz)) {
            return isJsonType((Map<?, ?>) bean);
        }
        return false;
    }

    public static boolean isJsonType(List<?> list) {
        // array, empty case
        if (list.isEmpty()) return true;
        for (Object bean : list) {
            if (!isJsonType(bean)) return false;
        }
        return true;
    }

    public static boolean isJsonType(Map<?, ?> map) {
        // object, empty case
        if (map.isEmpty()) return true;
        for (Entry<?, ?> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (!(key instanceof String)) return false;
            if (!isJsonType(value)) return false;
        }
        return true;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static <T> T newInstance(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            if (!constructor.isAccessible()) constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getZeroValue(Class<?> clazz) {
        if (clazz == null || !clazz.isPrimitive()) return null;
        if (clazz.equals(byte.class)) {
            return 0b0;
        } else if (clazz.equals(short.class)) {
            return (short) 0;
        } else if (clazz.equals(char.class)) {
            return (char) 0;
        } else if (clazz.equals(int.class)) {
            return 0;
        } else if (clazz.equals(long.class)) {
            return 0L;
        } else if (clazz.equals(float.class)) {
            return (float) 0;
        } else if (clazz.equals(double.class)) {
            return 0.;
        } else if (clazz.equals(boolean.class)) {
            return false;
        } else {
            throw new IllegalStateException("primitive types are not enumerated completely");
        }
    }

    /**
     * @param source      source object
     * @param targetClass require not null
     * @return null if cannot cast and targetClass is not a primitive class
     */
    public static Object cast(Object source, Class<?> targetClass) {
        Objects.requireNonNull(targetClass);
        if (source == null) {
            return getZeroValue(targetClass);
        }

        Class<?> sourceClass = source.getClass();
        // target is same or a super class of source
        if (targetClass.isAssignableFrom(sourceClass)) {
            return targetClass.cast(source);
        }
        if (targetClass.isPrimitive()) {
            if (targetClass.equals(byte.class)) {
                if (Number.class.isAssignableFrom(sourceClass)) {
                    return ((Number) source).byteValue();
                } else if (sourceClass.equals(Boolean.class)) {
                    return (boolean) source ? 0b1 : 0b0;
                } else {
                    return 0b0;
                }
            } else if (targetClass.equals(short.class)) {
                if (Number.class.isAssignableFrom(sourceClass)) {
                    return ((Number) source).shortValue();
                } else if (sourceClass.equals(Boolean.class)) {
                    return (boolean) source ? (short) 1 : (short) 0;
                } else {
                    return (short) 0;
                }
            } else if (targetClass.equals(char.class)) {
                // Note that avoid unexpected actions
                return (char) 0;
            } else if (targetClass.equals(int.class)) {
                if (Number.class.isAssignableFrom(sourceClass)) {
                    return ((Number) source).intValue();
                } else if (sourceClass.equals(Boolean.class)) {
                    return (boolean) source ? 1 : 0;
                } else {
                    return 0;
                }
            } else if (targetClass.equals(long.class)) {
                if (Number.class.isAssignableFrom(sourceClass)) {
                    return ((Number) source).longValue();
                } else if (sourceClass.equals(Boolean.class)) {
                    return (boolean) source ? 1L : 0L;
                } else {
                    return 0L;
                }
            } else if (targetClass.equals(float.class)) {
                if (Number.class.isAssignableFrom(sourceClass)) {
                    return ((Number) source).floatValue();
                } else if (sourceClass.equals(Boolean.class)) {
                    return (boolean) source ? (float) 1 : (float) 0;
                } else {
                    return (float) 0;
                }
            } else if (targetClass.equals(double.class)) {
                if (Number.class.isAssignableFrom(sourceClass)) {
                    return ((Number) source).doubleValue();
                } else if (sourceClass.equals(Boolean.class)) {
                    return (boolean) source ? (double) 1 : (double) 0;
                } else {
                    return (double) 0;
                }
            } else if (targetClass.equals(boolean.class)) {
                if (Number.class.isAssignableFrom(sourceClass)) {
                    return ((Number) source).longValue() != 0;
                } else {
                    return source.toString().equalsIgnoreCase("true");
                }
            } else {
                throw new RuntimeException("Primitive types are not enumerated completely");
            }
        }
        return null;
    }

    public static Object[] castToArray(Object a) {
        if (a == null) return null;
        Class<?> clazz = a.getClass();
        if (Collection.class.isAssignableFrom(clazz)) {
            return ((Collection<?>) a).toArray();
        }
        if (!clazz.isArray()) {
            throw new ClassCastException(clazz + " is neither a Array or a Collection");
        }
        return castArrayToArray(a, clazz);
    }

    public static List<?> castToList(Object a) {
        if (a == null) return null;
        Class<?> clazz = a.getClass();
        if (Collection.class.isAssignableFrom(clazz)) {
            return new ArrayList<>((Collection<?>) a);
        }
        if (!clazz.isArray()) {
            throw new ClassCastException(clazz + " is neither a Array or a Collection");
        }
        return Arrays.asList(castArrayToArray(a, clazz));
    }

    private static Object[] castArrayToArray(Object a, Class<?> clazz) {
        Class<?> componentType = clazz.getComponentType();
        if (!componentType.isPrimitive()) {
            return (Object[]) a;
        }

        if (clazz == boolean[].class) {
            return ArrayUtil.boxed((boolean[]) a);
        } else if (clazz == byte[].class) {
            return ArrayUtil.boxed((byte[]) a);
        } else if (clazz == short[].class) {
            return ArrayUtil.boxed((short[]) a);
        } else if (clazz == char[].class) {
            return ArrayUtil.boxed((char[]) a);
        } else if (clazz == int[].class) {
            return ArrayUtil.boxed((int[]) a);
        } else if (clazz == long[].class) {
            return ArrayUtil.boxed((long[]) a);
        } else if (clazz == float[].class) {
            return ArrayUtil.boxed((float[]) a);
        } else if (clazz == double[].class) {
            return ArrayUtil.boxed((double[]) a);
        } else {
            throw new IllegalStateException("primitive types are not enumerated completely");
        }
    }

    // bean op
    public static void nullify(Object bean) {
        Class<?> clazz = bean.getClass();
        List<Field> fields = ReflectUtil.retrieveNoStaticFields(clazz);
        if (fields.isEmpty()) return;
        for (Field field : fields) {
            nullify(bean, field);
        }
    }

    public static void nullify(Object bean, Field field) {
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

    public static Class<?> getBoxedClass(Class<?> clazz) {
        if (!clazz.isPrimitive()) return clazz;
        if (clazz.equals(boolean.class)) {
            return Boolean.class;
        } else if (clazz.equals(char.class)) {
            return Character.class;
        } else if (clazz.equals(byte.class)) {
            return Byte.class;
        } else if (clazz.equals(short.class)) {
            return Short.class;
        } else if (clazz.equals(int.class)) {
            return Integer.class;
        } else if (clazz.equals(long.class)) {
            return Long.class;
        } else if (clazz.equals(float.class)) {
            return Float.class;
        } else {
            return Double.class;
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    /**
     * string to object,
     *
     * @param s           string
     * @param targetClass target class
     * @return object
     * @throws NumberFormatException           not a number
     * @throws StringIndexOutOfBoundsException empty string to char
     */
    public static Object parse(String s, Class<?> targetClass)
            throws NumberFormatException, StringIndexOutOfBoundsException {
        if (targetClass == null) return null;
        if (s == null) return getZeroValue(targetClass);

        // target is same or a super class of String
        if (targetClass.isAssignableFrom(String.class)) {
            return s;
        }
        if (targetClass.equals(byte.class) || targetClass.equals(Byte.class)) {
            return Byte.valueOf(s);
        } else if (targetClass.equals(short.class) || targetClass.equals(Short.class)) {
            return Short.valueOf(s);
        } else if (targetClass.equals(int.class) || targetClass.equals(Integer.class)) {
            return Integer.valueOf(s);
        } else if (targetClass.equals(long.class) || targetClass.equals(Long.class)) {
            return Long.valueOf(s);
        } else if (targetClass.equals(float.class) || targetClass.equals(Float.class)) {
            return Float.valueOf(s);
        } else if (targetClass.equals(double.class) || targetClass.equals(Double.class)) {
            return Double.valueOf(s);
        } else if (targetClass.equals(boolean.class) || targetClass.equals(Boolean.class)) {
            return Boolean.valueOf(s);
        } else if (targetClass.equals(char.class)) {
            // a string cannot be cast to a char normally, so ignore it
            return (char) 0;
        }
        return null;
    }

    public static Object parse(boolean b, Class<?> targetClass) {
        if (targetClass == null) return null;
        if (targetClass.equals(Boolean.class) || targetClass.equals(boolean.class)) {
            return b;
        }
        if (targetClass.equals(String.class)) {
            return Boolean.toString(b);
        }
        if (targetClass.equals(byte.class) || targetClass.equals(Byte.class)) {
            return b ? 0b1 : 0b0;
        } else if (targetClass.equals(short.class) || targetClass.equals(Short.class)) {
            return b ? (short) 1 : (short) 0;
        } else if (targetClass.equals(char.class) || targetClass.equals(Character.class)) {
            return b ? (char) 1 : (char) 0;
        } else if (targetClass.equals(int.class) || targetClass.equals(Integer.class)) {
            return b ? (int) 1 : (int) 0;
        } else if (targetClass.equals(long.class) || targetClass.equals(Long.class)) {
            return b ? (long) 1 : (long) 0;
        } else if (targetClass.equals(float.class) || targetClass.equals(Float.class)) {
            return b ? (float) 1 : (float) 0;
        } else if (targetClass.equals(double.class) || targetClass.equals(Double.class)) {
            return b ? (double) 1 : (double) 0;
        } else if (targetClass.equals(BigInteger.class)) {
            return b ? BigInteger.ONE : BigInteger.ZERO;
        } else if (targetClass.equals(BigDecimal.class)) {
            return b ? BigDecimal.ONE : BigDecimal.ZERO;
        }
        return null;
    }

    // helpful for Data-Access-Object
    public static Object parse(long n, Class<?> targetClass, ZoneId zoneId) {
        if (targetClass == null) return null;
        if (targetClass.equals(String.class)) {
            return Long.toString(n);
        } else if (targetClass.equals(Date.class)) {
            return new Date(n);
        } else if (targetClass.equals(LocalDateTime.class)) {
            return DateUtil.ofEpochMilli(n, zoneId);
        } else if (targetClass.equals(LocalDate.class)) {
            return DateUtil.ofEpochMilli(n, zoneId).toLocalDate();
        } else if (targetClass.equals(byte.class) || targetClass.equals(Byte.class)) {
            return (byte) n;
        } else if (targetClass.equals(short.class) || targetClass.equals(Short.class)) {
            return (short) n;
        } else if (targetClass.equals(char.class) || targetClass.equals(Character.class)) {
            return (char) n;
        } else if (targetClass.equals(int.class) || targetClass.equals(Integer.class)) {
            return (int) n;
        } else if (targetClass.equals(long.class) || targetClass.equals(Long.class)) {
            return n;
        } else if (targetClass.equals(float.class) || targetClass.equals(Float.class)) {
            return (float) n;
        } else if (targetClass.equals(double.class) || targetClass.equals(Double.class)) {
            return (double) n;
        } else if (targetClass.equals(boolean.class) || targetClass.equals(Boolean.class)) {
            return n != 0;
        } else if (targetClass.equals(BigInteger.class)) {
            return BigInteger.valueOf(n);
        } else if (targetClass.equals(BigDecimal.class)) {
            return BigDecimal.valueOf(n);
        }
        return null;
    }

    public static Object parse(double n, Class<?> targetClass) {
        if (targetClass == null) return null;
        if (targetClass.isAssignableFrom(String.class)) {
            return Double.toString(n);
        } else if (targetClass.equals(byte.class) || targetClass.equals(Byte.class)) {
            return (byte) n;
        } else if (targetClass.equals(short.class) || targetClass.equals(Short.class)) {
            return (short) n;
        } else if (targetClass.equals(char.class) || targetClass.equals(Character.class)) {
            return (char) n;
        } else if (targetClass.equals(int.class) || targetClass.equals(Integer.class)) {
            return (int) n;
        } else if (targetClass.equals(long.class) || targetClass.equals(Long.class)) {
            return (long) n;
        } else if (targetClass.equals(float.class) || targetClass.equals(Float.class)) {
            return (float) n;
        } else if (targetClass.equals(double.class) || targetClass.equals(Double.class)) {
            return n;
        } else if (targetClass.equals(boolean.class) || targetClass.equals(Boolean.class)) {
            return n != 0;
        } else if (targetClass.equals(BigInteger.class)) {
            return BigInteger.valueOf((long) n);
        } else if (targetClass.equals(BigDecimal.class)) {
            return BigDecimal.valueOf(n);
        }
        return null;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static <T> Object getFirstFieldValue(T bean) {
        if (bean == null) return null;
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getFields();
        if (ObjectUtil.isEmpty(fields)) return null;
        Field field = fields[0];
        return getFieldValue(field, bean);
    }

    public static Object getFieldValue(Field field, Object bean) {
        if (bean == null) return null;
        field.setAccessible(true);
        try {
            return field.get(bean);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static String[] getParameterNames(Method method) {
        Parameter[] parameters = method.getParameters();
        int len = parameters.length;
        String[] parameterNames = new String[len];
        for (int i = 0; i < len; i++) {
            Parameter parameter = parameters[i];
            if (parameter.isNamePresent()) {
                parameterNames[i] = parameter.getName();
            }
        }
        return parameterNames;
    }

    public static Class<?> getTypeArgument(Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type[] types = parameterizedType.getActualTypeArguments();
            return (Class<?>) types[0];
        }
        return null;
    }

    public static Class<?>[] getTypeArguments(Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type[] types = parameterizedType.getActualTypeArguments();

            int size = types.length;
            Class<?>[] classes = new Class[size];
            for (int i = 0; i < size; i++) {
                classes[i] = (Class<?>) types[i];
            }
            return classes;
        }
        return null;
    }

    // long[][].class -> long[][], String -> java.lang.String
    public static String getLiteralType(Class<?> clazz) {
        if (!Objects.requireNonNull(clazz).isPrimitive()) {
            return clazz.getCanonicalName();
        }

        if (clazz.equals(byte.class)) {
            return "byte";
        } else if (clazz.equals(short.class)) {
            return "short";
        } else if (clazz.equals(char.class)) {
            return "char";
        } else if (clazz.equals(int.class)) {
            return "int";
        } else if (clazz.equals(long.class)) {
            return "long";
        } else if (clazz.equals(float.class)) {
            return "float";
        } else if (clazz.equals(double.class)) {
            return "double";
        } else if (clazz.equals(boolean.class)) {
            return "boolean";
        } else {
            throw new AssertionError("Primitive types are not enumerated completely");
        }
    }

    public static Method getGetter(Class<?> clazz, Field field) {
        String name = field.getName();
        String suffix = StringUtil.toCapitalCase(name);
        Class<?> fieldType = field.getType();
        String prefix = fieldType.equals(boolean.class) ? "is" : "get";
        try {
            return clazz.getDeclaredMethod(prefix + suffix);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Method getSetter(Class<?> clazz, Field field) {
        String name = field.getName();
        String suffix = StringUtil.toCapitalCase(name);
        Class<?> fieldType = field.getType();
        try {
            return clazz.getDeclaredMethod("set" + suffix, fieldType);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private static <T> void findSubClasses(File classFile, String prefix, Class<T> clazz,
            List<Class<? extends T>> classList) {
        if (classFile.isDirectory()) {
            File[] files = classFile.listFiles();
            if (ObjectUtil.isEmpty(files)) return;
            for (File file : files) {
                findSubClasses(file, prefix, clazz, classList);
            }
            return;
        }
        if (!classFile.getPath().contains(".class")) return;

        String path = classFile.getPath();
        // 6 for .class
        String className = path.substring(prefix.length(), path.length() - 6)
                .replace("/", ".");
        try {
            Class<?> classObject = Class.forName(className);
            // not a subtype or same type, then skip it
            if (!clazz.isAssignableFrom(classObject) || clazz.equals(classObject)) return;

            classList.add((Class<? extends T>) classObject);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
        } catch (ClassCastException e) {
            if (log.isDebugEnabled()) {
                log.warn(e.getMessage());
            }
        }
    }
}
