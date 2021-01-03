package org.dreamcat.common.el.stack;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import org.dreamcat.common.el.ElContext;
import org.dreamcat.common.el.ElEngine;
import org.dreamcat.common.el.ElOperator;
import org.dreamcat.common.el.ElOption;
import org.dreamcat.common.el.ElString;
import org.dreamcat.common.el.util.DALUtil;
import org.dreamcat.common.el.util.RPNUtil;

/**
 * Create by tuke on 2020/11/21
 */
public class StackElString implements ElString {

    final String expression;
    /**
     * Object is one of {@link BigDecimal}, {@link String} and {@link ElOperator}
     */
    final List<Object> formulas;

    StackElString(String expression) {
        this.expression = expression;
        List<Object> dalFormulas = DALUtil.getDAL(expression);
        this.formulas = RPNUtil.getRPN(dalFormulas);
    }

    @Override
    public String getExpression() {
        return expression;
    }

    @Override
    public BigDecimal evaluate(ElContext context) {
        ElOption option = context.getOption();
        LinkedList<BigDecimal> stack = new LinkedList<>();
        BigDecimal evaluated;
        for (Object formula : formulas) {
            if (formula instanceof BigDecimal) {
                stack.push((BigDecimal) formula);
                continue;
            } else if (formula instanceof String) {
                String variable = (String) formula;
                evaluated = context.get(variable);
                if (evaluated == null) {
                    throw new NoSuchElementException(variable);
                }
                stack.push(evaluated);
                continue;
            }

            ElOperator operator = (ElOperator) formula;
            BigDecimal right = stack.pop();
            if (operator.isUnary()) {
                evaluated = operator.evaluate(right, option);
                stack.push(evaluated);
                continue;
            }
            BigDecimal left = stack.pop();
            evaluated = operator.evaluate(left, right, option);
            stack.push(evaluated);
        }
        return stack.pop();
    }

    public static class ElEngineImpl implements ElEngine {

        @Override
        public ElString createEL(String expression) {
            return new StackElString(expression);
        }
    }

}
