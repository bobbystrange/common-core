package com.tukeof.common.function;

@FunctionalInterface
public interface ThrowableBiPredicate<T1, T2> {
    boolean test(T1 v1, T2 v2) throws Exception;
}
