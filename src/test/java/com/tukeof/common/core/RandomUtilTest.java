package com.tukeof.common.core;

import org.junit.Test;

import static com.tukeof.common.util.RandomUtil.generateUuid32;
import static com.tukeof.common.util.RandomUtil.nonce72;

public class RandomUtilTest {

    @Test
    public void uuid() {
        String uuid = generateUuid32();
        System.out.printf("%s\t%d\n", uuid, uuid.length());
        System.out.println(nonce72(32));

    }
}
