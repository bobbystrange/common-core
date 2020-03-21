package org.dreamcat.common.crypto;

import org.dreamcat.common.util.Base64Util;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <strong>Ron Rivest, Adi Shamir and Leonard Adleman</strong>
 * <p>
 * //@see this#generateKeyPair
 * A: keep privateKeyOfA and pass publicKeyOfA to B
 * A: use privateKeyOfA to encrypt the data and generate a sign
 * B: use publicKeyOfB to verify the sign and decrypt the data
 */
public enum RSACryptoEnum {
    RSA("RSA");

    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;
    private static final int KEY_SIZE = 1024;

    private final String algorithm;
    private final String PUBLIC_KEY;
    private final String PRIVATE_KEY;

    RSACryptoEnum(String algorithm) {
        this.algorithm = algorithm;
        PUBLIC_KEY = algorithm + "PublicKey";
        PRIVATE_KEY = algorithm + "PrivateKey";
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public byte[] encryptByPublicKey(byte[] data, byte[] publicKey)
            throws Exception {
        Cipher cipher = getCipherByPublishKey(publicKey, Cipher.ENCRYPT_MODE);
        return cipher(data, cipher, MAX_ENCRYPT_BLOCK);
    }

    public byte[] encryptByPrivateKey(byte[] data, byte[] privateKey)
            throws Exception {
        Cipher cipher = getCipherByPrivateKey(privateKey, Cipher.ENCRYPT_MODE);
        return cipher(data, cipher, MAX_ENCRYPT_BLOCK);
    }

    public byte[] decryptByPrivateKey(byte[] data, byte[] privateKey)
            throws Exception {
        Cipher cipher = getCipherByPrivateKey(privateKey, Cipher.DECRYPT_MODE);
        return cipher(data, cipher, MAX_DECRYPT_BLOCK);
    }

    public byte[] decryptByPublicKey(byte[] data, byte[] publicKey)
            throws Exception {
        Cipher cipher = getCipherByPublishKey(publicKey, Cipher.DECRYPT_MODE);
        return cipher(data, cipher, MAX_DECRYPT_BLOCK);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public byte[] encryptByBase64PublicKey(byte[] data, String publicKey)
            throws Exception {
        return encryptByPublicKey(data, Base64Util.decode(publicKey));
    }

    public byte[] encryptByBase64PrivateKey(byte[] data, String privateKey)
            throws Exception {
        return encryptByPrivateKey(data, Base64Util.decode(privateKey));
    }

    public byte[] decryptByBase64PrivateKey(byte[] data, String privateKey)
            throws Exception {
        return decryptByPrivateKey(data, Base64Util.decode(privateKey));

    }

    public byte[] decryptByBase64PublicKey(byte[] data, String publicKey)
            throws Exception {
        return decryptByPublicKey(data, Base64Util.decode(publicKey));
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public String getBase64PrivateKey(Map<String, Key> keyPair) {
        return Base64Util.encodeAsString(getPrivateKey(keyPair));
    }

    public String getBase64PublicKey(Map<String, Key> keyPair) {
        return Base64Util.encodeAsString(getPublicKey(keyPair));
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
        keyPairGen.initialize(KEY_SIZE, secureRandom);
        keyPairGen.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGen.generateKeyPair();

        Map<String, Key> pair = new HashMap<>(2);
        pair.put(PUBLIC_KEY, keyPair.getPublic());
        pair.put(PRIVATE_KEY, keyPair.getPrivate());
        return pair;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private byte[] cipher(byte[] data, Cipher cipher, int block) throws Exception {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            int size = data.length;
            int offset = 0;
            byte[] cache;

            while (size > offset) {
                if (size - offset > block) {
                    cache = cipher.doFinal(data, offset, block);
                } else {
                    cache = cipher.doFinal(data, offset, size - offset);
                }
                output.write(cache, 0, cache.length);
                offset += block;
            }
            return output.toByteArray();
        }
    }

    private Cipher getCipherByPrivateKey(byte[] privateKey, int mode) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        Key priKey = keyFactory.generatePrivate(pkcs8KeySpec);

        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(mode, priKey);
        return cipher;
    }

    private Cipher getCipherByPublishKey(byte[] publishKey, int mode) throws Exception {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publishKey);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        Key pubKey = keyFactory.generatePublic(x509KeySpec);

        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(mode, pubKey);
        return cipher;
    }

}
