package org.dreamcat.common.el;

import java.math.BigDecimal;

/**
 * Create by tuke on 2020/11/28
 */
@FunctionalInterface
public
interface UnaryOp {

    /**
     * unary operation
     *
     * @param value value to be operated
     * @return result of operation
     * @throws UnsupportedOperationException when operation is not supported
     */
    BigDecimal evaluate(BigDecimal value, ElOption option);
}
