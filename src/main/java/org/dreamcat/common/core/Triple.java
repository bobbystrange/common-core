package org.dreamcat.common.core;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class Triple<T1, T2, T3> extends Pair<T1, T2> {

    private T3 third;

    public Triple(T1 first, T2 second, T3 third) {
        super(first, second);
        this.third = third;
    }

    public Triple(Pair<T1, T2> pair, T3 third) {
        this(pair.first(), pair.second(), third);
    }

    public Triple(T1 first, Pair<T2, T3> pair) {
        this(first, pair.first(), pair.second());
    }

    public T3 third() {
        return third;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s)",
                first().toString(), second().toString(), third().toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Triple)) return false;
        if (!super.equals(o)) return false;
        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
        return third.equals(triple.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), third);
    }
}
