package org.dreamcat.common.core.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Create by tuke on 2020/3/29
 */
public class RealEventEmitter implements EventEmitter {
    public final Set<ModalEventListener<?>> listeners = new LinkedHashSet<>();

    @Override
    public EventEmitter addListener(ModalEventListener<?> listener) {
        synchronized (this.listeners) {
            this.listeners.add(listener);
        }
        return this;
    }

    @Override
    public void addListener(Collection<ModalEventListener<?>> listeners) {
        synchronized (this.listeners) {
            this.listeners.addAll(listeners);
        }
    }

    @Override
    public void removeListener(ModalEventListener<?> listener) {
        synchronized (this.listeners) {
            this.listeners.remove(listener);
        }
    }

    @Override
    public void removeAllListeners() {
        synchronized (this.listeners) {
            this.listeners.clear();
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void emit(ModalEvent event) {
        Collection<ModalEventListener<?>> listeners = this.getListentes();
        for (ModalEventListener listener: listeners) {
            listener.onModalEvent(event);
        }
    }

    public Collection<ModalEventListener<?>> getListentes() {
        synchronized (this.listeners) {
            return new ArrayList<>(this.listeners);
        }
    }
}
