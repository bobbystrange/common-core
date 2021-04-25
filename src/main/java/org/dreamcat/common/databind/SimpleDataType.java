package org.dreamcat.common.databind;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2021/4/19
 */
public class SimpleDataType implements DataType {

    protected final Class<?> type;
    protected final DataType[] parameterTypes;

    public SimpleDataType(Class<?> type) {
        this(type, EMPTY_ARRAY);
    }

    public SimpleDataType(Class<?> type, DataType... parameterTypes) {
        this.type = type;
        this.parameterTypes = parameterTypes;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String toString() {
        String parameterTypesString = Arrays.stream(parameterTypes)
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        String typeName = type.getName();
        return new StringBuilder(typeName.length() + parameterTypesString.length() + 23)
                .append("[simple type, class ")
                .append(type.getName())
                .append('<')
                .append(parameterTypesString)
                .append('>')
                .append(']')
                .toString();
    }
}
