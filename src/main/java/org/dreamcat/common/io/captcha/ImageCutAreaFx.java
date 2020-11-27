package org.dreamcat.common.io.captcha;

import java.util.function.IntBinaryOperator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * Create by tuke on 2020/5/12
 */
public class ImageCutAreaFx {

    public static IntBinaryOperator rect(int w) {
        return rect(w, w);
    }

    public static IntBinaryOperator rect(int w, int h) {
        return new Rect(w, h);
    }

    public static IntBinaryOperator notchRect(int w) {
        int radius = w / 6;
        return notchRect(w, w, radius);
    }

    public static IntBinaryOperator notchRect(int w, int h, int radius) {
        return new ClassicNotchedRect(w, h, radius);
    }

    @RequiredArgsConstructor
    public static class Rect implements IntBinaryOperator {

        private final int w;
        private final int h;

        @Override
        public int applyAsInt(int i, int j) {
            // outside
            if (i < 0 || i > w || j < 0 || j > h) {
                return 1;
            }
            // border
            else if (i == 0 || i == w || j == 0 || j == h) {
                return 0;
            }
            // inside
            else {
                return -1;
            }
        }
    }

    @AllArgsConstructor
    public static class ClassicNotchedRect implements IntBinaryOperator {

        private final int w;
        private final int h;
        private final int radius;
        private final int dx;
        private final int dy;
        private final double dw;
        private final double dh;

        public ClassicNotchedRect(int w, int h, int radius) {
            this.w = w;
            this.h = h;
            this.radius = radius;
            this.dx = (w - (radius << 1)) >> 1;
            this.dy = (h - (radius << 1)) >> 1;
            this.dw = 1.0 / w;
            this.dh = 1.0 / h;
        }

        @Override
        public int applyAsInt(int i, int j) {
            if (i < 0 || i > w + radius || j > h || j < -radius) {
                return 1;
            } else if ((i == 0 || i == w) && ((j >= 0 && j <= dy) || (j >= h - dy && j <= h))) {
                return 0;
            } else if (j == 0 && ((i >= 0 && i <= dx) || (i >= w - dx && i <= w))) {
                return 0;
            } else if (j == h && (i >= 0 && i <= w)) {
                return 0;
            } else if (j < 0) {
                if (i < dx || i > w - dx) {
                    return 1;
                } else {
                    double dist = Math.sqrt(Math.pow(i - (w >> 1), 2) + (j * j));
                    double sig = dist - radius;
                    return sig > dw ? 1 : (sig < -dw ? -1 : 0);
                }
            } else if (i > w) {
                if (j < dy || j > h - dy) {
                    return 1;
                } else {
                    double dist = Math.sqrt(Math.pow(j - (h >> 1), 2) + Math.pow(i - w, 2));
                    double sig = dist - radius;
                    return sig > dh ? 1 : (sig < -dh ? -1 : 0);
                }
            } else if (i < radius) {
                if (j < dy || j > h - dy) {
                    return -1;
                } else {
                    double dist = Math.sqrt(Math.pow(j - (h >> 1), 2) + (i * i));
                    double sig = dist - radius;
                    return sig > dh ? -1 : (sig < -dh ? 1 : 0);
                }
            } else {
                return -1;
            }
        }
    }


}
