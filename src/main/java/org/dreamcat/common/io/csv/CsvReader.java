package org.dreamcat.common.io.csv;

import org.dreamcat.common.core.Pair;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by tuke on 2020/8/3
 */
public class CsvReader implements Closeable {
    private static final char DEFAULT_ESCAPE_CHAR = '"';
    private static final char DEFAULT_DELIMITER_CHAR = ',';
    private static final int DEFAULT_CHAR_BUFFER_SIZE = 8192;
    private final char delimiter;
    private final char escape;
    // note that makes sure mark/reset is supported
    private BufferedReader in;
    private char[] cb;
    private int offset;
    // size = cb.length typical, if not, then it means `in` has no more data to read
    private int size;

    public CsvReader(BufferedReader in) {
        this(in, DEFAULT_DELIMITER_CHAR);
    }

    public CsvReader(BufferedReader in, char delimiter) {
        this(in, delimiter, DEFAULT_CHAR_BUFFER_SIZE);
    }

    public CsvReader(BufferedReader in, char delimiter, int bufferSize) {
        this(in, delimiter, bufferSize, DEFAULT_ESCAPE_CHAR);
    }

    public CsvReader(BufferedReader in, char delimiter, int bufferSize, char escape) {
        this.in = in;
        this.delimiter = delimiter;
        this.escape = escape;
        if (bufferSize <= 0)
            throw new IllegalArgumentException("Buffer size <= 0");
        cb = new char[bufferSize];
    }

    public Pair<String, Boolean> readWord() throws IOException {
        synchronized (in) {
            ensureOpen();
            return readWord0();
        }
    }

    public Pair<String, Boolean> readWord0() throws IOException {
        for (; ; ) {
            if (size == -1) {
                fill();
                continue;
            }

            if (size == 0) return null;

            if (offset == size) {
                fill();
                continue;
            }

            char firstChar = cb[offset];
            if (firstChar == delimiter) {
                if (offset < size - 1) {
                    char nextChar = cb[offset + 1];
                    if (nextChar == '\r') {
                        if (offset < size - 2) {
                            char nextNextChar = cb[offset + 2];
                            if (nextNextChar == '\n') {
                                offset += 3;
                            } else {
                                offset += 2;
                            }
                            return Pair.of("", true);
                        }
                        // offset = size - 2
                        else {
                            in.mark(1);
                            int nextNextChar = in.read();
                            // no more input
                            if (nextNextChar == -1) {
                                size = 0;
                                return Pair.of("", true);
                            } else if (nextNextChar == '\n') {
                                offset += 2;
                                return Pair.of("", true);
                            } else {
                                in.reset();
                                offset += 2;
                                return Pair.of("", true);
                            }
                        }
                    } else if (nextChar == '\n') {
                        offset += 2;
                        return Pair.of("", true);
                    } else {
                        offset += 1;
                        return Pair.of("", false);
                    }
                }
                // offset = size - 1
                else {
                    in.mark(2);
                    int nextChar = in.read();
                    if (nextChar == -1) {
                        size = 0;
                        return Pair.of("", true);
                    } else if (nextChar == '\r') {
                        int nextNextChar = in.read();
                        if (nextNextChar == -1) {
                            size = 0;
                            return Pair.of("", true);
                        } else if (nextNextChar == '\n') {
                            offset++;
                            return Pair.of("", true);
                        } else {
                            in.reset();
                            if (in.read() != '\r') {
                                throw new IllegalStateException("mark/reset error");
                            }
                            offset++;
                            return Pair.of("", true);
                        }
                    } else if (nextChar == '\n') {
                        offset++;
                        return Pair.of("", true);
                    } else {
                        in.reset();
                        offset++;
                        return Pair.of("", false);
                    }
                }
            } else if (firstChar != escape) {
                for (int i = offset + 1; i < size; i++) {
                    char c = cb[i];
                    // new line
                    if (c == '\r') {
                        if (i < size - 1) {
                            char nextChar = cb[i + 1];
                            if (nextChar == '\n') {
                                int off = offset;
                                int count = i - offset;
                                offset = i + 2;
                                return Pair.of(new String(cb, off, count), true);
                            } else {
                                int off = offset;
                                int count = i - offset;
                                offset = i + 1;
                                return Pair.of(new String(cb, off, count), true);
                            }
                        }
                        // i = size - 1
                        else {
                            in.mark(1);
                            int nextChar = in.read();
                            if (nextChar == -1) {
                                size = 0;
                                int off = offset;
                                int count = i - offset;
                                return Pair.of(new String(cb, off, count), true);
                            } else if (nextChar == '\n') {
                                int off = offset;
                                int count = i - offset;
                                offset = i + 1;
                                return Pair.of(new String(cb, off, count), true);
                            } else {
                                in.reset();
                                int off = offset;
                                int count = i - offset;
                                offset = i + 1;
                                return Pair.of(new String(cb, off, count), true);
                            }
                        }
                    } else if (c == '\n') {
                        int off = offset;
                        int count = i - offset;
                        offset = i + 1;
                        return Pair.of(new String(cb, off, count), true);
                    }
                }

                StringBuilder sb = new StringBuilder(size - offset);
                sb.append(cb, offset, size - offset);
                for (; ; ) {
                    fill();
                    for (int i = offset; i < size; i++) {
                        char c = cb[i];
                        if (c == delimiter) {
                            sb.append(cb, offset, i - offset);
                            offset = i + 1;
                            return Pair.of(sb.toString(), false);
                        } else if (c == '\r') {

                        }
                    }
                }
            }
        }
    }

    public List<String> readRecord() throws IOException {
        Pair<String, Boolean> pair;
        List<String> record = new ArrayList<>();
        for (; ; ) {
            pair = readWord();
            // no more data
            if (pair == null) break;

            String word = pair.first();
            boolean isLastWord = pair.second();

            record.add(word);

            if (isLastWord) break;
        }
        return record;
    }

    @Override
    public void close() throws IOException {
        try {
            in.close();
        } finally {
            in = null;
            cb = null;
        }
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

}
