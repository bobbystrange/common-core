package org.dreamcat.common.function;

@FunctionalInterface
public interface ThrowableTriFunction<T1, T2, T3, R> {
    R apply(T1 v1, T2 v2, T3 v3) throws Exception;
}
