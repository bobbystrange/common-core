package org.dreamcat.common.crypto;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.RandomUtil;
import org.junit.Test;

/**
 * Create by tuke on 2019-04-03
 */
@Slf4j
public class DESTest {

    @Test
    public void test() throws Exception {

        String key = "FxAmNDB8";
        String text = RandomUtil.choose72(36);
        log.info("source {}", text);
        log.info("before {}", MD5Util.md5Hex(text));
        String result = DESUtil.encryptAsBase64(text, key);
        log.info("after {}", MD5Util.md5Hex(result));
        String decrypted = DESUtil.decryptFromBase64(result, key);
        log.info("decrypted {}", MD5Util.md5Hex(decrypted));

    }
}
