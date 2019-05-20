package com.tukeof.common.core;

import com.tukeof.common.core.chain.InterceptTarget;
import com.tukeof.common.core.chain.Interceptor;
import com.tukeof.common.core.chain.RealCall;
import com.tukeof.common.core.chain.RealInterceptTarget;
import org.junit.Test;

import static com.tukeof.common.util.bean.BeanStringUtil.toPrettyString;

public class InterceptorTest {


    @Test
    public void sync() throws Exception {
        InterceptTarget<String, String> client = newClient();

        String resp = client.newCall("How are you?").execute();
        System.out.printf("execute resp:\t%s\t\n\n", resp);

    }

    @Test
    public void async() {
        InterceptTarget<String, String> client = newClient();

        client.newCall("How are you?").enqueue(
                new Interceptor.Callback<String, String>() {
                    @Override
                    public void onError(RealCall call, Exception e) {
                        System.out.printf("execute resp:\t%s\t\n\n", toPrettyString(call));
                        e.printStackTrace();
                    }

                    @Override
                    public void onComptele(RealCall call, String resp) {
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
