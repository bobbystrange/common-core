package org.dreamcat.common.bean;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.test.BeanData;
import org.junit.Test;

/**
 * Create by tuke on 2019-02-01
 */
@Slf4j
public class LessFieldUtilTest {

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
        LessFieldUtil.lessFiled(obj, expr);
        log.info("\n{}", obj);
    }
}
