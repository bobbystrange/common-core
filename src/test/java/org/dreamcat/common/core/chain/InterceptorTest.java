package org.dreamcat.common.core.chain;

import org.dreamcat.common.bean.BeanUtil;
import org.junit.Test;

public class InterceptorTest {

    @Test
    public void sync() throws Exception {
        InterceptTarget<String, String> client = newClient();

        String res = client.newCall("How are you?").execute();
        System.out.printf("execute res:\t%s\t\n\n", res);

    }

    @Test
    public void async() {
        InterceptTarget<String, String> client = newClient();

        client.newCall("How are you?").enqueue(
                new Interceptor.Callback<String, String>() {
                    @Override
                    public void onError(RealCall<String, String> call, Exception e) {
                        System.out.printf("execute resp:\t%s\t\n\n", BeanUtil.toPrettyString(call));
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
                    return chain.proceed(req);
                })
                .build();
    }

}
