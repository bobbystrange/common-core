package org.dreamcat.common.core;

import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class Pair<T1, T2> {

    private final T1 first;

    private final T2 second;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return first.equals(pair.first) &&
                second.equals(pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
