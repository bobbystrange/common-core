package org.dreamcat.common.function;

/**
 * @author Jerry Will
 * @since 2021-07-02
 */
@FunctionalInterface
public interface ExpPredicate<T, E extends Exception> {

    boolean test(T v) throws E;
}

