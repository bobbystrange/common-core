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
            byte[] output = CipherAlgorithm.DES.encryptCbc(input, key.getBytes());
            return Base64Util.encodeAsString(output);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decryptDES(String input, String key) {
        return decryptDES(input.getBytes(), key);
    }

    public static String decryptDES(byte[] input, String key) {
        try {
            byte[] output = CipherAlgorithm.DES.encryptCbc(input, key.getBytes());
            return Base64Util.encodeAsString(output);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
