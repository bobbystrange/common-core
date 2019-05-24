package org.dreamcat.common.util;

/**
 * Create by tuke on 2019-02-16
 */
public class StringUtil {

    public static String repeat(char c, int length) {
        if (length <= 0) return "";

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    public static String repeat(CharSequence s, int length) {
        if (length <= 0) return "";

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(s);
        }
        return sb.toString();
    }
}
