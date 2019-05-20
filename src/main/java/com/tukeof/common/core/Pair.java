package com.tukeof.common.core;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Pair<T1, T2> {

    private T1 first;

    private T2 second;

    public T1 first() {
        return first;
    }

    public T2 second() {
        return second;
    }

    public Pair<T2, T1> reverse() {
        return new Pair<>(second, first);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)",
                first().toString(), second().toString());
    }

}
