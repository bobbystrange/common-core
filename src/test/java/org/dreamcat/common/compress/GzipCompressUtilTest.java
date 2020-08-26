package org.dreamcat.common.compress;

import org.dreamcat.common.util.RandomUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.function.IntFunction;

/**
 * Create by tuke on 2020/4/6
 */
public class GzipCompressUtilTest {

    @Test
    public void test() throws IOException {
        byte[] data = RandomUtil.choose72(100).getBytes();
        byte[] compressed = GzipCompressUtil.compress(data);
        byte[] uncompressed = GzipCompressUtil.uncompress(compressed);

        System.out.println(data.length + ", " + new String(data));
        System.out.println(compressed.length);
        System.out.println(uncompressed.length + ", " + new String(uncompressed));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRatio() throws IOException {
        IntFunction<String>[] fns = new IntFunction[]{
                RandomUtil::choose10,
                RandomUtil::choose16,
                RandomUtil::choose26,
                RandomUtil::choose36,
                RandomUtil::choose52,
                RandomUtil::choose62,
                RandomUtil::choose72,
        };

        for (int i = 1; i < 1000; i++) {
            System.out.print(i + "\t");
            for (IntFunction<String> fn : fns) {
                byte[] data = fn.apply(i).getBytes();
                byte[] compressed = GzipCompressUtil.compress(data);
                byte[] uncompressed = GzipCompressUtil.uncompress(compressed);
                System.out.print(compressed.length + " ");
            }
            System.out.print("\n");
        }
    }

}
