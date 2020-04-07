package org.dreamcat.common.core.filter;

/**
 * Create by tuke on 2020/3/29
 */
public interface ModalFilter<S, T> {

    void filter(S source , T target, Chain<S, T> chain);

    interface Chain<S, T> {
        void filter(S source , T target);
    }
}
