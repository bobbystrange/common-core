package org.dreamcat.common.crypto;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.ByteUtil;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * Create by tuke on 2019-02-17
 */
@Slf4j
public class CipherAlgorithmTest {

    private String msg = "0123456789abcdef";
    byte[] data = msg.getBytes(StandardCharsets.ISO_8859_1);

    @Test
    public void cipher() throws Exception {
        byte[] output, input;
        for (CipherAlgorithm cipher : CipherAlgorithm.values()) {
            byte[] key = cipher.generateKey();
            log.info("\tcipher:\t{}", cipher.name());

            log.info("\tkey:\t{}  {}", key.length, ByteUtil.hex(key));

            log.info("\toriginal input:\t{}  {}", data.length, ByteUtil.hex(data));

            try {
                output = cipher.encryptEcb(data, key);
                log.info("\tecb output:\t{}  {}", output.length, ByteUtil.hex(output));

                input = cipher.decryptEcb(output, key);
                log.info("\tecb decrypted input:\t{}  {}", input.length, ByteUtil.hex(input));

                output = cipher.encryptCbc(data, key);
                log.info("\tcbc output:\t{}  {}", output.length, ByteUtil.hex(output));

                input = cipher.decryptCbc(output, key);
                log.info("\tcbc decrypted input:\t{}  {}", input.length, ByteUtil.hex(input));

                output = cipher.encryptCbcNoPadding(data, key);
                log.info("\tcbc nopadding output:\t{}  {}", output.length, ByteUtil.hex(output));

                input = cipher.decryptCbcNoPadding(output, key);
                log.info("\tcbc nopadding decrypted input:\t{}  {}", input.length, ByteUtil.hex(input));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            System.out.println("\n");
        }

    }
}
