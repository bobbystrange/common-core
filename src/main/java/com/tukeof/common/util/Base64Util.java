package com.tukeof.common.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Create by tuke on 2019-02-15
 */
public class Base64Util {
    private static final Base64.Encoder Base64_ENCODER = Base64.getEncoder();
    private static final Base64.Decoder Base64_DECODER = Base64.getDecoder();

    public static byte[] encode(byte[] input) {
        return Base64_ENCODER.encode(input);
    }

    public static byte[] encode(String input) {
        return encode(input.getBytes(StandardCharsets.ISO_8859_1));
    }

    public static String encodeToString(byte[] input) {
        return new String(encode(input), StandardCharsets.ISO_8859_1);
    }

    public static String encodeToString(String input) {
        return new String(encode(input), StandardCharsets.ISO_8859_1);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    /**
     * @param input any bytes
     * @return ascii bytes
     * @see Base64.Decoder#decode(String)
     */
    public static byte[] decode(byte[] input) {
        return Base64_DECODER.decode(input);
    }

    public static byte[] decode(String input) {
        return decode(input.getBytes(StandardCharsets.ISO_8859_1));
    }

    public static String decodeToString(byte[] input) {
        return new String(decode(input), StandardCharsets.ISO_8859_1);
    }

    public static String decodeToString(String input) {
        return new String(decode(input), StandardCharsets.ISO_8859_1);
    }

}
