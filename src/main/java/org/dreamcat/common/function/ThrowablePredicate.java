package org.dreamcat.common.function;

@FunctionalInterface
public interface ThrowablePredicate<T> {
    boolean test(T v) throws Exception;
}
