package org.dreamcat.common.el.operation;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.util.function.UnaryOperator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dreamcat.common.el.ElOperator;

/**
 * Create by tuke on 2020/11/23
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LogicUnaryOperator implements ElOperator.Unary {

    public static final LogicUnaryOperator NOT = of(
            "!", 5,
            a -> a.equals(ZERO) ? ONE : ZERO,
            "not");

    private final String keyword;
    private final int priority;
    private final UnaryOperator<BigDecimal> unaryOperator;
    private final String[] aliases;

    private static LogicUnaryOperator of(
            String keyword, int priority,
            UnaryOperator<BigDecimal> unaryOperator, String... aliases) {
        return new LogicUnaryOperator(keyword, priority, unaryOperator, aliases);
    }

    static {
        ElOperator.register(NOT);
    }

    @Override
    public String toString() {
        return String.join(", ", aliases);
    }
}
