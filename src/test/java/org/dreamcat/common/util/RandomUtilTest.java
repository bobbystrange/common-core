package org.dreamcat.common.util;

import org.junit.Test;

import static org.dreamcat.common.util.PrintUtil.println;
import static org.dreamcat.common.util.RandomUtil.rand;
import static org.dreamcat.common.util.RandomUtil.uuid;

public class RandomUtilTest {

    @Test
    public void testUuid() {
        String uuid = uuid();
        System.out.printf("%s\t%d\n", uuid, uuid.length());
        System.out.println(RandomUtil.choose72(32));
    }

    @Test
    public void testRand() {
        for (int i = 0; i < 32; i++) {
            println(i + "\t" + rand(0, 3));
        }
        for (int i = 0; i < 32; i++) {
            println(i + "\t" + rand(-3, 4));
        }
    }
}
