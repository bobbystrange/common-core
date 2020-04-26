package org.dreamcat.common.core;

import lombok.RequiredArgsConstructor;

/**
 * Create by tuke on 2020/4/25
 */
@RequiredArgsConstructor
public class Tuple4<T1, T2, T3, T4> {
    public final T1 _1;
    public final T2 _2;
    public final T3 _3;
    public final T4 _4;

    public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> of(T1 _1, T2 _2, T3 _3, T4 _4) {
        return new Tuple4<>(_1, _2, _3, _4);
    }
}
