package org.dreamcat.common.core.event;

import java.util.EventListener;

/**
 * Create by tuke on 2020/3/29
 */
@FunctionalInterface
public interface ModalEventListener<E extends ModalEvent> extends EventListener {

    void onModalEvent(E event);
}
