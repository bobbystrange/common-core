package org.dreamcat.common.crypto;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.function.BiFunction;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum CipherAlgorithm implements CipherCrackAlgorithm {
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

    private final String algorithm;
    private final int[] keySizes;
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
     * @param algorithm Following algorithm
     * @param keySizes  depends on a the specific algorithm
     * @see CipherAlgorithm#DES
     * key size must be equal to 56
     * @see CipherAlgorithm#TRIPLE_DES
     * <strong>DESede</strong>, key size must be equal to 112 or 168
     * @see CipherAlgorithm#AES
     * key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available
     * @see CipherAlgorithm#BLOWFISH
     * key size must be multiple of 8, and can only range from 32 to 448 (inclusive)
     * @see CipherAlgorithm#RC2
     * key size must be between 40 and 1024 bits
     */
    CipherAlgorithm(String algorithm, int... keySizes) {
        this.algorithm = algorithm;
        ECB_PKCS5_PADDING = algorithm + "/ECB/PKCS5Padding";
        CBC_PKCS5_PADDING = algorithm + "/CBC/PKCS5Padding";
        CBC_NO_PADDING = algorithm + "/CBC/NoPadding";
        this.keySizes = keySizes;

        // 4K Buffer Area
        this.bufferSize = 4096;
    }

    public byte[] encryptEcb(String data, String key) throws Exception {
        return encryptEcb(data.getBytes(StandardCharsets.ISO_8859_1), key);
    }

    public byte[] encryptEcb(byte[] data, String key) throws Exception {
        return encryptEcb(data, key.getBytes(StandardCharsets.ISO_8859_1));
    }

    public byte[] encryptEcb(byte[] data, byte[] key) throws Exception {
        return encrypt(data, key,
                (k, m) -> newCipher(ECB_PKCS5_PADDING, k, m));
    }

    public byte[] encryptEcb(byte[] data, int offset, int length, byte[] key) throws Exception {
        return encrypt(data, offset, length, key,
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

    public byte[] decryptEcb(String data, String key) throws Exception {
        return decryptEcb(data.getBytes(StandardCharsets.ISO_8859_1), key);
    }

    public byte[] decryptEcb(byte[] data, String key) throws Exception {
        return decryptEcb(data, key.getBytes(StandardCharsets.ISO_8859_1));
    }

    public byte[] decryptEcb(byte[] data, byte[] key) throws Exception {
        return decrypt(data, key, (k, m) -> newCipher(ECB_PKCS5_PADDING, k, m));
    }

    public byte[] decryptEcb(byte[] data, int offset, int length, byte[] key) throws Exception {
        return decrypt(data, offset, length, key, (k, m) -> newCipher(ECB_PKCS5_PADDING, k, m));
    }

    public byte[] decryptEcb(InputStream data, byte[] key) throws Exception {
        return decrypt(data, key,
                (k, m) -> newCipher(ECB_PKCS5_PADDING, k, m));
    }

    public void decryptEcb(InputStream input, OutputStream output, byte[] key) throws Exception {
        decrypt(input, output, key,
                (k, m) -> newCipher(ECB_PKCS5_PADDING, k, m));
    }

    public int decryptEcb(ByteBuffer input, ByteBuffer output, byte[] key) throws Exception {
        return decrypt(input, output, key,
                (k, m) -> newCipher(ECB_PKCS5_PADDING, k, m));
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public byte[] encryptCbc(String data, String key) throws Exception {
        return encryptCbc(data.getBytes(StandardCharsets.ISO_8859_1), key);
    }

    public byte[] encryptCbc(byte[] data, String key) throws Exception {
        return encryptCbc(data, key.getBytes(StandardCharsets.ISO_8859_1));
    }

    public byte[] encryptCbc(byte[] data, byte[] key) throws Exception {
        return encrypt(data, key, (k, m) -> newCipherByIv(CBC_PKCS5_PADDING, k, m));
    }

    public byte[] encryptCbc(byte[] data, int offset, int length, byte[] key) throws Exception {
        return encrypt(data, offset, length, key, (k, m) -> newCipherByIv(CBC_PKCS5_PADDING, k, m));
    }

    public byte[] encryptCbc(InputStream data, byte[] key) throws Exception {
        return encrypt(data, key,
                (k, m) -> newCipherByIv(CBC_PKCS5_PADDING, k, m));
    }

    public int encryptCbc(ByteBuffer input, ByteBuffer output, byte[] key) throws Exception {
        return encrypt(input, output, key,
                (k, m) -> newCipherByIv(CBC_PKCS5_PADDING, k, m));
    }

    public byte[] decryptCbc(String data, String key) throws Exception {
        return decryptCbc(data.getBytes(StandardCharsets.ISO_8859_1), key);
    }

    public byte[] decryptCbc(byte[] data, String key) throws Exception {
        return decryptCbc(data, key.getBytes(StandardCharsets.ISO_8859_1));
    }

    public byte[] decryptCbc(byte[] data, byte[] key) throws Exception {
        return decrypt(data, key, (k, m) -> newCipherByIv(CBC_PKCS5_PADDING, k, m));
    }

    public byte[] decryptCbc(byte[] data, int offset, int length, byte[] key) throws Exception {
        return decrypt(data, offset, length, key, (k, m) -> newCipherByIv(CBC_PKCS5_PADDING, k, m));
    }

    public byte[] decryptCbc(InputStream data, byte[] key) throws Exception {
        return decrypt(data, key,
                (k, m) -> newCipherByIv(CBC_PKCS5_PADDING, k, m));
    }

    public void decryptCbc(InputStream input, OutputStream output, byte[] key) throws Exception {
        decrypt(input, output, key,
                (k, m) -> newCipherByIv(CBC_PKCS5_PADDING, k, m));
    }

    public int decryptCbc(ByteBuffer input, ByteBuffer output, byte[] key) throws Exception {
        return decrypt(input, output, key,
                (k, m) -> newCipherByIv(CBC_PKCS5_PADDING, k, m));
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public byte[] encryptCbcNoPadding(String data, String key) throws Exception {
        return encryptCbcNoPadding(data.getBytes(StandardCharsets.ISO_8859_1), key);
    }

    public byte[] encryptCbcNoPadding(byte[] data, String key) throws Exception {
        return encryptCbcNoPadding(data, key.getBytes(StandardCharsets.ISO_8859_1));
    }

    public byte[] encryptCbcNoPadding(byte[] data, byte[] key) throws Exception {
        return encrypt(data, key, (k, m) -> newCipherByIv(CBC_NO_PADDING, k, m));
    }

    public byte[] encryptCbcNoPadding(InputStream data, byte[] key) throws Exception {
        return encrypt(data, key,
                (k, m) -> newCipherByIv(CBC_NO_PADDING, k, m));
    }

    public int encryptCbcNoPadding(ByteBuffer input, ByteBuffer output, byte[] key)
            throws Exception {
        return encrypt(input, output, key,
                (k, m) -> newCipherByIv(CBC_NO_PADDING, k, m));
    }

    public byte[] decryptCbcNoPadding(String data, String key) throws Exception {
        return decryptCbcNoPadding(data.getBytes(StandardCharsets.ISO_8859_1), key);
    }

    public byte[] decryptCbcNoPadding(byte[] data, String key) throws Exception {
        return decryptCbcNoPadding(data, key.getBytes(StandardCharsets.ISO_8859_1));
    }

    public byte[] decryptCbcNoPadding(byte[] data, byte[] key) throws Exception {
        return decrypt(data, key, (k, m) -> newCipherByIv(CBC_NO_PADDING, k, m));
    }

    public byte[] decryptCbcNoPadding(InputStream data, byte[] key) throws Exception {
        return decrypt(data, key,
                (k, m) -> newCipherByIv(CBC_NO_PADDING, k, m));
    }

    public int decryptCbcNoPadding(ByteBuffer input, ByteBuffer output, byte[] key)
            throws Exception {
        return decrypt(input, output, key,
                (k, m) -> newCipherByIv(CBC_NO_PADDING, k, m));
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public byte[] generateKey() {
        return generateKey(keySizes[0]);
    }

    public byte[] generateKey(int size) {
        SecretKey secretKey = generateSecretKey(size);
        return secretKey.getEncoded();
    }

    public SecretKey generateSecretKey(int size) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
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

            SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
            cipher.init(mode, keySpec);
            return cipher;
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Cipher newCipherByIv(String transformation, byte[] key, int mode) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);

            // inital vector
            key = truncKeyForIv(key);
            IvParameterSpec iv = new IvParameterSpec(key/*, 0, 16*/);
            cipher.init(mode, keySpec, iv);
            return cipher;
        } catch (InvalidKeyException | NoSuchAlgorithmException |
                NoSuchPaddingException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] truncKeyForIv(byte[] key) {
        if (equals(TRIPLE_DES)
                || equals(BLOWFISH)
                || equals(RC2)) {
            if (key.length != 8) {
                byte[] iv = new byte[8];
                System.arraycopy(key, 0, iv, 0, Math.min(key.length, 8));
                return iv;
            }
        } else if (equals(AES)) {
            if (key.length != 16) {
                byte[] iv = new byte[16];
                System.arraycopy(key, 0, iv, 0, Math.min(key.length, 16));
                return iv;
            }
        }
        return key;
    }

    private byte[] encrypt(
            byte[] data, byte[] key, BiFunction<byte[], Integer, Cipher> constructor)
            throws Exception {
        return crypto(data, key, constructor, Cipher.ENCRYPT_MODE);
    }

    private byte[] encrypt(
            byte[] data, int offset, int length, byte[] key,
            BiFunction<byte[], Integer, Cipher> constructor) throws Exception {
        return crypto(data, offset, length, key, constructor, Cipher.ENCRYPT_MODE);
    }

    private byte[] encrypt(
            InputStream data, byte[] key, BiFunction<byte[], Integer, Cipher> constructor)
            throws Exception {
        return crypto(data, key, constructor, Cipher.ENCRYPT_MODE);
    }

    private void encrypt(
            InputStream input, OutputStream output, byte[] key,
            BiFunction<byte[], Integer, Cipher> constructor) throws Exception {
        crypto(input, output, key, constructor, Cipher.ENCRYPT_MODE);
    }

    private int encrypt(
            ByteBuffer input, ByteBuffer output, byte[] key,
            BiFunction<byte[], Integer, Cipher> constructor) throws Exception {
        return crypto(input, output, key, constructor, Cipher.ENCRYPT_MODE);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private byte[] decrypt(
            byte[] data, byte[] key, BiFunction<byte[], Integer, Cipher> constructor)
            throws Exception {
        return crypto(data, key, constructor, Cipher.DECRYPT_MODE);
    }

    private byte[] decrypt(
            byte[] data, int offset, int length, byte[] key,
            BiFunction<byte[], Integer, Cipher> constructor) throws Exception {
        return crypto(data, offset, length, key, constructor, Cipher.DECRYPT_MODE);
    }

    private byte[] decrypt(
            InputStream data, byte[] key, BiFunction<byte[], Integer, Cipher> constructor)
            throws Exception {
        return crypto(data, key, constructor, Cipher.DECRYPT_MODE);
    }

    private void decrypt(
            InputStream input, OutputStream output, byte[] key,
            BiFunction<byte[], Integer, Cipher> constructor) throws Exception {
        crypto(input, output, key, constructor, Cipher.DECRYPT_MODE);
    }

    private int decrypt(
            ByteBuffer input, ByteBuffer output, byte[] key,
            BiFunction<byte[], Integer, Cipher> constructor) throws Exception {
        return crypto(input, output, key, constructor, Cipher.DECRYPT_MODE);
    }

    private byte[] crypto(
            byte[] data, byte[] key, BiFunction<byte[], Integer, Cipher> constructor, int mode)
            throws Exception {
        return crypto(data, 0, data.length, key, constructor, mode);
    }

    private byte[] crypto(
            byte[] data, int offset, int length, byte[] key,
            BiFunction<byte[], Integer, Cipher> constructor, int mode) throws Exception {
        Cipher cipher = constructor.apply(key, mode);
        return cipher.doFinal(data, offset, length);
    }

    @Deprecated
    private byte[] crypto(
            InputStream data, byte[] key, BiFunction<byte[], Integer, Cipher> constructor, int mode)
            throws Exception {
        Cipher cipher = constructor.apply(key, mode);
        final int size = bufferSize;
        byte[] buffer = new byte[size];
        int readSize;
        while ((readSize = data.read(buffer)) > 0) {
            cipher.update(buffer, 0, readSize);
        }
        return cipher.doFinal();
    }

    private void crypto(
            InputStream input, OutputStream output, byte[] key,
            BiFunction<byte[], Integer, Cipher> constructor, int mode) throws Exception {
        final int size = bufferSize;
        byte[] buffer = new byte[size];
        int readSize;
        while ((readSize = input.read(buffer)) > 0) {
            Cipher cipher = constructor.apply(key, mode);
            byte[] data = cipher.doFinal(buffer, 0, readSize);
            output.write(data);
        }
    }

    private int crypto(
            ByteBuffer input, ByteBuffer output, byte[] key,
            BiFunction<byte[], Integer, Cipher> constructor, int mode) throws Exception {
        Cipher cipher = constructor.apply(key, mode);
        return cipher.doFinal(input, output);
    }

    public CipherAlgorithm withBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }
}
