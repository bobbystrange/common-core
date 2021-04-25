package org.dreamcat.common.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamcat.common.util.StringUtil;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
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

    public static <T1, T2, T3> Triple<T1, T2, T3> emptyTriple() {
        return of(null, null, null);
    }

    public static <T1, T2, T3> Triple<T1, T2, T3> of(T1 first, T2 second, T3 third) {
        return new Triple<>(first, second, third);
    }

    public static <T1, T2, T3> Triple<T1, T2, T3> of(Pair<T1, T2> pair, T3 third) {
        return of(pair.first(), pair.second(), third);
    }

    public static <T1, T2, T3> Triple<T1, T2, T3> of(T1 first, Pair<T2, T3> pair) {
        return of(first, pair.first(), pair.second());
    }

    public T3 third() {
        return third;
    }

    public Triple<T3, T2, T1> reverseTriple() {
        return new Triple<>(third, second, first);
    }

    public Triple<T1, T2, T3> update(T1 first, T2 second, T3 third) {
        super.update(first, second);
        this.third = third;
        return this;
    }

    @Override
    public Triple<T1, T2, T3> updateFirst(T1 first) {
        super.updateFirst(first);
        return this;
    }

    @Override
    public Triple<T1, T2, T3> updateSecond(T2 second) {
        super.updateSecond(second);
        return this;
    }

    public Triple<T1, T2, T3> updateThird(T3 third) {
        this.third = third;
        return this;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && third == null;
    }

    public boolean hasThird() {
        return third != null;
    }

    /// impl

    @Override
    public String toString() {
        return String.format("(%s, %s, %s)",
                StringUtil.string(first),
                StringUtil.string(second),
                StringUtil.string(third));
    }
}
