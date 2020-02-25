package org.dreamcat.common.crypto;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.Base64Util;
import org.junit.Before;
import org.junit.Test;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Create by tuke on 2019-01-27
 */
@Slf4j
public class RSACryptoEnumTest {

    private RSACryptoEnum rsa = RSACryptoEnum.RSA;
    private String publicKey;
    private String privateKey;

    @Before
    public void init() throws NoSuchAlgorithmException {
        Map<String, Key> keyMap = rsa.generateKeyPair();
        publicKey = rsa.getBase64PublicKey(keyMap);
        privateKey = rsa.getBase64PrivateKey(keyMap);
        log.info("====================================================");
        log.warn("publicKey:\t{}", publicKey);
        log.info("====================================================");
        log.warn("privateKey:\t{}", privateKey);
        log.info("====================================================");
    }

    @Test
    public void rsa() throws Exception {
        String data = "source string ÿ";
        log.info("data:\t{}", data);
        byte[] encryptedData = rsa.encryptByBase64PublicKey(data.getBytes(), publicKey);
        log.info("encryptedData:\t{}", Base64Util.encode(encryptedData));

        byte[] decryptedData = rsa.decryptByBase64PrivateKey(encryptedData, privateKey);
        log.info("decryptedData:\t{}", new String(decryptedData));

        log.info("====================================================");
        log.info("====================================================");

        encryptedData = rsa.encryptByBase64PrivateKey(data.getBytes(), privateKey);
        log.info("encryptedData:\t{}", Base64Util.encode(encryptedData));

        decryptedData = rsa.decryptByBase64PublicKey(encryptedData, publicKey);
        log.info("decryptedData:\t{}", new String(decryptedData));

    }
}
