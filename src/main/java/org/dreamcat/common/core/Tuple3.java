package org.dreamcat.common.core;

import lombok.RequiredArgsConstructor;

/**
 * Create by tuke on 2020/4/25
 */
@RequiredArgsConstructor
public class Tuple3<T1, T2, T3> {
    public final T1 _1;
    public final T2 _2;
    public final T3 _3;

    public static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 _1, T2 _2, T3 _3) {
        return new Tuple3<>(_1, _2, _3);
    }
}
