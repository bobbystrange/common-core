package org.dreamcat.common.function;

/**
 * see also {@link java.util.function.ToLongBiFunction }
 */
@FunctionalInterface
public interface ToLongTriFunction<T1, T2, T3> {

    long applyAsLong(T1 v1, T2 v2, T3 v3);
}
