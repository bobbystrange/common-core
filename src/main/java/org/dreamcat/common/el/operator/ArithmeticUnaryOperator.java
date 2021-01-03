package org.dreamcat.common.el.operator;

import lombok.Getter;
import org.dreamcat.common.el.ElOperator;
import org.dreamcat.common.el.UnaryOp;

/**
 * Create by tuke on 2020/11/23
 */
@Getter
public enum ArithmeticUnaryOperator implements ElOperator.Unary {
    NEG("-", 5,
            (a, option) -> a.negate(), "neg");

    private final String keyword;
    private final int priority;
    private final UnaryOp unaryOperator;
    private final String[] aliases;

    ArithmeticUnaryOperator(
            String keyword, int priority,
            UnaryOp unaryOperator,
            String... aliases) {
        this.keyword = keyword;
        this.priority = priority;
        this.unaryOperator = unaryOperator;
        this.aliases = aliases;
    }

    static {
        ElOperator.register(NEG);
    }

    @Override
    public String toString() {
        return String.join(", ", aliases);
    }
}
