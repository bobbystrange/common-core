package org.dreamcat.common.crypto;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
public class MessageDigestEnumTest {

    @Test
    public void base() throws Exception {

        MessageDigestEnum md5 = MessageDigestEnum.MD5;
        MessageDigestEnum sha256 = MessageDigestEnum.SHA_256;


        String msg = "0123456789abcdef";
        log.info("utf-8:\t{}", Arrays.toString(msg.getBytes()));
        log.info("ios-8859-1:\t{}", Arrays.toString(msg.getBytes(StandardCharsets.ISO_8859_1)));

        log.info("\t{}:\t{}", "md5", md5.digest(msg.getBytes()));
        log.info("\t{}:\t{}", "md5", md5.digestToBase64(msg.getBytes()));
        log.info("\t{}:\t{}", "md5", md5.digestToHex(msg.getBytes()));

        log.info("\t{}:\t{}", "sha265", sha256.digest(msg.getBytes()));
        log.info("\t{}:\t{}", "sha265", sha256.digestToBase64(msg.getBytes()));
        log.info("\t{}:\t{}", "sha265", sha256.digestToHex(msg.getBytes()));

    }
}
