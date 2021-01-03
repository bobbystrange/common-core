package org.dreamcat.common.el.operator;

import static java.math.RoundingMode.HALF_EVEN;

import java.math.BigDecimal;
import lombok.Getter;
import org.dreamcat.common.el.BinaryOp;
import org.dreamcat.common.el.ElOperator;

/**
 * Create by tuke on 2020/11/16
 */
@Getter
public enum ArithmeticOperator implements ElOperator {
    ADD("+", 1,
            (a, b, option) -> a.add(b), "add", "add"),
    SUBTRACT("-", 1,
            (a, b, option) -> a.subtract(b), "sub", "subtract"),
    MULTIPLY("*", 2,
            (a, b, option) -> a.multiply(b), "mul", "multiply"),
    DIVIDE("/", 2,
            (a, b, option) -> a.divide(b, option.getScale(), option.getRoundingMode()),
            "div", "divide"),
    REMAINDER("%", 2,
            (a, b, option) -> a.remainder(b), "rem", "remainder");

    private final String keyword;
    private final int priority;
    private final BinaryOp binaryOperator;
    private final String[] aliases;

    static {
        ElOperator.register(ADD, SUBTRACT, MULTIPLY, DIVIDE, REMAINDER);
    }

    ArithmeticOperator(
            String keyword, int priority,
            BinaryOp binaryOperator, String... aliases) {
        this.keyword = keyword;
        this.priority = priority;
        this.binaryOperator = binaryOperator;
        this.aliases = aliases;
    }

    @Override
    public String toString() {
        return String.join(", ", aliases);
    }

    private BigDecimal divide(BigDecimal a, BigDecimal b) {
        return a.divide(b, 6, HALF_EVEN);
    }
}
