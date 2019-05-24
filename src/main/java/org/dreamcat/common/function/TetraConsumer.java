package org.dreamcat.common.function;

@FunctionalInterface
public interface TetraConsumer<T1, T2, T3, T4> {
    void accept(T1 v1, T2 v2, T3 v3, T4 v4);
}
