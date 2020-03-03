package org.dreamcat.common.bean;

import org.dreamcat.common.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by tuke on 2019-06-04
 */
public class BeanUtil {

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====
    // bean op
    public static <T> T nullify(T bean) {
        Class<?> clazz = bean.getClass();
        List<Field> fields = new ArrayList<>();
        ReflectUtil.retrieveFields(clazz, fields);
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

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====


}
