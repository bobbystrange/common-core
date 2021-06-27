package org.dreamcat.common.text;

import java.io.IOException;
import java.io.Reader;
import org.dreamcat.common.Pair;

/**
 * Create by tuke on 2019-02-16
 */
public final class NumericSearcher {

    private NumericSearcher() {
    }

    // for double, 1sign + 11 exponent + 52 fraction, such as +1023.2251799813685248
    private static final int EXPECT_MAX_NUMBER_SIZE = 22;

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
     * @return number + offset
     */
    public static Pair<Number, Integer> extractNumber(String s, int offset) {
        Pair<Integer, Boolean> pair = search(s, offset);
        if (pair == null) return null;
        int nextOffset = pair.first();
        boolean floating = pair.second();
        String num = s.substring(offset, nextOffset);
        return Pair.of(valueOf(num, floating), nextOffset);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    /**
     * try extract a number in the head
     *
     * @param reader which support mark/reset
     * @return one of int, long or double
     * @throws IOException I/O error
     * @see #extractNumber(String, int)
     */
    public static Number extractNumber(Reader reader) throws IOException {
        Pair<String, Boolean> pair = search(reader);
        if (pair == null) return null;
        String num = pair.first();
        boolean floating = pair.second();
        return valueOf(num, floating);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static Pair<String, Boolean> search(Reader reader) throws IOException {
        if (!reader.markSupported()) {
            throw new IllegalArgumentException("reader is unsupported to mark/reset");
        }

        boolean dot = false, permitDot = true;
        boolean hasE = false, hasESign = false, hasPow = false;
        boolean expectPow = false, expectESignOrPow = false, permitSign = true;

        int i = 0, c;
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
                    // go back
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
        if ((i == 0)) return null;
        // go back
        if (expectESignOrPow) {
            i -= 1;
        } else if (expectPow) {
            i -= 2;
        }
        // consume it
        char[] s = new char[i];
        if (reader.read(s) != i) {
            throw new RuntimeException("Assertion failure");
        }

        String num = new String(s);
        boolean floating = dot || hasE; // float case
        return Pair.of(num, floating);
    }

    public static Pair<Integer, Boolean> search(String s, int offset) {
        boolean dot = false, permitDot = true;
        boolean hasE = false, hasESign = false, hasPow = false;
        boolean expectPow = false, expectESignOrPow = false, permitSign = true;
        int i, len = s.length();

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

        boolean floating = dot || hasE; // float case
        return Pair.of(i, floating);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private static Number valueOf(String num, boolean floating) {
        if (floating) {
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
}
