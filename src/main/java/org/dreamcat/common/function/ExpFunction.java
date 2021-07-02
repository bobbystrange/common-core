package org.dreamcat.common.function;

/**
 * @author Jerry Will
 * @since 2021-07-02
 */
@FunctionalInterface
public interface ExpFunction<T, R, E extends Exception> {

    R apply(T v) throws E;
}