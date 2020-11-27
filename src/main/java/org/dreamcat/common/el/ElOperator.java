package org.dreamcat.common.el;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;
import lombok.Getter;
import org.dreamcat.common.util.ReflectUtil;

/**
 * Create by tuke on 2020/11/16
 */
public interface ElOperator {

    Map<String, ElOperator> ALIASES = new HashMap<>();

    String getKeyword();

    int getPriority();

    String[] getAliases();

    default UnaryOperator<BigDecimal> getUnaryOperator() {
        throw new UnsupportedOperationException("unary operation for " + getKeyword());
    }

    default BinaryOperator<BigDecimal> getBinaryOperator() {
        throw new UnsupportedOperationException("binary operation for " + getKeyword());
    }

    default boolean isUnary() {
        return false;
    }

    /**
     * unary operation
     *
     * @param value value to be operated
     * @return result of operation
     * @throws UnsupportedOperationException when operation is not supported
     */
    default BigDecimal evaluate(BigDecimal value) {
        return getUnaryOperator().apply(value);
    }

    /**
     * binary operation
     *
     * @param leftValue  left value to be operated
     * @param rightValue right value to be operated
     * @return result of operation
     * @throws UnsupportedOperationException when operation is not supported
     */
    default BigDecimal evaluate(BigDecimal leftValue, BigDecimal rightValue) {
        return getBinaryOperator().apply(leftValue, rightValue);
    }

    interface Unary extends ElOperator {

        @Override
        default boolean isUnary() {
            return true;
        }
    }

    @Getter
    enum Parentheses implements ElOperator {
        LEFT("("),
        RIGHT(")");

        private final String keyword;
        private final int priority = -1;
        private final String[] aliases;

        Parentheses(String keyword, String... aliases) {
            this.keyword = keyword;
            this.aliases = aliases;
        }

        @Override
        public String toString() {
            return keyword;
        }
    }

    static void register(ElOperator... operators) {
        for (ElOperator operator : operators) {
            String[] aliases = operator.getAliases();
            for (String alias : aliases) {
                ALIASES.put(alias, operator);
            }
        }
    }

    static ElOperator lookup(String alias) {
        if (ALIASES.isEmpty()) {
            synchronized (ALIASES) {
                if (ALIASES.isEmpty()) {
                    List<Class<? extends ElOperator>> subClasses = ReflectUtil.retrieveSubClasses(
                            ElOperator.class, ElOperator.class.getName());
                    for (Class<? extends ElOperator> subClass : subClasses) {
                        if (!subClass.isEnum()) continue;
                        ElOperator[] operators = subClass.getEnumConstants();
                        for (ElOperator operator : operators) {
                            String[] operatorAliases = operator.getAliases();
                            for (String operatorAlias : operatorAliases) {
                                ALIASES.put(operatorAlias, operator);
                            }
                        }
                    }
                }
            }
        }
        return ALIASES.get(alias);
    }

    Comparator<ElOperator> COMPARATOR = Comparator.comparingInt(ElOperator::getPriority);
}

