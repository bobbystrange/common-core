package com.tukeof.common.util;

import com.tukeof.common.annotation.NotNull;
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
import java.util.stream.Collectors;

@Slf4j
public class ReflectUtil {

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private static final String[] ANNOTATION_ALIAS_METHODS = new String[]{
            "value", "name"
    };

    public static void retrieveSuperClasses(List<Class> classList, Class<?> clazz) {
        Class superClazz = clazz.getSuperclass();
        classList.add(superClazz);
        if (!superClazz.equals(Object.class)) {
            retrieveSuperClasses(classList, superClazz);
        }
    }

    public static void retrieveMethods(List<Method> methodList, Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        methodList.addAll(Arrays.asList(methods));

        Class superClazz = clazz.getSuperclass();
        if (!superClazz.equals(Object.class)) {
            retrieveMethods(methodList, superClazz);
        }
    }

    public static void retrieveFields(List<Field> fieldList, Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        fieldList.addAll(Arrays.asList(fields));

        Class superClazz = clazz.getSuperclass();
        if (!superClazz.equals(Object.class)) {
            retrieveFields(fieldList, superClazz);
        }
    }

    public static <T> void retrieveSubClasses(List<Class<? extends T>> classList, Class<T> clazz, Collection<String> classPaths) {
        if (ObjectUtil.isEmpty(classPaths)) {
            retrieveSubClasses(classList, clazz, "");
            return;
        }

        for (String classPath : classPaths) {
            String name = "/" + classPath.replace(".", "/");
            File classFile = new File(Class.class.getResource(name).getFile());
            String prefix = classFile.getPath() + "/";
            findSubClasses(classFile, prefix, clazz, classList);
        }
    }

    public static <T> void retrieveSubClasses(List<Class<? extends T>> classList, Class<T> clazz, String... classPaths) {
        retrieveSubClasses(classList, clazz, Arrays.asList(classPaths));
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static void retrieveFields(Class<?> clazz, Map<String, Field> fieldMap, Class... annotationClasses) {
        Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields)
                .forEach((field) -> {
                    String name = retrieveFieldName(field, annotationClasses);
                    fieldMap.put(name, field);
                });

        Class superClazz = clazz.getSuperclass();
        if (!superClazz.equals(Object.class)) {
            retrieveFields(superClazz, fieldMap, annotationClasses);
        }
    }

    // hasModifiersAndAnyAnnotation static, transient, volatile
    public static void retrieveFieldNames(Class<?> clazz, List<String> fieldNameList, Class... annotationClasses) {
        Field[] fields = clazz.getDeclaredFields();
        fieldNameList.addAll(Arrays.stream(fields)
                .filter(field -> {
                    int modifiers = field.getModifiers();
                    return !Modifier.isStatic(modifiers)
                            && !Modifier.isTransient(modifiers)
                            && !Modifier.isVolatile(modifiers);
                })
                .map(field -> retrieveFieldName(field, annotationClasses))
                .collect(Collectors.toList()));

        Class superClazz = clazz.getSuperclass();
        if (!superClazz.equals(Object.class)) {
            retrieveFieldNames(superClazz, fieldNameList, annotationClasses);
        }
    }

    // not excluding strategy
    public static String retrieveFieldName(Field field, Class... annotationClasses) {
        String fieldName = field.getName();
        Annotation[] annotations = field.getAnnotations();

        if (annotations == null || annotations.length == 0
                || annotationClasses == null || annotationClasses.length == 0) {
            return fieldName;
        }

        List<Class> aliasAnnotationList = Arrays.asList(annotationClasses);
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

    public static String retrieveAnnotationAlias(Annotation annotation) {
        for (String name : ANNOTATION_ALIAS_METHODS) {
            try {
                return annotation.getClass().getDeclaredMethod(name).invoke(annotation).toString();
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

    @SuppressWarnings("unchecked")
    public static boolean hasAnyAnnotation(Field field, Class... annotationClasses) {
        if (ObjectUtil.isEmpty(annotationClasses)) return false;

        Annotation[] annotations = field.getAnnotations();
        if (ObjectUtil.isEmpty(annotations)) return false;

        for (Class<? extends Annotation> annotationClass : annotationClasses) {
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

    public static boolean isBoxedClass(@NotNull Class<?> clazz) {
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

    @SuppressWarnings("unchecked")
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
