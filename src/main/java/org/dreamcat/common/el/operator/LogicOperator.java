package org.dreamcat.common.el.operator;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;

import lombok.Getter;
import org.dreamcat.common.el.BinaryOp;
import org.dreamcat.common.el.ElOperator;

/**
 * Create by tuke on 2020/11/20
 */
@Getter
public enum LogicOperator implements ElOperator {
    AND("&&",
            (a, b, option) -> !a.equals(ZERO) && !b.equals(ZERO) ? ONE : ZERO, "and"),
    OR("||",
            (a, b, option) -> !a.equals(ZERO) || !b.equals(ZERO) ? ONE : ZERO, "or");

    private final String keyword;
    private final int priority = 4;
    private final BinaryOp binaryOperator;
    private final String[] aliases;

    LogicOperator(
            String keyword,
            BinaryOp binaryOperator,
            String... aliases) {
        this.keyword = keyword;
        this.binaryOperator = binaryOperator;
        this.aliases = aliases;
    }

    static {
        ElOperator.register(AND, OR);
    }

    @Override
    public String toString() {
        return String.join(", ", aliases);
    }
}
