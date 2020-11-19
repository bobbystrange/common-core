package org.dreamcat.common.core.event;

import java.util.EventObject;
import lombok.Getter;

/**
 * Create by tuke on 2020/3/29
 */
@Getter
public class ModalEvent extends EventObject {

    private final long timestamp;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ModalEvent(Object source) {
        super(source);
        this.timestamp = System.currentTimeMillis();
    }


}
