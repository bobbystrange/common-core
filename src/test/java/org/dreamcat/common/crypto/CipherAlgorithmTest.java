package org.dreamcat.common.crypto;

import static org.dreamcat.common.util.FormatUtil.log;

import java.nio.charset.StandardCharsets;
import org.dreamcat.common.util.ByteUtil;
import org.junit.Test;


/**
 * Create by tuke on 2019-02-17
 */
public class CipherAlgorithmTest {

    private final String msg = "0123456789abcdef";
    byte[] data = msg.getBytes(StandardCharsets.ISO_8859_1);

    @Test
    public void cipher() throws Exception {
        byte[] output, input;
        for (CipherAlgorithm cipher : CipherAlgorithm.values()) {
            byte[] key = cipher.generateKey();
            log("cipher:\t{}", cipher.name());

            log("key:\t{}  {}", key.length, ByteUtil.hex(key));

            log("original input:\t{}  {}", data.length, ByteUtil.hex(data));

            try {
                output = cipher.encryptEcb(data, key);
                log("ecb output:\t{}  {}", output.length, ByteUtil.hex(output));

                input = cipher.decryptEcb(output, key);
                log("ecb decrypted input:\t{}  {}", input.length, ByteUtil.hex(input));

                output = cipher.encryptCbc(data, key);
                log("cbc output:\t{}  {}", output.length, ByteUtil.hex(output));

                input = cipher.decryptCbc(output, key);
                log("cbc decrypted input:\t{}  {}", input.length, ByteUtil.hex(input));

                output = cipher.encryptCbcNoPadding(data, key);
                log("cbc nopadding output:\t{}  {}", output.length, ByteUtil.hex(output));

                input = cipher.decryptCbcNoPadding(output, key);
                log("cbc nopadding decrypted input:\t{}  {}", input.length, ByteUtil.hex(input));
            } catch (Exception e) {
                log(e.getMessage(), e);
            }
            System.out.println("\n");
        }
    }
}
