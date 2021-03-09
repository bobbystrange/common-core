package org.dreamcat.common.io.image;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.imageio.ImageIO;

/**
 * Create by tuke on 2019-06-19
 */
public final class ImageUtil {

    private ImageUtil() {
    }

    public static String base64(RenderedImage image, String formatName) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(image, formatName, Base64.getEncoder().wrap(os));
            return os.toString(StandardCharsets.ISO_8859_1.name());
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    public static String base64Jpeg(RenderedImage image) {
        return base64Image(image, "jpeg");
    }

    public static String base64Png(RenderedImage image) {
        return base64Image(image, "png");
    }

    public static String base64Image(RenderedImage image, String imageType) {
        String base64 = base64(image, imageType);
        return String.format("data:image/%s;base64,%s", imageType, base64);
    }

    public static BufferedImage fromBase64(String base64) {
        try (ByteArrayInputStream is = new ByteArrayInputStream(base64.getBytes())) {
            return ImageIO.read(Base64.getDecoder().wrap(is));
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    public static BufferedImage fromBase64Image(String base64Image) {
        return fromBase64(base64Image.replaceAll("^data:image/[a-zA-Z0-9]+?;base64,", ""));
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    // [0-255, 0-255, 0-255], black -1 to white -16777216
    public static int[][] getData(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        int[][] data = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                data[i][j] = image.getRGB(i, j);
            }
        }
        return data;
    }

}
