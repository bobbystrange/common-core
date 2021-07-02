package org.dreamcat.common.function;

/**
 * see also {@link java.util.function.ToIntBiFunction }
 */
@FunctionalInterface
public interface ToIntTriFunction<T1, T2, T3> {

    int applyAsInt(T1 v1, T2 v2, T3 v3);
}
