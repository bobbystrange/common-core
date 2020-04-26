package org.dreamcat.common.core;

import lombok.RequiredArgsConstructor;

/**
 * Create by tuke on 2020/4/25
 */
@RequiredArgsConstructor
public class Tuple6<T1, T2, T3, T4, T5, T6> {
    public final T1 _1;
    public final T2 _2;
    public final T3 _3;
    public final T4 _4;
    public final T5 _5;
    public final T6 _6;

    public static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> of(T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6) {
        return new Tuple6<>(_1, _2, _3, _4, _5, _6);
    }
}
