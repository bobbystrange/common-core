package org.dreamcat.common.function;

@FunctionalInterface
public interface ThrowableSupplier<R> {
    R get() throws Exception;
}
