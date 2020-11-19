package org.dreamcat.common.core;

/**
 * Create by tuke on 2020/4/25
 */
public class WriteResult<T> {

    public boolean applied;
    public T data;

    public static <T> WriteResult<T> empty() {
        return new WriteResult<>();
    }

    public boolean wasApplied() {
        return applied;
    }

    public T getData() {
        return data;
    }

    public void update(boolean applied) {
        this.applied = applied;
    }

    public void update(boolean applied, T data) {
        this.applied = applied;
        this.data = data;
    }
}
