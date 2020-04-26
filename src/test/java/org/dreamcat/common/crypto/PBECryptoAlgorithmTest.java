package org.dreamcat.common.crypto;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.ByteUtil;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Create by tuke on 2019-02-17
 */
@Slf4j
public class PBECryptoAlgorithmTest {


    private String msg = "0123456789abcdef";
    byte[] data = msg.getBytes(StandardCharsets.ISO_8859_1);

    @Test
    public void cipher() throws Exception {
        byte[] output, input;
        for (PBECryptoAlgorithm pbe : PBECryptoAlgorithm.values()) {
            log.info("\tpbe:\t{}", pbe.name());

            String password = UUID.randomUUID().toString()
                    .replace("-", "");
            byte[] salt = pbe.generateSalt();
            log.info("\tpassword:\t{}  {}", password.length(), password);
            log.info("\tsalt:\t{}  {}", salt.length, ByteUtil.hex(salt));

            log.info("\toriginal input:\t{}  {}", data.length, ByteUtil.hex(data));

            try {
                output = pbe.encrypt(data, password, salt);
                log.info("\toutput:\t{}  {}", output.length, ByteUtil.hex(output));

                input = pbe.decrypt(output, password, salt);
                log.info("\tdecrypted input:\t{}  {}", input.length, ByteUtil.hex(input));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

            System.out.println("\n");
        }

    }


}
