package org.dreamcat.common.core.filter;

import org.dreamcat.common.function.VoidConsumer;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by tuke on 2020/3/29
 */
public class VoidFilterChain implements VoidFilter.Chain {
    private final VoidConsumer service;
    private final List<VoidFilter> filters = new ArrayList<>();
    private int pos = 0;

    public VoidFilterChain(VoidConsumer service) {
        this.service = service;
    }

    @Override
    public void filter() {
        // Call the next filter if there is one
        if (pos < filters.size()) {
            VoidFilter filter = filters.get(pos++);
            filter.filter(this);
            return;
        }

        // We fell off the end of the chain -- call the service instance
        service.accept();
    }

    public VoidFilterChain addFilter(VoidFilter filter) {
        if (filters.contains(filter)) return this;
        synchronized (this.filters) {
            filters.add(filter);
        }
        return this;
    }

    public VoidFilterChain addFilter(List<VoidFilter> filters) {
        for (VoidFilter filter : filters) {
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

    public VoidFilterChain newChain() {
        VoidFilterChain chain = new VoidFilterChain(service);
        synchronized (this.filters) {
            chain.filters.addAll(this.filters);
        }
        return chain;
    }
}
