package org.dreamcat.common.util;

import org.junit.Test;

import static org.dreamcat.common.util.PrintUtil.log;

/**
 * Create by tuke on 2020/4/8
 */
public class PrintUtilTest {

    @Test
    public void testLog() {
        log("({})", 1, Math.PI);
        log("({}, {})", 1, Math.PI);
        log("({}, {}, {})", 1, Math.PI);
        log("({}{{}}{})", 1, Math.PI);
    }
}
