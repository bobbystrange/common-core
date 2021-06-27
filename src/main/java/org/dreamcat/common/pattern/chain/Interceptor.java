package org.dreamcat.common.pattern.chain;

public interface Interceptor<I, O> {

    O intercept(Chain<I, O> chain) throws Exception;

    interface Chain<I, O> {

        I original();

        O proceed(I i) throws Exception;

        Call<I, O> call();
    }

    interface Call<I, O> extends Cloneable {

        I original();

        O execute() throws Exception;

        void enqueue(Interceptor.Callback<I, O> callback);

        void cancel();

        boolean isExecuted();

        boolean isCanceled();
    }

    interface AsyncCall<I, O> extends Runnable {

        I original();

        void execute();

        Call<I, O> get();
    }

    interface Callback<I, O> {

        void onComptele(RealCall<I, O> call, O o);

        void onError(RealCall<I, O> call, Exception e);
    }

    // only works on synchronous call
    interface Listener<I, O> {

        default void onBefore(Call<I, O> call) {
        }

        default void onReturn(Call<I, O> call, O result) {
        }

        default void onThrow(Call<I, O> call, Exception e) {
        }
    }

    interface Dispatcher<I, O> {

        void enqueue(AsyncCall<I, O> call);

        void executed(Call<I, O> call);

        void finished(AsyncCall<I, O> call);

        void finished(Call<I, O> call);

    }
}
