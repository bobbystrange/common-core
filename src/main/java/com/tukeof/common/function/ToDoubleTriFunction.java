package com.tukeof.common.function;

@FunctionalInterface
public interface ToDoubleTriFunction<T1, T2, T3> {
    double apply(T1 v1, T2 v2, T3 v3);
}
