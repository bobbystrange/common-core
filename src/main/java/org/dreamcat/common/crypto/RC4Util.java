package org.dreamcat.common.crypto;

import java.nio.charset.StandardCharsets;
import org.dreamcat.common.util.Base64Util;

/**
 * Create by tuke on 2020/5/13
 */
public class RC4Util {

    public static String encryptAsBase64(String input, String key) throws Exception {
        return Base64Util.encodeAsString(cipher(input, key));
    }

    public static String decryptFromBase64(String input, String key) throws Exception {
        byte[] output = cipher(Base64Util.decode(input), key);
        return new String(output);
    }

    public static byte[] cipher(String input, String key) {
        return cipher(input.getBytes(StandardCharsets.ISO_8859_1), key);
    }

    public static byte[] cipher(byte[] input, String key) {
        return cipher(input, key.getBytes(StandardCharsets.ISO_8859_1));
    }

    public static byte[] cipher(byte[] input, byte[] key) {
        int x = 0;
        int y = 0;
        key = initKey(key);
        int xorIndex;
        byte[] result = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            x = (x + 1) & 0xff;
            y = ((key[x] & 0xff) + y) & 0xff;
            byte tmp = key[x];
            key[x] = key[y];
            key[y] = tmp;
            xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
            result[i] = (byte) (input[i] ^ key[xorIndex]);
        }
        return result;
    }

    private static byte[] initKey(byte[] key) {
        byte[] state = new byte[256];
        for (int i = 0; i < 256; i++) {
            state[i] = (byte) i;
        }

        int index1 = 0;
        int index2 = 0;
        for (int i = 0; i < 256; i++) {
            index2 = ((key[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
            byte tmp = state[i];
            state[i] = state[index2];
            state[index2] = tmp;
            index1 = (index1 + 1) % key.length;
        }
        return state;
    }

}
