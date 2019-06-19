package org.dreamcat.common.io;

import org.dreamcat.common.core.Pair;

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
public class RenderedImageUtil {

    // <image src="data:image/png,xxx">
    public static String imageToBase64Source(RenderedImage image, String imageType) {
        return String.format("data:image/%s;base64,%s", imageType, imageToBase64(image, imageType));
    }

    /**
     * image to base64
     * @param image image object
     * @param imageType jpg, png
     * @return base64
     */
    public static String imageToBase64(RenderedImage image, String imageType) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(image, imageType, Base64.getEncoder().wrap(os));
            return os.toString(StandardCharsets.ISO_8859_1.name());
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    public Pair<BufferedImage, String> generateImageCode(int width, int height, int length) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        StringBuilder code = new StringBuilder(length);
        Graphics g = null;
        try {
            g = image.getGraphics();
            Random random = new Random();

            g.setColor(generateRandomColor(192, 256));
            g.fillRect(0, 0, width, height);
            g.setFont(new Font("DejaVu Sans", Font.ITALIC, 20));
            g.setColor(generateRandomColor(128, 192));
            for (int i = 0; i < 155; i++) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                int xl = random.nextInt(12);
                int yl = random.nextInt(12);
                g.drawLine(x, y, x + xl, y + yl);
            }

            for (int i = 0; i < length; i++) {
                String rand = String.valueOf(random.nextInt(10));
                code.append(rand);
                g.setColor(generateRandomColor(24, 136));
                g.drawString(rand, 13 * i + 6, 16);
            }
        } finally {
            if (g != null) g.dispose();;
        }

        return new Pair<>(image, code.toString());
    }

    private static Color generateRandomColor(int rgbStart, int rgbEnd) {
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
