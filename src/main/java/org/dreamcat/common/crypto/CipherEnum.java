package org.dreamcat.common.crypto;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.function.BiFunction;

@Slf4j
public enum CipherEnum {
    /**
     * <strong>Data Encryption Standard</strong>
     * DES: translate 64 bits input to 64-bits encrypted output
     * using a 64-bits secret key
     */
    DES("DES", 56),
    TRIPLE_DES("DESede", 168, 128),
    /**
     * <strong>Advanced Encryption Standard</strong>
     */
    AES("AES", 128, 192, 256),
    BLOWFISH("Blowfish", 448),
    RC2("RC2", 1024);

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private final String ALGORITHM;
    private final int[] KEY_SIZES;
    /**
     * ECB: same input, same output
     * CBC: same input, different output
     */
    private final String ECB_PKCS5_PADDING;
    private final String CBC_PKCS5_PADDING;
    //  NoPadding: input length must be multiple of 16 bytes
    private final String CBC_NO_PADDING;
    private volatile int bufferSize;

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    /**
     * <h3>Symmetric cryptographic algorithm</h3>
     *
     * @param ALGORITHM Following algorithm
     * @param KEY_SIZES depends on a the specific algorithm
     * @see CipherEnum#DES
     * key size must be equal to 56
     * @see CipherEnum#TRIPLE_DES
     * <strong>DESede</strong>, key size must be equal to 112 or 168
     * @see CipherEnum#AES
     * key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available
     * @see CipherEnum#BLOWFISH
     * key size must be multiple of 8, and can only range from 32 to 448 (inclusive)
     * @see CipherEnum#RC2
     * key size must be between 40 and 1024 bits
     */
    CipherEnum(String ALGORITHM, int... KEY_SIZES) {
        this.ALGORITHM = ALGORITHM;
        ECB_PKCS5_PADDING = ALGORITHM + "/ECB/PKCS5Padding";
        CBC_PKCS5_PADDING = ALGORITHM + "/CBC/PKCS5Padding";
        CBC_NO_PADDING = ALGORITHM + "/CBC/NoPadding";
        this.KEY_SIZES = KEY_SIZES;

        // 4K Buffer Area
        this.bufferSize = 4096;
    }

    public byte[] encryptEcb(byte[] data, byte[] key) throws Exception {
        return encrypt(data, key,
                (k, m) -> newCipher(ECB_PKCS5_PADDING, k, m));
    }

    public byte[] encryptEcb(InputStream data, byte[] key) throws Exception {
        return encrypt(data, key,
                (k, m) -> newCipher(ECB_PKCS5_PADDING, k, m));
    }

    public int encryptEcb(ByteBuffer input, ByteBuffer output, byte[] key) throws Exception {
        return encrypt(input, output, key,
                (k, m) -> newCipher(ECB_PKCS5_PADDING, k, m));
    }

    public byte[] decryptEcb(byte[] data, byte[] key) throws Exception {
        return decrypt(data, key, (k, m) -> newCipher(ECB_PKCS5_PADDING, k, m));
    }

    public byte[] decryptEcb(InputStream data, byte[] key) throws Exception {
        return decrypt(data, key,
                (k, m) -> newCipher(ECB_PKCS5_PADDING, k, m));
    }

    public int decryptEcb(ByteBuffer input, ByteBuffer output, byte[] key) throws Exception {
        return decrypt(input, output, key,
                (k, m) -> newCipher(ECB_PKCS5_PADDING, k, m));
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public byte[] encryptCbc(byte[] data, byte[] key) throws Exception {
        return encrypt(data, key, (k, m) -> newCipherByIv(CBC_PKCS5_PADDING, k, m));
    }

    public byte[] encryptCbc(InputStream data, byte[] key) throws Exception {
        return encrypt(data, key,
                (k, m) -> newCipherByIv(CBC_PKCS5_PADDING, k, m));
    }

    public int encryptCbc(ByteBuffer input, ByteBuffer output, byte[] key) throws Exception {
        return encrypt(input, output, key,
                (k, m) -> newCipherByIv(CBC_PKCS5_PADDING, k, m));
    }

    public byte[] decryptCbc(byte[] data, byte[] key) throws Exception {
        return decrypt(data, key, (k, m) -> newCipherByIv(CBC_PKCS5_PADDING, k, m));
    }

    public byte[] decryptCbc(InputStream data, byte[] key) throws Exception {
        return decrypt(data, key,
                (k, m) -> newCipherByIv(CBC_PKCS5_PADDING, k, m));
    }

    public int decryptCbc(ByteBuffer input, ByteBuffer output, byte[] key) throws Exception {
        return decrypt(input, output, key,
                (k, m) -> newCipherByIv(CBC_PKCS5_PADDING, k, m));
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public byte[] encryptCbcNoPadding(byte[] data, byte[] key) throws Exception {
        return encrypt(data, key, (k, m) -> newCipherByIv(CBC_NO_PADDING, k, m));
    }

    public byte[] encryptCbcNoPadding(InputStream data, byte[] key) throws Exception {
        return encrypt(data, key,
                (k, m) -> newCipherByIv(CBC_NO_PADDING, k, m));
    }

    public int encryptCbcNoPadding(ByteBuffer input, ByteBuffer output, byte[] key) throws Exception {
        return encrypt(input, output, key,
                (k, m) -> newCipherByIv(CBC_NO_PADDING, k, m));
    }

    public byte[] decryptCbcNoPadding(byte[] data, byte[] key) throws Exception {
        return decrypt(data, key, (k, m) -> newCipherByIv(CBC_NO_PADDING, k, m));
    }

    public byte[] decryptCbcNoPadding(InputStream data, byte[] key) throws Exception {
        return decrypt(data, key,
                (k, m) -> newCipherByIv(CBC_NO_PADDING, k, m));
    }

    public int decryptCbcNoPadding(ByteBuffer input, ByteBuffer output, byte[] key) throws Exception {
        return decrypt(input, output, key,
                (k, m) -> newCipherByIv(CBC_NO_PADDING, k, m));
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public byte[] generateKey() {
        return generateKey(KEY_SIZES[0]);
    }

    public byte[] generateKey(int size) {
        SecretKey secretKey = generateSecretKey(size);
        return secretKey.getEncoded();
    }

    public SecretKey generateSecretKey(int size) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(size);
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private Cipher newCipher(String transformation, byte[] key, int mode) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);

            SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
            cipher.init(mode, keySpec);
            return cipher;
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Cipher newCipherByIv(String transformation, byte[] key, int mode) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);

            // inital vector
            key = truncKeyForIv(key);
            IvParameterSpec iv = new IvParameterSpec(key/*, 0, 16*/);
            cipher.init(mode, keySpec, iv);
            return cipher;
        } catch (InvalidKeyException | NoSuchAlgorithmException |
                NoSuchPaddingException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private byte[] truncKeyForIv(byte[] key) {
        if (equals(TRIPLE_DES)
                || equals(BLOWFISH)
                || equals(RC2)) {
            if (key.length != 8) {
                byte[] newKey = new byte[8];
                System.arraycopy(key, 0, newKey, 0, Math.min(key.length, 8));
                return newKey;
            }
        }
        return key;
    }

    private byte[] encrypt(
            byte[] data, byte[] key, BiFunction<byte[], Integer, Cipher> constructor) throws Exception {

        return crypto(data, key, constructor, Cipher.ENCRYPT_MODE);
    }

    private byte[] encrypt(
            InputStream data, byte[] key, BiFunction<byte[], Integer, Cipher> constructor) throws Exception {

        return crypto(data, key, constructor, Cipher.ENCRYPT_MODE);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private int encrypt(
            ByteBuffer input, ByteBuffer output, byte[] key, BiFunction<byte[], Integer, Cipher> constructor) throws Exception {

        return crypto(input, output, key, constructor, Cipher.ENCRYPT_MODE);
    }

    private byte[] decrypt(
            byte[] data, byte[] key, BiFunction<byte[], Integer, Cipher> constructor) throws Exception {
        return crypto(data, key, constructor, Cipher.DECRYPT_MODE);
    }

    private byte[] decrypt(
            InputStream data, byte[] key, BiFunction<byte[], Integer, Cipher> constructor) throws Exception {
        return crypto(data, key, constructor, Cipher.DECRYPT_MODE);
    }

    private int decrypt(
            ByteBuffer input, ByteBuffer output, byte[] key, BiFunction<byte[], Integer, Cipher> constructor) throws Exception {
        return crypto(input, output, key, constructor, Cipher.DECRYPT_MODE);
    }

    private byte[] crypto(
            byte[] data, byte[] key, BiFunction<byte[], Integer, Cipher> constructor, int mode) throws Exception {
        Cipher cipher = constructor.apply(key, mode);
        return cipher.doFinal(data);
    }

    private byte[] crypto(
            InputStream data, byte[] key, BiFunction<byte[], Integer, Cipher> constructor, int mode) throws Exception {
        Cipher cipher = constructor.apply(key, mode);
        final int size = bufferSize;
        byte[] buffer = new byte[size];
        int readSize;
        while ((readSize = data.read(buffer)) > 0) {
            cipher.update(buffer, 0, readSize);
        }
        return cipher.doFinal();
    }

    private int crypto(
            ByteBuffer input, ByteBuffer output, byte[] key, BiFunction<byte[], Integer, Cipher> constructor, int mode) throws Exception {
        Cipher cipher = constructor.apply(key, mode);
        return cipher.doFinal(input, output);
    }

    public CipherEnum withBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }
}
