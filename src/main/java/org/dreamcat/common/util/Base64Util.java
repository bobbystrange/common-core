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
        return ENCODER.encode(input);
    }

    public static byte[] encode(String input) {
        return encode(input, DEFAULT_CHARSET);
    }

    public static byte[] encode(String input, Charset charset) {
        return encode(input.getBytes(charset));
    }

    public static String encodeAsString(byte[] input) {
        return ENCODER.encodeToString(input);
    }

    public static String encodeAsString(String input) {
        return encodeAsString(input, DEFAULT_CHARSET);
    }

    public static String encodeAsString(String input, Charset charset) {
        return encodeAsString(input.getBytes(charset));
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

    public static String decodeAsString(String input, Charset sourceCharset, Charset targetCharset) {
        return new String(decode(input, sourceCharset), targetCharset);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static byte[] encodeUrlSafe(byte[] input) {
        return URL_ENCODER.encode(input);
    }

    public static byte[] encodeUrlSafe(String input) {
        return encodeUrlSafe(input, DEFAULT_CHARSET);
    }

    public static byte[] encodeUrlSafe(String input, Charset charset) {
        return encodeUrlSafe(input.getBytes(charset));
    }

    public static String encodeUrlSafeAsString(byte[] input) {
        return URL_ENCODER.encodeToString(input);
    }

    public static String encodeUrlSafeAsString(String input) {
        return encodeUrlSafeAsString(input, DEFAULT_CHARSET);
    }

    public static String encodeUrlSafeAsString(String input, Charset charset) {
        return encodeUrlSafeAsString(input.getBytes(charset));
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

    public static String decodeUrlSafeAsString(String input, Charset sourceCharset, Charset targetCharset) {
        return new String(decodeUrlSafe(input, sourceCharset), targetCharset);
    }

}
