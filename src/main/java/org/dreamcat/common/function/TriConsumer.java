package org.dreamcat.common.function;

@FunctionalInterface
public interface TriConsumer<T1, T2, T3> {

    void accept(T1 v1, T2 v2, T3 v3);
}
