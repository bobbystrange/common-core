package org.dreamcat.common.crypto;

import java.security.NoSuchAlgorithmException;

/**
 * Create by tuke on 2019-02-19
 */
public class SignUtil {

    public static String md5Hex(String input) {
        return md5Hex(input.getBytes());
    }

    /**
     * @param input bytes
     * @return ascii string
     */
    public static String md5Hex(byte[] input) {
        try {
            return MessageDigestEnum.MD5.digestToHex(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5Base64(String input) {
        return md5Base64(input.getBytes());
    }

    public static String md5Base64(byte[] input) {
        try {
            return MessageDigestEnum.MD5.digestToBase64(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String hmacsha1Base64(String input, String key) {
        return hmacsha1Base64(input.getBytes(), key);
    }

    public static String hmacsha1Base64(byte[] input, String key) {
        try {
            return HmacEnum.HMAC_SHA_1.digestToBase64(input, key.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hmacsha256Base64(String input, String key) {
        return hmacsha256Base64(input.getBytes(), key);
    }

    public static String hmacsha256Base64(byte[] input, String key) {
        try {
            return HmacEnum.HMAC_SHA_256.digestToBase64(input, key.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
