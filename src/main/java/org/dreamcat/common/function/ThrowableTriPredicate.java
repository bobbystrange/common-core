package org.dreamcat.common.function;

@FunctionalInterface
public interface ThrowableTriPredicate<T1, T2, T3> {

    boolean test(T1 v1, T2 v2, T3 v3) throws Exception;
}
