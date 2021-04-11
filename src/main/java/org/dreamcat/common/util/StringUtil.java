package org.dreamcat.common.util;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * Create by tuke on 2019-02-16
 */
@Slf4j
public final class StringUtil {

    private StringUtil() {
    }

    /**
     * repeat a string and fill gaps
     *
     * @param s    string to repeat
     * @param gap  gap to fill
     * @param size times of repetition for s
     * @return {s}{filler}{s}{filler}{s}
     */
    public static String interval(String s, String gap, int size) {
        StringBuilder sb = new StringBuilder(s.length() * size + gap.length() * (size - 1));
        for (int i = 0; i < size - 1; i++) sb.append(s).append(gap);
        sb.append(s);
        return sb.toString();
    }

    public static String repeat(char c, int length) {
        if (length <= 0) return "";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) sb.append(c);
        return sb.toString();
    }

    public static String repeat(CharSequence s, int length) {
        if (length <= 0) return "";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) sb.append(s);
        return sb.toString();
    }

    public static char[] repeatArray(char c, int length) {
        if (length <= 0) return new char[0];

        char[] result = new char[length];
        for (int i = 0; i < length; i++) result[i] = c;
        return result;
    }

    public static String[] repeatArray(String s, int length) {
        if (length <= 0) return new String[0];
        String[] result = new String[length];
        for (int i = 0; i < length; i++) result[i] = s;
        return result;
    }

    public static String repeatJoin(char c, int length, CharSequence joining) {
        return String.join(joining, repeatArray(String.valueOf(c), length));
    }

    public static String repeatJoin(String s, int length, CharSequence joining) {
        return String.join(joining, repeatArray(s, length));
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    /**
     * never throws ArrayIndexOutOfBoundsException
     *
     * @param s          string to extract
     * @param beginIndex begin index
     * @param endIndex   end index exclusive
     * @return extracted string
     */
    public static String substring(String s, int beginIndex, int endIndex) {
        if (s == null) return null;
        return s.substring(Math.max(beginIndex, 0), Math.min(endIndex, s.length()));
    }


    public static String trimEnd(String s) {
        return trimEnd(s, " \t\r\n");
    }

    public static String trimEnd(String s, char c) {
        int offset = 0;
        int size = s.length();
        for (int i = size - 1; i >= 0; i--) {
            if (s.charAt(i) != c) break;
            offset++;
        }
        if (offset == 0) return s;
        return s.substring(0, size - offset);
    }

    public static String trimEnd(String s, String chars) {
        int offset = 0;
        int size = s.length();
        for (int i = size - 1; i >= 0; i--) {
            if (chars.indexOf(s.charAt(i)) == -1) break;
            offset++;
        }
        if (offset == 0) return s;
        return s.substring(0, size - offset);
    }

    /**
     * just return null, not the 'null' literal when o in null
     *
     * @param o object
     * @return a string representation of the object
     */
    public static String string(Object o) {
        return o == null ? null : o.toString();
    }

    public static String padding(String s, int width, char c) {
        int size = s.length();
        if (size == width) return s;
        if (size > width) return s.substring(size - width);
        StringBuilder sb = new StringBuilder(width);
        for (int i = 0; i < width - size; i++) {
            sb.append(c);
        }
        return sb.append(s).toString();
    }

    public static String paddingRight(String s, int width, char c) {
        int size = s.length();
        if (size == width) return s;
        if (size > width) return s.substring(0, width);
        StringBuilder sb = new StringBuilder(width).append(s);
        for (int i = 0; i < width - size; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    // only upper case first letter, and keep others
    public static String toCapitalCase(String s) {
        if (ObjectUtil.isBlank(s)) return s;
        char firstChar = s.charAt(0);
        if (firstChar >= 'a' && firstChar <= 'z') {
            int size = s.length();
            StringBuilder sb = new StringBuilder(size);
            sb.append((char) (firstChar - 32)).append(s, 1, size);
            return sb.toString();
        }
        return s;
    }

    public static String toCapitalLowerCase(String s) {
        if (ObjectUtil.isBlank(s)) return s;
        char firstChar = s.charAt(0);
        if (firstChar >= 'A' && firstChar <= 'Z') {
            int size = s.length();
            StringBuilder sb = new StringBuilder(size);
            sb.append((char) (firstChar + 32)).append(s, 1, size);
            return sb.toString();
        }
        return s;
    }

    public static String toCamelCase(String snake) {
        if (snake == null) return null;
        int len = snake.length();
        StringBuilder s = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = snake.charAt(i);
            if (c == '_' && i < len - 1 && i > 0 &&
                    snake.charAt(i - 1) >= 'a' && snake.charAt(i - 1) <= 'z') {
                char nextChar = snake.charAt(i + 1);
                if (nextChar >= 'a' && nextChar <= 'z') {
                    s.append((char) (nextChar - 32));
                    i++;
                    continue;
                }
            }
            s.append(c);
        }
        return s.toString();
    }

    public static String toCapitalCamelCase(String snake) {
        if (snake == null) return null;
        int len = snake.length();
        if (len == 0) return snake;
        StringBuilder s = new StringBuilder(len);
        char firstChar = snake.charAt(0);
        if (firstChar >= 'a' && firstChar <= 'z') {
            s.append((char) (firstChar - 32));
        }
        for (int i = 1; i < len; i++) {
            char c = snake.charAt(i);
            if (c == '_' && i < len - 1 && i > 0 &&
                    snake.charAt(i - 1) >= 'a' && snake.charAt(i - 1) <= 'z') {
                char nextChar = snake.charAt(i + 1);
                if (nextChar >= 'a' && nextChar <= 'z') {
                    s.append((char) (nextChar - 32));
                    i++;
                    continue;
                }
            }
            s.append(c);
        }
        return s.toString();
    }

    public static String toSnakeCase(String camel) {
        if (camel == null) return null;
        int len = camel.length();
        StringBuilder s = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = camel.charAt(i);
            if (c >= 'A' && c <= 'Z' && i > 0 &&
                    camel.charAt(i - 1) >= 'a' && camel.charAt(i - 1) <= 'z') {
                s.append('_').append((char) (c + 32));
                continue;
            }
            s.append(c);
        }
        return s.toString();
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    // \a\b\c  --> abc
    public static String fromBackslash(String s) {
        return fromBackslash(s, '\\');
    }

    public static String fromBackslash(String s, char backslash) {
        if (ObjectUtil.isEmpty(s)) return "";
        int len = s.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            if (s.charAt(i) == backslash) {
                if (i == len - 1) {
                    log.warn("found the unmatched backslash in the end of your string");
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

    public static String toBackslash(String s) {
        return toBackslash(s, '\\');
    }

    public static String toBackslash(String s, char backslash) {
        if (ObjectUtil.isEmpty(s)) return "";
        int len = s.length();
        StringBuilder sb = new StringBuilder(len * 2);
        s.chars().forEach(c -> sb.append(backslash).append((char) c));
        return sb.toString();
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    /**
     * only a-zA-Z0-9_ is considered, 97-122, 65-90, 48-57, 95
     *
     * @param c variable char
     * @return valid character if {@code true}
     */
    public static boolean isVariableChar(char c) {
        return isFirstVariableChar(c) ||
                (c >= '0' && c <= '9');
    }

    public static boolean isFirstVariableChar(char c) {
        return c == '_' || (c >= 'A' && c <= 'Z') ||
                (c >= 'a' && c <= 'z');
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private static final Map<Integer, Map<Integer, BigInteger>> MAPPING_TO_CACHE = new ConcurrentHashMap<>();

    private static final char[] toWord62 = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    private static final char[] toBase64 = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    };

    private static final char[] toBase64URL = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'
    };

    public static String mappingTo62(BigInteger n, int width) {
        return mappingTo(n, width, toWord62);
    }

    public static String mappingToBase64(BigInteger n, int width) {
        return mappingTo(n, width, toBase64);
    }

    public static String mappingToBase64URL(BigInteger n, int width) {
        return mappingTo(n, width, toBase64URL);
    }

    public static String mappingTo(BigInteger n, int width, char[] letters) {
        int size = letters.length;
        BigInteger bound = MAPPING_TO_CACHE.computeIfAbsent(size, ConcurrentHashMap::new)
                .computeIfAbsent(width, it -> BigInteger.valueOf(size).pow(width));
        long rem = n.remainder(bound).longValue();
        int[] digits = NumericUtil.digit(rem, size);
        int len = digits.length;
        StringBuilder s = new StringBuilder(len);
        for (int i = len - 1; i >= 0; i--) {
            s.append(letters[digits[i]]);
        }
        return s.toString();
    }
}
