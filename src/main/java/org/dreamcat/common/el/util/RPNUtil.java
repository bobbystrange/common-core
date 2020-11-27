package org.dreamcat.common.el.util;

import static org.dreamcat.common.el.ElOperator.COMPARATOR;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.dreamcat.common.el.ElOperator;
import org.dreamcat.common.el.ElOperator.Parentheses;

/**
 * Create by tuke on 2020/11/23
 */
public final class RPNUtil {

    /**
     * Direct Algebraic Logic to Reverse Polish Notation
     * <p>
     * Object is one of {@link BigDecimal}, {@link String} and {@link ElOperator}
     *
     * @param adlFormulas Direct Algebraic Logic (ADL) used on standard calculators
     * @return RPN
     */
    public static List<Object> getRPN(List<Object> adlFormulas) {
        List<Object> rpnFormulas = new ArrayList<>(adlFormulas.size());
        LinkedList<ElOperator> operatorStack = new LinkedList<>();
        for (Object formula : adlFormulas) {
            if (formula instanceof Parentheses) {
                if (formula.equals(Parentheses.LEFT)) {
                    operatorStack.push(Parentheses.LEFT);
                } else {
                    while (!Parentheses.LEFT.equals(operatorStack.peek())) {
                        rpnFormulas.add(operatorStack.pop());
                    }
                    operatorStack.pop();
                }
            } else if (formula instanceof ElOperator) {
                ElOperator operator = (ElOperator) formula;
                while (!operatorStack.isEmpty() &&
                        COMPARATOR.compare(operatorStack.peek(), operator) >= 0) {
                    rpnFormulas.add(operatorStack.pop());
                }
                operatorStack.push(operator);
            } else {
                rpnFormulas.add(formula);
            }
        }
        while (!operatorStack.isEmpty()) {
            rpnFormulas.add(operatorStack.pop());
        }
        return rpnFormulas;
    }

    /**
     * Reverse Polish Notation
     *
     * @param arguments not empty
     * @param operators not empty, require {@code operators.size() = arguments.size() - 1 || operators.size() = arguments.size()}
     * @return RPN
     */
    public static List<Object> getRPN(List<?> arguments,
            List<ElOperator> operators) {
        List<Object> formulas = new ArrayList<>(arguments.size() + operators.size());
        LinkedList<ElOperator> operatorStack = new LinkedList<>();
        int size = operators.size();
        for (int i = 0; i < size; i++) {
            Object argument = arguments.get(i);
            ElOperator operator = operators.get(i);
            formulas.add(argument);
            while (!operatorStack.isEmpty() &&
                    COMPARATOR.compare(operatorStack.peek(), operator) >= 0) {
                formulas.add(operatorStack.pop());
            }
            operatorStack.push(operator);
        }
        if (arguments.size() > size) {
            formulas.add(arguments.get(size));
        }

        while (!operatorStack.isEmpty()) {
            formulas.add(operatorStack.pop());
        }
        return formulas;
    }
}
