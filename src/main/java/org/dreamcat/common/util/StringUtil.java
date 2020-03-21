package org.dreamcat.common.util;

import lombok.extern.slf4j.Slf4j;

/**
 * Create by tuke on 2019-02-16
 */
@Slf4j
public class StringUtil {

    public static String unbackslash(String string) {
        return unbackslash(string, '\\');
    }

    public static String unbackslash(String string, char backslash) {
        if (ObjectUtil.isEmpty(string)) return "";
        int len = string.length();
        StringBuilder sb = new StringBuilder(string.length());
        for (int i = 0; i < len; i++) {
            if (string.charAt(i) == backslash) {
                if (i == len - 1) {
                    log.warn("Found the unmatched backslash in the end of your string");
                    sb.append(string.charAt(i));
                    break;
                } else {
                    sb.append(string.charAt(++i));
                    continue;
                }
            }
            sb.append(string.charAt(i));
        }
        return sb.toString();
    }

    public static String capitalize(String s) {
        if (ObjectUtil.isBlank(s)) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

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

    public static char[] repeatArray(char c, int length) {
        if (length <= 0) return new char[0];

        char[] result = new char[length];
        for (int i = 0; i < length; i++) {
            result[i] = c;
        }
        return result;
    }

    public static String[] repeatArray(String s, int length) {
        if (length <= 0) return new String[0];
        String[] result = new String[length];
        for (int i = 0; i < length; i++) {
            result[i] = s;
        }
        return result;
    }

    public static String repeatJoin(char c, int length, CharSequence joining) {
        return String.join(joining, repeatArray(String.valueOf(c), length));
    }

    public static String repeatJoin(String s, int length, CharSequence joining) {
        return String.join(joining, repeatArray(s, length));
    }

}
