package org.dreamcat.common.io.captcha;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import lombok.RequiredArgsConstructor;
import org.dreamcat.common.io.image.ImageUtil;
import org.dreamcat.common.util.RandomUtil;

/**
 * Create by tuke on 2020/5/11
 */
public class ImageCaptcha {

    private Font font = new Font("DejaVu Sans", Font.ITALIC, 20);
    private int rgbStart = 0;
    private int rgbEnd = 256;
    private int padding = 5;

    public static Builder builder() {
        return new Builder(new ImageCaptcha());
    }

    public String base64Jpeg(String code, int width, int height) {
        return base64Image(code, width, height, "jpeg");
    }

    public String base64Png(String code, int width, int height) {
        return base64Image(code, width, height, "png");
    }

    public String base64Image(String code, int width, int height, String imageType) {
        BufferedImage image = generateImage(code, width, height);
        return ImageUtil.base64Image(image, imageType);
    }

    public BufferedImage generateImage(String code, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setFont(font);

        int n = code.length();
        int size = font.getSize();
        int x = (width - n * size) >> 1;
        int y = (height + size) >> 1;
        float dx = (float) size + padding;
        float dy = (height - y);

        Color color;
        AlphaComposite ac;
        for (int i = 0; i < n; i++) {
            float xi = x + i * dx + dx * ((float) Math.random() - 0.5f) * 0.382f;
            float yi = y + dy * ((float) Math.random() - 0.5f) * 0.382f;

            color = RandomUtil.color(rgbStart, rgbEnd);
            g.setColor(color);
            ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);
            g.setComposite(ac);
            g.drawString(code.substring(i, i + 1), xi, yi);

            float alpha = (float) RandomUtil.rand(0.75, 1);
            ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            g.setComposite(ac);
            drawNoise(g, width, height, 1);
        }
        return image;
    }

    private void drawNoise(Graphics2D g, int width, int height, int n) {
        int halfOfW = width >> 1;
        int halfOfH = height >> 1;
        int w = RandomUtil.randi(halfOfW) + halfOfW >> 1;
        int h = RandomUtil.randi(halfOfH) + halfOfH >> 1;

        for (int i = 0; i < n; i++) {
            int rx = RandomUtil.randi(0, width - w);
            int ry = RandomUtil.randi(0, height - h);
            int startAngle = RandomUtil.randi(360);
            int angle = RandomUtil.randi(180);

            g.drawArc(rx, ry, w, h, startAngle, angle);
        }
    }

    @RequiredArgsConstructor
    public static class Builder {

        private final ImageCaptcha target;

        public Builder useSongFont() {
            return useFont(new Font("宋体", Font.BOLD, 20));
        }

        public Builder useFont(Font font) {
            target.font = font;
            return this;
        }

        public Builder colorRange(int rgbStart, int rgbEnd) {
            target.rgbStart = rgbStart;
            target.rgbEnd = rgbEnd;
            return this;
        }

        public Builder padding(int padding) {
            target.padding = padding;
            return this;
        }

        public ImageCaptcha build() {
            return target;
        }
    }
}
