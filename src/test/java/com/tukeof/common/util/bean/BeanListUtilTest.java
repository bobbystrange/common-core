package com.tukeof.common.util.bean;

import com.tukeof.common.test.BeanBase;
import com.tukeof.common.test.BeanUnion;
import lombok.extern.slf4j.Slf4j;
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
        BeanBase obj = BeanBase.newInstance();

        list = BeanListUtil.toList(obj);
        log.info("\n{}", BeanStringUtil.toPrettyString(list));
        assert list.size() == 6;
    }

    @Test
    public void toList2() {
        List<Object> list;
        BeanBase obj = BeanBase.newInstance();

        list = BeanListUtil.toList(obj, BeanBase.Anno.class);
        log.info("\n{}", BeanStringUtil.toPrettyString(list));
        assert list.size() == 4;
    }

    @Test
    public void toList3() {
        List<Object> list;
        BeanBase obj = BeanBase.newInstance();

        list = BeanListUtil.toList(obj,
                Modifier.PROTECTED | Modifier.VOLATILE, BeanBase.Anno.class);
        log.info("\n{}", BeanStringUtil.toPrettyString(list));
        assert list.size() == 4;
    }

    @Test
    public void retrieveExpandedList() {
        List<Object> list;
        BeanUnion obj = BeanUnion.newInstance();
        list = new ArrayList<>();
        BeanListUtil.retrieveExpandedList(list, obj, null);
        log.info("\n{}", BeanStringUtil.toPrettyString(list));
        assert list.size() == 3;


        list = new ArrayList<>();
        BeanListUtil.retrieveExpandedList(list, obj, null, BeanUnion.BeanBlock1.class);
        log.info("\n\n{}", BeanStringUtil.toPrettyString(list));
        assert list.size() == 6;

        list = new ArrayList<>();
        BeanListUtil.retrieveExpandedList(list, obj, null,
                BeanUnion.BeanBlock1.class, BeanUnion.BeanBlock2.class, BeanUnion.BeanBlock3.class);
        log.info("\n\n{}", BeanStringUtil.toPrettyString(list));
        assert list.size() == 12;

    }

}
