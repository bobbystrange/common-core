package org.dreamcat.common.util.bean.copy;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.sf.cglib.beans.BeanCopier;
import org.dreamcat.common.core.Timeit;
import org.dreamcat.common.function.ThrowableSupplier;
import org.dreamcat.common.util.BeanUtil;
import org.dreamcat.test.BeanData;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

/**
 * Create by tuke on 2019-06-03
 */
public class BeanCopySpeedTest {

    private static void speed(
            int count, int skip, int repeat,
            ThrowableSupplier<Object[]> supplier, Supplier<Object> constructor, BeanCopier copier) {
        Timeit timeit = Timeit.ofActions()
                .repeat(repeat)
                .count(count)
                .skip(skip)
                .addAction(supplier, args -> {
                    copier.copy(args[0], constructor.get(), null);
                })
                .addAction(supplier, args -> {
                    BeanUtils.copyProperties(args[0], constructor.get());
                })
                .addAction(supplier, args -> {
                    BeanUtil.copy(args[0], constructor.get());
                });
        long[] ts = timeit.run();
        String fmt = Arrays.stream(ts)
                .mapToObj(it -> String.format("%09.3f", it / 1000.))
                .collect(Collectors.joining("\t"));
        System.out.printf("%07d times copy cost us %s\n", repeat, fmt);
    }

    // cglib is about twice as fast as common
    @Test
    public void testSpeedPojo() throws Exception {
        BeanCopier copier = BeanCopier.create(
                BeanData.Pojo.class, BeanData.Pojo.class, false);
        BeanData.Pojo source = BeanData.ofPojo();
        ThrowableSupplier<Object[]> supplier = () -> new Object[]{source};
        Supplier<Object> constructor = BeanData::ofPojo;
        System.out.println("pojo\t\t\t\t\t\tcglib\t\tspring\t\tcommon");
        for (int i = 1; i < 1 << 13; i *= 2) {
            speed(50, 5, i, supplier, constructor, copier);
        }
    }

    @Test
    public void testSpeedAll() throws Exception {
        BeanCopier copier = BeanCopier.create(
                BeanData.All.class, BeanData.All.class, false);
        BeanData.All source = BeanData.ofAll();
        ThrowableSupplier<Object[]> supplier = () -> new Object[]{source};
        Supplier<Object> constructor = BeanData::ofAll;
        System.out.println("all\t\t\t\t\t\tcglib\t\tspring\t\tcommon");
        for (int i = 1; i < 1 << 13; i *= 2) {
            speed(100, 10, i, supplier, constructor, copier);
        }
    }
}