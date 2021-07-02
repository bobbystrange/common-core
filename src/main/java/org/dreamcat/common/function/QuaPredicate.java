package org.dreamcat.common.function;

/**
 * @author Jerry Will
 * @since 2021-07-02
 */
@FunctionalInterface
public interface QuaPredicate<T1, T2, T3, T4> {

    boolean test(T1 v1, T2 v2, T3 v3, T4 v4);
}
