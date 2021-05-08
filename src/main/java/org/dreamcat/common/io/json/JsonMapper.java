package org.dreamcat.common.io.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.dreamcat.common.core.Pair;
import org.dreamcat.common.core.Triple;
import org.dreamcat.common.text.NumericSearcher;

/**
 * Create by tuke on 2020/5/10
 */
public final class JsonMapper {

    private JsonMapper() {
    }

    /**
     * convert object to json string, same as JSON.stringify
     * <p>
     * note that as it is a very slow implement
     * only use it for test purpose
     *
     * @param raw one of String, Integer/Long/Double, Boolean, List&lt;Object&gt;, Map&lt;String, Object&gt;
     * @return json string
     */
    @SuppressWarnings({"unchecked"})
    static String stringify(Object raw) {
        if (raw == null) {
            return "null";
        } else if (raw instanceof String) {
            String string = (String) raw;
            // "  ->  \"
            return String.format("\"%s\"", string.replace("\"", "\\\""));
        } else if (raw instanceof Number || raw instanceof Boolean) {
            return raw.toString();
        } else if (raw instanceof List) {
            List<Object> array = (List<Object>) raw;
            if (array.isEmpty()) return "[]";

            return String.format("[%s]", array.stream()
                    .map(JsonMapper::stringify)
                    .collect(Collectors.joining(",")));
        } else if (raw instanceof Map) {
            Map<String, Object> object = (Map<String, Object>) raw;
            if (object.isEmpty()) return "{}";

            return String.format("{%s}", object.entrySet().stream()
                    .map(it -> String.format("%s:%s",
                            stringify(it.getKey()), stringify(it.getValue())))
                    .collect(Collectors.joining(",")));
        }

        throw new UnsupportedOperationException("unsupported input type " + raw.getClass());
    }

    /**
     * parse json string to object, same as JSON.parse
     *
     * @param expression json string
     * @return one of String, Double/Long/Integer, Boolean, List&lt;Object&gt;, Map&lt;String, Object&gt;
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
        } else if ((c >= '0' && c <= '9') || c == '-') {
            pair = NumericSearcher.extractNumber(expression, offset);
            if (pair == null) {
                throw new IllegalArgumentException(invalid_token(c, offset));
            }
        } else {
            pair = parseLiteral(expression, offset);
        }

        value = pair.first();
        offset = pair.second();
        if (offset < len) {
            for (int i = offset; i < len; i++) {
                c = expression.charAt(i);
                if (c > ' ') {
                    throw new IllegalArgumentException(invalid_token(c, i));
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
                throw new IllegalArgumentException(
                        "redundant field `" + field + "` at pos " + offset);
            }

            Triple<?, Integer, Boolean> valueTriple = parseHead(expression, colon + 1, '}', false);
            if (head) head = false;
            Object value = valueTriple.first();
            offset = valueTriple.second();
            closed = valueTriple.third();
            if (closed) {
                throw new IllegalArgumentException(
                        "unmatched field `" + field + "` at pos " + offset);
            }

            map.put(field, value);
        }

        return Pair.of(map, offset);
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

        return Pair.of(array, offset);
    }

    // Note that string case, make sure offset char is "
    private static Pair<String, Integer> parseString(String expression, int offset) {
        int len = expression.length();
        StringBuilder s = new StringBuilder();
        offset = extractString(expression, offset, len, s);
        return Pair.of(s.toString(), offset);
    }

    // Note that false,null,true case
    private static Pair<Boolean, Integer> parseLiteral(String expression, int offset) {
        int len = expression.length();
        if (len < offset + 4) {
            throw new IllegalArgumentException(invalid_token(
                    expression.substring(offset, len), offset));
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
            throw new IllegalArgumentException(invalid_token(s, offset));
        }

        offset = r;
        if (offset >= len) {
            throw new IllegalArgumentException("unclosed body at end");
        }
        return Pair.of(bool, offset);
    }

    private static Triple<?, Integer, Boolean> parseHead(String expression, int offset, char end,
            boolean trimComma) {
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
        } else if ((c >= '0' && c <= '9') || c == '-') {
            Pair<Number, Integer> pair = NumericSearcher.extractNumber(expression, offset);
            if (pair == null) {
                throw new IllegalArgumentException(invalid_token(c, offset));
            }
            return pair.join(false);
        } else {
            return parseLiteral(expression, offset).join(false);
        }
    }

    private static Triple<String, Integer, Boolean> parseField(String expression, int offset,
            boolean trimComma) {
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
                    throw new IllegalArgumentException(invalid_token(c, offset));
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

        return Triple.of(field, colon, false);
    }

    // Note that extract string
    static int extractString(String expression, int offset, int len, StringBuilder s) {
        int oldOffset = offset++;
        for (; offset < len; offset++) {
            char c = expression.charAt(offset);
            if (c == '\\') {
                offset++;
                if (offset >= len) {
                    throw new IllegalArgumentException(invalid_token('\\', offset - 1));
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
                        throw new IllegalArgumentException(invalid_token("\\u", offset - 1));
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

    private static String invalid_token(char c, int offset) {
        return "invalid token " + c + " at pos " + offset;
    }

    private static String invalid_token(String c, int offset) {
        return "invalid token " + c + " at pos " + offset;
    }
}
