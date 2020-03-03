package org.dreamcat.common.collection;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.bean.BeanArrayUtil;
import org.dreamcat.common.bean.BeanFormatUtil;
import org.dreamcat.test.BeanKey;
import org.dreamcat.test.BeanUnion;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by tuke on 2019-01-28
 */
@Slf4j
public class CollectionUnionBasicUtilTest {

    private static final List<BeanUnion.BeanBlock1> list1 = BeanUnion.ofSub1();
    private static final List<BeanUnion.BeanBlock2> list2 = BeanUnion.ofSub2();
    private static final List<BeanUnion.BeanBlock3> list3 = BeanUnion.ofSub3();
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyymm");

    @SuppressWarnings("unchecked")
    @Test
    public void unionVerticalBlocks() {
        List<String> stringList = BeanArrayUtil.toStringList(list1.get(0));
        log.info("\n{}", BeanFormatUtil.pretty(stringList));


        List<List<String>> data = CollectionUnionUtil.unionVertical(
                // key to key
                (Object it) -> {
                    BeanKey key = (BeanKey) it;
                    return new BeanKey(key.getYearMonth(), key.getPartnerId());
                },
                // key to list
                (BeanKey key) -> {
                    List<String> list = new ArrayList<>();
                    list.add(SDF.format(key.getYearMonth()));
                    list.add(key.getPartnerId() + "");
                    return list;
                },
                //
                new Class[]{BeanKey.Anno.class},
                -1,
                new List[]{list1, list2, list3});

        log.info("{}", data.size());
        for (List<String> list : data) {
            log.info("\n{}", BeanFormatUtil.pretty(list));
        }
    }

}
