package org.dreamcat.common.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Create by tuke on 2019-02-15
 */
public class Base64Util {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.ISO_8859_1;

    private static final Base64.Encoder ENCODER = Base64.getEncoder();
    private static final Base64.Decoder DECODER = Base64.getDecoder();
    private static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder();
    private static final Base64.Decoder URL_DECODER = Base64.getUrlDecoder();

    public static byte[] encode(byte[] input) {
        return encode(input, false);
    }

    public static byte[] encode(byte[] input, boolean trimEnd) {
        byte[] bytes = ENCODER.encode(input);
        return trimEnd ? ArrayUtil.trimEnd(bytes, (byte) '=') : bytes;
    }

    public static byte[] encode(String input) {
        return encode(input, false);
    }

    public static byte[] encode(String input, boolean trimEnd) {
        byte[] bytes = encode(input, DEFAULT_CHARSET);
        return trimEnd ? ArrayUtil.trimEnd(bytes, (byte) '=') : bytes;
    }

    public static byte[] encode(String input, Charset charset) {
        return encode(input, charset, false);
    }

    public static byte[] encode(String input, Charset charset, boolean trimEnd) {
        byte[] bytes = encode(input.getBytes(charset));
        return trimEnd ? ArrayUtil.trimEnd(bytes, (byte) '=') : bytes;
    }

    public static String encodeAsString(byte[] input) {
        return encodeAsString(input, false);
    }

    public static String encodeAsString(byte[] input, boolean trimEnd) {
        String s = ENCODER.encodeToString(input);
        return trimEnd ? StringUtil.trimEnd(s, '=') : s;
    }

    public static String encodeAsString(String input) {
        return encodeAsString(input, false);
    }

    public static String encodeAsString(String input, boolean trimEnd) {
        return encodeAsString(input, DEFAULT_CHARSET, trimEnd);
    }

    public static String encodeAsString(String input, Charset charset) {
        return encodeAsString(input, charset, false);
    }

    public static String encodeAsString(String input, Charset charset, boolean trimEnd) {
        return encodeAsString(input.getBytes(charset), trimEnd);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static byte[] decode(byte[] input) {
        return DECODER.decode(input);
    }

    public static byte[] decode(String input) {
        return decode(input, DEFAULT_CHARSET);
    }

    public static byte[] decode(String input, Charset charset) {
        return decode(input.getBytes(charset));
    }

    public static String decodeAsString(byte[] input) {
        return decodeAsString(input, DEFAULT_CHARSET);
    }

    public static String decodeAsString(byte[] input, Charset charset) {
        return new String(decode(input), charset);
    }

    public static String decodeAsString(String input) {
        return decodeAsString(input, DEFAULT_CHARSET, DEFAULT_CHARSET);
    }

    public static String decodeAsString(String input, Charset sourceCharset,
            Charset targetCharset) {
        return new String(decode(input, sourceCharset), targetCharset);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static byte[] encodeUrlSafe(byte[] input) {
        return encodeUrlSafe(input, false);
    }

    public static byte[] encodeUrlSafe(byte[] input, boolean trimEnd) {
        byte[] bytes = URL_ENCODER.encode(input);
        return trimEnd ? ArrayUtil.trimEnd(bytes, (byte) '=') : bytes;
    }

    public static byte[] encodeUrlSafe(String input) {
        return encodeUrlSafe(input, false);
    }

    public static byte[] encodeUrlSafe(String input, boolean trimEnd) {
        byte[] bytes = encodeUrlSafe(input, DEFAULT_CHARSET);
        return trimEnd ? ArrayUtil.trimEnd(bytes, (byte) '=') : bytes;
    }

    public static byte[] encodeUrlSafe(String input, Charset charset) {
        return encodeUrlSafe(input, charset, false);
    }

    public static byte[] encodeUrlSafe(String input, Charset charset, boolean trimEnd) {
        byte[] bytes = encodeUrlSafe(input.getBytes(charset));
        return trimEnd ? ArrayUtil.trimEnd(bytes, (byte) '=') : bytes;
    }

    public static String encodeUrlSafeAsString(byte[] input) {
        return encodeUrlSafeAsString(input, false);
    }

    public static String encodeUrlSafeAsString(byte[] input, boolean trimEnd) {
        String s = URL_ENCODER.encodeToString(input);
        return trimEnd ? StringUtil.trimEnd(s, '=') : s;
    }

    public static String encodeUrlSafeAsString(String input) {
        return encodeUrlSafeAsString(input, false);
    }

    public static String encodeUrlSafeAsString(String input, boolean trimEnd) {
        return encodeUrlSafeAsString(input, DEFAULT_CHARSET, trimEnd);
    }

    public static String encodeUrlSafeAsString(String input, Charset charset) {
        return encodeUrlSafeAsString(input, charset, false);
    }

    public static String encodeUrlSafeAsString(String input, Charset charset, boolean trimEnd) {
        return encodeUrlSafeAsString(input.getBytes(charset), trimEnd);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static byte[] decodeUrlSafe(byte[] input) {
        return URL_DECODER.decode(input);
    }

    public static byte[] decodeUrlSafe(String input) {
        return decodeUrlSafe(input, DEFAULT_CHARSET);
    }

    public static byte[] decodeUrlSafe(String input, Charset charset) {
        return decodeUrlSafe(input.getBytes(charset));
    }

    public static String decodeUrlSafeAsString(byte[] input) {
        return decodeUrlSafeAsString(input, DEFAULT_CHARSET);
    }

    public static String decodeUrlSafeAsString(byte[] input, Charset charset) {
        return new String(decodeUrlSafe(input), charset);
    }

    public static String decodeUrlSafeAsString(String input) {
        return decodeUrlSafeAsString(input, DEFAULT_CHARSET, DEFAULT_CHARSET);
    }

    public static String decodeUrlSafeAsString(String input, Charset sourceCharset,
            Charset targetCharset) {
        return new String(decodeUrlSafe(input, sourceCharset), targetCharset);
    }

}
