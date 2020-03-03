package org.dreamcat.common.io;

import org.dreamcat.common.util.PrintUtil;
import org.dreamcat.common.util.RandomUtil;
import org.junit.Test;

/**
 * Create by tuke on 2020/2/26
 */
public class ImageUtilTest {
    @Test
    public void test() {
        String code = RandomUtil.choose72(6);
        String imageSource = ImageUtil.generateBase64ImageSource(
                code, 100, 100);
        PrintUtil.println(imageSource);
    }
}
