package com.tukeof.common.function;

@FunctionalInterface
public interface ThrowableFunction<T, R> {
    R apply(T v) throws Exception;
}
