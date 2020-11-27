package org.dreamcat.common.el.operation;

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
public class ArithmeticUnaryOperator implements ElOperator.Unary {

    public static final ArithmeticUnaryOperator NEG = of(
            "-", 5,
            BigDecimal::negate,
            "neg");

    private final String keyword;
    private final int priority;
    private final UnaryOperator<BigDecimal> unaryOperator;
    private final String[] aliases;

    private static ArithmeticUnaryOperator of(
            String keyword, int priority,
            UnaryOperator<BigDecimal> unaryOperator, String... aliases) {
        return new ArithmeticUnaryOperator(keyword, priority, unaryOperator, aliases);
    }

    static {
        ElOperator.register(NEG);
    }

    @Override
    public String toString() {
        return String.join(", ", aliases);
    }
}
