package org.dreamcat.common.io;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * Create by tuke on 2020/4/9
 */
public class NioUtilTest {

    @Test
    public void testCopy() throws IOException {
    }

    private void copyFile(String file1, String file2) throws IOException {
        ByteChannel i = Files.newByteChannel(
                new File(file1).toPath(),
                StandardOpenOption.READ);
        ByteChannel o = Files.newByteChannel(
                new File(file2).toPath(),
                StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
        NioUtil.copy(i, o);
    }
}
