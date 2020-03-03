package org.dreamcat.common.bean.copy;

import net.sf.cglib.beans.BeanCopier;
import org.dreamcat.test.BeanData;
import org.junit.Test;

import java.util.function.Supplier;

import static org.dreamcat.common.bean.copy.CopySpeedCase.speed;
import static org.dreamcat.common.util.PrintUtil.println;

/**
 * Create by tuke on 2020/3/3
 */
public class BeanCopySpeedTest {

    // cglib is about twice as fast as common
    @Test
    public void testSpeedPojo() throws Exception {
        BeanCopier copier = BeanCopier.create(
                BeanData.Pojo.class, BeanData.Pojo.class, false);
        BeanData.Pojo source = BeanData.ofPojo();
        Supplier<Object[]> supplier = () -> new Object[]{source};
        Supplier<Object> constructor = BeanData::ofPojo;
        println("pojo\t\t\t\t\t\tcglib\t\tspring\t\tcommon");
        for (int i = 1; i < 1 << 13; i *= 2) {
            speed(50, 5, i, supplier, constructor, copier);
        }
    }

    @Test
    public void testSpeedAll() throws Exception {
        BeanCopier copier = BeanCopier.create(
                BeanData.All.class, BeanData.All.class, false);
        BeanData.All source = BeanData.ofAll();
        Supplier<Object[]> supplier = () -> new Object[]{source};
        Supplier<Object> constructor = BeanData::ofAll;
        println("all\t\t\t\t\t\tcglib\t\tspring\t\tcommon");
        for (int i = 1; i < 1 << 13; i *= 2) {
            speed(100, 10, i, supplier, constructor, copier);
        }
    }
}
