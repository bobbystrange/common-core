package org.dreamcat.common.bean;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.test.BeanData;
import org.junit.Before;
import org.junit.Test;

/**
 * Create by tuke on 2019-02-01
 */
@Slf4j
public class BeanExpressionTest {

    BeanExpression beanExpression;

    @Before
    public void init() {
        beanExpression = new BeanExpression();
    }

    @Test
    public void t01() throws Exception {
        test("timestamp");
    }

    @Test
    public void t02() throws Exception {
        test("-timestamp");
    }

    private void test(String expr) throws Exception {
        BeanData.Pojo obj = BeanData.ofPojo();
        beanExpression.lessFiled(obj, expr);
        log.info("\n{}", BeanFormatUtil.pretty(obj));
    }
}
