package org.dreamcat.common.util;

import org.junit.Test;

/**
 * Create by tuke on 2020/5/7
 */
public class StringUtilTest {

    @Test
    public void toCamelCase() {
        System.out.println(StringUtil.toCamelCase("howDoYouFeel"));
        System.out.println(StringUtil.toCamelCase("WhereWillWeGoNow"));
        System.out.println(StringUtil.toCamelCase("who_am_i"));
        System.out.println(StringUtil.toCamelCase("WHY_SO_CONFUSED"));
        System.out.println(StringUtil.toCamelCase("WH_a_sO_con_fUs_eD"));
        System.out.println();
        System.out.println(StringUtil.toSnakeCase("howDoYouFeel"));
        System.out.println(StringUtil.toSnakeCase("WhereWillWeGoNow"));
        System.out.println(StringUtil.toSnakeCase("who_am_i"));
        System.out.println(StringUtil.toSnakeCase("WHY_SO_CONFUSED"));
        System.out.println(StringUtil.toSnakeCase("WH_a_sO_con_fUs_eD"));
    }
}
