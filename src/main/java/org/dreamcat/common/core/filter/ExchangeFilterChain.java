package org.dreamcat.common.core.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Create by tuke on 2020/3/29
 */
public class ExchangeFilterChain<T> implements ExchangeFilter.Chain<T> {

    private final Consumer<T> service;
    private final List<ExchangeFilter<T>> filters = new ArrayList<>();
    private int pos = 0;

    public ExchangeFilterChain(Consumer<T> service) {
        this.service = service;
    }

    @Override
    public void filter(T source) {
        // Call the next filter if there is one
        if (pos < filters.size()) {
            ExchangeFilter<T> filter = filters.get(pos++);
            filter.filter(source, this);
            return;
        }

        // We fell off the end of the chain -- call the service instance
        service.accept(source);
    }

    public ExchangeFilterChain<T> addFilter(ExchangeFilter<T> filter) {
        if (filters.contains(filter)) return this;
        synchronized (this.filters) {
            filters.add(filter);
        }
        return this;
    }

    public ExchangeFilterChain<T> addFilter(List<ExchangeFilter<T>> filters) {
        for (ExchangeFilter<T> filter : filters) {
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

    public ExchangeFilterChain<T> newChain() {
        ExchangeFilterChain<T> chain = new ExchangeFilterChain<>(service);
        synchronized (this.filters) {
            chain.filters.addAll(this.filters);
        }
        return chain;
    }
}
