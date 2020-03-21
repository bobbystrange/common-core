package org.dreamcat.common.crypto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Create by tuke on 2019-03-30
 */
public class MD5Util {

    public static String md5Hex(String input) {
        return SignUtil.md5Hex(input);
    }

    public static String md5Hex(byte[] input) {
        return SignUtil.md5Hex(input);
    }

    public static String md5Hex(InputStream input) {
        return SignUtil.md5Hex(input);
    }

    public static String md5Hex(InputStream input, OutputStream output) {
        return SignUtil.md5Hex(input, output);
    }

    public static String md5Hex(File file) throws IOException {
        return SignUtil.md5Hex(file);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static String md5Base64(String input) {
        return SignUtil.md5Base64(input);
    }

    public static String md5Base64(byte[] input) {
        return SignUtil.md5Base64(input);
    }

    public static String md5Base64(InputStream input) {
        return SignUtil.md5Base64(input);
    }

    public static String md5Base64(InputStream input, OutputStream output) {
        return SignUtil.md5Base64(input, output);
    }

    public static String md5Base64(File file) throws IOException {
        return SignUtil.md5Base64(file);
    }
}
