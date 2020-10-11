package org.dreamcat.common.util;

import org.dreamcat.common.io.IOUtil;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

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

    @Test
    public void extractNumberInteger() {
        // int
        System.out.println(StringUtil.extractNumber("11", 0));
        System.out.println(StringUtil.extractNumber("+11", 0));
        System.out.println(StringUtil.extractNumber("-11", 0));
        System.out.println(StringUtil.extractNumber("11+11", 0));
        System.out.println(StringUtil.extractNumber("+11-11", 0));
        System.out.println(StringUtil.extractNumber("-11+11", 0));
        System.out.println();

        // long
        System.out.println(StringUtil.extractNumber("68719476736", 0));
        System.out.println(StringUtil.extractNumber("+68719476736", 0));
        System.out.println(StringUtil.extractNumber("-68719476736", 0));
        System.out.println(StringUtil.extractNumber("68719476736+68719476736", 0));
        System.out.println(StringUtil.extractNumber("+68719476736-68719476736", 0));
        System.out.println(StringUtil.extractNumber("-68719476736+68719476736", 0));
    }

    /// [+-], [0-9, [.], [eE], other chars

    // [+-]?[0-9]*[.]?[0-9]*([eE][+-]?[0-9]+)?
    @Test
    public void extractNumberFloat() {
        // 3.
        System.out.println(StringUtil.extractNumber("3.", 0));
        System.out.println(StringUtil.extractNumber("+3..", 0));
        System.out.println(StringUtil.extractNumber("-3.-", 0));
        System.out.println(StringUtil.extractNumber("3.+", 0));
        System.out.println(StringUtil.extractNumber("3.e", 0));
        System.out.println(StringUtil.extractNumber("3.xxx", 0));
        System.out.println();

        // 3.14.
        System.out.println(StringUtil.extractNumber("3.14.", 0));
        System.out.println(StringUtil.extractNumber("+3.14..", 0));
        System.out.println(StringUtil.extractNumber("-3.14.-", 0));
        System.out.println(StringUtil.extractNumber("3.14.+", 0));
        System.out.println(StringUtil.extractNumber("3.14.e", 0));
        System.out.println(StringUtil.extractNumber("3.14.xxx", 0));
        System.out.println();

        // 3.14
        System.out.println(StringUtil.extractNumber("3.14-", 0));
        System.out.println(StringUtil.extractNumber("3.14+", 0));
        System.out.println(StringUtil.extractNumber("3.14xx", 0));
    }

    // 3.14e
    @Test
    public void extractNumberFloatEdge1() {
        System.out.println(StringUtil.extractNumber("3.14e", 0));

        System.out.println();
        System.out.println(StringUtil.extractNumber("3.14ee", 0));
        System.out.println(StringUtil.extractNumber("3.14e.", 0));
        System.out.println(StringUtil.extractNumber("3.14e.", 0));
        System.out.println(StringUtil.extractNumber("3.14exxx", 0));

        System.out.println();
        System.out.println(StringUtil.extractNumber("+3.14ee", 0));
        System.out.println(StringUtil.extractNumber("+3.14e.", 0));
        System.out.println(StringUtil.extractNumber("+3.14e.", 0));
        System.out.println(StringUtil.extractNumber("+3.14exxx", 0));

        System.out.println();
        System.out.println(StringUtil.extractNumber("-3.14ee", 0));
        System.out.println(StringUtil.extractNumber("-3.14e.", 0));
        System.out.println(StringUtil.extractNumber("-3.14e.", 0));
        System.out.println(StringUtil.extractNumber("-3.14exxx", 0));
    }

    // 3.14e-
    @Test
    public void extractNumberFloatEdge2() {
        System.out.println(StringUtil.extractNumber("3.14e-", 0));
        System.out.println(StringUtil.extractNumber("3.14e+", 0));

        System.out.println();
        System.out.println(StringUtil.extractNumber("3.14e-+", 0));
        System.out.println(StringUtil.extractNumber("3.14e--", 0));
        System.out.println(StringUtil.extractNumber("3.14e-.", 0));
        System.out.println(StringUtil.extractNumber("3.14e-xxx", 0));
        System.out.println(StringUtil.extractNumber("3.14e++", 0));
        System.out.println(StringUtil.extractNumber("3.14e+-", 0));
        System.out.println(StringUtil.extractNumber("3.14e+.", 0));
        System.out.println(StringUtil.extractNumber("3.14e+xxx", 0));

        System.out.println();
        System.out.println(StringUtil.extractNumber("+3.14e-+", 0));
        System.out.println(StringUtil.extractNumber("+3.14e--", 0));
        System.out.println(StringUtil.extractNumber("+3.14e-.", 0));
        System.out.println(StringUtil.extractNumber("+3.14e-xxx", 0));
        System.out.println(StringUtil.extractNumber("+3.14e++", 0));
        System.out.println(StringUtil.extractNumber("+3.14e+-", 0));
        System.out.println(StringUtil.extractNumber("+3.14e+.", 0));
        System.out.println(StringUtil.extractNumber("+3.14e+xxx", 0));

        System.out.println();
        System.out.println(StringUtil.extractNumber("-3.14e-+", 0));
        System.out.println(StringUtil.extractNumber("-3.14e--", 0));
        System.out.println(StringUtil.extractNumber("-3.14e-.", 0));
        System.out.println(StringUtil.extractNumber("-3.14e-xxx", 0));
        System.out.println(StringUtil.extractNumber("-3.14e++", 0));
        System.out.println(StringUtil.extractNumber("-3.14e+-", 0));
        System.out.println(StringUtil.extractNumber("-3.14e+.", 0));
        System.out.println(StringUtil.extractNumber("-3.14e+xxx", 0));
    }

    // 3.14e-2
    @Test
    public void extractNumberFloatEdge3() {
        System.out.println(StringUtil.extractNumber("23.14e-2-", 0));
        System.out.println(StringUtil.extractNumber("23.14e-2+", 0));
        System.out.println(StringUtil.extractNumber("23.14e-2.", 0));
        System.out.println(StringUtil.extractNumber("23.14e-2e", 0));
        System.out.println(StringUtil.extractNumber("23.14e+2E", 0));
        System.out.println(StringUtil.extractNumber("23.14e-2xxx", 0));
        System.out.println(StringUtil.extractNumber("3.14e-23-", 0));
        System.out.println(StringUtil.extractNumber("3.14e-23+", 0));
        System.out.println(StringUtil.extractNumber("3.14e-23.", 0));
        System.out.println(StringUtil.extractNumber("3.14e-23e", 0));
        System.out.println(StringUtil.extractNumber("3.14e+23E", 0));
        System.out.println(StringUtil.extractNumber("3.14e-23xxx", 0));

        System.out.println(StringUtil.extractNumber("+3.14e-2-", 0));
        System.out.println(StringUtil.extractNumber("+3.14e-2+", 0));
        System.out.println(StringUtil.extractNumber("+3.14e-2.", 0));
        System.out.println(StringUtil.extractNumber("+3.14e-2e", 0));
        System.out.println(StringUtil.extractNumber("+3.14e+2E", 0));
        System.out.println(StringUtil.extractNumber("+3.14e-2xxx", 0));
        System.out.println(StringUtil.extractNumber("+3.14e-23-", 0));
        System.out.println(StringUtil.extractNumber("+3.14e-23+", 0));
        System.out.println(StringUtil.extractNumber("+3.14e-23.", 0));
        System.out.println(StringUtil.extractNumber("+3.14e-23e", 0));
        System.out.println(StringUtil.extractNumber("+3.14e+23E", 0));
        System.out.println(StringUtil.extractNumber("+3.14e-23xxx", 0));

        System.out.println(StringUtil.extractNumber("-3.14e-2-", 0));
        System.out.println(StringUtil.extractNumber("-3.14e-2+", 0));
        System.out.println(StringUtil.extractNumber("-3.14e-2.", 0));
        System.out.println(StringUtil.extractNumber("-3.14e-2e", 0));
        System.out.println(StringUtil.extractNumber("-3.14e+2E", 0));
        System.out.println(StringUtil.extractNumber("-3.14e-2xxx", 0));
        System.out.println(StringUtil.extractNumber("-3.14e-23-", 0));
        System.out.println(StringUtil.extractNumber("-3.14e-23+", 0));
        System.out.println(StringUtil.extractNumber("-3.14e-23.", 0));
        System.out.println(StringUtil.extractNumber("-3.14e-23e", 0));
        System.out.println(StringUtil.extractNumber("-3.14e+23E", 0));
        System.out.println(StringUtil.extractNumber("-3.14e-23xxx", 0));
    }

    //3.14e2
    @Test
    public void extractNumberFloatEdge4() {
        System.out.println(StringUtil.extractNumber("23.14e2-", 0));
        System.out.println(StringUtil.extractNumber("23.14e2+", 0));
        System.out.println(StringUtil.extractNumber("23.14e2.", 0));
        System.out.println(StringUtil.extractNumber("23.14e2e", 0));
        System.out.println(StringUtil.extractNumber("23.14e2E", 0));
        System.out.println(StringUtil.extractNumber("23.14e2xxx", 0));
        System.out.println(StringUtil.extractNumber("3.14e23-", 0));
        System.out.println(StringUtil.extractNumber("3.14e23+", 0));
        System.out.println(StringUtil.extractNumber("3.14e23.", 0));
        System.out.println(StringUtil.extractNumber("3.14e23e", 0));
        System.out.println(StringUtil.extractNumber("3.14e23E", 0));
        System.out.println(StringUtil.extractNumber("3.14e23xxx", 0));

        System.out.println(StringUtil.extractNumber("+3.14e2-", 0));
        System.out.println(StringUtil.extractNumber("+3.14e2+", 0));
        System.out.println(StringUtil.extractNumber("+3.14e2.", 0));
        System.out.println(StringUtil.extractNumber("+3.14e2e", 0));
        System.out.println(StringUtil.extractNumber("+3.14e+2E", 0));
        System.out.println(StringUtil.extractNumber("+3.14e2xxx", 0));
        System.out.println(StringUtil.extractNumber("+3.14e23-", 0));
        System.out.println(StringUtil.extractNumber("+3.14e23+", 0));
        System.out.println(StringUtil.extractNumber("+3.14e23.", 0));
        System.out.println(StringUtil.extractNumber("+3.14e23e", 0));
        System.out.println(StringUtil.extractNumber("+3.14e23E", 0));
        System.out.println(StringUtil.extractNumber("+3.14e23xxx", 0));

        System.out.println(StringUtil.extractNumber("-3.14e2-", 0));
        System.out.println(StringUtil.extractNumber("-3.14e2+", 0));
        System.out.println(StringUtil.extractNumber("-3.14e2.", 0));
        System.out.println(StringUtil.extractNumber("-3.14e2e", 0));
        System.out.println(StringUtil.extractNumber("-3.14e+2E", 0));
        System.out.println(StringUtil.extractNumber("-3.14e2xxx", 0));
        System.out.println(StringUtil.extractNumber("-3.14e23-", 0));
        System.out.println(StringUtil.extractNumber("-3.14e23+", 0));
        System.out.println(StringUtil.extractNumber("-3.14e23.", 0));
        System.out.println(StringUtil.extractNumber("-3.14e23e", 0));
        System.out.println(StringUtil.extractNumber("-3.14e23E", 0));
        System.out.println(StringUtil.extractNumber("-3.14e23xxx", 0));
    }

    @Test
    public void extractNumberReader() throws IOException {
        try (StringReader sr = new StringReader("3.14e-2.csv")) {
            System.out.println(StringUtil.extractNumber(sr));
            ;
            System.out.println(IOUtil.readAsString(sr));
        }
    }
}
