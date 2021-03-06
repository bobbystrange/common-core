package org.dreamcat.java.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Create by tuke on 2020/4/8
 */
@Ignore
@Slf4j
public class FileChannelTest {

    @Test
    public void test() throws IOException {
        Path path = null;
        try {
            path = Files.createTempFile(getClass().getName(), "tmp");
            log.info("cat {}", path);
            try (FileChannel channel = new RandomAccessFile(path.toFile(), "rw").getChannel()) {
                channel.write(ByteBuffer.wrap("#!/usr/bin/env bash".getBytes()));
            }

            try (FileChannel channel = new RandomAccessFile(path.toFile(), "rw").getChannel()) {
                ByteBuffer buffer = ByteBuffer.allocate(4096);
                int count = channel.read(buffer);
                if (count > 0) {
                    log.info("({})", new String(buffer.array(), 0, count));

                }
            }
        } finally {
            if (path != null) {
                Files.delete(path);
            }
        }

    }
}
