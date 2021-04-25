package org.dreamcat.common.core.legacy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Create by tuke on 2020/3/29
 */
class ModalEventEmitter {

    public final Set<ModalEventListener<?>> listeners = new LinkedHashSet<>();

    public ModalEventEmitter addListener(ModalEventListener<?> listener) {
        synchronized (this.listeners) {
            this.listeners.add(listener);
        }
        return this;
    }

    public void addListener(Collection<ModalEventListener<?>> listeners) {
        synchronized (this.listeners) {
            this.listeners.addAll(listeners);
        }
    }

    public void removeListener(ModalEventListener<?> listener) {
        synchronized (this.listeners) {
            this.listeners.remove(listener);
        }
    }

    public void removeAllListeners() {
        synchronized (this.listeners) {
            this.listeners.clear();
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void emit(ModalEvent event) {
        Collection<ModalEventListener<?>> currentListeners = this.getListeners();
        for (ModalEventListener listener : currentListeners) {
            listener.onModalEvent(event);
        }
    }

    public Collection<ModalEventListener<?>> getListeners() {
        synchronized (this.listeners) {
            return new ArrayList<>(this.listeners);
        }
    }
}
