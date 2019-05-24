package org.dreamcat.common.function;

@FunctionalInterface
public interface ThrowableBiConsumer<T1, T2> {
    void accept(T1 v1, T2 v2) throws Exception;
}
