package org.dreamcat.common.function;

@FunctionalInterface
public interface ThrowableConsumer<T> {

    void accept(T v) throws Exception;
}
