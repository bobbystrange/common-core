package org.dreamcat.common.core.filter;

/**
 * Create by tuke on 2020/3/29
 */
public interface VoidFilter {

    void filter(Chain chain);

    interface Chain {
        void filter();
    }
}
