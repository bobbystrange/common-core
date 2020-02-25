package org.dreamcat.common.core.chain;

import java.util.List;

public class RealChain<Req, Res>
        implements Interceptor.Chain<Req, Res> {

    private final Req original;

    private final List<Interceptor<Req, Res>> interceptors;

    private final Interceptor.Call<Req, Res> call;

    private final int index;

    private int calls;

    public RealChain(
            List<Interceptor<Req, Res>> interceptors,
            int index,
            Req original,
            Interceptor.Call<Req, Res> call) {
        this.interceptors = interceptors;
        this.index = index;
        this.original = original;
        this.call = call;
    }


    @Override
    public Req original() {
        return original;
    }

    @Override
    public Interceptor.Call<Req, Res> call() {
        return call;
    }

    public Res proceed(Req req) throws Exception {
        if (index >= interceptors.size()) throw new AssertionError();

        calls++;

        if (calls > 1) {
            throw new IllegalStateException("interceptor " + interceptors.get(index - 1)
                    + " must call proceed() exactly once");
        }

        // Call the next interceptor in the chain.
        RealChain<Req, Res> next = new RealChain<>(interceptors, index + 1, req, call);
        Interceptor<Req, Res> interceptor = interceptors.get(index);
        Res res = interceptor.intercept(next);

        // Confirm that the next interceptor made its required call to chain.proceed().
        if (index + 1 < interceptors.size() && next.calls != 1) {
            throw new IllegalStateException("interceptor " + interceptor
                    + " must call proceed() exactly once");
        }

        // Confirm that the intercepted response isn't null.
        if (res == null) {
            throw new NullPointerException("interceptor " + interceptor + " returned null");
        }

        return res;
    }

}
