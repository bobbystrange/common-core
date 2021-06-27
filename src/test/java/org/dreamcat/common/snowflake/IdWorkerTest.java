package org.dreamcat.common.snowflake;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Create by tuke on 2020/5/31
 */
@Ignore
public class IdWorkerTest {

    @Test
    public void test() {
        IdWorker idWorker = new IdWorker(0, 0, 0);
        long ts = System.currentTimeMillis();
        for (int i = 0; i < (1 << 14); i++) {
            System.out.println(idWorker.nextId());
        }
        ts = System.currentTimeMillis() - ts;
        System.out.println("cost " + ts + " ms");
    }
}
