package org.dreamcat.common.core.di;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.ObjectUtil;
import org.dreamcat.common.util.ReflectUtil;

/**
 * Create by tuke on 2021/2/27
 */
@Slf4j
@SuppressWarnings({"unchecked", "accessChecks"})
public class DependencyInjection {

    private final Map<Class<?>, Object> beans = new ConcurrentHashMap<>();

    public <T> void setBean(Class<T> beanClass, T bean) {
        beans.put(beanClass, bean);
    }

    public <T> T getBean(Class<T> beanClass) {
        Object bean = beans.get(beanClass);
        if (bean == null) {
            bean = ReflectUtil.newInstance(beanClass);
            beans.put(beanClass, bean);
            resolveDependencies(bean, beanClass);
        }
        return (T) bean;
    }

    public <T> List<T> getBeansOfType(Class<T> beanClass) {
        return beans.values().stream()
                .filter(beanClass::isInstance)
                .map(beanClass::cast)
                .collect(Collectors.toList());
    }

    private void resolveDependencies(Object bean, Class<?> beanClass) {
        List<Field> fields = ReflectUtil.retrieveFields(beanClass);
        if (ObjectUtil.isEmpty(fields)) return;
        for (Field field : fields) {
            if (Modifier.isFinal(field.getModifiers()) ||
                    resourceClass == null ||
                    field.getDeclaredAnnotation(resourceClass) == null) {
                continue;
            }

            Class<?> fieldType = field.getType();
            Object fieldValue = getBean(fieldType);

            try {
                Method setter = ReflectUtil.getSetter(beanClass, field);
                if (setter != null) {
                    setter.invoke(bean, fieldValue);
                    continue;
                }
                // no setter, then try set the field directly
                field.setAccessible(true);
                field.set(bean, fieldValue);
            } catch (Exception e) {
                log.error("Error while resolve dependencies for " + beanClass, e);
                throw new RuntimeException(e);
            }
        }
    }

    private static final Class<? extends Annotation> resourceClass;

    static {
        resourceClass = findAnnotationClass("javax.annotation.Resource");
    }

    private static Class<? extends Annotation> findAnnotationClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (Annotation.class.isAssignableFrom(clazz)) {
                return (Class<? extends Annotation>) clazz;
            }
        } catch (Exception e) {
            // nop
        }
        return null;
    }
}
