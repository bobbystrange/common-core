package org.dreamcat.common.databind;

import java.lang.reflect.Type;
import java.util.Arrays;
import org.dreamcat.common.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create by tuke on 2021/4/15
 */
public interface DataType extends Type {

    boolean isArray();

    @Override
    String toString();

    Class<?> getType();

    DataType[] EMPTY_ARRAY = new DataType[0];

    Logger log = LoggerFactory.getLogger(DataType.class);

    static DataType type(Class<?> type) {
        return type(type, EMPTY_ARRAY);
    }

    static DataType type(Class<?> type, DataType... parameterTypes) {
        if (!type.isArray()) {
            return new SimpleDataType(type, parameterTypes);
        }
        if (ObjectUtil.isNotEmpty(parameterTypes) && log.isWarnEnabled()) {
            log.warn("parameterTypes `{}` is ignored since parsing a array type {}",
                    Arrays.deepToString(parameterTypes), type);
        }

        Class<?> componentType = type.getComponentType();
        DataType componentDataType = type(componentType);
        return new ArrayDataType(componentDataType);
    }

    static ArrayDataType arrayType(Class<?> componentType) {
        return arrayType(type(componentType));
    }

    static ArrayDataType arrayType(DataType componentType) {
        return new ArrayDataType(componentType);
    }
}
