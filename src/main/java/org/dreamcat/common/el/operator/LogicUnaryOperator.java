package org.dreamcat.common.el.operator;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;

import lombok.Getter;
import org.dreamcat.common.el.ElOperator;
import org.dreamcat.common.el.UnaryOp;

/**
 * Create by tuke on 2020/11/23
 */
@Getter
public enum LogicUnaryOperator implements ElOperator.Unary {
    NOT("!", 5,
            (a, option) -> a.equals(ZERO) ? ONE : ZERO, "not");

    private final String keyword;
    private final int priority;
    private final UnaryOp unaryOperator;
    private final String[] aliases;

    LogicUnaryOperator(
            String keyword, int priority,
            UnaryOp unaryOperator,
            String... aliases) {
        this.keyword = keyword;
        this.priority = priority;
        this.unaryOperator = unaryOperator;
        this.aliases = aliases;
    }

    static {
        ElOperator.register(NOT);
    }

    @Override
    public String toString() {
        return String.join(", ", aliases);
    }
}
