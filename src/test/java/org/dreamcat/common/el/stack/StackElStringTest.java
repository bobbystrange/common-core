package org.dreamcat.common.el.stack;

import static org.dreamcat.common.el.operator.ArithmeticOperator.ADD;
import static org.dreamcat.common.el.operator.ArithmeticOperator.DIVIDE;
import static org.dreamcat.common.el.operator.ArithmeticOperator.MULTIPLY;
import static org.dreamcat.common.el.operator.ArithmeticOperator.SUBTRACT;
import static org.dreamcat.common.util.RandomUtil.rand;
import static org.dreamcat.common.util.RandomUtil.randi;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.dreamcat.common.core.Timeit;
import org.dreamcat.common.el.ElContext;
import org.dreamcat.common.el.ElOperator;
import org.dreamcat.common.el.ElString;
import org.dreamcat.common.el.unit.UnitElStringSplitter;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Create by tuke on 2020/11/23
 */
public class StackElStringTest {

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

    @Test
    public void randomExpressionTest() {
        System.out.println(randomExpression(3, 4, false));
        System.out.println(randomExpression(3, 4, false));
        System.out.println(randomExpression(3, 4, false));
        System.out.println(randomExpression(3, 4, false));
    }

    @Ignore
    @Test
    public void testSpeed() {
        System.out.println("depth \t width \t ");
        for (int depth = 1; depth <= 12; depth++) {
            for (int width = 2; width <= (1 << 2); width *= 2) {
                int d = depth;
                int w = width;
                long[] ts = Timeit.ofActions()
                        .count(3).skip(1).repeat(1)
                        .addUnaryAction(() -> randomExpression(
                                d, w, false), this::evaluate)
                        .addUnaryAction(() -> randomExpression(
                                d, w, false), this::evaluateByUnit)
                        // .addUnaryAction(() -> randomExpression(
                        //         d, w, true), this::evaluate)
                        // .addUnaryAction(() -> randomExpression(
                        //         d, w, true), this::evaluateByUnit)
                        .run();
                String us = Arrays.stream(ts)
                        .mapToObj(it -> String.format("%06.2f%s", it / 1000.0, "us"))
                        .collect(Collectors.joining("\t"));
                System.out.printf("%d %03d %s\n", depth, width, us);
            }
        }
    }

    private ElString of(String expression) {
        return new StackElString(expression);
    }

    private BigDecimal evaluate(String expression) {
        return of(expression).evaluate();
    }

    private BigDecimal evaluateByUnit(String expression) {
        return UnitElStringSplitter.split(expression).evaluate();
    }

    private String randomExpression(int depth, int width, boolean floating) {
        StringBuilder s = new StringBuilder();
        if (depth > 1) {
            for (int i = 0; i < width; i++) {
                s.append("(")
                        .append(randomExpression(depth - 1, width, floating))
                        .append(")");
                if (i == width - 1) break;
                s.append(" ")
                        .append(operators[randi(3)].getKeyword())
                        .append(" ");
            }
        } else {
            for (int i = 0; i < width; i++) {
                if (floating) {
                    s.append(rand(-Math.PI, Math.PI));
                    if (i == width - 1) break;
                    s.append(" ")
                            .append(floating_operators[randi(4)].getKeyword())
                            .append(" ");
                } else {
                    s.append(randi(width * 2));
                    if (i == width - 1) break;
                    s.append(" ")
                            .append(operators[randi(3)].getKeyword())
                            .append(" ");
                }
            }
        }
        return s.toString();
    }

    static ElOperator[] operators = new ElOperator[]{
            ADD, SUBTRACT, MULTIPLY};
    static ElOperator[] floating_operators = new ElOperator[]{
            ADD, SUBTRACT, MULTIPLY, DIVIDE};
}
