package org.dreamcat.common.crypto;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import org.dreamcat.common.util.Base64Util;

/**
 * Create by tuke on 2019-02-17
 */
public enum DHCryptoAlgorithm {
    AES("AES"),
    DES("DES"),
    DESede("DESede"),
    TripleDES("TripleDES");

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private final String algorithm;
    private final String PUBLIC_KEY;
    private final String PRIVATE_KEY;
    private final String secretAlgorithm;
    // [512, 1024], multiple of 64
    private final int keySize;

    DHCryptoAlgorithm(String secretAlgorithm) {
        this.algorithm = "DH";
        this.secretAlgorithm = secretAlgorithm;
        this.keySize = 1024;
        PUBLIC_KEY = algorithm + "PublicKey";
        PRIVATE_KEY = algorithm + "PrivateKey";

        // -Djdk.crypto.KeyAgreement.legacyKDF=true
        System.setProperty("jdk.crypto.KeyAgreement.legacyKDF", "true");
    }

    public byte[] encryptByBase64Key(byte[] data, String publicKey, String privateKey)
            throws Exception {
        return encrypt(data, Base64Util.decode(publicKey), Base64Util.decode(privateKey));
    }

    public byte[] decryptByBase64Key(byte[] data, String publicKey, String privateKey)
            throws Exception {
        return decrypt(data, Base64Util.decode(publicKey), Base64Util.decode(privateKey));
    }

    public byte[] encrypt(byte[] data, byte[] publicKey, byte[] privateKey) throws Exception {
        return crypto(data, publicKey, privateKey, Cipher.ENCRYPT_MODE);
    }

    public byte[] decrypt(byte[] data, byte[] publicKey, byte[] privateKey) throws Exception {
        return crypto(data, publicKey, privateKey, Cipher.DECRYPT_MODE);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public String getBase64PrivateKey(Map<String, Key> keyPair) {
        return Base64Util.encodeAsString(getPrivateKey(keyPair));
    }

    public String getBase64PublicKey(Map<String, Key> keyPair) {
        return Base64Util.encodeAsString(getPublicKey(keyPair));
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public byte[] getPrivateKey(Map<String, Key> keyPair) {
        return keyPair.get(PRIVATE_KEY).getEncoded();
    }

    public byte[] getPublicKey(Map<String, Key> keyPair) {
        return keyPair.get(PUBLIC_KEY).getEncoded();
    }

    /**
     * generate keys
     *
     * @return PUBLIC_KEY and PRIVATE_KEY
     * @throws NoSuchAlgorithmException algorithm
     * @see #generateKeyPairByPublicKey(byte[]) (String)
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

    /**
     * A: generateKeyPair(), and keep privateKeyOfA and pass publicKeyOfA to B
     * B: generateKeyPairByPublicKey(publicKeyOfA),
     * and keep privateKeyOfB and pass publicKeyOfB to A
     * <p>
     * A: use privateKeyOfA, publicKeyOfB, secretAlgorithm to generated a secretKey
     * then encrypt some data and send to B
     * <p>
     * B: use privateKeyOfB, publicKeyOfA, secretAlgorithm to decrypt the data
     *
     * @param publicKey publicKey of other, generated by
     * @return pair of publicKey and privateKey
     * @throws Exception any error such as a wrong algorithm name is given
     * @see #generateKeyPair()
     */
    public Map<String, Key> generateKeyPairByPublicKey(byte[] publicKey) throws Exception {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

        // gen keyMap by public key of other party
        DHParameterSpec dhParamSpec = ((DHPublicKey) pubKey).getParams();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator
                .getInstance(keyFactory.getAlgorithm());
        keyPairGenerator.initialize(dhParamSpec);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        DHPublicKey otherPublicKey = (DHPublicKey) keyPair.getPublic();
        DHPrivateKey otherPrivateKey = (DHPrivateKey) keyPair.getPrivate();

        Map<String, Key> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY, otherPublicKey);
        keyMap.put(PRIVATE_KEY, otherPrivateKey);
        return keyMap;
    }

    private byte[] crypto(byte[] data, byte[] publicKey, byte[] privateKey, int mode)
            throws Exception {
        SecretKey secretKey = generateSecretKey(publicKey, privateKey);
        Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
        cipher.init(mode, secretKey);
        return cipher.doFinal(data);
    }

    private SecretKey generateSecretKey(byte[] publicKey, byte[] privateKey)
            throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

        KeyAgreement keyAgree = KeyAgreement.getInstance(keyFactory
                .getAlgorithm());
        keyAgree.init(priKey);
        keyAgree.doPhase(pubKey, true);

        return keyAgree.generateSecret(secretAlgorithm);
    }

}
