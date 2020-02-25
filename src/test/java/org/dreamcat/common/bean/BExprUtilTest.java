package org.dreamcat.common.bean;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.test.BeanBase;
import org.junit.Test;

/**
 * Create by tuke on 2019-02-01
 */
@Slf4j
public class BExprUtilTest {

    public void test(String expr) throws Exception {
        BeanBase obj = BeanBase.newInstance();
        BExpressionUtil.lessFiled(obj, expr);
        log.info("\n{}", BeanUtil.toPrettyString(obj));
    }

    @Test
    public void t01() throws Exception {
        test("timestamp");
    }

    @Test
    public void t02() throws Exception {
        test("-timestamp");
    }
}
