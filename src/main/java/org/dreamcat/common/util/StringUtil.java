package org.dreamcat.common.util;

import java.io.IOException;
import java.io.Reader;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.core.Pair;

/**
 * Create by tuke on 2019-02-16
 */
@Slf4j
public class StringUtil {

    // for double, 1sign + 11 exponent + 52 fraction
    // +1023.2251799813685248
    private static final int EXPECT_MAX_NUMBER_SIZE = 22;

    // {s}{filler}{s}{filler}{s}
    public static String interval(String s, String filler, int size) {
        StringBuilder sb = new StringBuilder(s.length() * size + filler.length() * (size - 1));
        for (int i = 0; i < size - 1; i++) {
            sb.append(s).append(filler);
        }
        sb.append(s);
        return sb.toString();
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

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    // never throws ArrayIndexOutOfBoundsException
    public static String substring(String s, int beginIndex, int endIndex) {
        if (s == null) return null;
        return s.substring(Math.max(beginIndex, 0), Math.min(endIndex, s.length()));
    }

    // for function only
    public static String toString(Object o) {
        return o == null ? null : o.toString();
    }

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
            if (c == '_') {
                if (i < len - 1 && i > 0 &&
                        snake.charAt(i - 1) >= 'a' && snake.charAt(i - 1) <= 'z') {
                    char nextChar = snake.charAt(i + 1);
                    if (nextChar >= 'a' && nextChar <= 'z') {
                        s.append((char) (nextChar - 32));
                        i++;
                        continue;
                    }
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
            if (c == '_') {
                if (i < len - 1 && i > 0 &&
                        snake.charAt(i - 1) >= 'a' && snake.charAt(i - 1) <= 'z') {
                    char nextChar = snake.charAt(i + 1);
                    if (nextChar >= 'a' && nextChar <= 'z') {
                        s.append((char) (nextChar - 32));
                        i++;
                        continue;
                    }
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
        s.chars().forEach(c -> {
            sb.append(backslash).append((char) c);
        });
        return sb.toString();
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static String trimEnd(String s) {
        return trimEnd(s, ' ');
    }

    public static String trimEnd(String s, char c) {
        int offset = 0, size = s.length();
        for (int i = size - 1; i >= 0; i--) {
            if (s.charAt(i) != c) break;
            offset++;
        }
        if (offset == 0) return s;
        return s.substring(0, size - offset);
    }

    public static String trimEnd(String s, String chars) {
        int offset = 0, size = s.length();
        for (int i = size - 1; i >= 0; i--) {
            if (chars.indexOf(s.charAt(i)) == -1) break;
            offset++;
        }
        if (offset == 0) return s;
        return s.substring(0, size - offset);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static Pair<Number, Integer> extractNumber(String s) {
        return extractNumber(s, 0);
    }

    /**
     * try extract a number at the offset
     * int/long     [+-]?[0-9]+
     * double       [+-]?[0-9]*[.]?[0-9]*([eE][+-]?[0-9]+)?
     *
     * @param s      string
     * @param offset offset in string
     * @return number+offset
     */
    public static Pair<Number, Integer> extractNumber(String s, int offset) {
        boolean dot = false;
        boolean permitDot = true;
        boolean hasE = false;
        boolean hasESign = false;
        boolean hasPow = false;
        boolean expectPow = false;
        boolean expectESignOrPow = false;
        boolean permitSign = true;
        int i;
        int len = s.length();

        for (i = offset; i < len; i++) {
            char c = s.charAt(i);

            if (c == '-' || c == '+') {
                if (!permitSign) break;
                permitSign = false;
                if (hasE) {
                    hasESign = true;
                    expectPow = true;
                    expectESignOrPow = false;
                }
                continue;
            }

            if (c >= '0' && c <= '9') {
                if (hasE) {
                    hasPow = true;
                    expectPow = false;
                    expectESignOrPow = false;
                }
                permitSign = false;
            } else if (c == '.') {
                if (!permitDot) {
                    if (hasPow) break;
                    // go back
                    //if (expectPow) i ++;
                    if (hasESign) i--;
                    if (expectPow) i--;
                    expectPow = false;
                    break;
                }

                dot = true;
                permitDot = false;
            } else if (c == 'e' || c == 'E') {
                if (!hasE) {
                    hasE = true;
                    permitSign = true;
                    permitDot = false;
                    expectESignOrPow = true;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        if ((i == offset)) return null;
        // go back
        if (expectESignOrPow) {
            i -= 1;
        } else if (expectPow) {
            i -= 2;
        }

        String num = s.substring(offset, i);
        // float case
        if (dot || hasE) {
            return Pair.of(Double.valueOf(num), i);
        } else {
            long n = Long.parseLong(num);
            if (n <= Integer.MAX_VALUE && n >= Integer.MIN_VALUE) {
                return Pair.of((int) n, i);
            } else {
                return Pair.of(n, i);
            }
        }
    }

    /**
     * try extract a number in the head
     *
     * @param reader which support mark/reset
     * @return one of int, long or double
     * @see #extractNumber(String, int)
     */
    public static Number extractNumber(Reader reader) throws IOException {
        if (!reader.markSupported()) {
            throw new IllegalArgumentException("reader is unsupported to mark/reset");
        }

        boolean dot = false;
        boolean permitDot = true;
        boolean hasE = false;
        boolean hasESign = false;
        boolean hasPow = false;
        boolean expectPow = false;
        boolean expectESignOrPow = false;
        boolean permitSign = true;

        int i = 0;
        int c;
        reader.mark(EXPECT_MAX_NUMBER_SIZE);
        for (; ; i++) {
            c = reader.read();
            if (c == -1) break;
            if (c == '-' || c == '+') {
                if (!permitSign) break;
                permitSign = false;
                if (hasE) {
                    hasESign = true;
                    expectPow = true;
                    expectESignOrPow = false;
                }
                continue;
            }

            if (c >= '0' && c <= '9') {
                if (hasE) {
                    hasPow = true;
                    expectPow = false;
                    expectESignOrPow = false;
                }
                permitSign = false;
            } else if (c == '.') {
                if (!permitDot) {
                    if (hasPow) break;
                    ;
                    // go back
                    //if (expectPow) i ++;
                    if (hasESign) i--;
                    if (expectPow) i--;
                    expectPow = false;
                    break;
                }

                dot = true;
                permitDot = false;
            } else if (c == 'e' || c == 'E') {
                if (!hasE) {
                    hasE = true;
                    permitSign = true;
                    permitDot = false;
                    expectESignOrPow = true;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        reader.reset();

        // no input
        if ((i == 0)) {
            return null;
        }

        // go back
        if (expectESignOrPow) {
            i -= 1;
        } else if (expectPow) {
            i -= 2;
        }
        // consume it
        char[] s = new char[i];
        if (reader.read(s) != i) {
            log.warn("Assertion failure");
        }

        String num = new String(s);
        // float case
        if (dot || hasE) {
            return Double.valueOf(num);
        } else {
            long n = Long.parseLong(num);
            if (n <= Integer.MAX_VALUE && n >= Integer.MIN_VALUE) {
                return (int) n;
            } else {
                return n;
            }
        }
    }

    public static Boolean extractBool(String s, int offset) {
        int size = s.length();
        int diff = size - offset;
        if (diff < 4) return null;
        // then size - offset >= 4
        Boolean enterTrueCase = null;
        for (int i = offset; i <= offset + 3; i++) {
            char c = s.charAt(i);
            // toLowerCase
            if (c >= 'A' && c <= 'Z') c += 32;

            if (c == ("true".charAt(i - offset))) {
                if (enterTrueCase == null) {
                    enterTrueCase = true;
                } else if (!enterTrueCase) {
                    return null;
                }
            } else if (c == ("false".charAt(i - offset))) {
                if (enterTrueCase == null) {
                    enterTrueCase = false;
                } else if (enterTrueCase) {
                    return null;
                }
            } else return null;
        }

        // never null
        if (enterTrueCase == null) throw new RuntimeException("Assertion failure");

        if (enterTrueCase) {
            return true;
        }
        if (diff < 5) return null;
        char c = s.charAt(offset + 4);
        if (c >= 'A' && c <= 'Z') c += 32;
        // "false".charAt(4)
        if (c == 'e') {
            return false;
        }
        return null;
    }

    public static Boolean extractBool(Reader reader) throws IOException {
        if (!reader.markSupported()) {
            throw new IllegalArgumentException("reader is unsupported to mark/reset");
        }

        char[] s = new char[5];
        reader.mark(5);
        int n = reader.read(s);
        reader.reset();
        if (n == -1) return null;

        Boolean enterTrueCase = null;
        for (int i = 0; i <= 3; i++) {
            char c = s[i];
            // toLowerCase
            if (c >= 'A' && c <= 'Z') c += 32;

            if (c == ("true".charAt(i))) {
                if (enterTrueCase == null) {
                    enterTrueCase = true;
                } else if (!enterTrueCase) {
                    return null;
                }
            } else if (c == ("false".charAt(i))) {
                if (enterTrueCase == null) {
                    enterTrueCase = false;
                } else if (enterTrueCase) {
                    return null;
                }
            } else return null;
        }

        // never null
        if (enterTrueCase == null) throw new RuntimeException("Assertion failure");

        if (enterTrueCase) {
            if (reader.read(s, 0, 4) != 4) {
                throw new RuntimeException("Assertion failure");
            }
            return true;
        }
        if (n < 5) return null;
        char c = s[4];
        if (c >= 'A' && c <= 'Z') c += 32;
        // "false".charAt(4)
        if (c == 'e') {
            if (reader.read(s, 0, 5) != 5) {
                throw new RuntimeException("Assertion failure");
            }
            return false;
        }
        return null;
    }

}
