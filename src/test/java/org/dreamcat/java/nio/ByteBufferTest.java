package org.dreamcat.java.nio;

import java.nio.ByteBuffer;
import org.junit.Test;

/**
 * Create by tuke on 2020/4/26
 */
public class ByteBufferTest {

    @Test
    public void test() {
        // native method invocation
        ByteBuffer buffer = ByteBuffer.allocateDirect(16);
        buffer.put((byte) 0b01);
    }
}
