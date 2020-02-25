package org.dreamcat.common.sql;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.ReflectUtil;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Create by tuke on 2019-01-23
 */
@Slf4j
public class ResultSetUtil {

    public static <T> T mapper(ResultSet rs, int rowNum, Class<T> clazz) throws SQLException {
        T bean;
        try {
            bean = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }

        List<Field> fieldList = new ArrayList<>();
        ReflectUtil.retrieveFields(fieldList, clazz);
        int size = fieldList.size();
        for (int i = 1; i <= size; i++) {
            Field field = fieldList.get(i);

            field.setAccessible(true);
            Class<?> fieldClass = field.getType();
            Object value = getByClass(fieldClass, rs, i);

            try {
                field.set(bean, value);
            } catch (IllegalAccessException ignored) {
            }
        }
        return bean;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private static Object getByClass(Class<?> clazz, ResultSet rs, int columnIndex) throws SQLException {
        if (clazz.equals(Long.class) || clazz.equals(long.class)) {
            return rs.getLong(columnIndex);
        }

        if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
            return rs.getInt(columnIndex);
        }

        if (clazz.equals(Double.class) || clazz.equals(double.class)) {
            return rs.getDouble(columnIndex);
        }

        if (clazz.equals(Float.class) || clazz.equals(float.class)) {
            return rs.getFloat(columnIndex);
        }

        if (clazz.equals(Date.class)) {
            return rs.getDate(columnIndex);
        }

        return rs.getString(columnIndex);
    }

}
