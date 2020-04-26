package org.dreamcat.common.core.chain;

import org.dreamcat.common.function.ThrowableFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Create by tuke on 2018-09-08
 */
public class RealInterceptTarget<I, O> implements InterceptTarget<I, O>, Interceptor<I, O> {

    protected final ThrowableFunction<I, O> function;

    protected Interceptor.Dispatcher<I, O> dispatcher;

    protected List<Interceptor<I, O>> interceptors;

    protected Interceptor.Listener<I, O> listener;

    protected Function<I, String> originalName;

    // don't call it directly, use Builder instead
    protected RealInterceptTarget(ThrowableFunction<I, O> function) {
        this.function = function;
        this.dispatcher = new RealDispatcher<>();
        this.interceptors = new ArrayList<>();
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static <I, O> Builder<I, O> builder(
            ThrowableFunction<I, O> function) {
        return new Builder<>(function);
    }

    @Override
    public Interceptor.Dispatcher<I, O> dispatcher() {
        return dispatcher;
    }

    @Override
    public List<Interceptor<I, O>> interceptors() {
        return interceptors;
    }

    @Override
    public Interceptor.Listener<I, O> listener() {
        return listener;
    }

    @Override
    public String originalName(I i) {
        return originalName.apply(i);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    @Override
    public O intercept(Chain<I, O> chain) throws Exception {
        return function.apply(chain.original());
    }

    public static class Builder<I, O> {

        private final RealInterceptTarget<I, O> target;

        public Builder(ThrowableFunction<I, O> function) {
            this.target = new RealInterceptTarget<>(function);
        }

        public RealInterceptTarget<I, O> build() {
            if (this.target.listener == null) {
                this.target.listener = new Interceptor.Listener<I, O>() {
                };
            }
            if (this.target.originalName == null) {
                this.target.originalName = Object::toString;
            }
            return target;
        }

        public RealInterceptTarget.Builder<I, O> addInterceptor(Interceptor<I, O> interceptor) {
            this.target.interceptors.add(interceptor);
            return this;
        }

        public RealInterceptTarget.Builder<I, O> listener(Interceptor.Listener<I, O> listener) {
            this.target.listener = listener;
            return this;
        }

        public RealInterceptTarget.Builder<I, O> originalName(Function<I, String> originalName) {
            this.target.originalName = originalName;
            return this;
        }
    }
}
