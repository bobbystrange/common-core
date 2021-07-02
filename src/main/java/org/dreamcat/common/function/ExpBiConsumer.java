package org.dreamcat.common.function;

/**
 * @author Jerry Will
 * @since 2021-07-02
 */
@FunctionalInterface
public interface ExpBiConsumer<T1, T2, E extends Exception> {

    void accept(T1 v1, T2 v2) throws E;
}

