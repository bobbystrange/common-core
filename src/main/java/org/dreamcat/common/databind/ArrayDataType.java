package org.dreamcat.common.databind;

import java.lang.reflect.Array;
import org.dreamcat.common.util.ReflectUtil;

/**
 * Create by tuke on 2021/4/19
 */

public class ArrayDataType implements DataType {

    /**
     * just for cache purpose
     */
    protected final Class<?> type;
    /**
     * array component type
     */
    protected final DataType componentType;
    /**
     * a immutable empty array and thus can be shared
     */
    protected final Object emptyArray;

    public ArrayDataType(DataType componentType) {
        this(ReflectUtil.getArrayClass(componentType.getType()), componentType);
    }

    public ArrayDataType(Class<?> type, DataType componentType) {
        this(type, componentType, Array.newInstance(componentType.getType(), 0));
    }

    public ArrayDataType(Class<?> type, DataType componentType, Object emptyArray) {
        this.type = type;
        this.componentType = componentType;
        this.emptyArray = emptyArray;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "[array type, component type: " + componentType + "]";
    }
}
