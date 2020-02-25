package org.dreamcat.common.util;

import org.junit.Test;

public class RandomUtilTest {

    @Test
    public void uuid() {
        String uuid = RandomUtil.uuid();
        System.out.printf("%s\t%d\n", uuid, uuid.length());
        System.out.println(RandomUtil.choose72(32));
    }
}
