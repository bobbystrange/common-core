package org.dreamcat.common.io.captcha;

import org.dreamcat.common.util.RandomUtil;
import org.junit.Test;

/**
 * Create by tuke on 2020/6/8
 */
public class ImageCaptchaTest {

    @Test
    public void test() {
        ImageCaptcha captcha = ImageCaptcha.builder()
                .useSongFont()
                .build();

        System.out.println(captcha.base64Jpeg(RandomUtil.choose72(4), 300, 100));
        System.out.println(captcha.base64Jpeg(RandomUtil.choose72(6), 300, 100));
        System.out.println(captcha.base64Jpeg(RandomUtil.choose72(6), 300, 100));
        System.out.println(captcha.base64Jpeg(RandomUtil.choose72(6), 300, 100));
        System.out.println(captcha.base64Jpeg(RandomUtil.choose72(8), 300, 100));
    }
}
