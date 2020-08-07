package org.dreamcat.common.collection;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Create by tuke on 2020/5/23
 */
public class BlockingTimeoutWorkQueue<E> extends ArrayBlockingQueue<E> {
    private final long timeout;
    private final TimeUnit unit;

    public BlockingTimeoutWorkQueue(int capacity, long timeout, TimeUnit unit) {
        super(capacity);
        this.timeout = timeout;
        this.unit = unit;
    }

    public BlockingTimeoutWorkQueue(int capacity, boolean fair, long timeout, TimeUnit unit) {
        super(capacity, fair);
        this.timeout = timeout;
        this.unit = unit;
    }

    public BlockingTimeoutWorkQueue(int capacity, boolean fair, Collection<? extends E> c, long timeout, TimeUnit unit) {
        super(capacity, fair, c);
        this.timeout = timeout;
        this.unit = unit;
    }

    @Override
    public boolean offer(E e) {
        try {
            super.offer(e, timeout, unit);
            return true;
        } catch (InterruptedException ex) {
            return false;
        }
    }
}
