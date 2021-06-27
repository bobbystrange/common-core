package org.dreamcat.common.el.unit;

import java.math.BigDecimal;
import org.dreamcat.common.Pair;
import org.dreamcat.common.Triple;
import org.dreamcat.common.el.ElOperator;
import org.dreamcat.common.el.operator.ArithmeticUnaryOperator;
import org.dreamcat.common.el.operator.LogicUnaryOperator;
import org.dreamcat.common.el.util.DALUtil;
import org.dreamcat.common.text.NumericSearcher;
import org.dreamcat.common.text.StringSearcher;
import org.dreamcat.common.util.StringUtil;

/**
 * Create by tuke on 2020/11/20
 */
public final class UnitElStringSplitter {

    // // (((a + 1) * b) and (c / d - 3.14)) or e > 3
    public static UnitElString split(String expression) {
        int size = expression.length();
        Triple<UnitElString, Integer, Integer> triple = search(expression, 0, size, 0);
        int offset = triple.second();
        int closed = triple.third();
        if (offset < size) {
            throw new IllegalArgumentException(invalidChar(offset));
        } else if (closed != 0) {
            throw new IllegalArgumentException(invalidChar(size - 1));
        }
        return triple.first();
    }

    private static Triple<UnitElString, Integer, Integer> search(
            String expression, int offset, int size, int closed) {
        for (int i = offset; i < size; i++) {
            char c = expression.charAt(i);
            if (c <= ' ') continue; // ignore blank chars

            if (c == '(') {
                Triple<UnitElString, Integer, Integer> pair = search(
                        expression, i + 1, size, closed + 1);
                UnitElString string = pair.first();
                int nextOffset = pair.second();
                closed = pair.third();
                if (nextOffset == size) {
                    return Triple.of(string, nextOffset, closed);
                }
                Pair<Integer, Integer> offsetAndClosed = searchRemainingString(
                        string, expression, nextOffset, size, closed);
                i = offsetAndClosed.first();
                closed = offsetAndClosed.second();
                return Triple.of(string, i, closed);
            }
            Pair<UnitElString, Integer> pair;
            if (c == '-' || c == '+' || c == '!') {
                pair = searchArgument(expression, offset + 1, size, closed);
            } else if (c >= '0' && c <= '9') {
                pair = searchValueArgument(expression, i, size, closed);
            } else if (StringUtil.isFirstVariableChar(c)) {
                pair = searchVariableArgument(expression, i);
            } else {
                throw new IllegalArgumentException(invalidChar(i));
            }
            UnitElString argument = pair.first();
            int nextOffset = pair.second();

            UnitElString string = new UnitElString();
            string.addArgument(argument);
            Pair<Integer, Integer> offsetAndClosed = searchRemainingString(
                    string, expression, nextOffset, size, closed);
            i = offsetAndClosed.first();
            closed = offsetAndClosed.second();
            return Triple.of(string, i, closed);
        }
        throw new RuntimeException("Assertion failure");
    }

    private static Pair<UnitElString, Integer> searchArgument(
            String expression, int offset, int size, int closed) {
        for (int i = offset; i < size; i++) {
            char c = expression.charAt(i);
            if (c <= ' ') continue; // ignore blank chars
            if (c == '(') {
                Triple<UnitElString, Integer, Integer> pair = search(
                        expression, i + 1, size, closed + 1);
                UnitElString substring = pair.first();
                int nextOffset = pair.second();
                // closed = pair.third(); // fixme: there maybe some bugs
                return Pair.of(substring, nextOffset);
            } else if (c >= '0' && c <= '9') {
                return searchValueArgument(expression, i, size, closed);
            } else if (StringUtil.isFirstVariableChar(c)) {
                return searchVariableArgument(expression, i);
            } else if (c == '-' || c == '+' || c == '!') {
                return searchUnaryArgument(expression, i, size, c, closed);
            } else {
                throw new IllegalArgumentException(invalidChar(i));
            }
        }
        throw new RuntimeException("Assertion failure");
    }

    private static Pair<UnitElString, Integer> searchValueArgument(
            String expression, int offset, int size, int closed) {
        Pair<Integer, Boolean> pn = NumericSearcher.search(expression, offset);
        int nextOffset = pn.first();
        BigDecimal value = new BigDecimal(expression.substring(offset, nextOffset));
        return Pair.of(new UnitElString(value), nextOffset);
    }

    private static Pair<UnitElString, Integer> searchVariableArgument(
            String expression, int offset) {
        String name = StringSearcher.extractVariable(expression, offset);
        int nextOffset = offset + name.length();
        return Pair.of(new UnitElString(name), nextOffset);
    }

    private static Pair<UnitElString, Integer> searchUnaryArgument(
            String expression, int offset, int size, char c, int closed) {
        boolean negative = c == '-';
        boolean not = c == '!';
        Pair<UnitElString, Integer> pair = searchArgument(expression, offset + 1, size, closed);
        UnitElString substring = pair.first();
        offset = pair.second();
        if (!negative && !not) {
            return pair;
        }
        UnitElString string = new UnitElString();
        string.addArgument(substring);
        if (not) {
            string.addOperation(LogicUnaryOperator.NOT);
        } else {
            string.addOperation(ArithmeticUnaryOperator.NEG);
        }
        return Pair.of(string, offset);
    }

    private static Pair<Integer, Integer> searchRemainingString(
            UnitElString string, String expression, int offset, int size, int closed) {
        // (((a + 1) * b) and (c / d - 3.14)) or e > 3
        if (closed > 0) {
            for (int i = offset; i < size; i++) {
                char c = expression.charAt(i);
                if (c <= ' ') continue;
                if (c == ')') {
                    return Pair.of(i + 1, --closed);
                } else {
                    offset = i;
                    break;
                }
            }
        }

        // search for operations, such as +, -, *, / and so on
        Pair<ElOperator, Integer> operationAndOffset = DALUtil.searchBinaryOperation(
                expression, offset);
        ElOperator operation = operationAndOffset.first();
        offset = operationAndOffset.second();
        string.addOperation(operation);

        Pair<UnitElString, Integer> pair = searchArgument(expression, offset, size, closed);
        string.addArgument(pair.first());
        offset = pair.second();
        if (offset == size) return Pair.of(offset, closed);
        return searchRemainingString(string, expression, offset, size, closed);
    }

    private static String invalidChar(int offset) {
        return "invalid character at pos " + offset;
    }
}
