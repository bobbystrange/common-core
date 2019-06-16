package org.dreamcat.common.function;

/**
 * Create by tuke on 2019-06-06
 */
@FunctionalInterface
public interface ThrowableObjectArrayFunction<T> {
    T apply(Object... args) throws Exception;
}
