package org.dreamcat.common.core;

import lombok.RequiredArgsConstructor;

/**
 * Create by tuke on 2020/4/25
 */
@RequiredArgsConstructor
public class Tuple2<T1, T2> {
    public final T1 _1;
    public final T2 _2;

    public static <T1, T2> Tuple2<T1, T2> of(T1 _1, T2 _2) {
        return new Tuple2<>(_1, _2);
    }

}
