package org.dreamcat.common.crypto;

import org.dreamcat.common.util.RandomUtil;
import org.junit.Test;

import static org.dreamcat.common.util.FormatUtil.println;

/**
 * Create by tuke on 2020/5/13
 */
public class RC4Test {

    @Test
    public void test() throws Exception {
        String key = "FxAmNDB8";
        String text = RandomUtil.choose72(36);
        println("source ", text);
        println("before ", MD5Util.md5Hex(text));
        String result = RC2Util.encryptAsBase64(text, key);
        println("after ", MD5Util.md5Hex(result));
        String decrypted = RC2Util.decryptFromBase64(result, key);
        println("decrypted ", MD5Util.md5Hex(decrypted));

    }
}
