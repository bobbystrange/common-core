package org.dreamcat.common.reflect;

import static org.dreamcat.common.bean.BeanFormatUtil.pretty;

import java.util.List;
import org.dreamcat.test.BeanData;
import org.junit.Test;

/**
 * Create by tuke on 2020/8/30
 */
public class FieldColumnTest {

    @Test
    public void test() {
        List<FieldColumn> columns = FieldColumn.parse(BeanData.All.class);
        columns.forEach(it -> System.out.println(pretty(it)));
    }
}
