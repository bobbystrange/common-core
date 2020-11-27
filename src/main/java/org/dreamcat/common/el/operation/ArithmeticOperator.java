package org.dreamcat.common.el.operation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.BinaryOperator;
import lombok.Getter;
import org.dreamcat.common.el.ElOperator;

/**
 * Create by tuke on 2020/11/16
 */
@Getter
public enum ArithmeticOperator implements ElOperator {
    ADD("+", 1, BigDecimal::add, "add"),
    SUBTRACT("-", 1, BigDecimal::subtract, "sub"),
    MULTIPLY("*", 2, BigDecimal::multiply, "mul"),
    DIVIDE("/", 2,
            (a, b) -> a.divide(b, 6, RoundingMode.HALF_EVEN),
            "div"),
    REMAINDER("%", 2, BigDecimal::remainder, "rem");

    private final String keyword;
    private final int priority;
    private final BinaryOperator<BigDecimal> binaryOperator;
    private final String[] aliases;

    static {
        ElOperator.register(ADD, SUBTRACT, MULTIPLY, DIVIDE, REMAINDER);
    }

    ArithmeticOperator(String keyword, int priority,
            BinaryOperator<BigDecimal> binaryOperator, String... aliases) {
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
        return a.divide(b, 6, RoundingMode.HALF_EVEN);
    }
}
