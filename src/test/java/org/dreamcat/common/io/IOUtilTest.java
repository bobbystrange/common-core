package org.dreamcat.common.io;

import java.io.File;
import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import org.junit.Test;

/**
 * Create by tuke on 2020/4/9
 */
public class IOUtilTest {

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
        IOUtil.copy(i, o);
    }
}
