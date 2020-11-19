package org.dreamcat.common.util;

import static org.dreamcat.common.util.RandomUtil.randi;

import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Create by tuke on 2020/3/3
 */
@Slf4j
public class DateUtilTest {

    @Test
    public void testDate() {

        for (int i = 2; i < 30; i++) {
            int offset = 2 << i;
            int r = randi(offset / 2, offset);
            Date date = new Date(System.currentTimeMillis() - r);
            System.out
                    .printf("[%d] [%d] rand=%d, %s\n", i, offset, r, date + "\t" + date.getTime());
        }

    }
}
