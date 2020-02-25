package org.dreamcat.common.core.chain;

import org.dreamcat.common.annotation.NotNull;
import org.dreamcat.common.function.ThrowableFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Create by tuke on 2018-09-08
 */
public class RealInterceptTarget<Req, Res> implements InterceptTarget<Req, Res>, Interceptor<Req, Res> {

    private final ThrowableFunction<Req, Res> function;

    private final List<Interceptor<Req, Res>> interceptors;

    private final Interceptor.Dispatcher<Req, Res> dispatcher;

    private final Interceptor.Listener<Req, Res> listener;

    private final Function<Req, String> originalName;

    private RealInterceptTarget(RealInterceptTarget.Builder<Req, Res> builder) {
        function = builder.function;
        interceptors = builder.interceptors;
        originalName = builder.originalName;

        dispatcher = builder.dispatcher != null ?
                builder.dispatcher : new RealDispatcher<>();

        listener = builder.listener != null ?
                builder.listener : InterceptTarget.super.listener();

    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static <Req, Resp> Builder<Req, Resp> builder(
            @NotNull ThrowableFunction<Req, Resp> function) {
        return new Builder<>(function);
    }

    @Override
    public Interceptor.Dispatcher<Req, Res> dispatcher() {
        return dispatcher;
    }

    @Override
    public List<Interceptor<Req, Res>> interceptors() {
        return interceptors;
    }

    @Override
    public Interceptor.Listener<Req, Res> listener() {
        return listener;
    }

    @Override
    public String originalName(Req req) {
        return originalName.apply(req);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    @Override
    public Res intercept(Chain<Req, Res> chain) throws Exception {
        return function.apply(chain.original());
    }

    public static class Builder<Req, Resp> {

        private final ThrowableFunction<Req, Resp> function;

        private final List<Interceptor<Req, Resp>> interceptors;

        private Interceptor.Dispatcher<Req, Resp> dispatcher;

        private Interceptor.Listener<Req, Resp> listener;

        private Function<Req, String> originalName;

        public Builder(ThrowableFunction<Req, Resp> function) {
            this.function = function;
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
