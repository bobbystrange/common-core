package org.dreamcat.common.el;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

/**
 * Create by tuke on 2020/10/26
 */
public interface ElString {

    String getExpression();

    default BigDecimal evaluate() {
        return evaluate(ElContext.EMPTY);
    }

    /**
     * evaluate for <strong>Expression Language</strong>
     *
     * @param context context which holds the variables
     * @return evaluated result
     * @throws NoSuchElementException if any variable is not found in context
     */
    BigDecimal evaluate(ElContext context);
}
