package org.dreamcat.common.function;

/**
 * Create by tuke on 2020/4/16
 */
@FunctionalInterface
public interface QuaFunction<T1, T2, T3, T4, R> {

    R apply(T1 v1, T2 v2, T3 v3, T4 v4);
}
