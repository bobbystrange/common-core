package org.dreamcat.common.core.legacy;

import java.util.EventListener;

/**
 * Create by tuke on 2020/3/29
 */
@FunctionalInterface
interface ModalEventListener<E extends ModalEvent> extends EventListener {

    void onModalEvent(E event);
}
