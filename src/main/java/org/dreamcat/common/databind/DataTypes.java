package org.dreamcat.common.databind;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.dreamcat.common.util.ObjectUtil;

/**
 * @author Jerry Will
 * @since 2021-06-20
 */
public final class DataTypes {

    private DataTypes() {
    }

    public static DataType fromType(Class<?> type, Type... parameterTypes) {
        if (ObjectUtil.isEmpty(parameterTypes)) return fromType(type, EMPTY_ARRAY);
        return fromType(type, Arrays.stream(parameterTypes).map(DataTypes::fromType)
                .toArray(DataType[]::new));
    }

    public static DataType fromType(Class<?> type) {
        return fromType(type, EMPTY_ARRAY);
    }

    public static DataType fromType(Class<?> type, DataType... parameterTypes) {
        if (!type.isArray()) {
            return new DataType(type, parameterTypes);
        }
        return arrayType(type.getComponentType());
    }

    public static DataType arrayType(Class<?> componentType) {
        return arrayType(fromType(componentType));
    }

    public static DataType arrayType(DataType componentDataType) {
        return new DataType(componentDataType);
    }

    public static DataType fromType(Type type) {
        return fromType(type, Collections.emptyMap());
    }

    public static DataType fromType(Type type, Map<String, DataType> typeVars) {
        if (type instanceof ParameterizedType) {
            ParameterizedType genericType = (ParameterizedType) type;
            Class<?> rawType = (Class<?>) genericType.getRawType();
            DataType[] parameterTypes = getTypeArguments(genericType, typeVars);
            return new DataType(rawType, parameterTypes);
        } else if (type instanceof GenericArrayType) {
            GenericArrayType genericType = (GenericArrayType) type;
            Type genericComponentType = genericType.getGenericComponentType();
            DataType componentType = fromType(genericComponentType, typeVars);
            return new DataType(componentType);
        } else if (type instanceof Class) {
            Class<?> clazz = (Class<?>) type;
            return fromType(clazz);
        } else if (type instanceof DataType) {
            return (DataType) type;
        } else if (type instanceof TypeVariable) {
            TypeVariable<?> tv = (TypeVariable<?>) type;
            return typeVars.getOrDefault(tv.getName(), DataType.OBJECT);
        } else {
            return new DataType(Object.class);
        }
    }

    private static DataType[] getTypeArguments(ParameterizedType parameterizedType, Map<String, DataType> typeVars) {
        Type[] arguments = parameterizedType.getActualTypeArguments();

        int size = arguments.length;
        DataType[] types = new DataType[size];
        for (int i = 0; i < size; i++) {
            types[i] = fromType(arguments[i], typeVars);
        }
        return types;
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    private static final DataType[] EMPTY_ARRAY = new DataType[0];

}
