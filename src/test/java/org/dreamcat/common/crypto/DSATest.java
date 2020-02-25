package org.dreamcat.common.crypto;

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
public class DSATest {

    SignatureEnum dsa = SignatureEnum.NONEwithDSA;
    private String publicKey;
    private String privateKey;

    @Before
    public void init() throws NoSuchAlgorithmException {
        Map<String, Key> keyMap = dsa.generateKeyPair();
        publicKey = dsa.getBase64PublicKey(keyMap);
        privateKey = dsa.getBase64PrivateKey(keyMap);
        log.info("====================================================");
        log.warn("publicKey:\t{}", publicKey);
        log.info("====================================================");
        log.warn("privateKey:\t{}", privateKey);
        log.info("====================================================");
    }

    @Test
    public void dsa() throws Exception {
        String data = "some staff";
        log.info("data:\t{}", data);

        log.info("====================================================");
        log.info("====================================================");
        String sign = dsa.signBase64(data.getBytes(), privateKey);
        log.info("sign:\t{}", sign);

        boolean verified = dsa.verifyBase64(data.getBytes(), publicKey, sign);
        log.info("verified:\t{}", verified);
    }

}
