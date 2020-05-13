package org.dreamcat.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@SuppressWarnings({"unchecked", "rawtypes"})
public class ReflectUtil {

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private static final String[] ANNOTATION_ALIAS_METHODS = new String[]{
            "value", "name", "alias"
    };

    public static void retrieveSuperClasses(Class<?> clazz, List<Class> classList) {
        Class superClazz = clazz.getSuperclass();
        classList.add(superClazz);
        if (!superClazz.equals(Object.class)) {
            retrieveSuperClasses(superClazz, classList);
        }
    }

    public static void retrieveMethods(Class<?> clazz, List<Method> methodList) {
        Method[] methods = clazz.getDeclaredMethods();
        methodList.addAll(Arrays.asList(methods));

        Class superClazz = clazz.getSuperclass();
        if (!superClazz.equals(Object.class)) {
            retrieveMethods(superClazz, methodList);
        }
    }

    public static void retrieveFields(Class<?> clazz, List<Field> fieldList) {
        Field[] fields = clazz.getDeclaredFields();
        fieldList.addAll(Arrays.asList(fields));

        Class superClazz = clazz.getSuperclass();
        if (!superClazz.equals(Object.class)) {
            retrieveFields(superClazz, fieldList);
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

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static void retrieveFields(Class<?> clazz, Map<String, Field> fieldMap, Class... aliasAnnotationClasses) {
        Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields)
                .forEach((field) -> {
                    String name = retrieveFieldName(field, aliasAnnotationClasses);
                    fieldMap.put(name, field);
                });

        Class superClazz = clazz.getSuperclass();
        if (!superClazz.equals(Object.class)) {
            retrieveFields(superClazz, fieldMap, aliasAnnotationClasses);
        }
    }

    // hasModifiersAndAnyAnnotation static, transient, volatile
    public static void retrieveFieldNames(Class<?> clazz, List<String> fieldNameList, Class... aliasAnnotationClasses) {
        Field[] fields = clazz.getDeclaredFields();
        fieldNameList.addAll(Arrays.stream(fields)
                .filter(field -> {
                    int modifiers = field.getModifiers();
                    return !Modifier.isStatic(modifiers)
                            && !Modifier.isTransient(modifiers)
                            && !Modifier.isVolatile(modifiers);
                })
                .map(field -> retrieveFieldName(field, aliasAnnotationClasses))
                .collect(Collectors.toList()));

        Class superClazz = clazz.getSuperclass();
        if (!superClazz.equals(Object.class)) {
            retrieveFieldNames(superClazz, fieldNameList, aliasAnnotationClasses);
        }
    }

    // not excluding strategy
    public static String retrieveFieldName(Field field, Class... aliasAnnotationClasses) {
        String fieldName = field.getName();
        Annotation[] annotations = field.getAnnotations();

        if (annotations == null || annotations.length == 0
                || aliasAnnotationClasses == null || aliasAnnotationClasses.length == 0) {
            return fieldName;
        }

        List<Class> aliasAnnotationList = Arrays.asList(aliasAnnotationClasses);
        List<Annotation> filteredAnnotationList = Arrays.stream(annotations)
                .filter(it -> aliasAnnotationList.contains(it.getClass()))
                .collect(Collectors.toList());
        if (ObjectUtil.isNotEmpty(filteredAnnotationList)) {
            for (Annotation annotation : filteredAnnotationList) {
                String alias = retrieveAnnotationAlias(annotation);
                if (alias != null) {
                    return alias;
                }
            }
        }
        return fieldName;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static String retrieveAnnotationAlias(Annotation aliasAnnotation) {
        for (String name : ANNOTATION_ALIAS_METHODS) {
            try {
                return aliasAnnotation.getClass().getDeclaredMethod(name).invoke(aliasAnnotation).toString();
            } catch (Exception ignored) {
            }
        }
        return null;
    }

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

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

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
            throw new RuntimeException("Primitive types are not enumerated completely");
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
