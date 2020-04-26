package org.dreamcat.common.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

/**
 * Create by tuke on 2020/4/9
 */
public class NioUtil {
    private static final int BUFFER_SIZE = 1024 * 4;

    public static void copy(ByteChannel input, ByteChannel output) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        while (input.read(buffer) > 0) {
            buffer.flip();
            output.write(buffer);
            buffer.clear();
        }
    }
}
