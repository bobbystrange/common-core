package org.dreamcat.common.sql;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.ReflectUtil;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2019-01-23
 */
@Slf4j
public final class ResultSetUtil {

    public static <T> T mapper(ResultSet rs, Class<T> clazz) throws SQLException {
        T bean;
        try {
            bean = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
        mapper(rs, bean);
        return bean;
    }

    public static <T> void mapper(ResultSet rs, T bean) throws SQLException {
        Class<?> clazz = Objects.requireNonNull(bean).getClass();
        List<Field> fieldList = ReflectUtil.retrieveFields(clazz);
        int size = fieldList.size();
        for (int i = 1; i <= size; i++) {
            Field field = fieldList.get(i);

            field.setAccessible(true);
            Class<?> fieldClass = field.getType();
            Object value = getByClass(rs, fieldClass, i);

            try {
                field.set(bean, value);
            } catch (IllegalAccessException e) {
                log.warn(e.getMessage());
            }
        }
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static Object[] getAll(ResultSet rs, int columnSize) throws SQLException {
        Object[] a = new Object[columnSize];
        for (int i = 0; i < columnSize; i++) {
            a[i] = rs.getObject(i + 1);
        }
        return a;
    }

    public static List<Object[]> batchGetAll(ResultSet rs, int columnSize, int batchNumber) throws SQLException {
        List<Object[]> a = new ArrayList<>(batchNumber);
        for (int i = 0; i < batchNumber; i++) {
            if (!rs.next()) break;
            a.add(getAll(rs, columnSize));
        }
        return a;
    }


    public static String toInsertSql(List<Object[]> batchArgs, String table) {
        return toInsertSql(batchArgs, table, "*");
    }

    public static String toInsertSql(List<Object[]> batchArgs, String table, String columnNames) {
        StringBuilder sql = new StringBuilder(String.format(
                "insert into `%s`(%s) values ", table, columnNames));

        int size = batchArgs.size();
        for (int i = 0; i < size; i++) {
            Object[] row = batchArgs.get(i);
            sql.append("(");
            sql.append(Arrays.stream(row).map(it -> {
                if (it == null) return "NULL";
                else if (it instanceof Boolean) {
                    return (Boolean) it ? "1" : "0";
                } else {
                    return String.format("'%s'", escape(it.toString()));
                }
            }).collect(Collectors.joining(",")));
            sql.append(")");
            if (i != size - 1) sql.append(",");
        }
        sql.append(";");
        return sql.toString();
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private static Object getByClass(ResultSet rs, Class<?> clazz, int columnIndex) throws SQLException {
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

    // escape to SQL Literal, only handle ' and /
    private static String escape(String s) {
        return s.replace("\\", "\\\\")
                .replace("'", "\\'");
    }
}
