package org.dreamcat.common.el.operator;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;

import lombok.Getter;
import org.dreamcat.common.el.BinaryOp;
import org.dreamcat.common.el.ElOperator;

/**
 * Create by tuke on 2020/11/19
 */
@Getter
public enum CompareOperator implements ElOperator {
    EQ("==",
            (a, b, option) -> a.equals(b) ? ONE : ZERO, "eq"),
    NE("!=",
            (a, b, option) -> a.equals(b) ? ZERO : ONE, "ne"),
    GT(">",
            (a, b, option) -> a.compareTo(b) > 0 ? ONE : ZERO, "gt"),
    GE(">=",
            (a, b, option) -> a.compareTo(b) >= 0 ? ONE : ZERO, "ge"),
    LT("<",
            (a, b, option) -> a.compareTo(b) < 0 ? ONE : ZERO, "lt"),
    LE("<=",
            (a, b, option) -> a.compareTo(b) <= 0 ? ONE : ZERO, "le");

    private final String keyword;
    private final int priority = 3;
    private final BinaryOp binaryOperator;
    private final String[] aliases;

    CompareOperator(
            String keyword,
            BinaryOp binaryOperator,
            String... aliases) {
        this.keyword = keyword;
        this.binaryOperator = binaryOperator;
        this.aliases = aliases;
    }

    static {
        ElOperator.register(EQ, NE, GT, GE, LT, LE);
    }

    @Override
    public String toString() {
        return String.join(", ", aliases);
    }
}
