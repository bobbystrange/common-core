package org.dreamcat.common.text;

import java.util.Map;
import java.util.MissingFormatArgumentException;
import org.dreamcat.common.util.StringUtil;

/**
 * Create by tuke on 2020/11/18
 * <p>
 * string interpolation by dollar sign
 * <code>[$][a-zA-Z0-9]+</code> and
 * <code>[$]{[a-zA-Z0-9]+:.*}</code>is supported
 * <p>
 * example:
 * $x_y_z_1, the variable is x_y_z_1, and has no default value
 * ${x_y_z_1:the_default_value}, the variable is x_y_z_1, and default is: the_default_value
 * ${{\:\}\\:{\:\}\\}, the variable is {:}\, and default is {:}\
 */
public final class DollarInterpolation {

    public static String format(String s, Map<String, String> context) {
        return format(s, context, true, null);
    }

    public static String format(String s, Map<String, String> context, String defaultValue) {
        return format(s, context, false, defaultValue);
    }

    /**
     * @param s              template text to format
     * @param context        context including the variables and their values
     * @param throwOnMissing throw {@link MissingFormatArgumentException} when missing default value
     * @param defaultValue   default value
     * @return the formatted text
     * @throws MissingFormatArgumentException when missing default value
     * @throws IllegalArgumentException       when the template text is invalid
     */
    private static String format(String s, Map<String, String> context, boolean throwOnMissing,
            String defaultValue) {
        int size = s.length();
        // 1.5 * size
        StringBuilder sb = new StringBuilder(size + size >> 1);
        boolean enterDollar = false; // in case like $...
        boolean enterDollarBrace = false; // in case like ${...}
        int leftIndex = -1, colonIndex = -1, rightIndex;
        for (int i = 0; i < size; i++) {
            char c = s.charAt(i);
            if (enterDollarBrace) {
                if (c == '\\') {
                    if (i == size - 1) {
                        throw new IllegalArgumentException("invalid template text");
                    }
                    i++;
                    continue;
                }
                if (c == ':' && colonIndex == -1) {
                    colonIndex = i;
                    continue;
                }
                if (c != '}') continue;
                // extract ... from ${...}
                rightIndex = colonIndex != -1 ? colonIndex : i;
                String variable = s.substring(leftIndex, rightIndex);
                // remove escape character
                variable = StringUtil.fromBackslash(variable);
                String value = context.get(variable);
                if (value == null) {
                    // has no default value
                    if (colonIndex == -1) {
                        if (throwOnMissing) {
                            throw new MissingFormatArgumentException(variable);
                        } else {
                            sb.append(defaultValue);
                        }
                    } else {
                        // extract ### from ${@@@:###}
                        sb.append(StringUtil.fromBackslash(s.substring(colonIndex + 1, i)));
                    }
                } else {
                    sb.append(value);
                }
                enterDollarBrace = false;
                colonIndex = -1;
                continue;
            }
            if (enterDollar) {
                if (StringUtil.isVariableChar(c)) continue;
                String variable = s.substring(leftIndex, i);
                String value = context.get(variable);
                if (value == null) {
                    if (throwOnMissing) {
                        throw new MissingFormatArgumentException(variable);
                    } else {
                        sb.append(defaultValue);
                    }
                } else {
                    sb.append(value);
                }
                i--;
                enterDollar = false;
                continue;
            }
            if (c != '$') {
                sb.append(c);
                continue;
            }
            // then c is $
            if (i == size - 1) {
                sb.append(c);
                break;
            }
            i++;
            char nextChar = s.charAt(i);
            if (StringUtil.isVariableChar(nextChar)) {
                leftIndex = i;
                enterDollar = true;
                continue;
            }
            if (nextChar == '{') {
                leftIndex = i + 1;
                enterDollarBrace = true;
                continue;
            }
            sb.append(c).append(nextChar);
        }
        return sb.toString();
    }
}
