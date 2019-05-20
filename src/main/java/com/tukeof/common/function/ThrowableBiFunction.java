package com.tukeof.common.function;

@FunctionalInterface
public interface ThrowableBiFunction<T1, T2, R> {
    R apply(T1 v1, T2 v2) throws Exception;
}
