package org.dreamcat.common.crypto;

import org.dreamcat.common.util.DESUtil;
import org.dreamcat.common.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Create by tuke on 2019-04-03
 */
@Slf4j
public class DESTest {

    @Test
    public void test() throws Exception {

        String key = "FxAmNDB8";
        String text = key;
        log.info("before {}", MD5Util.md5Hex(text));
        String result = DESUtil.encryptToBase64(text, key);
        log.info("after {}", MD5Util.md5Hex(result));

    }
}
