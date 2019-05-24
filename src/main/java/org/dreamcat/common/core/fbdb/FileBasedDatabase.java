package org.dreamcat.common.core.fbdb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Create by tuke on 2019-01-03
 */
public class FileBasedDatabase implements AutoCloseable {
    private static final String ACCESS_MODE = "rw";
    private final RandomAccessFile file;

    public FileBasedDatabase(String filename) throws FileNotFoundException {
        this(new File(filename));
    }

    public FileBasedDatabase(File file) throws FileNotFoundException {
        this.file = new RandomAccessFile(file, ACCESS_MODE);
    }

    @Override
    public void close() throws IOException {

    }
}
