package org.dreamcat.common.io;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;

/**
 * Create by tuke on 2019-06-19
 */
public class ImageUtil {

    // <image src="data:image/png,xxx">
    public static String base64ImageSource(RenderedImage image) {
        return base64ImageSource(image, "png");
    }

    public static String base64ImageSource(RenderedImage image, String imageType) {
        return String.format("data:image/%s;base64,%s", imageType, base64(image, imageType));
    }

    /**
     * image to base64
     *
     * @param image     image object
     * @param imageType jpeg, png
     * @return base64
     */
    public static String base64(RenderedImage image, String imageType) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(image, imageType, Base64.getEncoder().wrap(os));
            return os.toString(StandardCharsets.ISO_8859_1.name());
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    // <image src="data:image/png,xxx">
    public static String generateBase64ImageSource(String code, int width, int height) {
        BufferedImage image = generateImage(code, width, height);
        return base64ImageSource(image);
    }

    public static BufferedImage generateImage(String code, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = null;
        try {
            g = image.getGraphics();
            Random random = new Random();

            g.setColor(randomColor(192, 256));
            g.fillRect(0, 0, width, height);
            g.setFont(new Font("DejaVu Sans", Font.ITALIC, 20));
            g.setColor(randomColor(128, 192));
            for (int i = 0; i < 155; i++) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                int xl = random.nextInt(12);
                int yl = random.nextInt(12);
                g.drawLine(x, y, x + xl, y + yl);
            }

            int length = code.length();
            for (int i = 0; i < length; i++) {
                String chr = String.valueOf(code.charAt(i));
                g.setColor(randomColor(24, 136));
                g.drawString(chr, 13 * i + 6, 16);
            }
        } finally {
            if (g != null) g.dispose();
            ;
        }
        return image;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private static Color randomColor(int rgbStart, int rgbEnd) {
        Random random = new Random();
        if (rgbStart > 255) {
            rgbStart %= 256;
        }
        if (rgbEnd > 255) {
            rgbEnd %= 256;
        }
        int bound = rgbEnd - rgbStart;
        int r = rgbStart + random.nextInt(bound);
        int g = rgbStart + random.nextInt(bound);
        int b = rgbStart + random.nextInt(bound);
        return new Color(r, g, b);
    }
}
