package org.dreamcat.common.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Create by tuke on 2020/7/22
 */
@Getter
@EqualsAndHashCode
@SuppressWarnings("rawtypes")
public class Union {

    private Object value;
    private Class type;

    public Union(Object value) {
        set(value);
    }

    public <T> T get(Class<T> type) {
        if (!this.type.equals(type)) return null;
        return type.cast(value);
    }

    public <T> boolean hasType(Class<T> type) {
        return this.type.equals(type);
    }

    public void set(Object value) {
        this.value = value;
        this.type = value.getClass();
    }


}
