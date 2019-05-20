package com.tukeof.common.util.bean;

import com.tukeof.common.test.BeanBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Create by tuke on 2019-02-01
 */
@Slf4j
public class BeanExpressionUtilTest {

    public void test(String expr) throws Exception {
        BeanBase obj = BeanBase.newInstance();
        BeanExpressionUtil.lessFiled(obj, expr);
        log.info("\n{}", BeanStringUtil.toPrettyString(obj));
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
