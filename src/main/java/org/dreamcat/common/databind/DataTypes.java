package org.dreamcat.common.databind;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Jerry Will
 * @since 2021-06-20
 */
public final class DataTypes {

    private DataTypes() {
    }

    public static DataType fromType(Class<?> type) {
        return fromType(type, EMPTY_ARRAY);
    }

    public static DataType fromType(Class<?> type, DataType... parameterTypes) {
        if (!type.isArray()) {
            return new DataType(type, parameterTypes);
        }
        Class<?> componentType = type.getComponentType();
        DataType componentDataType = fromType(componentType);
        return new DataType(componentDataType, parameterTypes);
    }

    public static DataType fromArrayType(Class<?> componentType) {
        return fromArrayType(fromType(componentType));
    }

    public static DataType fromArrayType(DataType componentType) {
        return new DataType(componentType, EMPTY_ARRAY);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static DataType fromField(Field field) {
        Class<?> clazz = field.getType();
        if (clazz.isArray()) {
            return fromArrayType(fromType(clazz.getComponentType()));
        }
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            return fromType(clazz, getTypeArguments((ParameterizedType) genericType));
        }
        return fromType(clazz);
    }

    private static DataType[] getTypeArguments(ParameterizedType parameterizedType) {
        Type[] arguments = parameterizedType.getActualTypeArguments();

        int size = arguments.length;
        DataType[] types = new DataType[size];
        for (int i = 0; i < size; i++) {
            Type argument = arguments[i];
            if (argument instanceof ParameterizedType) {
                ParameterizedType subParameterizedType = (ParameterizedType) argument;
                Type rawType = subParameterizedType.getRawType();
                DataType[] parameterTypes = getTypeArguments(subParameterizedType);
                types[i] = fromType((Class<?>) rawType, parameterTypes);
            } else {
                types[i] = fromType((Class<?>) argument);
            }
        }
        return types;
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    private static final DataType[] EMPTY_ARRAY = new DataType[0];

}
