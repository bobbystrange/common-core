package org.dreamcat.common.text;

import java.io.IOException;
import java.io.Reader;
import org.dreamcat.common.util.StringUtil;

/**
 * Create by tuke on 2020/11/20
 */
public final class StringSearcher {

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

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static String extractVariable(String s, int offset) {
        int size = s.length(), i;
        for (i = offset; i < size; i++) {
            char c = s.charAt(i);
            if (i == offset && !StringUtil.isFirstVariableChar(c)) return null;
            if (!StringUtil.isVariableChar(c)) break;
        }
        return s.substring(offset, i);
    }
}
