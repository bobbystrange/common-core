package org.dreamcat.common.io.json;

import org.dreamcat.common.core.Pair;
import org.dreamcat.common.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2020/5/6
 * <p>
 * Note that it is a very slow (almost 10 times than jackson) implement
 */
public abstract class JsonMapperV1 {
    private static final List<String> literals = Arrays.asList("false", "null", "true");

    /**
     * convert object to json string, same as JSON.stringify
     *
     * @param raw one of String, Double, Boolean, List<Object>, Map<String, Object>
     * @return json string
     */
    @SuppressWarnings("unchecked")
    public static String stringify(Object raw) {
        if (raw == null) {
            return "null";
        } else if (raw instanceof String) {
            String string = (String) raw;
            // "  ->  \"
            return String.format("\"%s\"", string.replace("\"", "\\\""));
        } else if (raw instanceof Double || raw instanceof Boolean) {
            return raw.toString();
        } else if (raw instanceof List) {
            List<Object> array = (List<Object>) raw;
            if (array.isEmpty()) return "[]";

            return String.format("[%s]", array.stream()
                    .map(JsonMapperV1::stringify)
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
     * @return one of String, Double, Boolean, List<Object>, Map<String, Object>
     */
    public static Object parse(String expression) {
        if (expression == null) {
            throw new NullPointerException();
        }
        expression = expression.trim();
        if (expression.isEmpty()) {
            throw new IllegalArgumentException("input is empty");
        }

        int len = expression.length();
        // Note case {...}
        if (expression.startsWith("{") && expression.endsWith("}")) {
            // Watch out require twice memory
            expression = expression.substring(1, len - 1).trim();
            Map<String, Object> object = new HashMap<>();
            if (len > 2) parseObject(expression, object);
            return object;
        }

        // Note case [...]
        else if (expression.startsWith("[") && expression.endsWith("]")) {
            // Watch out require twice memory
            expression = expression.substring(1, len - 1).trim();
            List<Object> array = new ArrayList<>();
            if (len > 2) parseArray(expression, array);
            return array;
        }
        // Note case "..."
        else if (expression.startsWith("\"") && expression.endsWith("\"") && len >= 2) {
            String string = expression.substring(1, len - 1);
            // \"  ->  "
            return string.replace("\\\"", "\"");
        }
        // Note case false, null, true
        else if (literals.contains(expression)) {
            if (expression.equals("null")) {
                return null;
            } else {
                return expression.equals("true");
            }
        } else if (StringUtil.isNumber(expression)) {
            return Double.valueOf(expression);
        } else {
            throw new IllegalArgumentException("invalid token at head: " + expression.substring(0, 1));
        }
    }

    // Note case like    "a": 0, "b": "s1, s2", "c": {}
    private static void parseObject(String expression, Map<String, Object> object) {
        int len = expression.length();
        // "x":0
        if (!expression.startsWith("\"") || len < 5) {
            throw new IllegalArgumentException("invalid token at object: " + StringUtil.substring(expression, 0, 4));
        }

        int secondQuote = expression.indexOf("\"", 1);
        // "x":0
        if (secondQuote == -1) {
            throw new IllegalArgumentException("unmatched quote at object: " + StringUtil.substring(expression, 0, 5));
        }
        if (secondQuote > len - 3) {
            throw new IllegalArgumentException("invalid field at object: " + StringUtil.substring(expression, len - 5, len));
        }

        String field = expression.substring(1, secondQuote);
        if (object.containsKey(field)) {
            throw new IllegalArgumentException("redundant field " + field);
        }

        int colon = expression.indexOf(":", secondQuote);
        if (colon == -1) {
            throw new IllegalArgumentException("missing colon at field `" + field + "`: " + StringUtil.substring(expression, secondQuote, secondQuote + 3));
        }

        String middle = expression.substring(secondQuote + 1, colon).trim();
        if (!middle.isEmpty()) {
            throw new IllegalArgumentException("invalid token at field `" + field + "`: " + middle);
        }

        // Note case like    "a": 0, "b": "s1, s2", "c": {}
        //     ->    0, "b": "s1, s2", "c": {}
        String remaining = expression.substring(colon + 1).trim();
        if (remaining.isEmpty()) {
            throw new IllegalArgumentException("missing value at field `" + field + "`: " + StringUtil.substring(expression, colon - 5, colon));
        }
        Pair<Object, String> pair = parseHead(remaining);
        Object value = pair.first();
        expression = pair.second();

        object.put(field, value);

        if (!expression.isEmpty()) parseObject(expression, object);
    }

    // Note case like    0, "s", true, "s1,s2", [], {}
    private static void parseArray(String expression, List<Object> array) {
        Pair<Object, String> pair = parseHead(expression);
        Object value = pair.first();
        expression = pair.second();

        array.add(value);
        if (!expression.isEmpty()) parseArray(expression, array);
    }

    // this method will make sure the returned expression is trim
    // Note case like    0, "s", true, "s1,s2", [], {}
    //     ->    "s", true, "s1,s2", [], {}
    //    or     0, "b": "s1, s2", "c": {}
    //    ->    "b": "s1, s2", "c": {}
    private static Pair<Object, String> parseHead(String expression) {
        int len = expression.length();

        // Note case {...}...
        if (expression.startsWith("{")) {
            int matchedBrace = searchMatchedBracket(expression, '{', '}');
            String head = expression.substring(0, matchedBrace + 1);
            Object value = parse(head);
            if (matchedBrace == len - 1) {
                return new Pair<>(value, "");
            }

            String remaining = trimComma(expression, matchedBrace + 1);
            return new Pair<>(value, remaining);
        }
        // Note case [...]...
        else if (expression.startsWith("[")) {
            int matchedBracket = searchMatchedBracket(expression, '[', ']');
            String head = expression.substring(0, matchedBracket + 1);
            Object value = parse(head);
            if (matchedBracket == len - 1) {
                return new Pair<>(value, "");
            }

            String remaining = trimComma(expression, matchedBracket + 1);
            return new Pair<>(value, remaining);
        }
        // Note case "...\"..."...
        else if (expression.startsWith("\"")) {
            int secondQuote = StringUtil.searchMatchedQuote(expression, 1);
            if (secondQuote == -1) {
                throw new IllegalArgumentException("unmatched quote at body: " + StringUtil.substring(expression, 0, 2));
            }

            String value = expression.substring(1, secondQuote);
            if (secondQuote == len - 1) {
                return new Pair<>(value, "");
            }

            String remaining = trimComma(expression, secondQuote);
            return new Pair<>(value, remaining);
        }
        // Note case like    true,...
        else if (expression.startsWith("false")) {
            String remaining = len == 5 ? "" : trimComma(expression, 5);
            return new Pair<>(false, remaining);
        } else if (expression.startsWith("null")) {
            String remaining = len == 4 ? "" : trimComma(expression, 4);
            return new Pair<>(null, remaining);
        } else if (expression.startsWith("true")) {
            String remaining = len == 4 ? "" : trimComma(expression, 4);
            return new Pair<>(true, remaining);
        }
        // Note case like 3.14...
        else {
            String number = StringUtil.extractNumber(expression, 0);
            if (number != null) {
                int n = number.length();
                String remaining = len == n ? "" : trimComma(expression, n);
                return new Pair<>(Double.valueOf(number), remaining);
            }

            throw new IllegalArgumentException("invalid token at body: " + expression.substring(0, 1));
        }
    }

    // this method will make sure the returned index is valid
    // Note case like    {0, true, "{}[] \" ", {"a": [1, 2]}, }...
    private static int searchMatchedBracket(String expression, char left, char right) {
        int len = expression.length();

        boolean enterString = false;
        int nestedLevel = 1;
        for (int i = 1; i < len; i++) {
            char c = expression.charAt(i);

            if (expression.charAt(i - 1) != '\\') {
                if (c == '{' || c == '[') {
                    nestedLevel++;
                } else if (c == '}' || c == ']') {
                    nestedLevel--;
                    if (c == right && nestedLevel == 0) {
                        return i;
                    }
                } else if (c == '"') {
                    if (enterString) {
                        nestedLevel--;
                        enterString = false;
                    } else {
                        nestedLevel++;
                        enterString = true;
                    }
                }

                // check nestedLevel
                if (nestedLevel < 1) {
                    throw new IllegalArgumentException("unmatched closed char at body: " +
                            StringUtil.substring(expression, i, i + 3));
                }
            }
        }

        String msg = String.format("unmatched %c at body: %s", left, StringUtil.substring(expression, 0, 3));
        throw new IllegalArgumentException(msg);
    }

    // Note case like    , true, "{}[] \" ", {"a": [1, 2]}, }...
    //    ->    true, "{}[] \" ", {"a": [1, 2]}, }...
    private static String trimComma(String expression, int offset) {
        int comma = expression.indexOf(",", offset);
        if (comma == -1) {
            throw new IllegalArgumentException("invalid token at body: " + StringUtil.substring(expression, offset, offset + 2));
        }
        if (comma > offset + 1) {
            String middle = expression.substring(offset + 1, comma).trim();
            if (!middle.isEmpty()) {
                throw new IllegalArgumentException("invalid token at body: " + middle);
            }
        }

        return expression.substring(comma + 1).trim();
    }

}
