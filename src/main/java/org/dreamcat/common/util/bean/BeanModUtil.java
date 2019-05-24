package org.dreamcat.common.util.bean;

import org.dreamcat.common.util.ObjectUtil;
import org.dreamcat.common.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Create by tuke on 2019-01-23
 */
public class BeanModUtil {

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====
    // bean copy
    public static <T> T copy(Object source, Class<T> clazz) {
        ObjectUtil.checkNotNull(source, "source");
        T target = null;
        try {
            target = clazz.newInstance();
            copyProperties(source, target);
        } catch (Throwable ignored) {
        }

        return target;
    }

    public static void copyProperties(Object source, Object target) throws IllegalAccessException {
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
                targetField.set(target, sourceField.get(source));
            }
        }

    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====
    // bean op
    public static <T> T nullify(T bean) {
        Class<?> clazz = bean.getClass();
        List<Field> fields = new ArrayList<>();
        ReflectUtil.retrieveFields(fields, clazz);
        if (fields.isEmpty()) return bean;
        for (Field field : fields) {
            nullifyField(bean, field);
        }
        return bean;
    }

    public static void nullifyField(Object bean, Field field) {
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


}
