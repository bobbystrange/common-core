package org.dreamcat.common.core.captcha;

import org.junit.Test;

/**
 * Create by tuke on 2020/5/12
 */
public class GifCaptchaTest {

    @Test
    public void test() {
        GifCaptcha gifCaptcha = GifCaptcha.builder().build();
        String src = gifCaptcha.base64Gif("Ruby");
        System.out.println(src);
    }

}
