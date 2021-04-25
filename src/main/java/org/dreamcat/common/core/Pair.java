package org.dreamcat.common.core;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamcat.common.util.StringUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Pair<T1, T2> implements Map.Entry<T1, T2> {

    protected T1 first;

    protected T2 second;

    public static <T1, T2> Pair<T1, T2> empty() {
        return of(null, null);
    }

    public static <T1, T2> Pair<T1, T2> of(T1 first, T2 second) {
        return new Pair<>(first, second);
    }

    public T1 first() {
        return first;
    }

    public T2 second() {
        return second;
    }

    public Pair<T2, T1> reverse() {
        return new Pair<>(second, first);
    }

    public <T3> Triple<T1, T2, T3> join(T3 third) {
        return new Triple<>(this, third);
    }

    public <T3> Triple<T3, T1, T2> leftJoin(T3 third) {
        return new Triple<>(third, this);
    }

    public <T3> Triple<T1, T3, T2> middleJoin(T3 third) {
        return new Triple<>(first, third, second);
    }

    public Pair<T1, T2> update(T1 first, T2 second) {
        this.first = first;
        this.second = second;
        return this;
    }

    public Pair<T1, T2> updateFirst(T1 first) {
        this.first = first;
        return this;
    }

    public Pair<T1, T2> updateSecond(T2 second) {
        this.second = second;
        return this;
    }

    public boolean isEmpty() {
        return first == null && second == null;
    }

    public boolean hasFirst() {
        return first != null;
    }

    public boolean hasSecond() {
        return second != null;
    }

    /// impl

    @Override
    public String toString() {
        return String.format("(%s, %s)", StringUtil.string(first), StringUtil.string(second));
    }

    @Override
    public T1 getKey() {
        return first;
    }

    @Override
    public T2 getValue() {
        return second;
    }

    @Override
    public T2 setValue(T2 value) {
        T2 oldValue = second;
        this.second = value;
        return oldValue;
    }
}
