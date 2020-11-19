package org.dreamcat.common.crypto;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.ByteUtil;
import org.junit.Test;

/**
 * Create by tuke on 2019-02-17
 */
@Slf4j
public class SignatureAlgorithmTest {

    private String msg = "0123456789abcdef";
    byte[] data = msg.getBytes(StandardCharsets.ISO_8859_1);

    @Test
    public void sign() throws Exception {

        for (SignatureAlgorithm se : SignatureAlgorithm.values()) {
            log.info("\tsignature:\t{}", se.name());

            Map<String, Key> keyMap = se.generateKeyPair();
            byte[] publicKey = se.getPublicKey(keyMap);
            byte[] privateKey = se.getPrivateKey(keyMap);
            log.info("publicKey:\t{}  {}", publicKey.length, ByteUtil.hex(publicKey));
            log.info("privateKey:\t{}  {}", privateKey.length, ByteUtil.hex(privateKey));

            byte[] sign = se.sign(data, privateKey);
            log.info("sign:\t{}  {}", sign.length, ByteUtil.hex(sign));
            log.info("verify:\t{}", se.verify(data, publicKey, sign));

            System.out.println("\n");
        }
    }


}
