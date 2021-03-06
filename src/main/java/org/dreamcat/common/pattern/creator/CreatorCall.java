package org.dreamcat.common.pattern.creator;

import org.dreamcat.common.function.ExpFunction;
import org.dreamcat.common.pattern.chain.Interceptor;
import org.dreamcat.common.pattern.chain.RealCall;
import org.dreamcat.common.pattern.chain.RealInterceptTarget;

/**
 * Create by tuke on 2018-09-09
 */
public class CreatorCall<T> implements ModalCreator.Call<T> {

    private final Object original;
    private final ExpFunction<Object, T, ?> converter;

    private RealCall<Object, T> realCall;
    private boolean executed;
    private volatile boolean canceled;

    private CreatorCall(Object original, ExpFunction<Object, T, ?> converter) {
        this.original = original;
        this.converter = converter;
    }

    public static <T> CreatorCall<T> newCall(
            Object original, ExpFunction<Object, T, ?> converter) {
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
    public void enqueue(ModalCreator.Callback<T> callback) {
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
                callback.onComplete(CreatorCall.this, t);

            }

            @Override
            public void onError(RealCall<Object, T> call, Exception e) {
                callback.onError(CreatorCall.this, e);
            }
        });
    }

    @Override
    public <R> ModalCreator.Call<R> to(ExpFunction<T, R, ?> converter) {
        ExpFunction<Object, R, ?> newConverter = it -> converter.apply(
                CreatorCall.this.converter.apply(it));
        return newCall(original, newConverter);
    }

    private RealCall<Object, T> newRawCall(
            Object original, ExpFunction<Object, T, ?> converter) {
        RealInterceptTarget<Object, T> interceptable =
                new RealInterceptTarget.Builder<>(converter)
                        .build();
        return RealCall.newCall(interceptable, original);
    }
}
