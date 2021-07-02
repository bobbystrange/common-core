package org.dreamcat.common.function;

/**
 * see also {@link java.util.function.ToDoubleBiFunction }
 */
@FunctionalInterface
public interface ToDoubleTriFunction<T1, T2, T3> {

    double applyAsDouble(T1 v1, T2 v2, T3 v3);
}
