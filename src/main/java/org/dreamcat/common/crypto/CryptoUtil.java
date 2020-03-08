package org.dreamcat.common.crypto;

import org.dreamcat.common.util.Base64Util;

/**
 * Create by tuke on 2019-02-19
 */
public class CryptoUtil {

    public static String encryptDES(String input, String key) {
        return encryptDES(input.getBytes(), key);
    }

    public static String encryptDES(byte[] input, String key) {
        try {
            byte[] output = CipherEnum.DES.encryptCbc(input, key.getBytes());
            return Base64Util.encodeToString(output);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decryptDES(String input, String key) {
        return decryptDES(input.getBytes(), key);
    }

    public static String decryptDES(byte[] input, String key) {
        try {
            byte[] output = CipherEnum.DES.encryptCbc(input, key.getBytes());
            return Base64Util.encodeToString(output);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String digestHMACMD5(String data, String key) {
        return digestHMACMD5(data.getBytes(), key);
    }

    public static String digestHMACMD5(byte[] data, String key) {
        try {
            return HmacEnum.HMAC_MD5.digestToBase64(data, key.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String digestHMACSHA256(String data, String key) {
        return digestHMACSHA256(data.getBytes(), key);
    }

    public static String digestHMACSHA256(byte[] data, String key) {
        try {
            return HmacEnum.HMAC_SHA_256.digestToBase64(data, key.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String digestHMACSHA512(String data, String key) {
        return digestHMACSHA512(data.getBytes(), key);
    }

    public static String digestHMACSHA512(byte[] data, String key) {
        try {
            return HmacEnum.HMAC_SHA_512.digestToBase64(data, key.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
