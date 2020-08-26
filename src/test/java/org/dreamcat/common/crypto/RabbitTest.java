package org.dreamcat.common.crypto;

import org.dreamcat.common.util.ByteUtil;
import org.dreamcat.common.util.RandomUtil;
import org.junit.Test;

import java.util.Arrays;


/**
 * Create by tuke on 2020/5/13
 */
public class RabbitTest {

    private static byte[] cipher(byte[] input, byte[] key) {
        Rabbit rabbit = new Rabbit();
        key = RabbitUtil.paddingTo(key, 16);

        input = RabbitUtil.padding(input, 16);
        int len = input.length;
        byte[] output = new byte[len];

        byte[] inputBuf = new byte[16];
        byte[] outputBuf = new byte[16];
        for (int i = 0; i < len; i += 16) {
            inputBuf = Arrays.copyOfRange(input, i, i + 16);
            rabbit.initKey(key);
            rabbit.cipher(inputBuf, outputBuf);
            System.arraycopy(outputBuf, 0, output, i, 16);
        }
        return output;
    }

    @Test
    public void test() throws Exception {
        byte[] key = RandomUtil.choose72(16).getBytes();

        byte[] input = RandomUtil.choose72(64).getBytes();
        System.out.println("source " + ByteUtil.hex(input));
        byte[] result = RabbitUtil.cipher(input, key);
        System.out.println("target " + ByteUtil.hex(result));
        byte[] decrypted = RabbitUtil.cipher(result, key);
        System.out.println("decrypted " + ByteUtil.hex(decrypted));
    }

    @Test
    public void rabbitTest() throws Exception {
        byte[] key = RandomUtil.choose72(16).getBytes();
        byte[] input = RandomUtil.choose72(512).getBytes();
        byte[] output = new byte[input.length];
        byte[] decrypted = new byte[input.length];

        Rabbit rabbit = new Rabbit();

        rabbit.initKey(key);
        rabbit.cipher(input, output);

        rabbit.initKey(key);
        rabbit.cipher(output, decrypted);

        System.out.println("source " + ByteUtil.hex(input));
        System.out.println("target " + ByteUtil.hex(output));
        System.out.println("decrypted " + ByteUtil.hex(decrypted));

        System.out.println();
        output = cipher(input, key);
        decrypted = cipher(output, key);
        System.out.println("target " + ByteUtil.hex(output));
        System.out.println("decrypted " + ByteUtil.hex(decrypted));
    }
}
