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
 * Create by tuke on 2020/11/19
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CompareOperator implements ElOperator {

    public static final CompareOperator EQ = of(
            "==",
            (a, b) -> a.equals(b) ? ONE : ZERO,
            "eq");
    public static final CompareOperator NE = of(
            "!=",
            (a, b) -> a.equals(b) ? ZERO : ONE,
            "ne");
    public static final CompareOperator GT = of(
            ">",
            (a, b) -> a.compareTo(b) > 0 ? ONE : ZERO,
            "gt");
    public static final CompareOperator GE = of(
            ">=",
            (a, b) -> a.compareTo(b) >= 0 ? ONE : ZERO,
            "ge");
    public static final CompareOperator LT = of(
            "<",
            (a, b) -> a.compareTo(b) < 0 ? ONE : ZERO,
            "lt");
    public static final CompareOperator LE = of(
            "<=",
            (a, b) -> a.compareTo(b) <= 0 ? ONE : ZERO,
            "le");

    private final String keyword;
    private final int priority = 3;
    private final BinaryOperator<BigDecimal> binaryOperator;
    private final String[] aliases;

    private static CompareOperator of(
            String keyword,
            BinaryOperator<BigDecimal> operator, String... aliases) {
        return new CompareOperator(keyword, operator, aliases);
    }

    static {
        ElOperator.register(EQ, NE, GT, GE, LT, LE);
    }

    @Override
    public String toString() {
        return String.join(", ", aliases);
    }
}
