package org.dreamcat.common.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    public <T3> Triple<T1, T2, T3> join(T3 thrid) {
        return new Triple<>(this, thrid);
    }

    public <T3> Triple<T3, T1, T2> leftJoin(T3 thrid) {
        return new Triple<>(thrid, this);
    }

    public <T3> Triple<T1, T3, T2> middleJoin(T3 thrid) {
        return new Triple<>(first, thrid, second);
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
