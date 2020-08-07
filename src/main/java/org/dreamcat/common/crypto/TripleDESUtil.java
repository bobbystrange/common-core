package org.dreamcat.common.crypto;

import org.dreamcat.common.util.Base64Util;

/**
 * Create by tuke on 2020/5/13
 */
public class TripleDESUtil {

    public static String encryptAsBase64(String input, String key) throws Exception {
        byte[] output = CipherAlgorithm.TRIPLE_DES.encryptCbc(input, key);
        return Base64Util.encodeAsString(output);
    }

    public static String decryptFromBase64(String input, String key) throws Exception {
        byte[] output = CipherAlgorithm.TRIPLE_DES.decryptCbc(Base64Util.decode(input), key);
        return new String(output);
    }
}
