package com.tukeof.common.function;

@FunctionalInterface
public interface ThrowableSupplier<R> {
    R get() throws Exception;
}
