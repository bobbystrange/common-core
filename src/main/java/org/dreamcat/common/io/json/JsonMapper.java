package org.dreamcat.common.io.json;

import org.dreamcat.common.core.Pair;
import org.dreamcat.common.core.Triple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Create by tuke on 2020/5/10
 */
public class JsonMapper {

    /**
     * parse json string to object, same as JSON.parse
     *
     * @param expression json string
     * @return one of String, Double, Boolean, List<Object>, Map<String, Object>
     */
    public static Object parse(String expression) {
        if (expression == null) {
            throw new NullPointerException();
        }

        int len = expression.length();
        int offset;
        char c;
        offset = 0;
        do {
            c = expression.charAt(offset);
            // if c is not blank
            if (c > ' ') break;
            offset++;
        } while (offset < len);

        if (offset >= len) {
            throw new IllegalArgumentException("blank string is given");
        }

        Pair<?, Integer> pair;
        Object value;
        if (c == '{') {
            pair = parseObject(expression, offset);
        } else if (c == '[') {
            pair = parseArray(expression, offset);
        } else if (c == '"') {
            pair = parseString(expression, offset);
        } else if (c >= '0' && c <= '9') {
            pair = parseNumber(expression, offset, false);
        } else if (c == '-') {
            pair = parseNumber(expression, offset, true);
        } else {
            pair = parseLiteral(expression, offset);
        }

        value = pair.first();
        offset = pair.second();
        if (offset < len) {
            for (int i = offset; i < len; i++) {
                c = expression.charAt(i);
                if (c > ' ') {
                    throw new IllegalArgumentException("invalid token " + c + " at pos " + i);
                }
            }
        }
        return value;
    }

    private static Pair<Map<String, Object>, Integer> parseObject(String expression, int offset) {
        int len = expression.length();
        Map<String, Object> map = new HashMap<>();
        offset = offset + 1;
        boolean head = true;
        while (offset < len) {
            Triple<String, Integer, Boolean> fieldTriple = parseField(expression, offset, !head);
            String field = fieldTriple.first();
            int colon = fieldTriple.second();
            boolean closed = fieldTriple.third();
            if (closed) {
                offset = fieldTriple.second();
                break;
            }

            if (map.containsKey(field)) {
                throw new IllegalArgumentException("redundant field `" + field + "` at pos " + offset);
            }

            Triple<?, Integer, Boolean> valueTriple = parseHead(expression, colon + 1, '}', false);
            if (head) head = false;
            Object value = valueTriple.first();
            offset = valueTriple.second();
            closed = valueTriple.third();
            if (closed) {
                throw new IllegalArgumentException("unmatch field `" + field + "` at pos " + offset);
            }

            map.put(field, value);
        }

        return new Pair<>(map, offset);
    }

    // Note that array case, make sure offset char is [
    //    [{"a": 1}, [1, 2, 3], true, "get back"]
    private static Pair<List<Object>, Integer> parseArray(String expression, int offset) {
        int len = expression.length();
        List<Object> array = new ArrayList<>();

        offset = offset + 1;
        boolean head = true;
        while (offset < len) {
            Triple<?, Integer, Boolean> triple = parseHead(expression, offset, ']', !head);
            if (head) head = false;

            Object value = triple.first();
            offset = triple.second();
            boolean closed = triple.third();
            if (closed) break;

            array.add(value);
        }

        return new Pair<>(array, offset);
    }

    // Note that string case, make sure offset char is "
    private static Pair<String, Integer> parseString(String expression, int offset) {
        int len = expression.length();
        StringBuilder s = new StringBuilder();
        offset = extractString(expression, offset, len, s);
        return new Pair<>(s.toString(), offset);
    }

    // Note that number case, make sure offset char is [0-9]
    private static Pair<Double, Integer> parseNumber(String expression, int offset, boolean negative) {
        int len = expression.length();
        StringBuilder s = new StringBuilder();
        if (negative) s.append("-");
        offset = extractNumber(expression, offset, len, s);
        return new Pair<>(Double.valueOf(s.toString()), offset);
    }

    // Note that false,null,true case
    private static Pair<Boolean, Integer> parseLiteral(String expression, int offset) {
        int len = expression.length();
        if (len < offset + 4) {
            throw new IllegalArgumentException("invalid token at pos " + offset);
        }

        Boolean bool;
        int r = offset + 4;
        String s = expression.substring(offset, offset + 4);
        if (Objects.equals(s, "true")) {
            bool = true;
        } else if (Objects.equals(s, "null")) {
            bool = null;
        } else if (Objects.equals(s, "fals") &&
                len >= offset + 5 &&
                expression.charAt(offset + 4) == 'e') {
            bool = false;
            r++;
        } else {
            throw new IllegalArgumentException("invalid token `" + s + "` at pos " + offset);
        }

        offset = r;
        if (offset >= len) {
            throw new IllegalArgumentException("unclosed body at end");
        }
        return new Pair<>(bool, offset);
    }

    private static Triple<?, Integer, Boolean> parseHead(String expression, int offset, char end, boolean trimComma) {
        int len = expression.length();

        char c;
        boolean trimmed = false;
        do {
            c = expression.charAt(offset);
            if (c > ' ') {
                if (c == ',' && trimComma && !trimmed) {
                    trimmed = true;
                } else {
                    break;
                }
            }
            offset++;
        } while (offset < len);

        // not closed
        if (offset >= len) {
            throw new IllegalArgumentException("unclosed body at end");
        }
        // close
        if (c == end) {
            return new Triple<>(null, offset + 1, true);
        }

        if (c == '{') {
            return parseObject(expression, offset).join(false);
        } else if (c == '[') {
            return parseArray(expression, offset).join(false);
        } else if (c == '"') {
            return parseString(expression, offset).join(false);
        } else if (c >= '0' && c <= '9') {
            return parseNumber(expression, offset, false).join(false);
        } else if (c == '-') {
            return parseNumber(expression, offset + 1, true).join(false);
        } else {
            return parseLiteral(expression, offset).join(false);
        }
    }

    private static Triple<String, Integer, Boolean> parseField(String expression, int offset, boolean trimComma) {
        int len = expression.length();

        boolean trimmed = false;
        for (; offset < len; offset++) {
            char c = expression.charAt(offset);
            if (c > ' ') {
                if (c == '"') break;
                else if (c == ',' && trimComma && !trimmed) {
                    trimmed = true;
                } else if (c == '}') {
                    return new Triple<>(null, offset + 1, true);
                } else {
                    throw new IllegalArgumentException("invalid token `" + c + "` at pos " + offset);
                }
            }
        }
        // not found
        if (offset >= len) {
            throw new IllegalArgumentException("unclosed body at end");
        }

        StringBuilder s = new StringBuilder();
        offset = extractString(expression, offset, len, s);
        String field = s.toString();

        int colon = expression.indexOf(':', offset + 1);
        if (colon == -1 || colon == len - 1) {
            throw new IllegalArgumentException("unmatched field `" + field + "` at pos " + offset);
        }

        return new Triple<>(field, colon, false);
    }

    // Note that extract string
    static int extractString(String expression, int offset, int len, StringBuilder s) {
        int oldOffset = offset++;
        for (; offset < len; offset++) {
            char c = expression.charAt(offset);
            if (c == '\\') {
                offset++;
                if (offset >= len) {
                    throw new IllegalArgumentException("invalid token / at pos " + (offset - 1));
                }
                c = expression.charAt(offset);
                if (c == '"' || c == '\\') {
                    s.append(c);
                } else if (c == 'n') {
                    s.append('\n');
                } else if (c == 'r') {
                    s.append('\r');
                } else if (c == 't') {
                    s.append('\t');
                } else if (c == 'b') {
                    s.append('\b');
                } else if (c == 'f') {
                    s.append('\f');
                } else if (c == 'u') {
                    // \u0000
                    if (offset >= len - 4) {
                        throw new IllegalArgumentException("invalid token /u at pos " + (offset - 1));
                    }
                    String n = expression.substring(offset + 1, offset + 5);
                    char u = (char) Integer.parseInt(n, 16);
                    s.append(u);
                    offset += 4;
                }
            } else if (c == '"') {
                break;
            } else {
                s.append(c);
            }
        }
        if (offset >= len) {
            throw new IllegalArgumentException("unmatched quote \" at pos " + oldOffset);
        }

        return offset;
    }

    // Note that parse [0-9]+([.][0-9]+)?([eE][+-]?[0-9]+)?
    static int extractNumber(String expression, int offset, int len, StringBuilder s) {
        boolean dot = false;
        boolean edot = false;
        boolean permitDot = false;
        boolean hasE = false;
        boolean enterSign = false;
        for (; offset < len; offset++) {
            char c = expression.charAt(offset);

            if (c == '-' || c == '+') {
                if (enterSign) {
                    s.append(c);
                    enterSign = false;
                } else {
                    throw new IllegalArgumentException("invalid number format `" + c + "` at pos " + offset);
                }
                continue;
            } else {
                enterSign = false;
            }

            if (c >= '0' && c <= '9') {
                s.append(c);
                permitDot = true;
            } else if (c == '.') {
                if (!permitDot) {
                    throw new IllegalArgumentException("invalid number format `.` at pos " + offset);
                }

                if (!dot) {
                    s.append(c);
                    dot = true;
                } else if (hasE && !edot) {
                    s.append(c);
                    edot = true;
                } else {
                    throw new IllegalArgumentException("invalid number format `.` at pos " + offset);
                }
            } else if (c == 'e' || c == 'E') {
                if (!hasE) {
                    s.append(c);
                    hasE = true;
                    enterSign = true;
                } else {
                    throw new IllegalArgumentException("invalid number format `" + c + "` at pos " + offset);
                }
            } else {
                break;
            }
        }

        return offset;
    }

}
