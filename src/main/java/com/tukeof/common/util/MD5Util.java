package com.tukeof.common.util;

/**
 * Create by tuke on 2019-03-30
 */
public class MD5Util {

    public static String md5Hex(String input) {
        return SignatureUtil.md5Hex(input);
    }

    public static String md5Hex(byte[] input) {
        return SignatureUtil.md5Hex(input);
    }

    public static String md5Base64(String input) {
        return SignatureUtil.md5Base64(input);
    }

    public static String md5Base64(byte[] input) {
        return SignatureUtil.md5Base64(input);
    }

}
