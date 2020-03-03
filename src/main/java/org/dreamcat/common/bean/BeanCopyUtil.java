package org.dreamcat.common.bean;

import org.dreamcat.common.util.ObjectUtil;
import org.dreamcat.common.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Create by tuke on 2019-06-04
 */
public class BeanCopyUtil {

    // bean field-based copy
    public static <T> T copy(Object source, Class<T> clazz) {
        ObjectUtil.requireNotNull(source, "source");
        T target = null;
        try {
            target = clazz.newInstance();
            copy(source, target);
        } catch (Throwable ignored) {
        }

        return target;
    }

    // slower than cglib, use it in field-based pojo (no getter/setter) only
    public static void copy(Object source, Object target) {
        Map<String, Field> sourceFieldMap = new HashMap<>();
        ReflectUtil.retrieveFields(source.getClass(), sourceFieldMap);

        Map<String, Field> targetFieldMap = new HashMap<>();
        ReflectUtil.retrieveFields(target.getClass(), targetFieldMap);

        Set<String> targetFields = targetFieldMap.keySet();
        for (String sourceFieldName : sourceFieldMap.keySet()) {
            if (targetFields.contains(sourceFieldName)) {

                Field targetField = targetFieldMap.get(sourceFieldName);
                Field sourceField = sourceFieldMap.get(sourceFieldName);

                targetField.setAccessible(true);
                sourceField.setAccessible(true);
                try {
                    targetField.set(target, sourceField.get(source));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
