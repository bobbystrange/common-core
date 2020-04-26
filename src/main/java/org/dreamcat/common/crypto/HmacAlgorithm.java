package org.dreamcat.common.crypto;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.Base64Util;
import org.dreamcat.common.util.ByteUtil;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

// Message Authentication Code
@Slf4j
public enum HmacAlgorithm {
    HMAC_MD5("HmacMD5"),
    HMAC_SHA_1("HmacSHA1"),
    HMAC_SHA_224("HmacSHA224"),
    HMAC_SHA_256("HmacSHA256"),
    HMAC_SHA_384("HmacSHA384"),
    HMAC_SHA_512("HmacSHA512");

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private final String algorithm;
    private volatile int bufferSize;

    HmacAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        this.bufferSize = 4096;
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public byte[] digest(byte[] input, String key) throws Exception {
        return digest(input, key.getBytes());
    }

    public byte[] digest(byte[] input, byte[] key) throws Exception {
        Mac mac = generateMac(key);
        mac.update(input);
        return mac.doFinal();
    }

    public byte[] digest(InputStream input, String key) throws Exception {
        return digest(input, key.getBytes());
    }

    public byte[] digest(InputStream input, byte[] key) throws Exception {
        Mac mac = generateMac(key);

        final int size = bufferSize;
        final byte[] buffer = new byte[size];
        int read = input.read(buffer, 0, size);

        while (read > 0) {
            mac.update(buffer, 0, read);
            read = input.read(buffer, 0, size);
        }
        return mac.doFinal();
    }

    public byte[] digest(ByteBuffer input, String key) throws Exception {
        return digest(input, key.getBytes());
    }

    public byte[] digest(ByteBuffer input, byte[] key) throws Exception {
        Mac mac = generateMac(key);
        mac.update(input);
        return mac.doFinal();
    }

    public byte[] digest(String input, String key) throws Exception {
        return digest(input, key.getBytes());
    }

    public byte[] digest(String input, byte[] key) throws Exception {
        return digest(input.getBytes(), key);
    }

    public byte[] digest(InputStream input, OutputStream output, String key) throws Exception {
        return digest(input, output, key.getBytes());
    }

    public byte[] digest(InputStream input, OutputStream output, byte[] key) throws Exception {
        Mac mac = generateMac(key);

        final int size = bufferSize;
        final byte[] buffer = new byte[size];
        int read = input.read(buffer, 0, size);

        while (read > 0) {
            mac.update(buffer, 0, read);
            output.write(buffer, 0, read);
            read = input.read(buffer, 0, size);
        }
        return mac.doFinal();
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public String digestAsHex(byte[] input, String key) throws Exception {
        return digestAsHex(input, key.getBytes());
    }

    public String digestAsHex(byte[] input, byte[] key) throws Exception {
        return ByteUtil.hex(digest(input, key));
    }

    public String digestAsHex(InputStream input, String key) throws Exception {
        return digestAsHex(input, key.getBytes());
    }

    public String digestAsHex(InputStream input, byte[] key) throws Exception {
        return ByteUtil.hex(digest(input, key));
    }

    public String digestAsHex(ByteBuffer input, String key) throws Exception {
        return digestAsHex(input, key.getBytes());
    }

    public String digestAsHex(ByteBuffer input, byte[] key) throws Exception {
        return ByteUtil.hex(digest(input, key));
    }

    public String digestAsHex(String input, String key) throws Exception {
        return digestAsHex(input, key.getBytes());
    }

    public String digestAsHex(String input, byte[] key) throws Exception {
        return digestAsHex(input.getBytes(), key);
    }

    public String digestAsHex(InputStream input, OutputStream output, String key) throws Exception {
        return digestAsHex(input, output, key.getBytes());
    }

    public String digestAsHex(InputStream input, OutputStream output, byte[] key) throws Exception {
        return ByteUtil.hex(digest(input, output, key));
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public String digestAsBase64(byte[] input, String key) throws Exception {
        return digestAsBase64(input, key.getBytes());
    }

    public String digestAsBase64(byte[] input, byte[] key) throws Exception {
        return Base64Util.encodeAsString(digest(input, key));
    }

    public String digestAsBase64(InputStream input, String key) throws Exception {
        return digestAsBase64(input, key.getBytes());
    }

    public String digestAsBase64(InputStream input, byte[] key) throws Exception {
        return Base64Util.encodeAsString(digest(input, key));
    }

    public String digestAsBase64(ByteBuffer input, String key) throws Exception {
        return digestAsBase64(input, key.getBytes());
    }

    public String digestAsBase64(ByteBuffer input, byte[] key) throws Exception {
        return Base64Util.encodeAsString(digest(input, key));
    }

    public String digestAsBase64(String input, String key) throws Exception {
        return digestAsBase64(input, key.getBytes());
    }

    public String digestAsBase64(String input, byte[] key) throws Exception {
        return digestAsBase64(input.getBytes(), key);
    }

    public String digestAsBase64(InputStream input, OutputStream output, String key) throws Exception {
        return digestAsBase64(input, output, key.getBytes());

    }

    public String digestAsBase64(InputStream input, OutputStream output, byte[] key) throws Exception {
        return Base64Util.encodeAsString(digest(input, output, key));
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public byte[] generateKey() {
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    public String generateKeyAsHex() {
        return ByteUtil.hex(generateKey());
    }

    public String generateKeyAsBase64() {
        return Base64Util.encodeAsString(generateKey());
    }

    public HmacAlgorithm withBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private Mac generateMac(byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {
        Mac mac = Mac.getInstance(algorithm);
        SecretKeySpec secret_key = new SecretKeySpec(key, algorithm);
        mac.init(secret_key);
        return mac;
    }

}
