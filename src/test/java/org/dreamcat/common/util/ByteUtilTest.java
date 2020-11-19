package org.dreamcat.common.util;

import java.util.Arrays;
import org.junit.Test;

/**
 * Create by tuke on 2020/5/4
 */
public class ByteUtilTest {

    @Test
    public void test() {
        byte[] data = ByteUtil.unhex("a5e80b35cc8aadee");
        System.out.println(Arrays.toString(data));
        System.out.println(ByteUtil.hex(data));
    }
}
