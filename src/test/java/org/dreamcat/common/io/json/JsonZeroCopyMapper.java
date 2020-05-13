package org.dreamcat.common.io.json;

import org.dreamcat.common.core.Pair;
import org.dreamcat.common.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by tuke on 2020/5/7
 */
class JsonZeroCopyMapper {
    private static final List<String> literals = Arrays.asList("false", "null", "true");

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
        return parse(new ZeroCopyString(expression));
    }

    public static Object parse(ZeroCopyString expression) {

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
            ZeroCopyString string = expression.substring(1, len - 1);
            // \"  ->  "
            return string.toString().replace("\\\"", "\"");
        }

        String s = expression.toString();
        // Note case false, null, true
        if (literals.contains(s)) {
            if (s.equals("null")) {
                return null;
            } else {
                return s.equals("true");
            }
        } else if (StringUtil.isNumber(s)) {
            return Double.valueOf(s);
        } else {
            throw new IllegalArgumentException("invalid token at head: " + expression.substring(0, 1));
        }
    }

    // Note case like    "a": 0, "b": "s1, s2", "c": {}
    private static void parseObject(ZeroCopyString expression, Map<String, Object> object) {
        int len = expression.length();
        // "x":0
        if (!expression.startsWith("\"") || len < 5) {
            throw new IllegalArgumentException("invalid token at object: " + StringUtil.substring(expression.toString(), 0, 4));
        }

        int secondQuote = expression.indexOf("\"", 1);
        // "x":0
        if (secondQuote == -1) {
            throw new IllegalArgumentException("unmatched quote at object: " + StringUtil.substring(expression.toString(), 0, 5));
        }
        if (secondQuote > len - 3) {
            throw new IllegalArgumentException("invalid field at object: " + StringUtil.substring(expression.toString(), len - 5, len));
        }

        String field = expression.substring(1, secondQuote).toString();
        if (object.containsKey(field)) {
            throw new IllegalArgumentException("redundant field " + field);
        }

        int colon = expression.indexOf(":", secondQuote);
        if (colon == -1) {
            throw new IllegalArgumentException("missing colon at field `" + field + "`: " + StringUtil.substring(expression.toString(), secondQuote, secondQuote + 3));
        }

        ZeroCopyString middle = expression.substring(secondQuote + 1, colon).trim();
        if (!middle.isEmpty()) {
            throw new IllegalArgumentException("invalid token at field `" + field + "`: " + middle);
        }

        // Note case like    "a": 0, "b": "s1, s2", "c": {}
        //     ->    0, "b": "s1, s2", "c": {}
        ZeroCopyString remaining = expression.substring(colon + 1).trim();
        if (remaining.isEmpty()) {
            throw new IllegalArgumentException("missing value at field `" + field + "`: " + StringUtil.substring(expression.toString(), colon - 5, colon));
        }
        Pair<Object, ZeroCopyString> pair = parseHead(remaining);
        Object value = pair.first();
        expression = pair.second();

        object.put(field, value);

        if (expression != null && !expression.isEmpty()) parseObject(expression, object);
    }

    // Note case like    0, "s", true, "s1,s2", [], {}
    private static void parseArray(ZeroCopyString expression, List<Object> array) {
        Pair<Object, ZeroCopyString> pair = parseHead(expression);
        Object value = pair.first();
        expression = pair.second();

        array.add(value);
        if (expression != null && !expression.isEmpty()) parseArray(expression, array);
    }

    // this method will make sure the returned expression is trim
    // Note case like    0, "s", true, "s1,s2", [], {}
    //     ->    "s", true, "s1,s2", [], {}
    //    or     0, "b": "s1, s2", "c": {}
    //    ->    "b": "s1, s2", "c": {}
    private static Pair<Object, ZeroCopyString> parseHead(ZeroCopyString expression) {
        int len = expression.length();

        // Note case {...}...
        if (expression.startsWith("{")) {
            int matchedBrace = searchMatchedBracket(expression, '{', '}');
            ZeroCopyString head = expression.substring(0, matchedBrace + 1);
            Object value = parse(head);
            if (matchedBrace == len - 1) {
                return new Pair<>(value, ZeroCopyString.EMPTY);
            }

            ZeroCopyString remaining = trimComma(expression, matchedBrace + 1);
            return new Pair<>(value, remaining);
        }
        // Note case [...]...
        else if (expression.startsWith("[")) {
            int matchedBracket = searchMatchedBracket(expression, '[', ']');
            ZeroCopyString head = expression.substring(0, matchedBracket + 1);
            Object value = parse(head);
            if (matchedBracket == len - 1) {
                return new Pair<>(value, ZeroCopyString.EMPTY);
            }

            ZeroCopyString remaining = trimComma(expression, matchedBracket + 1);
            return new Pair<>(value, remaining);
        }
        // Note case "...\"..."...
        else if (expression.startsWith("\"")) {
            int secondQuote = searchMatchedQuote(expression, 0);

            ZeroCopyString value = expression.substring(1, secondQuote);
            if (secondQuote == len - 1) {
                return new Pair<>(value, ZeroCopyString.EMPTY);
            }

            ZeroCopyString remaining = trimComma(expression, secondQuote);
            return new Pair<>(value, remaining);
        }
        // Note case like    true,...
        else if (expression.startsWith("false")) {
            ZeroCopyString remaining = len == 5 ? null : trimComma(expression, 5);
            return new Pair<>(false, remaining);
        } else if (expression.startsWith("null")) {
            ZeroCopyString remaining = len == 4 ? null : trimComma(expression, 4);
            return new Pair<>(null, remaining);
        } else if (expression.startsWith("true")) {
            ZeroCopyString remaining = len == 4 ? null : trimComma(expression, 4);
            return new Pair<>(true, remaining);
        }
        // Note case like 3.14...
        else {
            String number = StringUtil.extractNumber(expression, 0);
            if (number != null) {
                int n = number.length();
                ZeroCopyString remaining = len == n ? ZeroCopyString.EMPTY : trimComma(expression, n);
                return new Pair<>(Double.valueOf(number), remaining);
            }

            throw new IllegalArgumentException("invalid token at body: " + expression.substring(0, 1));
        }
    }

    // this method will make sure the returned index is valid
    // Note case like    {0, true, "{}[] \" ", {"a": [1, 2]}, }...
    private static int searchMatchedBracket(ZeroCopyString expression, char left, char right) {
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
                            StringUtil.substring(expression.toString(), i, i + 3));
                }
            }
        }

        String msg = String.format("unmatched %c at body: %s", left, StringUtil.substring(expression.toString(), 0, 3));
        throw new IllegalArgumentException(msg);
    }

    private static int searchMatchedQuote(ZeroCopyString expression, int offset) {
        // search matched quote
        int secondQuote = offset + 1;
        do {
            secondQuote = expression.indexOf("\"", secondQuote);
            if (secondQuote == -1) {
                throw new IllegalArgumentException("unmatched quote at body: " + StringUtil.substring(expression.toString(), 0, 2));
            }
        } while (expression.charAt(secondQuote - 1) == '\\');
        return secondQuote;
    }

    // Note case like    , true, "{}[] \" ", {"a": [1, 2]}, }...
    //    ->    true, "{}[] \" ", {"a": [1, 2]}, }...
    private static ZeroCopyString trimComma(ZeroCopyString expression, int offset) {
        int comma = expression.indexOf(",", offset);
        if (comma == -1) {
            throw new IllegalArgumentException("invalid token at body: " + StringUtil.substring(expression.toString(), offset, offset + 2));
        }
        if (comma > offset + 1) {
            ZeroCopyString middle = expression.substring(offset + 1, comma).trim();
            if (!middle.isEmpty()) {
                throw new IllegalArgumentException("invalid token at body: " + middle);
            }
        }

        return expression.substring(comma + 1).trim();
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

}
