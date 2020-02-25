package org.dreamcat.common.crypto;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.Base64Util;
import org.dreamcat.common.util.ByteUtil;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

// Message Authentication Code
@Slf4j
public enum HmacEnum {
    HMAC_MD5("HmacMD5"),
    HMAC_SHA_1("HmacSHA1"),
    HMAC_SHA_224("HmacSHA224"),
    HMAC_SHA_256("HmacSHA256"),
    HMAC_SHA_384("HmacSHA384"),
    HMAC_SHA_512("HmacSHA512");

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private final String algorithm;
    private volatile int bufferSize;

    HmacEnum(String algorithm) {
        this.algorithm = algorithm;
        this.bufferSize = 4096;
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public byte[] digest(byte[] data, byte[] key) throws Exception {
        Mac mac = generateMac(key);
        mac.update(data);
        return mac.doFinal();
    }

    public byte[] digest(InputStream data, byte[] key) throws Exception {
        Mac mac = generateMac(key);

        final int size = bufferSize;
        final byte[] buffer = new byte[size];
        int read = data.read(buffer, 0, size);

        while (read > -1) {
            mac.update(buffer, 0, read);
            read = data.read(buffer, 0, bufferSize);
        }
        return mac.doFinal();
    }

    public byte[] digest(ByteBuffer data, byte[] key) throws Exception {
        Mac mac = generateMac(key);
        mac.update(data);
        return mac.doFinal();
    }

    public byte[] digest(String data, String key) throws Exception {
        return digest(data.getBytes(), key.getBytes());
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public String digestToHex(byte[] data, byte[] key) throws Exception {
        return ByteUtil.hex(digest(data, key));
    }

    public String digestToHex(InputStream data, byte[] key) throws Exception {
        return ByteUtil.hex(digest(data, key));
    }

    public String digestToHex(ByteBuffer data, byte[] key) throws Exception {
        return ByteUtil.hex(digest(data, key));
    }

    public String digestToHex(String data, String key) throws Exception {
        return digestToHex(data.getBytes(), key.getBytes());
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public String digestToBase64(byte[] data, byte[] key) throws Exception {
        return Base64Util.encodeToString(digest(data, key));
    }

    public String digestToBase64(InputStream data, byte[] key) throws Exception {
        return Base64Util.encodeToString(digest(data, key));
    }

    public String digestToBase64(ByteBuffer data, byte[] key) throws Exception {
        return Base64Util.encodeToString(digest(data, key));
    }

    public String digestToBase64(String data, String key) throws Exception {
        return digestToBase64(data.getBytes(), key.getBytes());
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

    public String generateBase64Key() {
        return Base64Util.encodeToString(generateKey());
    }

    public HmacEnum withBufferSize(int bufferSize) {
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
