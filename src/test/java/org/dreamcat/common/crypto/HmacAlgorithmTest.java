package org.dreamcat.common.crypto;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.ByteUtil;
import org.dreamcat.common.util.RandomUtil;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * Create by tuke on 2019-02-17
 */
@Slf4j
public class HmacAlgorithmTest {

    byte[] data = "0123456789abcdef".getBytes(StandardCharsets.ISO_8859_1);

    @Test
    public void sign() throws Exception {
        for (HmacAlgorithm i : HmacAlgorithm.values()) {
            log.info("\thmac:\t{}", i.name());

            byte[] key = i.generateKey();
            log.info("key:\t{}  {}", key.length, ByteUtil.hex(key));

            byte[] sign = i.digest(data, key);
            log.info("sign:\t{}  {}", sign.length, ByteUtil.hex(sign));

            String hex = i.digestAsHex(data, key);
            log.info("hex:\t{}  {}", hex.length(), hex);

            String base64 = i.digestAsBase64(data, key);
            log.info("base64:\t{}  {}", base64.length(), base64);

            System.out.println("===========================================");
        }
    }

    @Test
    public void sign2() throws Exception {
        for (HmacAlgorithm i : HmacAlgorithm.values()) {
            String base64 = i.digestAsBase64(data, RandomUtil.uuid());
            log.info("{}, base64:\t{}  {}", i.name(), base64.length(), base64);
        }
    }

}
