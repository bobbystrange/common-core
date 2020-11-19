package org.dreamcat.common.crypto;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class DigestAlgorithmTest {

    @Test
    public void base() throws Exception {

        DigestAlgorithm md5 = DigestAlgorithm.MD5;
        DigestAlgorithm sha256 = DigestAlgorithm.SHA_256;

        String msg = "0123456789abcdef";
        log.info("utf-8:\t{}", Arrays.toString(msg.getBytes()));
        log.info("ios-8859-1:\t{}", Arrays.toString(msg.getBytes(StandardCharsets.ISO_8859_1)));

        log.info("\t{}:\t{}", "md5", md5.digest(msg.getBytes()));
        log.info("\t{}:\t{}", "md5", md5.digestAsBase64(msg.getBytes()));
        log.info("\t{}:\t{}", "md5", md5.digestAsHex(msg.getBytes()));

        log.info("\t{}:\t{}", "sha265", sha256.digest(msg.getBytes()));
        log.info("\t{}:\t{}", "sha265", sha256.digestAsBase64(msg.getBytes()));
        log.info("\t{}:\t{}", "sha265", sha256.digestAsHex(msg.getBytes()));

    }
}
