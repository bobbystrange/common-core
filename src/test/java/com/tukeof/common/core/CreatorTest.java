package com.tukeof.common.core;

import com.tukeof.common.core.creator.Creator;
import com.tukeof.common.core.creator.CreatorCall;
import com.tukeof.common.core.creator.RealCreator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;
import java.util.function.BiFunction;

/**
 * @author tuke
 * @date 2018/9/9
 */
@Slf4j
public class CreatorTest {

    static BiFunction<Method, Object[], Object> methodAdaptor = (method, args) -> {
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Value) {
                Value value = (Value) annotation;

                log.info("Annotation Value:\t{}\n", value.value());
                return value.value();
            }
        }

        Parameter[] parameters = method.getParameters();
        for (int i = 0, size = parameters.length; i < size; i++) {
            Parameter parameter = parameters[i];
            Object arg = args[i];
            Annotation[] annos = parameter.getAnnotations();
            for (Annotation a : annos) {
                if (a instanceof Value) {
                    Value value = (Value) a;
                    log.info("Parameter Annotation Value:\t{}\n", value.value());

                    return value.value() + " called:\n" + arg;
                }
            }
        }

        return new Date().toString();
    };

    @Test
    public void test() throws Exception {
        RealCreator creator = new RealCreator(methodAdaptor);
        creator.setEagerly(false);

        Service service = creator.create(Service.class);

        service.say().to(String::toUpperCase).enqueue(new Creator.Callback<String>() {
            @Override
            public void onComptele(Creator.Call<String> call, String result) {
                log.info("What the service say is:\t{}\n", result);
            }

            @Override
            public void onError(Creator.Call<String> call, Throwable t) {
                log.error(t.getMessage(), t);
            }
        });

        String listen = service.listen("Catch me")
//                .to(it ->
//                        new Date().getTime())
                .execute();
        log.info("What the service listen is:\t{}\n", listen);

    }

    interface Service {

        @Value("Bobby John")
        CreatorCall<String> say();

        CreatorCall<String> listen(@Value("Bobby John") String value);

    }


    /**
     * @author tuke
     * @date 2018/9/9
     */
    @Retention(RetentionPolicy.RUNTIME)
    @interface Value {
        String value();
    }
}

