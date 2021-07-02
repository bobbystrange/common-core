package org.dreamcat.common.function;

/**
 * @author Jerry Will
 * @since 2021-07-02
 */
@FunctionalInterface
public interface ExpBiFunction<T1, T2, R, E extends Exception> {

    R apply(T1 v1, T2 v2) throws E;
}

