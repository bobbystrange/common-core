package org.dreamcat.common.core.chain;

import java.util.List;

public interface InterceptTarget<Req, Res> {

    List<Interceptor<Req, Res>> interceptors();

    Interceptor.Dispatcher<Req, Res> dispatcher();

    default Interceptor.Listener<Req, Res> listener() {
        return new Interceptor.Listener<Req, Res>() {
        };
    }

    default String originalName(Req req) {
        return req.toString();
    }

    default Interceptor.Call<Req, Res> newCall(Req req) {
        return RealCall.newCall(this, req);
    }

}
