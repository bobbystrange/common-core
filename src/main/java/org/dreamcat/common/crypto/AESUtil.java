package org.dreamcat.common.crypto;

import org.dreamcat.common.util.Base64Util;

/**
 * Create by tuke on 2020/5/13
 */
public class AESUtil {

    public static String encryptAsBase64(String input, String key) throws Exception {
        byte[] output = CipherAlgorithm.AES.encryptCbc(input, key);
        return Base64Util.encodeAsString(output);
    }

    public static String decryptFromBase64(String input, String key) throws Exception {
        byte[] output = CipherAlgorithm.AES.decryptCbc(Base64Util.decode(input), key);
        return new String(output);
    }
}
