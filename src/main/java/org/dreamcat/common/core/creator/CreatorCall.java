package org.dreamcat.common.core.creator;

import org.dreamcat.common.core.chain.Interceptor;
import org.dreamcat.common.core.chain.RealCall;
import org.dreamcat.common.core.chain.RealInterceptTarget;
import org.dreamcat.common.function.ThrowableFunction;

/**
 * Create by tuke on 2018-09-09
 */
public class CreatorCall<T> implements Creator.Call<T> {

    private final Object original;
    private final ThrowableFunction<Object, T> converter;

    private RealCall<Object, T> realCall;
    private boolean executed;
    private volatile boolean canceled;

    private CreatorCall(Object original, ThrowableFunction<Object, T> converter) {
        this.original = original;
        this.converter = converter;
    }

    public static <T> CreatorCall<T> newCall(Object original, ThrowableFunction<Object, T> converter) {
        return new CreatorCall<>(original, converter);
    }

    @Override
    public T execute() throws Exception {
        synchronized (this) {
            if (executed) throw new IllegalStateException("Already executed.");
            executed = true;
        }

        realCall = newRawCall(original, converter);
        if (canceled) {
            realCall.cancel();
        }

        return realCall.execute();
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }

    @Override
    public void cancel() {
        canceled = true;
        if (!realCall.isCanceled()) realCall.cancel();
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public Object original() {
        return original;
    }

    @Override
    public void enqueue(Creator.Callback<T> callback) {
        synchronized (this) {
            if (executed) throw new IllegalStateException("Already executed.");
            executed = true;
        }

        realCall = newRawCall(original, converter);
        if (canceled) {
            realCall.cancel();
        }

        realCall.enqueue(new Interceptor.Callback<Object, T>() {
            @Override
            public void onComptele(RealCall<Object, T> call, T t) {
                callback.onComptele(CreatorCall.this, t);

            }

            @Override
            public void onError(RealCall<Object, T> call, Exception e) {
                callback.onError(CreatorCall.this, e);
            }
        });
    }

    @Override
    public <R> Creator.Call<R> to(ThrowableFunction<T, R> converter) {
        ThrowableFunction<Object, R> newConverter = original -> converter.apply(
                CreatorCall.this.converter.apply(original));
        return newCall(original, newConverter);
    }

    private RealCall<Object, T> newRawCall(
            Object original, ThrowableFunction<Object, T> converter) {
        RealInterceptTarget<Object, T> interceptable =
                new RealInterceptTarget.Builder<>(converter)
                        .build();
        return RealCall.newCall(interceptable, original);
    }
}
