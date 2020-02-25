package org.dreamcat.common.core.chain;

public interface Interceptor<Req, Res> {

    Res intercept(Chain<Req, Res> chain) throws Exception;

    interface Chain<Req, Res> {

        Req original();

        Res proceed(Req req) throws Exception;

        Call<Req, Res> call();
    }

    interface Call<Req, Res> extends Cloneable {

        Req original();

        Res execute() throws Exception;

        void enqueue(Interceptor.Callback<Req, Res> respCallback);

        void cancel();

        boolean isExecuted();

        boolean isCanceled();
    }

    interface AsyncCall<Req, Res> extends Runnable {

        Req original();

        void execute();

        Call<Req, Res> get();
    }

    interface Callback<Req, Res> {

        void onComptele(RealCall<Req, Res> call, Res resp);

        void onError(RealCall<Req, Res> call, Exception e);
    }

    // only works on synchronous call
    interface Listener<Req, Res> {

        default void onBefore(Call<Req, Res> call) {
        }

        default void onReturn(Call<Req, Res> call, Res result) {
        }

        default void onThrow(Call<Req, Res> call, Exception e) {
        }
    }

    interface Dispatcher<Req, Res> {

        void enqueue(AsyncCall<Req, Res> call);

        void executed(Call<Req, Res> call);

        void finished(AsyncCall<Req, Res> call);

        void finished(Call<Req, Res> call);

    }
}
