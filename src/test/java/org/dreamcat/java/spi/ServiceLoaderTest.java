package org.dreamcat.java.spi;

import java.util.ServiceLoader;
import org.junit.Test;

/**
 * Create by tuke on 2020/4/28
 */
public class ServiceLoaderTest {

    @Test
    public void test() {
        ServiceLoader<Bootstrapper> loader = ServiceLoader.load(Bootstrapper.class);

        int n = 0;
        for (Bootstrapper bootstrapper : loader) {
            n++;
            bootstrapper.bootstrap();
        }
        System.out.printf("total %d implements for %s\n", n, loader);
    }

    interface Bootstrapper {

        void bootstrap();
    }
}
