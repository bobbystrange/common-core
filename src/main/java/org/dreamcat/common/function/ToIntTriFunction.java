package org.dreamcat.common.function;

@FunctionalInterface
public interface ToIntTriFunction<T1, T2, T3> {
    int apply(T1 v1, T2 v2, T3 v3);
}
