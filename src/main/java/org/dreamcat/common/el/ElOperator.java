package org.dreamcat.common.el;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.dreamcat.common.util.ReflectUtil;

/**
 * Create by tuke on 2020/11/16
 */
public interface ElOperator extends UnaryOp, BinaryOp {

    Map<String, ElOperator> ALIASES_MAPPINGS = new HashMap<>();

    String getKeyword();

    int getPriority();

    String[] getAliases();

    default UnaryOp getUnaryOperator() {
        throw new UnsupportedOperationException("unary operation for " + getKeyword());
    }

    default BinaryOp getBinaryOperator() {
        throw new UnsupportedOperationException("binary operation for " + getKeyword());
    }

    default boolean isUnary() {
        return false;
    }

    @Override
    default BigDecimal evaluate(BigDecimal value, ElOption option) {
        return getUnaryOperator().evaluate(value, option);
    }

    @Override
    default BigDecimal evaluate(BigDecimal leftValue, BigDecimal rightValue, ElOption option) {
        return getBinaryOperator().evaluate(leftValue, rightValue, option);
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

    static ElOperator lookup(String alias) {
        if (ALIASES_MAPPINGS.isEmpty()) {
            synchronized (ALIASES_MAPPINGS) {
                if (ALIASES_MAPPINGS.isEmpty()) {
                    List<Class<? extends ElOperator>> subClasses = ReflectUtil.retrieveSubClasses(
                            ElOperator.class, ElOperator.class.getName());
                    for (Class<? extends ElOperator> subClass : subClasses) {
                        if (!subClass.isEnum()) continue;
                        register(subClass.getEnumConstants());
                    }
                }
            }
        }
        return ALIASES_MAPPINGS.get(alias);
    }

    static void register(ElOperator... operators) {
        for (ElOperator operator : operators) {
            String[] aliases = operator.getAliases();
            for (String alias : aliases) {
                ALIASES_MAPPINGS.put(alias, operator);
            }
        }
    }

    Comparator<ElOperator> COMPARATOR = Comparator.comparingInt(ElOperator::getPriority);
}

