package org.dreamcat.common.core.captcha;

import lombok.RequiredArgsConstructor;
import org.dreamcat.common.image.ImageUtil;
import org.dreamcat.common.util.RandomUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Create by tuke on 2020/5/11
 */
public class ImageCaptcha {
    private Font font = new Font("DejaVu Sans", Font.ITALIC, 20);
    private int width = 160;
    private int height = 40;
    private int rgbStart = 0;
    private int rgbEnd = 256;
    private int padding = 5;

    private ImageCaptcha() {
    }

    public static Builder builder() {
        return new Builder(new ImageCaptcha());
    }

    public String base64Jpeg(String code) {
        return base64Image(code, "jpeg");
    }

    public String base64Png(String code) {
        return base64Image(code, "png");
    }

    public String base64Image(String code, String imageType) {
        BufferedImage image = generateImage(code);
        return ImageUtil.base64Image(image, imageType);
    }

    public BufferedImage generateImage(String code) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setFont(font);

        int n = code.length();
        int size = font.getSize();
        int x = (width - n * size) >> 1;
        int y = (height + size) >> 1;
        float dx = size + padding;
        float dy = (height - y);

        for (int i = 0; i < n; i++) {
            float xi = x + i * dx;
            float yi = y + (float) Math.random() * dy - dy / 2;

            Color color = RandomUtil.color(rgbStart, rgbEnd);
            g.setColor(color);

            float alpha = (float) RandomUtil.rand(0.75, 1);
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            g.setComposite(ac);
            g.drawString(code.substring(i, i + 1), xi, yi);
            drawNoise(g, color, (i + 1) << 1);
        }
        return image;
    }

    private void drawNoise(Graphics2D g, Color color, int n) {
        int halfOfW = width >> 1;
        int halfOfH = height >> 1;
        int w = RandomUtil.randi(halfOfW) + halfOfW >> 1;
        int h = RandomUtil.randi(halfOfH) + halfOfH >> 1;

        for (int i = 0; i < n; i++) {
            int rx = RandomUtil.randi(0, width - w);
            int ry = RandomUtil.randi(0, height - h);
            int startAngle = RandomUtil.randi(360);
            int angle = RandomUtil.randi(180);

            g.setColor(color);

            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);
            g.setComposite(ac);

            g.drawArc(rx, ry, w, h, startAngle, angle);
        }
    }

    @RequiredArgsConstructor
    public static class Builder {
        private final ImageCaptcha target;

        public Builder useChinsesFont() {
            target.font = new Font("宋体", Font.BOLD, 20);
            return this;
        }

        public Builder useFont(Font font) {
            target.font = font;
            return this;
        }

        public Builder shape(int width, int height) {
            target.width = width;
            target.height = height;
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
