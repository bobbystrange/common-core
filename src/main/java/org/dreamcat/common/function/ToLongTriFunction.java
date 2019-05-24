package org.dreamcat.common.function;

@FunctionalInterface
public interface ToLongTriFunction<T1, T2, T3> {
    long apply(T1 v1, T2 v2, T3 v3);
}
