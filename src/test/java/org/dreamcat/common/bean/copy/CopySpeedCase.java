package org.dreamcat.common.bean.copy;

import net.sf.cglib.beans.BeanCopier;
import org.dreamcat.common.bean.BeanCopyUtil;
import org.dreamcat.common.core.Timeit;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.dreamcat.common.util.PrintUtil.printf;

/**
 * Create by tuke on 2020/3/3
 */
public class CopySpeedCase {

    public static void speed(
            int count, int skip, int repeat,
            Supplier<Object[]> supplier, Supplier<Object> constructor, BeanCopier copier) {
        Timeit timeit = new Timeit()
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
                    BeanCopyUtil.copy(args[0], constructor.get());
                });
        long[] ts = timeit.runForActions();
        String fmt = Arrays.stream(ts)
                .mapToObj(it -> String.format("%09.3f", it / 1000.))
                .collect(Collectors.joining("\t"));
        printf("%07d times copy cost us %s\n", repeat, fmt);
    }
}
