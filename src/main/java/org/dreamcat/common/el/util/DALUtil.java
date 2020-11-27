package org.dreamcat.common.el.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.dreamcat.common.core.Pair;
import org.dreamcat.common.el.ElOperator;
import org.dreamcat.common.el.ElOperator.Parentheses;
import org.dreamcat.common.el.operation.ArithmeticOperator;
import org.dreamcat.common.el.operation.ArithmeticUnaryOperator;
import org.dreamcat.common.el.operation.CompareOperator;
import org.dreamcat.common.el.operation.LogicOperator;
import org.dreamcat.common.el.operation.LogicUnaryOperator;
import org.dreamcat.common.text.NumericSearcher;
import org.dreamcat.common.text.StringSearcher;
import org.dreamcat.common.util.StringUtil;

/**
 * Create by tuke on 2020/11/23
 */
public final class DALUtil {

    /**
     * Direct Algebraic Logic
     * <p>
     * Object is one of {@link BigDecimal}, {@link String} and {@link ElOperator}
     *
     * @param expression expression to be converted
     * @return DAL
     */
    public static List<Object> getDAL(String expression) {
        List<Object> formulas = new ArrayList<>();

        // unary operators of post position is also considered
        boolean preferBinaryOperator = false;
        for (int i = 0, size = expression.length(); i < size; ) {
            char c = expression.charAt(i);
            // ignore blank chars
            if (c <= ' ') {
                i++;
                continue;
            }
            if (c >= '0' && c <= '9') {
                Pair<Integer, Boolean> pair = NumericSearcher.search(expression, i);
                int nextOffset = pair.first();
                BigDecimal value = new BigDecimal(expression.substring(i, nextOffset));
                formulas.add(value);
                i = nextOffset;
                preferBinaryOperator = true;
            } else if (StringUtil.isFirstVariableChar(c)) {
                String name = StringSearcher.extractVariable(expression, i);
                int nextOffset = i + name.length();
                // try to treat as operator
                ElOperator operator = ElOperator.lookup(name);
                if (operator != null) formulas.add(operator);
                else formulas.add(name);
                i = nextOffset;
                preferBinaryOperator = true;
            } else if (c == '(') {
                formulas.add(Parentheses.LEFT);
                preferBinaryOperator = false;
                i++;
            } else if (c == ')') {
                formulas.add(Parentheses.RIGHT);
                preferBinaryOperator = true;
                i++;
            } else {
                // search unary operators
                if (!preferBinaryOperator) {
                    if (c == '-' || c == '+' || c == '!') {
                        boolean not = c == '!';
                        if (not) {
                            formulas.add(LogicUnaryOperator.NOT);
                        } else {
                            formulas.add(ArithmeticUnaryOperator.NEG);
                        }
                        i++;
                    } else {
                        doThrow(i);
                    }
                } else {
                    Pair<ElOperator, Integer> pair = searchBinaryOperation(expression, i);
                    i = pair.second();
                    formulas.add(pair.first());
                    preferBinaryOperator = false;
                }
            }
        }
        return formulas;
    }

    public static Pair<ElOperator, Integer> searchBinaryOperation(
            String expression, int offset) {
        int i, size;
        for (i = offset, size = expression.length(); i < size; i++) {
            char c = expression.charAt(i);
            if (c <= ' ') continue; // ignore blank chars

            throwIfReachEnd(i, size);
            if (c == '+') {
                return Pair.of(ArithmeticOperator.ADD, i + 1);
            } else if (c == '-') {
                return Pair.of(ArithmeticOperator.SUBTRACT, i + 1);
            } else if (c == '*') {
                return Pair.of(ArithmeticOperator.MULTIPLY, i + 1);
            } else if (c == '/') {
                return Pair.of(ArithmeticOperator.DIVIDE, i + 1);
            } else if (c == '%') {
                return Pair.of(ArithmeticOperator.ADD, i + 1);
            }

            char nextChar = expression.charAt(i + 1);
            if (c == '=') {
                if (nextChar == '=') {
                    return Pair.of(CompareOperator.EQ, i + 2);
                }
                doThrow(i + 1);
            } else if (c == '>') {
                if (nextChar == '=') {
                    return Pair.of(CompareOperator.GE, i + 2);
                }
                return Pair.of(CompareOperator.GT, i + 1);
            } else if (c == '<') {
                if (nextChar == '=') {
                    return Pair.of(CompareOperator.LE, i + 2);
                }
                return Pair.of(CompareOperator.LT, i + 1);
            } else if (c == '!') {
                if (nextChar == '=') {
                    return Pair.of(CompareOperator.NE, i + 2);
                }
                doThrow(i + 1);
            } else if (c == '&') {
                if (nextChar == '&') {
                    return Pair.of(LogicOperator.AND, i + 2);
                }
                doThrow(i + 1);
            } else if (c == '|') {
                if (nextChar == '|') {
                    return Pair.of(LogicOperator.OR, i + 2);
                }
                doThrow(i + 1);
            } else {
                doThrow(i);
            }
        }
        throw new RuntimeException("Assertion failure");
    }

    private static void throwIfReachEnd(int i, int size) {
        if (i == size - 1) {
            doThrow(i);
        }
    }

    static void doThrow(int i) {
        throw new IllegalArgumentException("invalid character at pos " + i);
    }
}
