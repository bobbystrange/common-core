package org.dreamcat.common.bean.copy;

import net.sf.cglib.beans.BeanCopier;
import org.dreamcat.common.bean.BeanCopyUtil;
import org.dreamcat.test.BeanData;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import static org.dreamcat.common.bean.BeanFormatUtil.pretty;
import static org.dreamcat.common.util.FormatUtil.println;

/**
 * Create by tuke on 2019-06-03
 */
public class BeanCopyTypeTest {

    @Test
    public void testPojo() throws Exception {
        BeanCopier copier = BeanCopier.create(
                BeanData.Pojo.class, BeanData.Pojo.class, false);

        BeanData.Pojo source = BeanData.ofPojo();
        println("raw");
        println(pretty(source));
        println();

        println("cglib");
        BeanData.Pojo target1 = BeanData.ofPojo();
        copier.copy(source, target1, null);
        println(pretty(target1));
        println();

        println("spring");
        BeanData.Pojo target2 = BeanData.ofPojo();
        BeanUtils.copyProperties(source, target2);
        println(pretty(target2));
        println();

        println("current");
        BeanData.Pojo target3 = BeanData.ofPojo();
        BeanCopyUtil.copy(source, target3);
        println(pretty(target3));
        println();
    }

    // only common works 🤣🤣🤣
    @Test
    public void testPrivatePojo() throws Exception {
        BeanCopier copier = BeanCopier.create(
                BeanData.PrivatePojo.class, BeanData.PrivatePojo.class, false);

        BeanData.PrivatePojo source = BeanData.ofPrivatePojo();
        println("raw");
        println(pretty(source));
        println();

        println("cglib");
        BeanData.PrivatePojo target1 = BeanData.ofPrivatePojo();
        copier.copy(source, target1, null);
        println(pretty(target1));
        println();

        println("spring");
        BeanData.PrivatePojo target2 = BeanData.ofPrivatePojo();
        BeanUtils.copyProperties(source, target2);
        println(pretty(target2));
        println();

        println("current");
        BeanData.PrivatePojo target3 = BeanData.ofPrivatePojo();
        BeanCopyUtil.copy(source, target3);
        println(pretty(target3));
        println();
    }

    @Test
    public void testAll() throws Exception {
        BeanCopier copier = BeanCopier.create(
                BeanData.All.class, BeanData.All.class, false);

        BeanData.All source = BeanData.ofAll();
        println("raw");
        println(pretty(source));
        println();

        println("cglib");
        BeanData.All target1 = BeanData.ofAll();
        copier.copy(source, target1, null);
        println(pretty(target1));
        println();

        println("spring");
        BeanData.All target2 = BeanData.ofAll();
        BeanUtils.copyProperties(source, target2);
        println(pretty(target2));
        println();

        println("current");
        BeanData.All target3 = BeanData.ofAll();
        BeanCopyUtil.copy(source, target3);
        println(pretty(target3));
        println();
    }

    // no deep clone any more in all cases

    @Test
    public void testNotSupportDeepCloneForCglib() {
        BeanCopier copier = BeanCopier.create(
                BeanData.Pojo.class, BeanData.Pojo.class, false);


        BeanData.Pojo source = BeanData.ofPojo();
        BeanData.Pojo target = new BeanData.Pojo();
        copier.copy(source, target, null);
        println(pretty(source));
        println(pretty(target));
        target.getD()[0] = -1;
        println(pretty(source));
        println(pretty(target));
    }

    @Test
    public void testNotSupportDeepCloneForSpring() {
        BeanData.Pojo source = BeanData.ofPojo();
        BeanData.Pojo target = new BeanData.Pojo();
        BeanUtils.copyProperties(source, target);
        println(pretty(source));
        println(pretty(target));
        target.getD()[0] = -1;
        println(pretty(source));
        println(pretty(target));
    }


}
