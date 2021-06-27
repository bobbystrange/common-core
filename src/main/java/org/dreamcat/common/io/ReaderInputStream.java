package org.dreamcat.common.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * @author Jerry Will
 * @since 2021-06-20
 */
public class ReaderInputStream extends InputStream {

    private static final int DEFAULT_CHAR_BUFFER_SIZE = 8192;
    private Reader in;
    private char[] cb;
    private int offset;
    // size = cb.length typical, if not, then it means `in` has no more data to read
    private int size;
    private boolean high; //

    public ReaderInputStream(Reader in) {
        this(in, DEFAULT_CHAR_BUFFER_SIZE);
    }

    public ReaderInputStream(Reader in, int bufferSize) {
        this.in = in;
        if (bufferSize <= 0)
            throw new IllegalArgumentException("Buffer size <= 0");
        cb = new char[bufferSize];
        offset = -1;
        size = -1;
        high = true;
    }

    @Override
    public int read() throws IOException {
        synchronized (this) {
            ensureOpen();
            if (size == -1) {
                fill();
                return read();
            }
            if (size == 0) return -1;

            // already has buffer in cb
            if (offset < size) {
                int c = cb[offset];
                if (high) {
                    high = false;
                    return c >> 8;
                } else {
                    high = true;
                    offset++;
                    return c & 0xffff;
                }
            }

            fill();
            return read();
        }
    }

    @Override
    public void close() throws IOException {
        synchronized (this) {
            if (in == null) return;
            try {
                in.close();
            } finally {
                in = null;
                cb = null;
            }
        }
    }

    private void fill() throws IOException {
        offset = 0;

        size = in.read(cb);
        if (size <= 0) size = 0;
        high = true;
    }

    private void ensureOpen() throws IOException {
        if (in == null)
            throw new IOException("Stream closed");
    }

}
