package org.dreamcat.common.core;

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

}
