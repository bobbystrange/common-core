package org.dreamcat.common.bean;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.test.BeanData;
import org.dreamcat.test.BeanUnion;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by tuke on 2019-02-12
 */
@Slf4j
public class BeanListUtilTest {

    @Test
    public void box() {
        Object obj = 2;
        log.info(obj.getClass().getName());
    }

    @Test
    public void toList() {
        List<Object> list;
        BeanData.Pojo obj = BeanData.ofPojo();

        list = BeanListUtil.toList(obj);
        log.info("\n{}", BeanFormatUtil.pretty(list));
        assert list.size() == 6;
    }

    @Test
    public void toList2() {
        List<Object> list;
        BeanData.Pojo obj = BeanData.ofPojo();

        list = BeanListUtil.toList(obj, BeanData.Ann.class);
        log.info("\n{}", BeanFormatUtil.pretty(list));
    }

    @Test
    public void toList3() {
        List<Object> list;
        BeanData.Pojo obj = BeanData.ofPojo();

        list = BeanListUtil.toList(obj,
                Modifier.PROTECTED | Modifier.VOLATILE, BeanData.Ann.class);
        log.info("\n{}", BeanFormatUtil.pretty(list));
    }

    @Test
    public void retrieveExpandedList() {
        List<Object> list;
        BeanUnion obj = BeanUnion.newInstance();
        list = new ArrayList<>();
        BeanListUtil.retrieveExpandedList(list, obj, null);
        log.info("\n{}", BeanFormatUtil.pretty(list));

        list = new ArrayList<>();
        BeanListUtil.retrieveExpandedList(list, obj, null, BeanUnion.BeanBlock1.class);
        log.info("\n\n{}", BeanFormatUtil.pretty(list));

        list = new ArrayList<>();
        BeanListUtil.retrieveExpandedList(list, obj, null,
                BeanUnion.BeanBlock1.class, BeanUnion.BeanBlock2.class, BeanUnion.BeanBlock3.class);
        log.info("\n\n{}", BeanFormatUtil.pretty(list));
    }

}
