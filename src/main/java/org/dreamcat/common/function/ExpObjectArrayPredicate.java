package org.dreamcat.common.function;

/**
 * @author Jerry Will
 * @since 2021-07-02
 */
@FunctionalInterface
public interface ExpObjectArrayPredicate<E extends Exception> {

    boolean apply(Object... args) throws E;
}
