package org.dreamcat.common.el.operation;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.util.function.BinaryOperator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dreamcat.common.el.ElOperator;

/**
 * Create by tuke on 2020/11/20
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LogicOperator implements ElOperator {

    public static final LogicOperator AND = of(
            "&&",
            (a, b) -> !a.equals(ZERO) && !b.equals(ZERO) ? ONE : ZERO,
            "and");
    public static final LogicOperator OR = of(
            "||",
            (a, b) -> !a.equals(ZERO) || !b.equals(ZERO) ? ONE : ZERO,
            "or");

    private final String keyword;
    private final int priority = 4;
    private final BinaryOperator<BigDecimal> binaryOperator;
    private final String[] aliases;

    private static LogicOperator of(
            String keyword,
            BinaryOperator<BigDecimal> binaryOperator, String... aliases) {
        return new LogicOperator(keyword, binaryOperator, aliases);
    }

    static {
        ElOperator.register(AND, OR);
    }

    @Override
    public String toString() {
        return String.join(", ", aliases);
    }
}
