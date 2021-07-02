package org.dreamcat.common.function;

/**
 * @author Jerry Will
 * @since 2021-07-02
 */
@FunctionalInterface
public interface ExpConsumer<T, E extends Exception> {

    void accept(T v) throws E;
}

