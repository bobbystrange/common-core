package org.dreamcat.common.core.event;

import java.util.Collection;

/**
 * Create by tuke on 2020/3/29
 */
public interface EventEmitter {

    EventEmitter addListener(ModalEventListener<?> listener);

    void addListener(Collection<ModalEventListener<?>> listeners);

    void removeListener(ModalEventListener<?> listener);

    void removeAllListeners();

    void emit(ModalEvent event);
}
