package org.dreamcat.common.core.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Create by tuke on 2020/3/29
 */
public class RealFilterChain<S, T> implements ModalFilter.Chain<S, T> {
    private final BiConsumer<S, T> service;
    private final List<ModalFilter<S, T>> filters = new ArrayList<>();
    private int pos = 0;

    public RealFilterChain(BiConsumer<S, T> service) {
        this.service = service;
    }

    @Override
    public void filter(S source, T target) {
        // Call the next filter if there is one
        if (pos < filters.size()) {
            ModalFilter<S, T> filter = filters.get(pos++);
            filter.filter(source, target, this);
            return;
        }

        // We fell off the end of the chain -- call the service instance
        service.accept(source, target);
    }

    public RealFilterChain<S, T> addFilter(ModalFilter<S, T> filter) {
        if (filters.contains(filter)) return this;
        synchronized (this.filters) {
            filters.add(filter);
        }
        return this;
    }

    public RealFilterChain<S, T> addFilter(List<ModalFilter<S, T>> filters) {
        for (ModalFilter<S, T> filter: filters) {
            addFilter(filter);
        }
        return this;
    }

    public void release() {
        filters.clear();
        pos = 0;
    }

    public void reuse() {
        pos = 0;
    }

    public RealFilterChain<S, T> newChain() {
        RealFilterChain<S, T> chain = new RealFilterChain<>(service);
        synchronized (this.filters){
            chain.filters.addAll(this.filters);
        }
        return chain;
    }
}
