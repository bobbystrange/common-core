package org.dreamcat.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@SuppressWarnings({"unchecked", "rawtypes"})
public class ReflectUtil {

    public static void retrieveSuperClasses(Class<?> clazz, List<Class> classList) {
        Class superClazz = clazz.getSuperclass();
        classList.add(superClazz);
        if (!superClazz.equals(Object.class)) {
            retrieveSuperClasses(superClazz, classList);
        }
    }

    public static <T> void retrieveSubClasses(Class<T> clazz, List<Class<? extends T>> classList, Collection<String> classPaths) {
        if (ObjectUtil.isEmpty(classPaths)) {
            retrieveSubClasses(clazz, classList, "");
            return;
        }

        for (String classPath : classPaths) {
            String name = "/" + classPath.replace(".", "/");
            File classFile = new File(Class.class.getResource(name).getFile());
            String prefix = classFile.getPath() + "/";
            findSubClasses(classFile, prefix, clazz, classList);
        }
    }

    public static <T> void retrieveSubClasses(Class<T> clazz, List<Class<? extends T>> classList, String... classPaths) {
        retrieveSubClasses(clazz, classList, Arrays.asList(classPaths));
    }

    public static void retrieveMethods(Class<?> clazz, List<Method> methodList) {
        Method[] methods = clazz.getDeclaredMethods();
        methodList.addAll(Arrays.asList(methods));

        Class superClazz = clazz.getSuperclass();
        if (!superClazz.equals(Object.class)) {
            retrieveMethods(superClazz, methodList);
        }
    }

    public static List<Field> retrieveFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        retrieveFields(clazz, fields);
        Collections.reverse(fields);
        return fields;
    }

    public static void retrieveFields(Class<?> clazz, List<Field> fieldList) {
        Field[] fields = clazz.getDeclaredFields();
        for (int size = fields.length, i=size -1; i>=0; i--) {
            fieldList.add(fields[i]);
        }

        Class superClazz = clazz.getSuperclass();
        if (!superClazz.equals(Object.class)) {
            retrieveFields(superClazz, fieldList);
        }
    }

    public static <A extends Annotation> Map<String, Field> retrieveFields(Class<?> clazz, Class<A> aliasClass, Function<A, String> aliasFunction) {
        Map<String, Field> fieldMap = new HashMap<>();
        retrieveFields(clazz, fieldMap, aliasClass, aliasFunction);
        return fieldMap;
    }

    public static <A extends Annotation> void retrieveFields(Class<?> clazz, Map<String, Field> fieldMap) {
        retrieveFields(clazz, fieldMap, null, null);
    }

    public static <A extends Annotation> void retrieveFields(Class<?> clazz, Map<String, Field> fieldMap, Class<A> aliasClass, Function<A, String> aliasFunction) {
        Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields)
                .forEach((field) -> {
                    String name = retrieveFieldName(field, aliasClass, aliasFunction);
                    fieldMap.put(name, field);
                });

        Class superClazz = clazz.getSuperclass();
        if (!superClazz.equals(Object.class)) {
            retrieveFields(superClazz, fieldMap, aliasClass, aliasFunction);
        }
    }

    public static List<String> retrieveFieldNames(Class<?> clazz) {
        return retrieveFieldNames(clazz, null, null);
    }

    public static <A extends Annotation> List<String> retrieveFieldNames(Class<?> clazz, Class<A> aliasClass, Function<A, String> aliasFunction) {
        List<String> fieldNameList = new ArrayList<>();
        retrieveFieldNames(clazz, fieldNameList, aliasClass, aliasFunction);
        return fieldNameList;
    }

    public static void retrieveFieldNames(Class<?> clazz, List<String> fieldNameList) {
        retrieveFieldNames(clazz, fieldNameList, null, null);
    }

    // only no static field
    public static <A extends Annotation> void retrieveFieldNames(Class<?> clazz, List<String> fieldNameList, Class<A> aliasClass, Function<A, String> aliasFunction) {
        Field[] fields = clazz.getDeclaredFields();
        fieldNameList.addAll(Arrays.stream(fields)
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .map(field -> retrieveFieldName(field, aliasClass, aliasFunction))
                .collect(Collectors.toList()));

        Class superClazz = clazz.getSuperclass();
        if (!superClazz.equals(Object.class)) {
            retrieveFieldNames(superClazz, fieldNameList, aliasClass, aliasFunction);
        }
    }

    public static <A extends Annotation> String retrieveFieldName(Field field, Class<A> aliasClass, Function<A, String> aliasFunction) {
        String fieldName = field.getName();
        Annotation[] annotations = field.getAnnotations();

        if (annotations == null || annotations.length == 0
                || aliasClass == null) {
            return fieldName;
        }

        for (Annotation annotation: annotations) {
            if (!aliasClass.isInstance(annotation)) continue;
            return aliasFunction.apply(aliasClass.cast(annotation));
        }
        return fieldName;
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
            throw new AssertionError("Primitive types are not enumerated completely");
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
                // note avoid unexpected actions
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
        Class<?> clazz = a.getClass();
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
            throw new AssertionError("Primitive types are not enumerated completely");
        }
    }

    // long[][].class  -->  long[][]
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

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

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

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====


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
                log.debug(e.getMessage());
            }
        }
    }
}
