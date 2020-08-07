package org.dreamcat.common.util;

import org.junit.Test;

import java.util.Arrays;

import static org.dreamcat.common.util.FormatUtil.println;

/**
 * Create by tuke on 2020/5/4
 */
public class ByteUtilTest {

    @Test
    public void test() {
        byte[] data = ByteUtil.unhex("a5e80b35cc8aadee");
        println(Arrays.toString(data));
        println(ByteUtil.hex(data));
    }
}
