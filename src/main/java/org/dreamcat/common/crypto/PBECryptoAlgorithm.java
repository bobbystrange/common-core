package org.dreamcat.common.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.SecureRandom;
import java.util.Arrays;

// Password-based encryption
public enum PBECryptoAlgorithm {
    PBEwithHmacSHA256andAES_128("PBEwithHmacSHA256andAES_128"),
    PBEwithMD5andDES("PBEWithMD5andDES"),
    PBEwithMD5andTripleDES("PBEwithMD5andTripleDES"),
    PBEwithSHA1andDESede("PBEwithSHA1andDESede"),
    PBEwithSHA1andRC2_128("PBEwithSHA1andRC2_128"),
    PBEwithSHA1andRC2_40("PBEwithSHA1andRC2_40");

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private static final int DEFAULT_ITERATIONCOUNT = 128;
    private static final int PBE_SALT_SIZE = 8;
    private final String algorithm;

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====
    private final int iterationCount;

    PBECryptoAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        this.iterationCount = DEFAULT_ITERATIONCOUNT;
    }

    public byte[] generateSalt() {
        byte[] salt = new byte[PBE_SALT_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public byte[] encrypt(byte[] data, String password, byte[] salt)
            throws Exception {
        return cipher(data, password, salt, Cipher.ENCRYPT_MODE);
    }

    public byte[] decrypt(byte[] data, String password, byte[] salt)
            throws Exception {
        return cipher(data, password, salt, Cipher.DECRYPT_MODE);

    }

    private byte[] cipher(byte[] data, String password, byte[] salt, int mode) throws Exception {
        SecretKey secretKey = generateSecretKey(password);
        PBEParameterSpec paramSpec = generatePBEParameterSpec(salt);

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(mode, secretKey, paramSpec);
        return cipher.doFinal(data);
    }

    private SecretKey generateSecretKey(String password) throws Exception {
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        return keyFactory.generateSecret(keySpec);
    }

    private PBEParameterSpec generatePBEParameterSpec(byte[] salt) {
        byte[] iv = new byte[16];
        System.arraycopy(salt, 0, iv, 0, 8);
        byte[] sortedSalt = Arrays.copyOf(salt, 8);
        Arrays.sort(sortedSalt);
        System.arraycopy(sortedSalt, 0, iv, 8, 8);

        IvParameterSpec ivParamSpec = new IvParameterSpec(iv);
        return new PBEParameterSpec(salt, iterationCount, ivParamSpec);
    }

}
