package org.dreamcat.common.io;

import org.dreamcat.common.core.Pair;

import java.io.IOException;
import java.io.Reader;

/**
 * Create by tuke on 2020/8/6
 * <p>
 * Note that this is a slower (almost 50%) implement than BufferedReader
 */
public class LineTerminatedReader extends Reader {
    public static final int CR = '\r';
    public static final int LF = '\n';
    public static final int CRLF = ('\r' << 8) + '\n';
    private static final int DEFAULT_CHAR_BUFFER_SIZE = 8192;
    private Reader in;
    private char[] cb;
    private int offset;
    // size = cb.length typical, if not, then it means `in` has no more data to read
    private int size;

    public LineTerminatedReader(Reader in) {
        this(in, DEFAULT_CHAR_BUFFER_SIZE);
    }

    public LineTerminatedReader(Reader in, int bufferSize) {
        super(in);
        this.in = in;
        if (bufferSize <= 0)
            throw new IllegalArgumentException("Buffer size <= 0");
        cb = new char[bufferSize];
        offset = -1;
        size = -1;
    }

    @Override
    public int read() throws IOException {
        synchronized (lock) {
            ensureOpen();
            if (size == -1) {
                fill();
                return read();
            }
            if (size == 0) return -1;

            // already has buffer in cb
            if (offset < size) {
                return cb[offset++];
            }
            fill();
            return read();
        }
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        synchronized (lock) {
            ensureOpen();
            if ((off < 0) || (off > cbuf.length) || (len < 0) ||
                    ((off + len) > cbuf.length) || ((off + len) < 0)) {
                throw new IndexOutOfBoundsException();
            } else if (len == 0) {
                return 0;
            }

            return read0(cbuf, off, len);
        }
    }

    @Override
    public void close() throws IOException {
        synchronized (lock) {
            if (in == null) return;
            try {
                in.close();
            } finally {
                in = null;
                cb = null;
            }
        }
    }

    public Pair<String, Integer> readLine() throws IOException {
        Pair<StringBuilder, Integer> pair = new Pair<>();
        readLine0(pair);
        StringBuilder sb = pair.first();
        Integer lineTerminated = pair.second();
        if (sb == null || lineTerminated == null) return null;
        return Pair.of(sb.toString(), lineTerminated);
    }

    /// private methods

    private void ensureOpen() throws IOException {
        if (in == null)
            throw new IOException("Stream closed");
    }

    private void fill() throws IOException {
        size = in.read(cb);
        if (size < 0) size = 0;
        offset = 0;
    }

    private int read0(char[] cbuf, int off, int len) throws IOException {
        for (; ; ) {
            if (size == -1) {
                fill();
                continue;
            }
            if (size == 0) return 0;

            if (offset == size) {
                fill();
                continue;
            }

            // already has buffer in cb
            int bs = size - offset;
            if (bs >= len) {
                System.arraycopy(cb, offset, cbuf, off, len);
                offset += len;
                return len;
            } else {
                System.arraycopy(cb, offset, cbuf, off, bs);
                offset = size;
                return bs + read0(cbuf, off + bs, len - bs);
            }
        }
    }

    private void readLine0(Pair<StringBuilder, Integer> pair) throws IOException {
        StringBuilder sb = pair.first();
        synchronized (lock) {
            ensureOpen();
            for (; ; ) {
                if (size == -1) {
                    fill();
                    continue;
                }
                if (size == 0) return;

                // no buffer in cb
                if (offset == size) {
                    fill();
                    continue;
                }

                // already has buffer in cb
                for (int i = offset; i < size; i++) {
                    char c = cb[i];
                    if (c == '\r') {
                        if (i < size - 1) {
                            char nextChar = cb[i + 1];
                            if (nextChar == '\n') {
                                append2(pair, sb, i);
                                return;
                            } else {
                                append1(pair, sb, i, CR);
                                return;
                            }
                        } else {
                            in.mark(1);
                            int nextChar = in.read();
                            // no more input
                            if (nextChar == -1) {
                                size = 0;
                                append1(pair, sb, i, CR);
                                return;
                            } else if (nextChar == '\n') {
                                append1(pair, sb, i, CRLF);
                                return;
                            } else {
                                in.reset();
                                append1(pair, sb, i, CR);
                                return;
                            }
                        }

                    } else if (c == '\n') {
                        append1(pair, sb, i, LF);
                        return;
                    }
                }

                sb.append(cb, offset, size - offset);
                fill();
            }
        }
    }

    private void append1(Pair<StringBuilder, Integer> pair, StringBuilder sb, int i, int lineTerminated) {
        if (sb == null) sb = new StringBuilder(i - offset);
        sb.append(cb, offset, i - offset);
        offset = i + 1;
        pair.setFirst(sb);
        pair.setSecond(lineTerminated);
    }

    private void append2(Pair<StringBuilder, Integer> pair, StringBuilder sb, int i) {
        if (sb == null) sb = new StringBuilder(i - offset);
        sb.append(cb, offset, i - offset);
        offset = i + 2;
        pair.setFirst(sb);
        pair.setSecond(LineTerminatedReader.CRLF);
    }

}
