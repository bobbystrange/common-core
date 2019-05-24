package org.dreamcat.common.core.chain;

import java.util.List;

public interface InterceptTarget<Req, Resp> {

    List<Interceptor<Req, Resp>> interceptors();

    Interceptor.Dispatcher<Req, Resp> dispatcher();

    default Interceptor.Listener<Req, Resp> listener() {
        return new Interceptor.Listener<Req, Resp>() {
        };
    }

    default String originalName(Req req) {
        return req.toString();
    }

    default Interceptor.Call<Req, Resp> newCall(Req req) {
        return RealCall.newCall(this, req);
    }

}
