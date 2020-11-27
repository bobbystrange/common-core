package org.dreamcat.common.el.unit;

import java.math.BigDecimal;
import org.dreamcat.common.el.ElContext;
import org.dreamcat.common.el.ElString;
import org.junit.Before;
import org.junit.Test;

/**
 * Create by tuke on 2020/11/16
 */
public class UnitElStringTest {

    ElContext context = ElContext.getContext();

    @Before
    public void init() {
        context.set("a", 1);
        context.set("b", 1);
        context.set("c", 1);
        context.set("d", 1);
        context.set("e", 1);
    }

    @Test
    public void test() {
        ElString expr;
        BigDecimal result;

        expr = of("+ 1 - - 2 * 3 + 4 / 5");
        result = expr.evaluate();
        System.out.println(expr.getExpression() + "\n" + result);

        expr = of("2 * 3 / 5 - 6");
        result = expr.evaluate();
        System.out.println(expr.getExpression() + "\n" + result);

        expr = of("((2 * 3) / 5) - 6");
        result = expr.evaluate();
        System.out.println(expr.getExpression() + "\n" + result);

        expr = of("(((a + 1) * b) && (c / d - 3.14)) || e > 3");
        result = expr.evaluate(context);
        System.out.println(expr.getExpression() + "\n" + result);
    }

    @Test
    public void evaluateTest() {
        System.out.println(evaluate("1 + 1"));
        System.out.println(evaluate("1 + 2.0"));
        System.out.println(evaluate("3.14 * 5 / 2"));
        System.out.println(evaluate("1 / 2 / 3"));
        System.out.println(evaluate("3 / 2 / 1"));
        System.out.println(evaluate("2 * 3 / 5 - 6"));
        System.out.println(evaluate("((2 * 3) / 5) - 6"));
        System.out.println(evaluate("1 && 1 && 1")); // 1
        System.out.println(evaluate("0 || 0 || 0")); // 0
        System.out.println(evaluate("1 && 1 && 1 || 0")); // 1
        System.out.println(evaluate("1 && 1 && 1 && 0")); // 0
        System.out.println(evaluate("0 || 0 || 0 && 1")); // 0
        System.out.println(evaluate("0 || 0 || 0 || 1")); // 1
    }

    private ElString of(String expression) {
        return new UnitElString(expression);
    }

    private BigDecimal evaluate(String expression) {
        return of(expression).evaluate();
    }
}
