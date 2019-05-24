package org.dreamcat.common.util;

import org.dreamcat.common.crypto.CipherEnum;

/**
 * Create by tuke on 2019-03-31
 */
public class DESUtil {

    public static String encryptToBase64(String input, String key) throws Exception {
        byte[] output = CipherEnum.DES.encryptCbc(input.getBytes(), key.getBytes());
        return Base64Util.encodeToString(output);
    }

    public static String decryptFromBase64(String input, String key) throws Exception {
        byte[] output = CipherEnum.DES.decryptCbc(Base64Util.decode(input), key.getBytes());
        return new String(output);
    }
}
