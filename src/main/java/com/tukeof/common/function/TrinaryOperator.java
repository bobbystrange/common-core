package com.tukeof.common.function;

@FunctionalInterface
public interface TrinaryOperator<T> {
    T apply(T v1, T v2, T v3);
}
