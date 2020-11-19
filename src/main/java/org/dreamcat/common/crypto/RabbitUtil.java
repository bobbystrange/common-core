package org.dreamcat.common.crypto;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.dreamcat.common.util.Base64Util;

/**
 * Create by tuke on 2020/5/13
 * <p>
 * see also
 * http://www.cryptico.com/Files/filer/rabbit_fse.pdf
 * http://www.ietf.org/rfc/rfc4503.txt
 */
public class RabbitUtil {

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
        key = paddingTo(key, 16);

        input = padding(input, 16);
        int len = input.length;
        byte[] output = new byte[len];

        Rabbit rabbit = new Rabbit();
        rabbit.initKey(key);
        rabbit.cipher(input, output);
        return output;
    }

    static byte[] padding(byte[] input, int multiple) {
        int len = input.length;
        int rem = len % multiple;
        if (rem == 0) return input;
        return Arrays.copyOf(input, len + 16 - rem);
    }

    static byte[] paddingTo(byte[] input, int length) {
        if (input.length == length) return input;
        return Arrays.copyOf(input, length);
    }
}
