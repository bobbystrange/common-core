package org.dreamcat.common.el.unit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.Getter;
import org.dreamcat.common.el.ElContext;
import org.dreamcat.common.el.ElEngine;
import org.dreamcat.common.el.ElOperator;
import org.dreamcat.common.el.ElOption;
import org.dreamcat.common.el.ElString;
import org.dreamcat.common.el.util.RPNUtil;

/**
 * Create by tuke on 2020/11/16
 */
@Getter
public class UnitElString implements ElString {

    List<UnitElString> arguments;
    List<ElOperator> operators;
    String name;
    BigDecimal value;
    List<Object> formulas;

    UnitElString() {
    }

    UnitElString(String name) {
        this.name = name;
    }

    UnitElString(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String getExpression() {
        if (value != null) return value.toString();
        if (name != null) return name;

        StringBuilder expression = new StringBuilder();
        // unary operator
        if (arguments.size() == 1) {
            UnitElString argument = arguments.get(0);
            ElOperator operator = operators.get(0);
            expression.append(operator.getKeyword());
            argument.fillClosedExpression(expression);
            return expression.toString();
        }

        int size = operators.size();
        for (int i = 0; i < size; i++) {
            UnitElString argument = arguments.get(i);
            argument.fillClosedExpression(expression);
            ElOperator operator = operators.get(i);
            expression.append(" ").append(operator.getKeyword()).append(" ");
        }

        UnitElString argument = arguments.get(size);
        argument.fillClosedExpression(expression);
        return expression.toString();
    }


    @Override
    public BigDecimal evaluate(ElContext context) {
        if (value != null) {
            return value;
        }
        if (name != null) {
            BigDecimal variableValue = context.get(name);
            if (variableValue == null) {
                throw new NoSuchElementException(name);
            }
            return variableValue.stripTrailingZeros();
        }

        calculateFormulas();
        ElOption option = context.getOption();
        LinkedList<BigDecimal> stack = new LinkedList<>();
        BigDecimal evaluated;
        for (Object formula : formulas) {
            if (formula instanceof ElString) {
                evaluated = ((ElString) formula).evaluate(context);
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

    void addArgument(UnitElString argument) {
        if (arguments == null) arguments = new ArrayList<>();
        arguments.add(argument);
    }

    void addOperation(ElOperator operation) {
        if (operators == null) operators = new ArrayList<>();
        operators.add(operation);
    }

    private synchronized void calculateFormulas() {
        if (formulas == null) {
            formulas = RPNUtil.getRPN(arguments, operators);
        }
    }

    private void fillClosedExpression(StringBuilder s) {
        if (name != null || value != null) {
            s.append(getExpression());
            return;
        }
        s.append("(").append(getExpression()).append(")");
    }

    public static class ElEngineImpl implements ElEngine {

        @Override
        public ElString createEL(String expression) {
            return UnitElStringSplitter.split(expression);
        }
    }
}
