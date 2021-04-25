package org.dreamcat.common.core.pattern.chain;

import java.util.List;

public interface InterceptTarget<I, O> {

    List<Interceptor<I, O>> interceptors();

    Interceptor.Dispatcher<I, O> dispatcher();

    Interceptor.Listener<I, O> listener();

    /**
     * as thread name when executing a
     *
     * @param i input parameter
     * @return name
     * @see RealCall.AsyncCall
     */
    default String originalName(I i) {
        return i.toString();
    }

    default Interceptor.Call<I, O> newCall(I i) {
        return RealCall.newCall(this, i);
    }

}
