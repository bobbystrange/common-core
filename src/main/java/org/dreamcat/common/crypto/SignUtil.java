package org.dreamcat.common.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

/**
 * Create by tuke on 2019-02-19
 */
public class SignUtil {

    public static String md5Hex(String input) {
        return md5Hex(input.getBytes());
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
        return md5Base64(input.getBytes());
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

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static String hs1Base64(String input, String key) {
        return hs1Base64(input.getBytes(), key);
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

    public static String hs256Base64(String input, String key) {
        return hs256Base64(input.getBytes(), key);
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

    public static String hs512Base64(String input, String key) {
        return hs512Base64(input.getBytes(), key);
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

    public static String hm5Base64(String input, String key) {
        return hm5Base64(input.getBytes(), key);
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
            return HmacAlgorithm.HMAC_SHA_512.digestAsBase64(input, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hm5Base64(InputStream input, OutputStream output, String key) {
        try {
            return HmacAlgorithm.HMAC_SHA_512.digestAsBase64(input, output, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
