package org.dreamcat.common.bean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.ReflectUtil;

/**
 * @author Jerry Will
 * @since 2018-12-27
 */
@Slf4j
public final class LessFieldUtil {

    private LessFieldUtil() {
    }

    /**
     * set the value of some fields to null
     * it maybe useful on xml or json formatting situation
     *
     * @param bean       pojo
     * @param expression a specified bean expression string, such as `c.{m.x,n.{y,z}},b,a`
     * @throws Exception uncaught exception
     */
    public static void lessFiled(Object bean, String expression) throws Exception {
        Class<?> clazz = bean.getClass();
        if (clazz.isPrimitive()) {
            if (log.isDebugEnabled()) {
                log.debug("ignore primitive type class `{}`", clazz);
            }
            return;
        }

        List<String> fieldNames = ReflectUtil.retrieveFieldNames(clazz);
        List<String> keptFiledNames = new ArrayList<>();
        List<String> levelOneFiledNames = new ArrayList<>();
        retrieveExpression(expression, levelOneFiledNames);

        boolean exclude = false;
        for (String levelOneFiledName : levelOneFiledNames) {
            // exclude mode
            if (levelOneFiledName.startsWith("-")) {
                exclude = true;
                String str = levelOneFiledName.substring(1);
                int i = str.indexOf('.');
                if (i < 0) {
                    Field field = clazz.getDeclaredField(str);
                    ReflectUtil.nullify(bean, field);
                    continue;
                }

                checkDotPosition(str, i);

                String name = str.substring(0, i);
                // chars after first '.'
                String rest = str.substring(i + 1);

                if (!fieldNames.contains(name)) {
                    continue;
                }

                Field field = clazz.getDeclaredField(name);
                Object fieldObject = ReflectUtil.getValue(bean, field);

                if (fieldObject == null) continue;

                Field fieldField = fieldObject.getClass().getDeclaredField(rest);
                Object fieldFieldObject = ReflectUtil.getValue(fieldObject, fieldField);

                if (fieldFieldObject == null) continue;
                ReflectUtil.setZeroValue(fieldObject, fieldField);
            } else {
                if (exclude) throw new IllegalArgumentException();

                int i = levelOneFiledName.indexOf('.');
                if (i < 0) {
                    if (!fieldNames.contains(levelOneFiledName)) {
                        if (log.isDebugEnabled()) {
                            log.debug("class {} hasn't field `{}`", clazz, levelOneFiledName);
                        }
                        continue;
                    }
                    keptFiledNames.add(levelOneFiledName);
                    continue;
                }

                checkDotPosition(levelOneFiledName, i);

                String name = levelOneFiledName.substring(0, i);
                // chars after first '.'
                String rest = levelOneFiledName.substring(i + 1);
                Field field = clazz.getDeclaredField(name);
                Object fieldObject = ReflectUtil.getValue(bean, field);

                if (fieldObject == null) continue;

                if (rest.startsWith("{") && rest.endsWith("}")) {
                    String levelTwoExpression = rest.substring(1, rest.length() - 1);
                    lessFiled(fieldObject, levelTwoExpression);
                } else {
                    try {
                        keepOneField(fieldObject, rest);
                    } catch (Exception e) {
                        // wanna kept field is not exists, so remove the object self
                        ReflectUtil.setZeroValue(bean, field);
                    }
                }
                keptFiledNames.add(name);
            }
        }

        if (!exclude) {
            for (String fieldName : fieldNames) {
                if (!keptFiledNames.contains(fieldName)) {
                    Field field = clazz.getDeclaredField(fieldName);
                    ReflectUtil.nullify(bean, field);
                }
            }
        }
    }

    // make all fields' value equal null but the specified one
    private static void keepOneField(Object bean, String fieldName) {
        Class<?> clazz = bean.getClass();
        List<Field> fields = ReflectUtil.retrieveFields(clazz);
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) continue;

            ReflectUtil.nullify(bean, field);
        }
    }

    // first '.' is last char of str
    private static void checkDotPosition(String str, int i) {
        if (i == str.length() - 1) {
            throw new IllegalArgumentException();
        }
    }

    // FIXME there are some known bugs when it accepts a invalid expression
    private static void retrieveExpression(String expression, List<String> levelOneFiledNames) {
        if (expression == null || expression.isEmpty()) return;

        expression = expression.trim();
        int firstComma = expression.indexOf(",");
        int firstLeftBigParentheses = expression.indexOf("{");

        // if not has ',', then treat as single field
        if (firstComma < 0) {
            levelOneFiledNames.add(expression);
            return;
        }
        // if not has '{'
        else if (firstLeftBigParentheses < 0) {
            levelOneFiledNames.addAll(Arrays.stream(expression.trim().split(","))
                    .map(String::trim)
                    .collect(Collectors.toList()));
            return;
        }

        String suffix = expression;
        // if expression is like a,b,c.{m.x,n.{y,z}}
        // in fact, just not is c.{m.x,n.{y,z}},b,a
        if (firstComma < firstLeftBigParentheses) {
            int index = expression.substring(0, firstLeftBigParentheses).lastIndexOf(",");
            String prefix = expression.substring(0, index);
            levelOneFiledNames.addAll(splitString(prefix));
            suffix = expression.substring(index + 1);
        }
        retrieveForFirstLeftBigParentheses(suffix, levelOneFiledNames);
    }

    // work on this case: c.{m.x,n.{y,z}},b,a
    private static void retrieveForFirstLeftBigParentheses(
            String expression, List<String> levelOneFiledNames) {
        int leftPos = expression.indexOf("{");
        int len = expression.length();
        // '{' count 1, '}' count -1, matched until count equal 0
        int count = 1, rightPos = 0;
        boolean matched = false;
        if (leftPos + 1 > len - 1) throw new IllegalArgumentException("not matched '}' for '{'");
        for (int i = leftPos + 1; i < len; i++) {
            char c = expression.charAt(i);
            if (c == '{') count++;
            else if (c == '}') count--;
            if (count == 0) {
                matched = true;
                rightPos = i;
                break;
            }
        }
        if (matched) {
            String subExpr = expression.substring(0, rightPos + 1);
            levelOneFiledNames.add(subExpr.trim());

            // still has some chars after matched '}'
            if (rightPos < len - 1) {
                char ch = expression.charAt(rightPos + 1);
                if (ch != ',') {
                    throw new IllegalArgumentException(
                            String.format("invalid char `%s` after `}`", ch + ""));
                }
                // still has some chars after matched '},'
                if (rightPos + 1 < len - 1) {
                    String restExpr = expression.substring(rightPos + 1);
                    retrieveExpression(restExpr, levelOneFiledNames);
                }
            }
        } else {
            throw new IllegalArgumentException("not matched '}' for '{'");
        }
    }

    private static List<String> splitString(String string) {
        if (string == null || string.isEmpty()) return Collections.emptyList();

        return Arrays.stream(string.trim().split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

}
