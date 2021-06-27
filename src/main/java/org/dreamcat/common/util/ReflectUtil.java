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
import java.net.URL;
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
import java.util.stream.Collectors;
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

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

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

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

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

    public static Map<String, Field> retrieveNoStaticFieldMap(Class<?> clazz) {
        return retrieveNoStaticFieldMap(clazz, null, null);
    }

    public static <A extends Annotation> Map<String, Field> retrieveNoStaticFieldMap(Class<?> clazz,
            Class<A> aliasClass, Function<A, String> aliasFunction) {
        Map<String, Field> fieldMap = retrieveFieldMap(clazz, aliasClass, aliasFunction);
        return fieldMap.entrySet().stream()
                .filter(entry -> !Modifier.isStatic(entry.getValue().getModifiers()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

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

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static List<Annotation> retrieveAnnotations(Class<?> clazz) {
        List<Annotation> annotationList = new ArrayList<>();
        while (clazz != null && !clazz.equals(Object.class)) {
            Class superClass = clazz.getSuperclass();

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
        return CollectionUtil.lastElement(annotationList);
    }

    public static <A extends Annotation> List<A> retrieveAnnotations(Class<?> clazz,
            Class<A> annotationClass) {
        List<A> annotationList = new ArrayList<>();
        while (clazz != null && !clazz.equals(Object.class)) {
            Class superClass = clazz.getSuperclass();

            A[] annotations = clazz.getDeclaredAnnotationsByType(annotationClass);
            for (int size = annotations.length, i = size - 1; i >= 0; i--) {
                annotationList.add(annotations[i]);
            }

            clazz = superClass;
        }
        Collections.reverse(annotationList);
        return annotationList;
    }

    public static List<Annotation> retrieveAnnotations(Method method) {
        List<Annotation> annotationList = new ArrayList<>();
        Class<?> clazz = method.getDeclaringClass();
        while (!clazz.equals(Object.class)) {
            Class superClass = clazz.getSuperclass();
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (int size = annotations.length, i = size - 1; i >= 0; i--) {
                annotationList.add(annotations[i]);
            }

            clazz = superClass;
            if (clazz == null) break;
            try {
                method = clazz.getMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                break;
            }
        }
        Collections.reverse(annotationList);
        return annotationList;
    }

    public static <A extends Annotation> A retrieveAnnotation(Method method,
            Class<A> annotationClass) {
        List<A> annotationList = retrieveAnnotations(method, annotationClass);
        return CollectionUtil.lastElement(annotationList);
    }

    public static <A extends Annotation> List<A> retrieveAnnotations(Method method,
            Class<A> annotationClass) {
        List<A> annotationList = new ArrayList<>();
        Class<?> clazz = method.getDeclaringClass();
        while (!clazz.equals(Object.class)) {
            Class superClass = clazz.getSuperclass();
            A[] annotations = method.getDeclaredAnnotationsByType(annotationClass);
            for (int size = annotations.length, i = size - 1; i >= 0; i--) {
                annotationList.add(annotations[i]);
            }

            clazz = superClass;
            if (clazz == null) break;
            try {
                method = clazz.getMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                break;
            }
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
                clazz.equals(Short.class) ||
                clazz.equals(Void.class);
    }

    public static boolean isCollectionOrArray(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz) ||
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
                // LocalDateTime, LocalDate, LocalTime
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
            if (clazz.isPrimitive()) {
                return (T) getZeroValue(clazz);
            }
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

    // bean op
    public static void nullify(Object bean) {
        Class<?> clazz = bean.getClass();
        List<Field> fields = retrieveNoStaticFields(clazz);
        if (fields.isEmpty()) return;
        for (Field field : fields) {
            nullify(bean, field);
        }
    }

    public static void nullify(Object bean, Field field) {
        if (ReflectUtil.isFinalOrStatic(field)) {
            return;
        }
        setZeroValue(bean, field);
    }

    public static Object getValue(Object bean, Field field) {
        if (!field.isAccessible()) field.setAccessible(true);
        try {
            return field.get(bean);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static void setValue(Object bean, Field field, Object newValue) {
        if (!field.isAccessible()) field.setAccessible(true);

        try {
            Class<?> type = field.getType();
            if (!type.isPrimitive()) {
                field.set(bean, newValue);
                return;
            }

            if (type.equals(boolean.class)) {
                field.setBoolean(bean, ((Boolean) newValue).booleanValue());
            } else if (type.equals(byte.class)) {
                field.setByte(bean, ((Number) newValue).byteValue());
            } else if (type.equals(short.class)) {
                field.setShort(bean, ((Number) newValue).shortValue());
            } else if (type.equals(char.class)) {
                field.setChar(bean, ((Character) newValue).charValue());
            } else if (type.equals(int.class)) {
                field.setInt(bean, ((Number) newValue).intValue());
            } else if (type.equals(long.class)) {
                field.setLong(bean, ((Number) newValue).longValue());
            } else if (type.equals(float.class)) {
                field.setFloat(bean, ((Number) newValue).floatValue());
            } else if (type.equals(double.class)) {
                field.setDouble(bean, ((Number) newValue).doubleValue());
            }
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
        }
    }

    public static void setZeroValue(Object bean, Field field) {
        if (!field.isAccessible()) field.setAccessible(true);

        try {
            Class<?> type = field.getType();
            if (!type.isPrimitive()) {
                field.set(bean, null);
                return;
            }

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
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
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

    public static Class<?> getArrayClass(Class<?> componentType) {
        String name;
        if (componentType.isArray()) {
            // just add a leading "["
            name = "[" + componentType.getName();
        } else if (componentType == boolean.class) {
            name = "[Z";
        } else if (componentType == byte.class) {
            name = "[B";
        } else if (componentType == char.class) {
            name = "[C";
        } else if (componentType == double.class) {
            name = "[D";
        } else if (componentType == float.class) {
            name = "[F";
        } else if (componentType == int.class) {
            name = "[I";
        } else if (componentType == long.class) {
            name = "[J";
        } else if (componentType == short.class) {
            name = "[S";
        } else {
            // must be an object non-array class
            name = "[L" + componentType.getName() + ";";
        }

        try {
            ClassLoader classLoader = componentType.getClassLoader();
            if (classLoader != null) {
                return classLoader.loadClass(name);
            } else {
                return Class.forName(name);
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
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
            return getTypeArgument((ParameterizedType) genericType);
        }
        return null;
    }

    public static Class<?>[] getTypeArguments(Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            return getTypeArguments((ParameterizedType) genericType);
        }
        return new Class[0];
    }

    public static Class<?> getTypeArgument(Method method) {
        Type genericType = method.getGenericReturnType();
        if (genericType instanceof ParameterizedType) {
            return getTypeArgument((ParameterizedType) genericType);
        }
        return null;
    }

    public static Class<?>[] getTypeArguments(Method method) {
        Type genericType = method.getGenericReturnType();
        if (genericType instanceof ParameterizedType) {
            return getTypeArguments((ParameterizedType) genericType);
        }
        return new Class[0];
    }

    public static Class<?> getTypeArgument(ParameterizedType parameterizedType) {
        Type[] types = parameterizedType.getActualTypeArguments();
        return types.length == 0 ? null : (Class<?>) types[0];
    }

    public static Class<?>[] getTypeArguments(ParameterizedType parameterizedType) {
        Type[] types = parameterizedType.getActualTypeArguments();

        int size = types.length;
        Class<?>[] classes = new Class[size];
        for (int i = 0; i < size; i++) {
            Type type = types[i];
            Type rawType = type;
            if (type instanceof ParameterizedType) {
                rawType = ((ParameterizedType) type).getRawType();
            }
            classes[i] = (Class<?>) rawType;
        }
        return classes;
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

}
