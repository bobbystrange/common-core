package org.dreamcat.common.core.component;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.test.BeanBase;
import org.dreamcat.common.util.BExpressionUtil;
import org.dreamcat.common.util.BeanUtil;
import org.junit.Test;

/**
 * Create by tuke on 2019-02-01
 */
@Slf4j
public class BExprUtilTest {

    private BExpressionUtil BExprUtil = new BExpressionUtil();

    public void test(String expr) throws Exception {
        BeanBase obj = BeanBase.newInstance();
        BExprUtil.lessFiled(obj, expr);
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
