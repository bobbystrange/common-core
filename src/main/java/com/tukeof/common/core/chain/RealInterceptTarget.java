package com.tukeof.common.core.chain;

import com.tukeof.common.annotation.NotNull;
import com.tukeof.common.function.ThrowableFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Create by tuke on 2018-09-08
 */
public class RealInterceptTarget<Req, Resp> implements InterceptTarget<Req, Resp>, Interceptor<Req, Resp> {

    private final ThrowableFunction<Req, Resp> entrypoint;

    private final List<Interceptor<Req, Resp>> interceptors;

    private final Interceptor.Dispatcher<Req, Resp> dispatcher;

    private final Interceptor.Listener<Req, Resp> listener;

    private final Function<Req, String> originalName;

    private RealInterceptTarget(RealInterceptTarget.Builder<Req, Resp> builder) {
        entrypoint = builder.entrypoint;
        interceptors = builder.interceptors;
        originalName = builder.originalName;

        dispatcher = builder.dispatcher != null ?
                builder.dispatcher : new RealDispatcher<>();

        listener = builder.listener != null ?
                builder.listener : InterceptTarget.super.listener();

    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static <Req, Resp> Builder<Req, Resp> builder(
            @NotNull ThrowableFunction<Req, Resp> entrypoint) {
        return new Builder<>(entrypoint);
    }

    @Override
    public Interceptor.Dispatcher<Req, Resp> dispatcher() {
        return dispatcher;
    }

    @Override
    public List<Interceptor<Req, Resp>> interceptors() {
        return interceptors;
    }

    @Override
    public Interceptor.Listener<Req, Resp> listener() {
        return listener;
    }

    @Override
    public String originalName(Req req) {
        return originalName.apply(req);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    @Override
    public Resp intercept(Chain<Req, Resp> chain) throws Exception {
        return entrypoint.apply(chain.original());
    }

    public static class Builder<Req, Resp> {

        private final ThrowableFunction<Req, Resp> entrypoint;

        private final List<Interceptor<Req, Resp>> interceptors;

        private Interceptor.Dispatcher<Req, Resp> dispatcher;

        private Interceptor.Listener<Req, Resp> listener;

        private Function<Req, String> originalName;

        public Builder(ThrowableFunction<Req, Resp> entrypoint) {
            this.entrypoint = entrypoint;
            this.interceptors = new ArrayList<>();
        }

        public RealInterceptTarget<Req, Resp> build() {
            if (originalName == null)
                originalName = Object::toString;

            return new RealInterceptTarget<>(this);
        }

        public RealInterceptTarget.Builder<Req, Resp> addInterceptor(Interceptor<Req, Resp> interceptor) {
            interceptors.add(interceptor);
            return this;
        }

        public RealInterceptTarget.Builder<Req, Resp> dispatcher(Interceptor.Dispatcher<Req, Resp> dispatcher) {
            this.dispatcher = dispatcher;
            return this;
        }

        public RealInterceptTarget.Builder<Req, Resp> listener(Interceptor.Listener<Req, Resp> listener) {
            this.listener = listener;
            return this;
        }

        public RealInterceptTarget.Builder<Req, Resp> originalName(Function<Req, String> originalName) {
            this.originalName = originalName;
            return this;
        }

    }

}
