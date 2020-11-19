package org.dreamcat.common.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.dreamcat.common.util.Base64Util;

// Message Authentication Code
public enum HmacUtil {
    HMAC_MD5("HmacMD5"),
    HMAC_SHA_1("HmacSHA1"),
    HMAC_SHA_224("HmacSHA224"),
    HMAC_SHA_256("HmacSHA256"),
    HMAC_SHA_384("HmacSHA384"),
    HMAC_SHA_512("HmacSHA512");

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    private final String algorithm;

    HmacUtil(String algorithm) {
        this.algorithm = algorithm;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static byte[] hmacsha256(byte[] input, byte[] key) throws Exception {
        return HMAC_SHA_256.digest(input, key);
    }

    public static byte[] hmacsha512(byte[] input, byte[] key) throws Exception {
        return HMAC_SHA_512.digest(input, key);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public byte[] digest(byte[] input, String base64Key) throws Exception {
        return digest(input, Base64Util.decode(base64Key));
    }

    public byte[] digest(byte[] input, byte[] key) throws Exception {
        Mac mac = generateMac(key);
        mac.update(input);
        return mac.doFinal();
    }

    public byte[] digest(String base64Input, String base64Key) throws Exception {
        return digest(Base64Util.decode(base64Input), base64Key);
    }

    public byte[] digest(String base64Input, byte[] key) throws Exception {
        return digest(Base64Util.decode(base64Input), key);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public String digestAsBase64(byte[] input, String base64Key) throws Exception {
        return digestAsBase64(input, Base64Util.decode(base64Key));
    }

    public String digestAsBase64(byte[] input, byte[] key) throws Exception {
        return Base64Util.encodeAsString(digest(input, key));
    }

    public String digestAsBase64(String base64Input, String base64Key) throws Exception {
        return digestAsBase64(Base64Util.decode(base64Input), base64Key);
    }

    public String digestAsBase64(String base64Input, byte[] key) throws Exception {
        return digestAsBase64(Base64Util.decode(base64Input), key);
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

    public String generateKeyAsBase64() {
        return Base64Util.encodeAsString(generateKey());
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private Mac generateMac(byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {
        Mac mac = Mac.getInstance(algorithm);
        SecretKeySpec secretKey = new SecretKeySpec(key, algorithm);
        mac.init(secretKey);
        return mac;
    }

}
