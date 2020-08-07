package org.dreamcat.common.compress;

import org.dreamcat.common.util.Base64Util;
import org.dreamcat.common.util.RandomUtil;
import org.junit.Test;

import java.util.function.IntFunction;

import static org.dreamcat.common.util.FormatUtil.print;
import static org.dreamcat.common.util.FormatUtil.println;

/**
 * Create by tuke on 2020/4/6
 */
public class InflaterCompressUtilTest {

    @Test
    public void test() throws Exception {
        byte[] data = RandomUtil.choose72(64).getBytes();
        byte[] compressed = DeflaterCompressUtil.compress(data);
        byte[] uncompressed = InflaterCompressUtil.uncompress(compressed);

        println(data.length, new String(data));
        println(compressed.length, Base64Util.encodeAsString(compressed));
        println(uncompressed.length, new String(uncompressed));
    }

    @Test
    public void testLevel() throws Exception {
        for (int size = 1; size <= 1000; size++) {
            byte[] data = RandomUtil.choose72(size).getBytes();
            print(size + "\t");
            for (int level = -1; level <= 9; level++) {
                byte[] compressed = DeflaterCompressUtil.compress(data, level);
                print(compressed.length + " ");
            }
            print("\n");
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRatio() throws Exception {
        IntFunction<String>[] fns = new IntFunction[]{
                RandomUtil::choose10,
                RandomUtil::choose16,
                RandomUtil::choose26,
                RandomUtil::choose36,
                RandomUtil::choose52,
                RandomUtil::choose62,
                RandomUtil::choose72,
        };

        for (int i = 1; i < 365; i++) {
            print(i + "\t");
            for (IntFunction<String> fn : fns) {
                byte[] data = fn.apply(i).getBytes();
                byte[] compressed = DeflaterCompressUtil.compress(data);
                print(compressed.length + " ");
            }
            print("\n");
        }
    }
}
