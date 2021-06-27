package org.dreamcat.common.pattern.filter;

/**
 * Create by tuke on 2020/3/29
 */
public interface ExchangeFilter<T> {

    void filter(T exchange, Chain<T> chain);

    interface Chain<T> {

        void filter(T exchange);
    }
}
