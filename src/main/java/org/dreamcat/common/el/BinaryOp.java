package org.dreamcat.common.el;

import java.math.BigDecimal;

/**
 * Create by tuke on 2020/11/28
 */
@FunctionalInterface
public
interface BinaryOp {

    /**
     * binary operation
     *
     * @param leftValue  left value to be operated
     * @param rightValue right value to be operated
     * @return result of operation
     * @throws UnsupportedOperationException when operation is not supported
     */
    BigDecimal evaluate(BigDecimal leftValue, BigDecimal rightValue, ElOption option);
}
