package org.dreamcat.common.compress;

import org.dreamcat.common.util.RandomUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.function.IntFunction;

import static org.dreamcat.common.util.FormatUtil.print;
import static org.dreamcat.common.util.FormatUtil.println;

/**
 * Create by tuke on 2020/4/6
 */
public class GzipCompressUtilTest {

    @Test
    public void test() throws IOException {
        byte[] data = RandomUtil.choose72(100).getBytes();
        byte[] compressed = GzipCompressUtil.compress(data);
        byte[] uncompressed = GzipCompressUtil.uncompress(compressed);

        println(data.length, new String(data));
        println(compressed.length);
        println(uncompressed.length, new String(uncompressed));
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
            print(i + "\t");
            for (IntFunction<String> fn : fns) {
                byte[] data = fn.apply(i).getBytes();
                byte[] compressed = GzipCompressUtil.compress(data);
                byte[] uncompressed = GzipCompressUtil.uncompress(compressed);
                print(compressed.length + " ");
            }
            print("\n");
        }
    }

}
