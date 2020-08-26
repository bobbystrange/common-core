package org.dreamcat.common.crypto;

import org.dreamcat.common.util.RandomUtil;
import org.junit.Test;


/**
 * Create by tuke on 2020/5/13
 */
public class RC4Test {

    @Test
    public void test() throws Exception {
        String key = "FxAmNDB8";
        String text = RandomUtil.choose72(36);
        System.out.println("source " + text);
        System.out.println("before " + MD5Util.md5Hex(text));
        String result = RC2Util.encryptAsBase64(text, key);
        System.out.println("after " + MD5Util.md5Hex(result));
        String decrypted = RC2Util.decryptFromBase64(result, key);
        System.out.println("decrypted " + MD5Util.md5Hex(decrypted));

    }
}
