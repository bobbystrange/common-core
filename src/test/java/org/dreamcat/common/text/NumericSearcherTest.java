package org.dreamcat.common.text;

import java.io.IOException;
import java.io.StringReader;
import org.dreamcat.common.io.IOUtil;
import org.junit.Test;

/**
 * Create by tuke on 2020/11/20
 */
public class NumericSearcherTest {

    @Test
    public void extractNumberInteger() {
        // int
        System.out.println(NumericSearcher.extractNumber("11", 0));
        System.out.println(NumericSearcher.extractNumber("+11", 0));
        System.out.println(NumericSearcher.extractNumber("-11", 0));
        System.out.println(NumericSearcher.extractNumber("11+11", 0));
        System.out.println(NumericSearcher.extractNumber("+11-11", 0));
        System.out.println(NumericSearcher.extractNumber("-11+11", 0));
        System.out.println();

        // long
        System.out.println(NumericSearcher.extractNumber("68719476736", 0));
        System.out.println(NumericSearcher.extractNumber("+68719476736", 0));
        System.out.println(NumericSearcher.extractNumber("-68719476736", 0));
        System.out.println(NumericSearcher.extractNumber("68719476736+68719476736", 0));
        System.out.println(NumericSearcher.extractNumber("+68719476736-68719476736", 0));
        System.out.println(NumericSearcher.extractNumber("-68719476736+68719476736", 0));
    }

    /// [+-], [0-9, [.], [eE], other chars

    // [+-]?[0-9]*[.]?[0-9]*([eE][+-]?[0-9]+)?
    @Test
    public void extractNumberFloat() {
        // 3.
        System.out.println(NumericSearcher.extractNumber("3.", 0));
        System.out.println(NumericSearcher.extractNumber("+3..", 0));
        System.out.println(NumericSearcher.extractNumber("-3.-", 0));
        System.out.println(NumericSearcher.extractNumber("3.+", 0));
        System.out.println(NumericSearcher.extractNumber("3.e", 0));
        System.out.println(NumericSearcher.extractNumber("3.xxx", 0));
        System.out.println();

        // 3.14.
        System.out.println(NumericSearcher.extractNumber("3.14.", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14..", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14.-", 0));
        System.out.println(NumericSearcher.extractNumber("3.14.+", 0));
        System.out.println(NumericSearcher.extractNumber("3.14.e", 0));
        System.out.println(NumericSearcher.extractNumber("3.14.xxx", 0));
        System.out.println();

        // 3.14
        System.out.println(NumericSearcher.extractNumber("3.14-", 0));
        System.out.println(NumericSearcher.extractNumber("3.14+", 0));
        System.out.println(NumericSearcher.extractNumber("3.14xx", 0));
    }

    // 3.14e
    @Test
    public void extractNumberFloatEdge1() {
        System.out.println(NumericSearcher.extractNumber("3.14e", 0));

        System.out.println();
        System.out.println(NumericSearcher.extractNumber("3.14ee", 0));
        System.out.println(NumericSearcher.extractNumber("3.14e.", 0));
        System.out.println(NumericSearcher.extractNumber("3.14e.", 0));
        System.out.println(NumericSearcher.extractNumber("3.14exxx", 0));

        System.out.println();
        System.out.println(NumericSearcher.extractNumber("+3.14ee", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e.", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e.", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14exxx", 0));

        System.out.println();
        System.out.println(NumericSearcher.extractNumber("-3.14ee", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e.", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e.", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14exxx", 0));
    }

    // 3.14e-
    @Test
    public void extractNumberFloatEdge2() {
        System.out.println(NumericSearcher.extractNumber("3.14e-", 0));
        System.out.println(NumericSearcher.extractNumber("3.14e+", 0));

        System.out.println();
        System.out.println(NumericSearcher.extractNumber("3.14e-+", 0));
        System.out.println(NumericSearcher.extractNumber("3.14e--", 0));
        System.out.println(NumericSearcher.extractNumber("3.14e-.", 0));
        System.out.println(NumericSearcher.extractNumber("3.14e-xxx", 0));
        System.out.println(NumericSearcher.extractNumber("3.14e++", 0));
        System.out.println(NumericSearcher.extractNumber("3.14e+-", 0));
        System.out.println(NumericSearcher.extractNumber("3.14e+.", 0));
        System.out.println(NumericSearcher.extractNumber("3.14e+xxx", 0));

        System.out.println();
        System.out.println(NumericSearcher.extractNumber("+3.14e-+", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e--", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e-.", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e-xxx", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e++", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e+-", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e+.", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e+xxx", 0));

        System.out.println();
        System.out.println(NumericSearcher.extractNumber("-3.14e-+", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e--", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e-.", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e-xxx", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e++", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e+-", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e+.", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e+xxx", 0));
    }

    // 3.14e-2
    @Test
    public void extractNumberFloatEdge3() {
        System.out.println(NumericSearcher.extractNumber("23.14e-2-", 0));
        System.out.println(NumericSearcher.extractNumber("23.14e-2+", 0));
        System.out.println(NumericSearcher.extractNumber("23.14e-2.", 0));
        System.out.println(NumericSearcher.extractNumber("23.14e-2e", 0));
        System.out.println(NumericSearcher.extractNumber("23.14e+2E", 0));
        System.out.println(NumericSearcher.extractNumber("23.14e-2xxx", 0));
        System.out.println(NumericSearcher.extractNumber("3.14e-23-", 0));
        System.out.println(NumericSearcher.extractNumber("3.14e-23+", 0));
        System.out.println(NumericSearcher.extractNumber("3.14e-23.", 0));
        System.out.println(NumericSearcher.extractNumber("3.14e-23e", 0));
        System.out.println(NumericSearcher.extractNumber("3.14e+23E", 0));
        System.out.println(NumericSearcher.extractNumber("3.14e-23xxx", 0));

        System.out.println(NumericSearcher.extractNumber("+3.14e-2-", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e-2+", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e-2.", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e-2e", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e+2E", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e-2xxx", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e-23-", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e-23+", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e-23.", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e-23e", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e+23E", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e-23xxx", 0));

        System.out.println(NumericSearcher.extractNumber("-3.14e-2-", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e-2+", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e-2.", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e-2e", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e+2E", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e-2xxx", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e-23-", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e-23+", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e-23.", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e-23e", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e+23E", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e-23xxx", 0));
    }

    //3.14e2
    @Test
    public void extractNumberFloatEdge4() {
        System.out.println(NumericSearcher.extractNumber("23.14e2-", 0));
        System.out.println(NumericSearcher.extractNumber("23.14e2+", 0));
        System.out.println(NumericSearcher.extractNumber("23.14e2.", 0));
        System.out.println(NumericSearcher.extractNumber("23.14e2e", 0));
        System.out.println(NumericSearcher.extractNumber("23.14e2E", 0));
        System.out.println(NumericSearcher.extractNumber("23.14e2xxx", 0));
        System.out.println(NumericSearcher.extractNumber("3.14e23-", 0));
        System.out.println(NumericSearcher.extractNumber("3.14e23+", 0));
        System.out.println(NumericSearcher.extractNumber("3.14e23.", 0));
        System.out.println(NumericSearcher.extractNumber("3.14e23e", 0));
        System.out.println(NumericSearcher.extractNumber("3.14e23E", 0));
        System.out.println(NumericSearcher.extractNumber("3.14e23xxx", 0));

        System.out.println(NumericSearcher.extractNumber("+3.14e2-", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e2+", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e2.", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e2e", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e+2E", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e2xxx", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e23-", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e23+", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e23.", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e23e", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e23E", 0));
        System.out.println(NumericSearcher.extractNumber("+3.14e23xxx", 0));

        System.out.println(NumericSearcher.extractNumber("-3.14e2-", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e2+", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e2.", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e2e", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e+2E", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e2xxx", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e23-", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e23+", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e23.", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e23e", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e23E", 0));
        System.out.println(NumericSearcher.extractNumber("-3.14e23xxx", 0));
    }

    @Test
    public void extractNumberReader() throws IOException {
        try (StringReader sr = new StringReader("3.14e-2.csv")) {
            System.out.println(NumericSearcher.extractNumber(sr));
            System.out.println(IOUtil.readAsString(sr));
        }
    }
}
