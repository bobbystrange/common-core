package org.dreamcat.common.el;

import java.math.BigDecimal;
import org.dreamcat.common.el.stack.StackElString;

/**
 * Create by tuke on 2020/10/26
 */
public interface ElEngine {

    default BigDecimal evaluate(String expression) {
        return createEL(expression).evaluate();
    }

    default BigDecimal evaluate(String expression, ElContext context) {
        return createEL(expression).evaluate(context);
    }

    ElString createEL(String expression);

    static ElEngine getEngine() {
        return new StackElString.ElEngineImpl();
    }
}
