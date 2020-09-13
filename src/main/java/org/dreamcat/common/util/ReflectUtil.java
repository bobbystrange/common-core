package org.dreamcat.common.util;

import lombok.extern.slf4j.Slf4j;

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
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@SuppressWarnings({"unchecked", "rawtypes"})
public class ReflectUtil {

    public static List<Class<?>> retrieveSuperClasses(Class<?> clazz) {
        List<Class<?>> classList = new ArrayList<>();
        while (!clazz.isPrimitive() && !clazz.equals(Object.class)) {
            Class superClass = clazz.getSuperclass();

            classList.add(clazz);

            clazz = superClass;
        }
        return classList;
    }

    public static <T> List<Class<? extends T>> retrieveSubClasses(Class<T> clazz, String... classPaths) {
        return retrieveSubClasses(clazz, Arrays.asList(classPaths));
    }

    public static <T> List<Class<? extends T>> retrieveSubClasses(Class<T> clazz, Collection<String> classPaths) {
        if (ObjectUtil.isEmpty(classPaths)) {
            return retrieveSubClasses(clazz, "");
        }

        List<Class<? extends T>> classList = new ArrayList<>();
        for (String classPath : classPaths) {
            String name = "/" + classPath.replace(".", "/");
            File classFile = new File(Class.class.getResource(name).getFile());
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
        while (!clazz.isPrimitive() && !clazz.equals(Object.class)) {
            Class superClass = clazz.getSuperclass();

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
        while (!clazz.isPrimitive() && !clazz.equals(Object.class)) {
            Class superClass = clazz.getSuperclass();

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

    public static <A extends Annotation> Map<String, Field> retrieveFieldMap(Class<?> clazz, Class<A> aliasClass, Function<A, String> aliasFunction) {
        Map<String, Field> fieldMap = new HashMap<>();
        while (!clazz.isPrimitive() && !clazz.equals(Object.class)) {
            Class superClass = clazz.getSuperclass();

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
    public static <A extends Annotation> List<String> retrieveFieldNames(Class<?> clazz, Class<A> aliasClass, Function<A, String> aliasFunction) {
        List<String> fieldNameList = new ArrayList<>();
        while (!clazz.isPrimitive() && !clazz.equals(Object.class)) {
            Class superClass = clazz.getSuperclass();

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

    public static <A extends Annotation> String retrieveFieldName(Field field, Class<A> aliasClass, Function<A, String> aliasFunction) {
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
        retrieveAnnotations(clazz, annotationList);
        Collections.reverse(annotationList);
        return annotationList;
    }

    public static void retrieveAnnotations(Class<?> clazz, List<Annotation> annotationList) {
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        for (int size = annotations.length, i = size - 1; i >= 0; i--) {
            annotationList.add(annotations[i]);
        }

        Class<?> superClazz = clazz.getSuperclass();
        if (!superClazz.equals(Object.class)) {
            retrieveAnnotations(superClazz, annotationList);
        }
    }

    public static List<Annotation> retrieveAnnotations(Field field) {
        return new ArrayList<>(Arrays.asList(field.getDeclaredAnnotations()));
    }

    public static <A extends Annotation> A retrieveAnnotation(Class<?> clazz, Class<A> annotationClass) {
        A a = clazz.getDeclaredAnnotation(annotationClass);
        if (a != null) return a;

        Class<?> superClazz = clazz.getSuperclass();
        if (!superClazz.equals(Object.class)) {
            return retrieveAnnotation(superClazz, annotationClass);
        }
        throw new RuntimeException("Assertion failure");
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

    private static boolean hasAnyModifiers(Field field, int... modifiersSet) {
        return hasAnyModifiers(field.getModifiers(), modifiersSet);
    }

    private static boolean hasAnyModifiers(int modifiers, int... modifiersSet) {
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
                    return (Boolean) source ? 0b1 : 0b0;
                } else {
                    return 0b0;
                }
            } else if (targetClass.equals(short.class)) {
                if (Number.class.isAssignableFrom(sourceClass)) {
                    return ((Number) source).shortValue();
                } else if (sourceClass.equals(Boolean.class)) {
                    return (Boolean) source ? (short) 1 : (short) 0;
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
                    return (Boolean) source ? 1 : 0;
                } else {
                    return 0;
                }
            } else if (targetClass.equals(long.class)) {
                if (Number.class.isAssignableFrom(sourceClass)) {
                    return ((Number) source).longValue();
                } else if (sourceClass.equals(Boolean.class)) {
                    return (Boolean) source ? 1L : 0L;
                } else {
                    return 0L;
                }
            } else if (targetClass.equals(float.class)) {
                if (Number.class.isAssignableFrom(sourceClass)) {
                    return ((Number) source).floatValue();
                } else if (sourceClass.equals(Boolean.class)) {
                    return (Boolean) source ? (float) 1 : (float) 0;
                } else {
                    return (float) 0;
                }
            } else if (targetClass.equals(double.class)) {
                if (Number.class.isAssignableFrom(sourceClass)) {
                    return ((Number) source).doubleValue();
                } else if (sourceClass.equals(Boolean.class)) {
                    return (Boolean) source ? (double) 1 : (double) 0;
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
        if (!clazz.isArray()) {
            throw new ClassCastException(clazz + " is not a Array");
        }

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
            return TimeUtil.ofEpochMilli(n, zoneId);
        } else if (targetClass.equals(LocalDate.class)) {
            return TimeUtil.ofEpochMilli(n, zoneId).toLocalDate();
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

    public static <T> Object getFirstFieldValue(T bean) {
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getFields();
        if (ObjectUtil.isEmpty(fields)) return null;
        Field field = fields[0];
        field.setAccessible(true);
        try {
            return field.get(bean);
        } catch (IllegalAccessException e) {
            return null;
        }
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

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private static <T> void findSubClasses(File classFile, String prefix, Class<T> clazz, List<Class<? extends T>> classList) {
        if (classFile.isDirectory()) {
            File[] files = classFile.listFiles();
            if (ObjectUtil.isEmpty(files)) return;
            for (File file : files) {
                findSubClasses(file, prefix, clazz, classList);
            }
            return;
        }

        try {
            if (classFile.getPath().contains(".class")) {
                String className = classFile.getPath()
                        .replace(prefix, "")
                        .replace(".class", "")
                        .replace("/", ".");
                Class<?> classObject = Class.forName(className);
                classObject.asSubclass(clazz);

                if (!className.equals(clazz.getCanonicalName()))
                    classList.add((Class<? extends T>) classObject);
            }
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
        } catch (ClassCastException e) {
            if (log.isDebugEnabled()) {
                log.warn(e.getMessage());
            }
        }
    }
}
