package org.dreamcat.common.core.captcha;

import lombok.RequiredArgsConstructor;
import org.dreamcat.common.image.AnimatedGifEncoder;
import org.dreamcat.common.util.ArrayUtil;
import org.dreamcat.common.util.Base64Util;
import org.dreamcat.common.util.RandomUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

/**
 * Create by tuke on 2020/5/12
 */
public class GifCaptcha {
    private Font font = new Font("DejaVu Sans", Font.ITALIC, 20);
    private int width = 160;
    private int height = 40;
    private int delay = 333;
    // quality sampling interval, default is 10ms, [1, 20]
    private int quality = 10;
    // loop to play
    private int repeat = 0;
    private int rgbStart = 0;
    private int rgbEnd = 256;
    private int padding = 5;

    private GifCaptcha() {
    }

    public static Builder builder() {
        return new Builder(new GifCaptcha());
    }

    public String base64Gif(String code) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            generateGif(code, os);
            String base64 = Base64Util.encodeAsString(os.toByteArray());
            return String.format("data:image/gif;base64,%s", base64);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void generateGif(String code, OutputStream os) {
        AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
        gifEncoder.start(os);
        gifEncoder.setQuality(quality);
        gifEncoder.setDelay(delay);
        gifEncoder.setRepeat(repeat);
        BufferedImage frame;

        char[] chars = code.toCharArray();
        int n = code.length();
        Color[] colors = new Color[n];
        int size = font.getSize();
        float y = (height + size) >> 1;
        float[] ys = new float[n];
        float dy = (height - y);
        for (int i = 0; i < n; i++) {
            colors[i] = RandomUtil.color(rgbStart, rgbEnd);
            ys[i] = y + (float) Math.random() * dy - dy / 2;
        }

        int[] orders = ArrayUtil.randomRangeOf(n);
        for (int i : orders) {
            char c = chars[i];
            frame = generateFrame(code, i, colors, ys);
            gifEncoder.addFrame(frame);
            frame.flush();
        }
        gifEncoder.finish();
    }

    private BufferedImage generateFrame(String code, int index, Color[] colors, float[] ys) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setFont(font);

        int n = code.length();
        int size = font.getSize();
        float dx = size + padding;
        float x = (width - size * n - padding * (n - 1)) >> 1;

        for (int i = 0; i < n; i++) {
            float xi = x + i * dx;
            float yi = ys[i];
            g.setColor(colors[i]);

            float alpha = (float) RandomUtil.rand(0.33, 0.75);
            if (i == index) {
                alpha = 1;
            }
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            g.setComposite(ac);

            g.drawString(code.substring(i, i + 1), xi, yi);
        }

        drawNoise(g, colors[index], index + 1);
        g.dispose();
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
        private final GifCaptcha target;

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

        public Builder repeat(int repeat) {
            target.repeat = repeat;
            return this;
        }

        public Builder delay(int delay) {
            target.delay = delay;
            return this;
        }

        public Builder quality(int quality) {
            target.quality = quality;
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

        public GifCaptcha build() {
            return target;
        }
    }
}
