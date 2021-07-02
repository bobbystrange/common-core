package org.dreamcat.common.function;

/**
 * @author Jerry Will
 * @since 2021-07-02
 */
@FunctionalInterface
public interface ExpTriFunction<T1, T2, T3, R, E extends Exception> {

    R apply(T1 v1, T2 v2, T3 v3) throws E;
}