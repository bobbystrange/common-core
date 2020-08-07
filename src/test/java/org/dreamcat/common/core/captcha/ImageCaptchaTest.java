package org.dreamcat.common.core.captcha;

import org.dreamcat.common.util.RandomUtil;
import org.junit.Test;

import static org.dreamcat.common.util.FormatUtil.println;

/**
 * Create by tuke on 2020/6/8
 */
public class ImageCaptchaTest {

    @Test
    public void test() {
        ImageCaptcha captcha = ImageCaptcha.builder()
                .shape(256, 64)
                .build();

        println(captcha.base64Jpeg(RandomUtil.choose72(4)));
        println(captcha.base64Jpeg(RandomUtil.choose72(6)));
        println(captcha.base64Jpeg(RandomUtil.choose72(6)));
        println(captcha.base64Jpeg(RandomUtil.choose72(6)));
        println(captcha.base64Jpeg(RandomUtil.choose72(8)));
    }
}
