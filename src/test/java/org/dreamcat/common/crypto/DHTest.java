package org.dreamcat.common.crypto;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.ByteUtil;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Map;

/**
 * Create by tuke on 2019-01-27
 */
@Slf4j
public class DHTest {

    private String msg = "0123456789abcdef";
    byte[] data = msg.getBytes(StandardCharsets.ISO_8859_1);

    @Test
    public void cipher() throws Exception {
        byte[] output, input;
        for (DHCryptoAlgorithm e : DHCryptoAlgorithm.values()) {
            log.info("\tdh:\t{}", e.name());

            Map<String, Key> keyMap = e.generateKeyPair();
            String publicKey = e.getBase64PublicKey(keyMap);
            String privateKey = e.getBase64PrivateKey(keyMap);

            log.info("\tpublicKey:\t{}  {}", publicKey.length(), publicKey);
            log.info("\tprivateKey:\t{}  {}", privateKey.length(), privateKey);

            log.info("\toriginal input:\t{}  {}", data.length, ByteUtil.hex(data));

            try {
                output = e.encryptByBase64Key(data, publicKey, privateKey);
                log.info("\toutput:\t{}  {}", output.length, ByteUtil.hex(output));

                input = e.decryptByBase64Key(output, publicKey, privateKey);
                log.info("\tdecrypted input:\t{}  {}", input.length, ByteUtil.hex(input));
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }

            System.out.println("\n");
        }

    }

}
