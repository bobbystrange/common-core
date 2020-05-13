package org.dreamcat.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by tuke on 2019-02-16
 */
@Slf4j
public class StringUtil {

    private static final Pattern INT_PATTERN = Pattern.compile(
            "[+-]?[0-9]+");
    private static final Pattern FLOAT_PATTERN = Pattern.compile(
            "[+-]?[0-9]*[.]?[0-9]*([eE][+-]?[0-9]+)?");
    private static final Pattern NUMBER_PATTERN = Pattern.compile(
            "[+-]?[0-9]*[.]?[0-9]*([eE][+-]?[0-9]+)?|[+-]?[0-9]+");
    private static final Pattern BOOL_PATTERN = Pattern.compile(
            "true|false", Pattern.CASE_INSENSITIVE);

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static String unbackslash(String s) {
        return unbackslash(s, '\\');
    }

    public static String unbackslash(String s, char backslash) {
        if (ObjectUtil.isEmpty(s)) return "";
        int len = s.length();
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < len; i++) {
            if (s.charAt(i) == backslash) {
                if (i == len - 1) {
                    log.warn("Found the unmatched backslash in the end of your string");
                    sb.append(s.charAt(i));
                    break;
                } else {
                    sb.append(s.charAt(++i));
                    continue;
                }
            }
            sb.append(s.charAt(i));
        }
        return sb.toString();
    }

    public static String capitalize(String s) {
        if (ObjectUtil.isBlank(s)) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    // never throws ArrayIndexOutOfBoundsException
    public static String substring(String s, int beginIndex, int endIndex) {
        if (s == null) return null;
        return s.substring(Math.max(beginIndex, 0), Math.min(endIndex, s.length()));
    }

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

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

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

    public static boolean isInt(CharSequence n) {
        return INT_PATTERN.matcher(n).matches();
    }

    public static boolean isFloat(CharSequence n) {
        return FLOAT_PATTERN.matcher(n).matches();
    }

    public static boolean isNumber(CharSequence n) {
        return isInt(n) || isFloat(n);
    }

    public static boolean isBool(CharSequence n) {
        return BOOL_PATTERN.matcher(n).matches();
    }

    public static String extractInt(CharSequence s, int offset) {
        return extractPattern(s, offset, INT_PATTERN);
    }

    public static String extractFloat(CharSequence s, int offset) {
        return extractPattern(s, offset, FLOAT_PATTERN);
    }

    public static String extractNumber(CharSequence s, int offset) {
        return extractPattern(s, offset, NUMBER_PATTERN);
    }

    public static String extractBool(CharSequence s, int offset) {
        return extractPattern(s, offset, BOOL_PATTERN);
    }

    public static String extractPattern(CharSequence s, int offset, Pattern pattern) {
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            int start = matcher.start();
            if (start == offset) {
                return matcher.group();
            } else if (start > offset) {
                break;
            }
        }
        return null;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    /**
     * @param s      "... \" ..."
     * @param offset begin index to search
     * @return index of matched quote
     */
    public static int searchMatchedQuote(String s, int offset) {
        // search matched quote
        int secondQuote = offset;
        do {
            secondQuote = s.indexOf("\"", secondQuote);
            // not found
            if (secondQuote == -1) return -1;
        } while (s.charAt(secondQuote - 1) == '\\');
        return secondQuote;
    }
}
