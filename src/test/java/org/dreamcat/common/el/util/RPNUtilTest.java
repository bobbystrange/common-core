package org.dreamcat.common.el.util;

import java.util.List;
import org.junit.Test;

/**
 * Create by tuke on 2020/11/23
 */
public class RPNUtilTest {

    @Test
    public void test() {
        List<Object> formulas;
        formulas = DALUtil.getDAL("((2 * 3) / 5) - 6");
        System.out.println(formulas);
        formulas = RPNUtil.getRPN(formulas);
        System.out.println(formulas);
        System.out.println();

        formulas = DALUtil.getDAL("(((a + 1) * b) && (c / d - 3.14)) || e > 3");
        System.out.println(formulas);
        formulas = RPNUtil.getRPN(formulas);
        System.out.println(formulas);
        System.out.println();

        formulas = DALUtil.getDAL("((1 + 1)");
        System.out.println(formulas);
        formulas = RPNUtil.getRPN(formulas);
        System.out.println(formulas);
    }

}
