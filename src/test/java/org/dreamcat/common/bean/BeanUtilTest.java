package org.dreamcat.common.bean;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.test.BeanData;
import org.junit.Test;

import static org.dreamcat.common.bean.BeanFormatUtil.pretty;
import static org.dreamcat.common.bean.BeanUtil.nullify;
import static org.dreamcat.common.util.ConsoleUtil.println;

@Slf4j
public class BeanUtilTest {

    @Test
    public void testullify() {
        BeanData.Pojo obj = BeanData.ofPojo();
        nullify(obj);
        println(pretty(obj));
    }


}
