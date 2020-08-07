package org.dreamcat.common.collection;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Create by tuke on 2020/5/23
 */
public class BlockingWorkQueue<E> extends ArrayBlockingQueue<E> {

    public BlockingWorkQueue(int capacity) {
        super(capacity);
    }

    public BlockingWorkQueue(int capacity, boolean fair) {
        super(capacity, fair);
    }

    public BlockingWorkQueue(int capacity, boolean fair, Collection<? extends E> c) {
        super(capacity, fair, c);
    }


    @Override
    public boolean offer(E e) {
        try {
            super.put(e);
            return true;
        } catch (InterruptedException ex) {
            return false;
        }
    }
}
