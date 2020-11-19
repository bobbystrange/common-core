package org.dreamcat.common.io.yaml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by tuke on 2020/7/6
 */
public class YamlMapper {

    /**
     * parse yaml string to object
     *
     * @param expression yaml string
     * @return Map<String, Object>
     */
    public static Map<String, Object> parse(String expression) {
        if (expression == null) {
            throw new NullPointerException();
        }

        try (BufferedReader br = new BufferedReader(
                new StringReader(expression))) {
            return parse(br);
        } catch (IOException e) {
            return null;
        }
    }

    public static Map<String, Object> parse(BufferedReader expression) throws IOException {
        Map<String, Object> root = new HashMap<>();
        String key = null;
        List<Object> currentArray = null;
        Map<String, Object> currentObject = null;

        String line;
        // current whitespace width
        int prevWidth = 0;
        int lineNo = 0;
        while ((line = expression.readLine()) != null) {
            lineNo++;
            int currentWidth = 0;
            int offset = 0;
            for (int len = line.length(); offset < len; offset++) {
                char c = line.charAt(offset);
                if (c < ' ') {
                    throw new IllegalArgumentException(
                            String.format("invalid char `0x%s` at pos %d, %d",
                                    Integer.toHexString(c), lineNo, offset));
                } else if (c == ' ') {
                    currentWidth++;
                } else break;
            }

            // same level
            if (currentWidth == prevWidth) {
                String field = line.substring(offset).trim();
                if (!field.startsWith("-")) {
                    int colonIndex = field.indexOf(":");
                    // Note case like  -->  a: b
                    if (colonIndex != -1) {
                        String fieldKey = field.substring(0, colonIndex).trim();
                        String fieldValue = null;
                        if (colonIndex != field.length() - 1) {
                            fieldValue = field.substring(colonIndex + 1);
                        }

                        if (currentWidth == 0) {
                            root.put(fieldKey, fieldValue);
                        } else if (key == null) {
                            throw new AssertionError();
                        } else {
                            currentObject.put(fieldKey, fieldValue);
                        }
                    }
                    // Note case like  -->  a:
                    else {
                        key = field.trim();
                        currentObject = new HashMap<>();
                    }
                }
                // Note case like  -->  - a: b  or  - a
                else {
                    field = field.substring(1).trim();
                    int colonIndex = field.indexOf(":");
                    // Note case like  -->  - a
                    if (colonIndex != -1) {
                        if (currentArray == null) {
                            currentArray = new ArrayList<>();
                        }
                        currentArray.add(field);
                    }
                    // Note case like  -->  - a:
                    else if (colonIndex == field.length() - 1) {
                        field = field.substring(0, field.length() - 1);
                    }

                }
            } else {

            }
        }

        return root;
    }
}
