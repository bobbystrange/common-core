package org.dreamcat.common.crypto;

import org.dreamcat.common.util.Base64Util;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Create by tuke on 2019-02-17
 */
public enum SignatureEnum {
    NONEwithRSA("NONEwithRSA"),
    MD2withRSA("MD2withRSA"),
    MD5withRSA("MD5withRSA"),
    SHA1withRSA("SHA1withRSA"),
    SHA256withRSA("SHA256withRSA"),
    SHA384withRSA("SHA384withRSA"),
    SHA512withRSA("SHA512withRSA"),

    DSA("DSA"),
    // Data for RawDSA must be exactly 20 bytes long
    NONEwithDSA("NONEwithDSA"),
    SHA1withDSA("SHA1withDSA"),
    SHA256withDSA("SHA256withDSA");

    //NONEwithECDSA("NONEwithECDSA"),
    //SHA1withECDSA("SHA1withECDSA"),
    //SHA256withECDSA("SHA256withECDSA"),
    //SHA384withECDSA("SHA384withECDSA"),
    //SHA512withECDSA("SHA512withECDSA");

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private static final int RSA_KEY_SIZE = 1024;
    private static final int DSA_KEY_SIZE = 1024;

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
    private final String algorithm;
    private final String SIGNATURE_ALGORITHM;
    private final String PUBLIC_KEY;
    private final String PRIVATE_KEY;
    private int keySize;

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    //
    SignatureEnum(
            String SIGNATURE_ALGORITHM) {
        this.SIGNATURE_ALGORITHM = SIGNATURE_ALGORITHM;
        if (SIGNATURE_ALGORITHM.contains("DSA")) {
            this.keySize = DSA_KEY_SIZE;
            this.algorithm = "DSA";
        } else {
            this.keySize = RSA_KEY_SIZE;
            this.algorithm = "RSA";
        }

        PUBLIC_KEY = algorithm + "PublicKey";
        PRIVATE_KEY = algorithm + "PrivateKey";
    }
    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public byte[] sign(byte[] data, byte[] privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);

        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(truncData(data));
        return signature.sign();
    }

    public boolean verify(
            byte[] data, byte[] publicKey, byte[] sign) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);

        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(truncData(data));

        return signature.verify(sign);
    }

    public String getBase64PrivateKey(Map<String, Key> keyPair) {
        return Base64Util.encodeToString(getPrivateKey(keyPair));
    }

    public String getBase64PublicKey(Map<String, Key> keyPair) {
        return Base64Util.encodeToString(getPublicKey(keyPair));
    }

    public byte[] getPrivateKey(Map<String, Key> keyPair) {
        return keyPair.get(PRIVATE_KEY).getEncoded();
    }

    public byte[] getPublicKey(Map<String, Key> keyPair) {
        return keyPair.get(PUBLIC_KEY).getEncoded();
    }

    /**
     * @return PUBLIC_KEY and PRIVATE_KEY
     * @throws NoSuchAlgorithmException KEY_ALGORITHM
     */
    public Map<String, Key> generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator
                .getInstance(algorithm);
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(UUID.randomUUID().toString().getBytes());
        keyPairGen.initialize(keySize, secureRandom);
        KeyPair keyPair = keyPairGen.generateKeyPair();

        Map<String, Key> pair = new HashMap<>(2);
        pair.put(PUBLIC_KEY, keyPair.getPublic());
        pair.put(PRIVATE_KEY, keyPair.getPrivate());
        return pair;
    }

    // DH,
    //    DSA,
    //    RSA;

    private byte[] truncData(byte[] data) {
        // Data for RawDSA must be exactly 20 bytes long
        if (equals(NONEwithDSA) && data.length != 20) {
            byte[] newData = new byte[20];
            System.arraycopy(data, 0, newData, 0, Math.min(data.length, 20));
            return newData;
        }
        return data;
    }

    public SignatureEnum withKeySize(int keySize) {
        if (algorithm.contains("DSA")) {
            if (keySize < 512 || keySize > 2048 || keySize % 64 != 0)
                throw new IllegalArgumentException("key size must be a multiple of 64," +
                        " ranging from 512 to 1024 (inclusive)");
        }

        this.keySize = keySize;
        return this;
    }
}
