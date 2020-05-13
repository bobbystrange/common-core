package org.dreamcat.common.util;

import org.junit.Test;

import static org.dreamcat.common.util.PrintUtil.println;

/**
 * Create by tuke on 2020/5/7
 */
public class StringUtilTest {

    @Test
    public void testExtractNumber() {
        println(StringUtil.extractInt("11", 0));
        println(StringUtil.extractInt("+1.1", 0));
        println(StringUtil.extractInt("3.14e-2", 0));
        println(StringUtil.extractInt("-3.14e+2", 0));
        println();

        println(StringUtil.extractFloat("11", 0));
        println(StringUtil.extractFloat("+1.1", 0));
        println(StringUtil.extractFloat("3.14e-2", 0));
        println(StringUtil.extractFloat("-3.14e+2", 0));
        println();

        println(StringUtil.extractNumber("11", 0));
        println(StringUtil.extractNumber("+1.1", 0));
        println(StringUtil.extractNumber("3.14e-2", 0));
        println(StringUtil.extractNumber("-3.14e+2", 0));

        println();
        println(StringUtil.extractNumber("11, 12", 0));

    }
}
