package org.dreamcat.common.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 * Create by tuke on 2019-02-19
 */
public class SignUtil {

    public static String md5Hex(String input) {
        return md5Hex(input.getBytes(StandardCharsets.ISO_8859_1));
    }

    public static String md5Hex(byte[] input) {
        try {
            return DigestAlgorithm.MD5.digestAsHex(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5Hex(InputStream input) {
        try {
            return DigestAlgorithm.MD5.digestAsHex(input);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5Hex(InputStream input, OutputStream output) {
        try {
            return DigestAlgorithm.MD5.digestAsHex(input, output);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5Hex(File file) throws IOException {
        try (FileInputStream input = new FileInputStream(file)) {
            return md5Hex(input);
        }
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static String md5Base64(String input) {
        return md5Base64(input.getBytes(StandardCharsets.ISO_8859_1));
    }

    public static String md5Base64(byte[] input) {
        try {
            return DigestAlgorithm.MD5.digestAsBase64(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5Base64(InputStream input) {
        try {
            return DigestAlgorithm.MD5.digestAsBase64(input);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5Base64(InputStream input, OutputStream output) {
        try {
            return DigestAlgorithm.MD5.digestAsBase64(input, output);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5Base64(File file) throws IOException {
        try (FileInputStream input = new FileInputStream(file)) {
            return md5Base64(input);
        }
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static byte[] md5(String input) {
        return md5(input.getBytes(StandardCharsets.ISO_8859_1));
    }

    public static byte[] md5(byte[] input) {
        try {
            return DigestAlgorithm.MD5.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] md5(InputStream input) {
        try {
            return DigestAlgorithm.MD5.digest(input);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] md5(InputStream input, OutputStream output) {
        try {
            return DigestAlgorithm.MD5.digest(input, output);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] md5(File file) throws IOException {
        try (FileInputStream input = new FileInputStream(file)) {
            return md5(input);
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static String hs1Hex(String input, String key) {
        return hs1Hex(input.getBytes(StandardCharsets.ISO_8859_1), key);
    }

    public static String hs1Hex(byte[] input, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_1.digestAsHex(input, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hs1Hex(InputStream input, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_1.digestAsHex(input, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hs1Hex(InputStream input, OutputStream output, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_1.digestAsHex(input, output, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hs1Hex(File file, String key) throws IOException {
        try (FileInputStream input = new FileInputStream(file)) {
            return hs1Hex(input, key);
        }
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static String hs1Base64(String input, String key) {
        return hs1Base64(input.getBytes(StandardCharsets.ISO_8859_1), key);
    }

    public static String hs1Base64(byte[] input, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_1.digestAsBase64(input, key.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hs1Base64(InputStream input, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_1.digestAsBase64(input, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hs1Base64(InputStream input, OutputStream output, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_1.digestAsBase64(input, output, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static byte[] hs1(String input, String key) {
        return hs1(input.getBytes(StandardCharsets.ISO_8859_1), key);
    }

    public static byte[] hs1(byte[] input, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_1.digest(input, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] hs1(InputStream input, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_1.digest(input, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] hs1(InputStream input, OutputStream output, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_1.digest(input, output, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] hs1(File file, String key) throws IOException {
        try (FileInputStream input = new FileInputStream(file)) {
            return hs1(input, key);
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static String hs256Hex(String input, String key) {
        return hs256Hex(input.getBytes(StandardCharsets.ISO_8859_1), key);
    }

    public static String hs256Hex(byte[] input, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_256.digestAsHex(input, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hs256Hex(InputStream input, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_256.digestAsHex(input, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hs256Hex(InputStream input, OutputStream output, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_256.digestAsHex(input, output, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hs256Hex(File file, String key) throws IOException {
        try (FileInputStream input = new FileInputStream(file)) {
            return hs256Hex(input, key);
        }
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static String hs256Base64(String input, String key) {
        return hs256Base64(input.getBytes(StandardCharsets.ISO_8859_1), key);
    }

    public static String hs256Base64(byte[] input, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_256.digestAsBase64(input, key.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hs256Base64(InputStream input, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_256.digestAsBase64(input, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hs256Base64(InputStream input, OutputStream output, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_256.digestAsBase64(input, output, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static byte[] hs256(String input, String key) {
        return hs256(input.getBytes(StandardCharsets.ISO_8859_1), key);
    }

    public static byte[] hs256(byte[] input, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_256.digest(input, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] hs256(InputStream input, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_256.digest(input, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] hs256(InputStream input, OutputStream output, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_256.digest(input, output, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] hs256(File file, String key) throws IOException {
        try (FileInputStream input = new FileInputStream(file)) {
            return hs256(input, key);
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static String hs512Hex(String input, String key) {
        return hs512Hex(input.getBytes(StandardCharsets.ISO_8859_1), key);
    }

    public static String hs512Hex(byte[] input, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_512.digestAsHex(input, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hs512Hex(InputStream input, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_512.digestAsHex(input, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hs512Hex(InputStream input, OutputStream output, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_512.digestAsHex(input, output, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hs512Hex(File file, String key) throws IOException {
        try (FileInputStream input = new FileInputStream(file)) {
            return hs512Hex(input, key);
        }
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static String hs512Base64(String input, String key) {
        return hs512Base64(input.getBytes(StandardCharsets.ISO_8859_1), key);
    }

    public static String hs512Base64(byte[] input, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_512.digestAsBase64(input, key.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hs512Base64(InputStream input, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_512.digestAsBase64(input, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hs512Base64(InputStream input, OutputStream output, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_512.digestAsBase64(input, output, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static byte[] hs512(String input, String key) {
        return hs512(input.getBytes(StandardCharsets.ISO_8859_1), key);
    }

    public static byte[] hs512(byte[] input, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_512.digest(input, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] hs512(InputStream input, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_512.digest(input, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] hs512(InputStream input, OutputStream output, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_512.digest(input, output, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] hs512(File file, String key) throws IOException {
        try (FileInputStream input = new FileInputStream(file)) {
            return hs512(input, key);
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static String hm5Hex(String input, String key) {
        return hm5Hex(input.getBytes(StandardCharsets.ISO_8859_1), key);
    }

    public static String hm5Hex(byte[] input, String key) {
        try {
            return HmacAlgorithm.HMAC_MD5.digestAsHex(input, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hm5Hex(InputStream input, String key) {
        try {
            return HmacAlgorithm.HMAC_MD5.digestAsHex(input, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hm5Hex(InputStream input, OutputStream output, String key) {
        try {
            return HmacAlgorithm.HMAC_MD5.digestAsHex(input, output, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hm5Hex(File file, String key) throws IOException {
        try (FileInputStream input = new FileInputStream(file)) {
            return hm5Hex(input, key);
        }
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static String hm5Base64(String input, String key) {
        return hm5Base64(input.getBytes(StandardCharsets.ISO_8859_1), key);
    }

    public static String hm5Base64(byte[] input, String key) {
        try {
            return HmacAlgorithm.HMAC_MD5.digestAsBase64(input, key.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hm5Base64(InputStream input, String key) {
        try {
            return HmacAlgorithm.HMAC_MD5.digestAsBase64(input, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hm5Base64(InputStream input, OutputStream output, String key) {
        try {
            return HmacAlgorithm.HMAC_MD5.digestAsBase64(input, output, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static byte[] hm5(String input, String key) {
        return hm5(input.getBytes(StandardCharsets.ISO_8859_1), key);
    }

    public static byte[] hm5(byte[] input, String key) {
        try {
            return HmacAlgorithm.HMAC_MD5.digest(input, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] hm5(InputStream input, String key) {
        try {
            return HmacAlgorithm.HMAC_MD5.digest(input, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] hm5(InputStream input, OutputStream output, String key) {
        try {
            return HmacAlgorithm.HMAC_MD5.digest(input, output, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] hm5(File file, String key) throws IOException {
        try (FileInputStream input = new FileInputStream(file)) {
            return hs512(input, key);
        }
    }

}
