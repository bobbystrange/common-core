package org.dreamcat.common.crypto;

import org.dreamcat.common.crypto.RSACryptoEnum;
import org.dreamcat.common.util.Base64Util;
import lombok.extern.slf4j.Slf4j;
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
        log.warn("publicKey:\n{}", publicKey);
        log.info("====================================================");
        log.warn("privateKey:\n{}", privateKey);
        log.info("====================================================");
    }

    @Test
    public void rsa() throws Exception {
        String data = "source string ÿ";
        log.info("data:\n{}", data);
        byte[] encryptedData = rsa.encryptByBase64PublicKey(data.getBytes(), publicKey);
        log.info("encryptedData:\n{}", Base64Util.encode(encryptedData));

        byte[] decryptedData = rsa.decryptByBase64PrivateKey(encryptedData, privateKey);
        log.info("decryptedData:\n{}", new String(decryptedData));

        log.info("====================================================");
        log.info("====================================================");

        encryptedData = rsa.encryptByBase64PrivateKey(data.getBytes(), privateKey);
        log.info("encryptedData:\n{}", Base64Util.encode(encryptedData));

        decryptedData = rsa.decryptByBase64PublicKey(encryptedData, publicKey);
        log.info("decryptedData:\n{}", new String(decryptedData));

    }
}
