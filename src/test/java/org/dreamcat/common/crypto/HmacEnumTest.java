package org.dreamcat.common.crypto;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.ByteUtil;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * Create by tuke on 2019-02-17
 */
@Slf4j
public class HmacEnumTest {

    private String msg = "0123456789abcdef";
    byte[] data = msg.getBytes(StandardCharsets.ISO_8859_1);

    @Test
    public void sign() throws Exception {

        for (HmacEnum he : HmacEnum.values()) {
            log.info("\thmac:\t{}", he.name());

            byte[] key = he.generateKey();
            log.info("key:\t{}  {}", key.length, ByteUtil.hex(key));

            byte[] sign = he.digest(data, key);
            log.info("sign:\t{}  {}", sign.length, ByteUtil.hex(sign));

            String hex = he.digestAsHex(data, key);
            log.info("hex:\t{}  {}", hex.length(), hex);

            String base64 = he.digestAsBase64(data, key);
            log.info("base64:\t{}  {}", base64.length(), base64);

            System.out.println("\n");
        }
    }

}
