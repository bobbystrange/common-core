package org.dreamcat.common.el.stack;

import org.dreamcat.common.el.ElString;
import org.junit.Test;

/**
 * Create by tuke on 2020/11/23
 */
public class StackElStringEdgeTest {

    @Test
    public void test1() {
        System.out.println(evaluate("1 + 1"));
        System.out.println(evaluate("(1 + 1"));
        System.out.println(evaluate("1 (+ 1"));
        System.out.println(evaluate("1 + (1"));
        System.out.println(evaluate("1 + 1("));

        System.out.println(evaluate(")1 + 1"));
        System.out.println(evaluate("1 )+ 1"));
        System.out.println(evaluate("1 + )1"));
        System.out.println(evaluate("1 + 1)"));
    }

    @Test
    public void test2() {
        System.out.println(evaluate("1 + 1"));
        System.out.println(evaluate("(1 + 1)"));
        System.out.println(evaluate("((1 + 1))"));
        System.out.println(evaluate("((1 + 1)"));
        System.out.println(evaluate("(1 + 1))"));
        System.out.println(evaluate("(((1 + 1)))"));
        System.out.println(evaluate("((1 + 1)))"));
        System.out.println(evaluate("(((1 + 1))"));

        System.out.println(evaluate("1 + 1 / 3"));
        System.out.println(evaluate("(1 + 1 / 3)"));
        System.out.println(evaluate("((1 + 1 / 3))"));
        System.out.println(evaluate("(1 + 1 / 3))"));
        System.out.println(evaluate("((1 + 1 / 3)"));
        System.out.println(evaluate("(((1 + 1 / 3)))"));
        System.out.println(evaluate("((1 + 1 / 3)))"));
        System.out.println(evaluate("(((1 + 1 / 3))"));
    }

    private ElString of(String expression) {
        return new StackElString(expression);
    }

    private String evaluate(String expression) {
        try {
            return expression + "\t\t\t" + of(expression).evaluate().toString();
        } catch (Exception e) {
            // if (true) throw new RuntimeException(e);
            return expression + "\t\t\t" + e.getMessage();
        }
    }

}
