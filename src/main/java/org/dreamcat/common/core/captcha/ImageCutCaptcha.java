package org.dreamcat.common.core.captcha;

import lombok.RequiredArgsConstructor;
import org.dreamcat.common.core.Pair;
import org.dreamcat.common.image.ImageUtil;
import org.dreamcat.common.util.ObjectUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.IntBinaryOperator;

/**
 * Create by tuke on 2020/5/12
 */
public class ImageCutCaptcha {
    private static final Color WHITE_ALPHA = new Color(255, 255, 255, 0);
    private int width;
    private int height;
    private int[][] data;
    private IntBinaryOperator areaFx;
    private int areaWidth;
    private int areaHeight;
    private int areaOffsetX = 0;
    private int areaOffsetY = 0;

    public static Builder builder() {
        return new Builder(new ImageCutCaptcha());
    }

    public static ImageCutCaptcha ofRect(BufferedImage image, int areaSize) {
        return builder()
                .source(image)
                .areaFx(ImageCutAreaFx.rect(areaSize))
                .areaShape(areaSize)
                .build();
    }

    public static ImageCutCaptcha ofNotchRect(BufferedImage image, int areaSize) {
        return ofNotchRect(image, areaSize, areaSize / 6);
    }

    public static ImageCutCaptcha ofNotchRect(BufferedImage image, int areaSize, int radius) {
        return builder()
                .source(image)
                .areaFx(ImageCutAreaFx.notchRect(areaSize, areaSize, radius))
                .areaShape(areaSize + radius)
                .areaOffset(0, radius)
                .build();
    }

    public Pair<String, String> base64Jpeg(int offsetX, int offsetY) {
        return base64Image(offsetX, offsetY, "jpeg");
    }

    public Pair<String, String> base64Png(int offsetX, int offsetY) {
        return base64Image(offsetX, offsetY, "png");
    }

    public Pair<String, String> base64Image(int offsetX, int offsetY, String imageType) {
        Pair<BufferedImage, BufferedImage> pair = generateImage(offsetX, offsetY);

        String target = ImageUtil.base64Image(pair.first(), imageType);
        String cutting = ImageUtil.base64Image(pair.second(), imageType);
        return new Pair<>(target, cutting);
    }

    public Pair<BufferedImage, BufferedImage> generateImage(int offsetX, int offsetY) {
        BufferedImage target = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D tg = target.createGraphics();
        //g.setColor(Color.WHITE);
        tg.setColor(WHITE_ALPHA);
        tg.fillRect(0, 0, width, height);
        //AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0);
        //g.setComposite(ac);

        BufferedImage cutting = new BufferedImage(areaWidth, areaHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D cg = cutting.createGraphics();
        //g.setColor(Color.WHITE);
        cg.setColor(WHITE_ALPHA);
        cg.fillRect(0, 0, areaWidth, areaHeight);
        //g.setComposite(ac);

        cut(target, cutting, offsetX, offsetY);

        tg.dispose();
        cg.dispose();
        return new Pair<>(target, cutting);
    }

    private void cut(BufferedImage target, BufferedImage cutting, int offsetX, int offsetY) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = data[i][j];
                int cx = i - offsetX;
                int cy = j - offsetY;
                int cmp = areaFx.applyAsInt(cx, cy);
                // outside
                if (cmp >= 0) {
                    target.setRGB(i, j, rgb);
                }
                // inside
                else {
                    cutting.setRGB(cx + areaOffsetX, cy + areaOffsetY, rgb);
                }
            }
        }
    }

    @RequiredArgsConstructor
    public static class Builder {
        private final ImageCutCaptcha target;

        public Builder source(BufferedImage image) {
            target.data = ImageUtil.getData(image);
            target.width = image.getWidth();
            target.height = image.getHeight();
            return this;
        }

        public Builder areaFx(IntBinaryOperator areaFx) {
            target.areaFx = areaFx;
            return this;
        }

        public Builder areaShape(int areaSize) {
            target.areaWidth = areaSize;
            target.areaHeight = areaSize;
            return this;
        }

        public Builder areaShape(int areaWidth, int areaHeight) {
            target.areaWidth = areaWidth;
            target.areaHeight = areaHeight;
            return this;
        }

        public Builder areaOffset(int areaOffsetX, int areaOffsetY) {
            target.areaOffsetX = areaOffsetX;
            target.areaOffsetY = areaOffsetY;
            return this;
        }

        public ImageCutCaptcha build() {
            ObjectUtil.requireNonNull(target.data, target.areaFx);
            return target;
        }
    }
}
