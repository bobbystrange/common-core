package org.dreamcat.common.util.bean;

import static org.dreamcat.common.util.BeanUtil.pretty;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.BeanUtil;
import org.dreamcat.test.BeanData;
import org.junit.Test;

/**
 * Create by tuke on 2019-02-01
 */
@Slf4j
public class BeanLessFieldTest {

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
        BeanUtil.lessFiled(obj, expr);
        log.info("\n{}", pretty(obj));
    }
}
