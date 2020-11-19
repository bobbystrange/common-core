package org.dreamcat.common.compress;

import java.util.function.IntFunction;
import org.dreamcat.common.util.Base64Util;
import org.dreamcat.common.util.RandomUtil;
import org.junit.Test;


/**
 * Create by tuke on 2020/4/6
 */
public class InflaterCompressUtilTest {

    @Test
    public void test() throws Exception {
        byte[] data = RandomUtil.choose72(64).getBytes();
        byte[] compressed = DeflaterCompressUtil.compress(data);
        byte[] uncompressed = InflaterCompressUtil.uncompress(compressed);

        System.out.println(data.length + ", " + new String(data));
        System.out.println(compressed.length + ", " + Base64Util.encodeAsString(compressed));
        System.out.println(uncompressed.length + ", " + new String(uncompressed));
    }

    @Test
    public void testLevel() throws Exception {
        for (int size = 1; size <= 1000; size++) {
            byte[] data = RandomUtil.choose72(size).getBytes();
            System.out.print(size + "\t");
            for (int level = -1; level <= 9; level++) {
                byte[] compressed = DeflaterCompressUtil.compress(data, level);
                System.out.print(compressed.length + " ");
            }
            System.out.print("\n");
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
            System.out.print(i + "\t");
            for (IntFunction<String> fn : fns) {
                byte[] data = fn.apply(i).getBytes();
                byte[] compressed = DeflaterCompressUtil.compress(data);
                System.out.print(compressed.length + " ");
            }
            System.out.print("\n");
        }
    }
}
