package org.dreamcat.common.io.captcha;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.dreamcat.common.core.Pair;
import org.dreamcat.common.io.image.ImageUtil;
import org.junit.Test;

/**
 * Create by tuke on 2020/5/12
 */
public class ImageCutCaptchaTest {

    @Test
    public void test() {
        int width = 500;
        int height = 500;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        int step = 50;
        for (int i = 0; i < width; i += step) {
            for (int j = 0; j < height; j += step) {
                int r = (2 * i * j) % 128 + 64;
                int g = (i * i * i / (j + 1)) % 128 + 64;
                int b = (i * i + j * j) % 192 + 32;
                g2d.setColor(new Color(r, g, b));
                g2d.fillRect(i, j, step, step);
            }
        }

        ImageCutCaptcha captcha = ImageCutCaptcha.builder()
                .source(image)
                .areaFx(ImageCutAreaFx.notchRect(120, 120, 20))
                .areaShape(140, 140)
                .areaOffset(0, 20)
                .build();
        Pair<BufferedImage, BufferedImage> pair = captcha.generateImage(width / 2, height / 2);
        System.out.println(ImageUtil.base64Png(image));
        System.out.println(ImageUtil.base64Png(pair.first()));
        System.out.println(ImageUtil.base64Png(pair.second()));
        System.out.println(pair.second().getWidth() + " " + pair.second().getHeight());
    }

    @Test
    public void testColor() {
        int i = 0b10__0000_0001__0000_0001__0000_0010;
        Color c = new Color(1, 1, 2, 2);
        System.out.println(Integer.toBinaryString(c.getRGB()));
    }

}
