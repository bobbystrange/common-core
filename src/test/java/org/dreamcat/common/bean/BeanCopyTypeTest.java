package org.dreamcat.common.bean;

import static org.dreamcat.common.bean.BeanFormatUtil.pretty;

import net.sf.cglib.beans.BeanCopier;
import org.dreamcat.test.BeanData;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

/**
 * Create by tuke on 2019-06-03
 */
public class BeanCopyTypeTest {

    @Test
    public void testPojo() throws Exception {
        BeanCopier copier = BeanCopier.create(
                BeanData.Pojo.class, BeanData.Pojo.class, false);

        BeanData.Pojo source = BeanData.ofPojo();
        System.out.println("raw");
        System.out.println(pretty(source));
        System.out.println();

        System.out.println("cglib");
        BeanData.Pojo target1 = BeanData.ofPojo();
        copier.copy(source, target1, null);
        System.out.println(pretty(target1));
        System.out.println();

        System.out.println("spring");
        BeanData.Pojo target2 = BeanData.ofPojo();
        BeanUtils.copyProperties(source, target2);
        System.out.println(pretty(target2));
        System.out.println();

        System.out.println("current");
        BeanData.Pojo target3 = BeanData.ofPojo();
        BeanUtil.copy(source, target3);
        System.out.println(pretty(target3));
        System.out.println();
    }

    // only common works 🤣🤣🤣
    @Test
    public void testPrivatePojo() throws Exception {
        BeanCopier copier = BeanCopier.create(
                BeanData.PrivatePojo.class, BeanData.PrivatePojo.class, false);

        BeanData.PrivatePojo source = BeanData.ofPrivatePojo();
        System.out.println("raw");
        System.out.println(pretty(source));
        System.out.println();

        System.out.println("cglib");
        BeanData.PrivatePojo target1 = BeanData.ofPrivatePojo();
        copier.copy(source, target1, null);
        System.out.println(pretty(target1));
        System.out.println();

        System.out.println("spring");
        BeanData.PrivatePojo target2 = BeanData.ofPrivatePojo();
        BeanUtils.copyProperties(source, target2);
        System.out.println(pretty(target2));
        System.out.println();

        System.out.println("current");
        BeanData.PrivatePojo target3 = BeanData.ofPrivatePojo();
        BeanUtil.copy(source, target3);
        System.out.println(pretty(target3));
        System.out.println();
    }

    @Test
    public void testAll() throws Exception {
        BeanCopier copier = BeanCopier.create(
                BeanData.All.class, BeanData.All.class, false);

        BeanData.All source = BeanData.ofAll();
        System.out.println("raw");
        System.out.println(pretty(source));
        System.out.println();

        System.out.println("cglib");
        BeanData.All target1 = BeanData.ofAll();
        copier.copy(source, target1, null);
        System.out.println(pretty(target1));
        System.out.println();

        System.out.println("spring");
        BeanData.All target2 = BeanData.ofAll();
        BeanUtils.copyProperties(source, target2);
        System.out.println(pretty(target2));
        System.out.println();

        System.out.println("current");
        BeanData.All target3 = BeanData.ofAll();
        BeanUtil.copy(source, target3);
        System.out.println(pretty(target3));
        System.out.println();
    }

    // no deep clone any more in all cases

    @Test
    public void testNotSupportDeepCloneForCglib() {
        BeanCopier copier = BeanCopier.create(
                BeanData.Pojo.class, BeanData.Pojo.class, false);

        BeanData.Pojo source = BeanData.ofPojo();
        BeanData.Pojo target = new BeanData.Pojo();
        copier.copy(source, target, null);
        System.out.println(pretty(source));
        System.out.println(pretty(target));
        target.getD()[0] = -1;
        System.out.println(pretty(source));
        System.out.println(pretty(target));
    }

    @Test
    public void testNotSupportDeepCloneForSpring() {
        BeanData.Pojo source = BeanData.ofPojo();
        BeanData.Pojo target = new BeanData.Pojo();
        BeanUtils.copyProperties(source, target);
        System.out.println(pretty(source));
        System.out.println(pretty(target));
        target.getD()[0] = -1;
        System.out.println(pretty(source));
        System.out.println(pretty(target));
    }


}
