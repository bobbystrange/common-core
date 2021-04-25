package org.dreamcat.common.core.pattern.chain;

import static org.dreamcat.common.util.BeanUtil.pretty;

import org.junit.Test;

public class InterceptorTest {

    @Test
    public void sync() throws Exception {
        String res = newClient().newCall("How are you?").execute();
        System.out.printf("execute res:\t%s\t\n\n", res);
    }

    @Test
    public void async() {
        newClient().newCall("How are you?").enqueue(
                new Interceptor.Callback<String, String>() {
                    @Override
                    public void onError(RealCall<String, String> call, Exception e) {
                        System.out.printf("execute resp:\t%s\t\n\n", pretty(call));
                        e.printStackTrace();
                    }

                    @Override
                    public void onComptele(RealCall<String, String> call, String resp) {
                        System.out.printf("enqueue resp:\t%s\t\n\n", resp);
                    }
                });
    }

    private InterceptTarget<String, String> newClient() {
        return new RealInterceptTarget.Builder<String, String>(String::toUpperCase)
                .addInterceptor(chain -> {
                    String req = chain.original();
                    System.out.printf("intercept one:\t%s\t\n\n", req);
                    return chain.proceed(req);
                })
                .addInterceptor(chain -> {
                    String req = chain.original();
                    req = new StringBuffer(req).reverse().toString();
                    System.out.printf("intercept two:\t%s\t\n\n", req);
                    // only stop the entrypoint function to perform
                    chain.call().cancel();
                    return chain.proceed(req);
                })
                .addInterceptor(chain -> {
                    String req = chain.original();
                    req = req + req;
                    System.out.printf("intercept three:\t%s\t\n\n", req);
                    return chain.proceed(req);
                })
                .build();
    }

}
