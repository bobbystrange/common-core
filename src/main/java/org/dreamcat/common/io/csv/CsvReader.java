package org.dreamcat.common.io.csv;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.dreamcat.common.core.Pair;
import org.dreamcat.common.util.ArrayUtil;

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
    private Reader in;
    private char[] cb;
    private int offset;
    // size = cb.length typical, if not, then it means `in` has no more data to read
    private int size;

    public CsvReader(Reader in) {
        this(in, DEFAULT_DELIMITER_CHAR);
    }

    public CsvReader(Reader in, char delimiter) {
        this(in, delimiter, DEFAULT_CHAR_BUFFER_SIZE);
    }

    public CsvReader(Reader in, char delimiter, int bufferSize) {
        this(in, delimiter, bufferSize, DEFAULT_ESCAPE_CHAR);
    }

    public CsvReader(Reader in, char delimiter, int bufferSize, char escape) {
        if (!in.markSupported()) {
            this.in = new BufferedReader(in);
        } else {
            this.in = in;
        }
        this.delimiter = delimiter;
        this.escape = escape;
        if (bufferSize <= 0)
            throw new IllegalArgumentException("buffer size <= 0");
        cb = new char[bufferSize];
    }

    public Pair<String, Boolean> readWord() throws IOException {
        synchronized (in) {
            ensureOpen();
            Pair<StringBuilder, Boolean> pair = new Pair<>();
            readWord0(pair);
            StringBuilder sb = pair.first();
            Boolean lastWord = pair.second();
            if (lastWord == null) return null;
            String word = sb == null ? "" : sb.toString();
            return new Pair<>(word, lastWord);
        }
    }

    private void readWord0(Pair<StringBuilder, Boolean> pair) throws IOException {
        for (; ; ) {
            // no init or no buffer
            if (size == -1 || offset == size) {
                fill();
                continue;
            }
            // no more input
            if (size == 0) return;

            char firstChar = cb[offset];
            if (firstChar == delimiter) {
                offset += 1;
                pair.setSecond(false);
                return;
            }
            // first char is CR
            else if (firstChar == '\r') {
                if (offset < size - 1) {
                    char nextChar = cb[offset + 1];
                    if (nextChar == '\n') {
                        offset += 2;
                        pair.setSecond(true);
                        return;
                    } else {
                        offset++;
                        pair.setSecond(true);
                        return;
                    }
                }
                // offset = size - 1
                else {
                    in.mark(1);
                    int nextNextChar = in.read();
                    if (nextNextChar == -1) {
                        size = 0;
                        pair.setSecond(true);
                        return;
                    } else if (nextNextChar == '\n') {
                        offset += 1;
                        pair.setSecond(true);
                        return;
                    } else {
                        in.reset();
                        offset += 1;
                        pair.setSecond(true);
                        return;
                    }
                }
            }
            // first char is LF
            else if (firstChar == '\n') {
                offset += 1;
                pair.setSecond(true);
                return;
            }
            // first char is normal
            else if (firstChar != escape) {
                for (; ; ) {
                    for (int i = offset + 1; i < size; i++) {
                        char c = cb[i];
                        if (c == delimiter) {
                            append(pair, i, false);
                            offset = i + 1;
                            return;
                        }
                        // new line
                        else if (c == '\r') {
                            if (i < size - 1) {
                                char nextChar = cb[i + 1];
                                if (nextChar == '\n') {
                                    append(pair, i, true);
                                    offset = i + 2;
                                    return;
                                } else {
                                    append(pair, i, true);
                                    offset = i + 1;
                                    return;
                                }
                            }
                            // i = size - 1
                            else {
                                in.mark(1);
                                int nextChar = in.read();
                                if (nextChar == -1) {
                                    size = 0;
                                    append(pair, i, true);
                                    return;
                                } else if (nextChar == '\n') {
                                    append(pair, i, true);
                                    offset = i + 1;
                                    return;
                                } else {
                                    in.reset();
                                    append(pair, i, true);
                                    offset = i + 1;
                                    return;
                                }
                            }
                        } else if (c == '\n') {
                            append(pair, i, true);
                            offset = i + 1;
                            return;
                        }
                    }

                    appendAll(pair);
                    fill();
                    if (size == 0) {
                        pair.setSecond(true);
                        return;
                    }
                }
            }
            // first char is escape "
            else {
                pair.setSecond(true);
                escape_loop:
                for (; ; ) {
                    for (offset = offset + 1; offset < size; offset++) {
                        char c = cb[offset];
                        if (c != escape) {
                            append(pair, c);
                        } else {
                            if (offset < size - 1) {
                                char nextChar = cb[offset + 1];
                                if (nextChar == escape) {
                                    append(pair, c);
                                    offset++;
                                } else if (nextChar == delimiter) {
                                    offset += 2;
                                    pair.setSecond(false);
                                    return;
                                } else if (nextChar == '\n') {
                                    offset += 2;
                                    return;
                                } else if (nextChar == '\r') {
                                    if (offset < size - 2) {
                                        char nextNextChar = cb[offset + 2];
                                        if (nextNextChar == '\n') {
                                            offset += 3;
                                        } else {
                                            offset += 2;
                                        }
                                        return;
                                    } // offset = size - 2
                                    else {
                                        in.mark(1);
                                        int nextNextChar = in.read();
                                        // no more input
                                        if (nextNextChar == -1) {
                                            size = 0;
                                            return;
                                        } else if (nextNextChar == '\n') {
                                            offset += 2;
                                            return;
                                        } else {
                                            in.reset();
                                            offset += 2;
                                            return;
                                        }
                                    }
                                } else {
                                    throw new IllegalArgumentException(
                                            String.format("illegal char %s at pos %d of %s",
                                                    fmt(nextChar), offset + 1,
                                                    formatCRLF(new String(cb, 0, size))));
                                }
                            }
                            // offset = size - 1
                            else {
                                in.mark(2);
                                int nextChar = in.read();
                                if (nextChar == -1) {
                                    size = 0;
                                    return;
                                } else if (nextChar == escape) {
                                    append(pair, c);
                                    fill();
                                    if (size == 0) {
                                        throw new IllegalArgumentException(
                                                String.format("illegal char %s at pos %d of %s",
                                                        fmt(c), offset + 1,
                                                        formatCRLF(new String(cb, 0, size))));
                                    }
                                    continue escape_loop;
                                } else if (nextChar == delimiter) {
                                    offset++;
                                    pair.setSecond(false);
                                    return;
                                } else if (nextChar == '\n') {
                                    offset++;
                                    return;
                                } else if (nextChar == '\r') {
                                    int nextNextChar = in.read();
                                    if (nextNextChar == -1) {
                                        size = 0;
                                        return;
                                    } else if (nextNextChar == '\n') {
                                        offset++;
                                        return;
                                    } else {
                                        in.reset();
                                        if (in.read() != '\r') {
                                            throw new IllegalStateException("mark/reset error");
                                        }
                                        offset++;
                                        return;
                                    }
                                } else {
                                    throw new IllegalArgumentException(
                                            String.format("illegal char %s at pos %d of %s",
                                                    fmt((char) nextChar), offset + 1,
                                                    formatCRLF(new String(cb, 0, size))));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public String[] readNext() throws IOException {
        List<String> record = readRecord();
        if (record == null) return null;
        if (record.isEmpty()) return ArrayUtil.EMPTY_STRING_ARRAY;
        else return record.toArray(ArrayUtil.EMPTY_STRING_ARRAY);
    }

    public List<String[]> readAll() throws IOException {
        List<String[]> all = new ArrayList<>();
        String[] next;
        while ((next = readNext()) != null) {
            all.add(next);
        }
        return all;
    }

    public List<String> readRecord() throws IOException {
        List<String> record = null;

        synchronized (in) {
            for (; ; ) {
                if (size == -1 || offset == size) {
                    fill();
                    continue;
                }
                // no more input
                if (size == 0) return record;
                break;
            }
        }

        char firstChar = cb[offset];
        if (firstChar == '\r' || firstChar == '\n') {
            readWord();
            return Collections.emptyList();
        }

        for (; ; ) {
            Pair<String, Boolean> pair = readWord();
            // no more data
            if (pair == null) break;

            String word = pair.first();
            boolean lastWord = pair.second();

            if (record == null) record = new ArrayList<>();
            record.add(word);

            if (lastWord) break;
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
        if (size < 0) {
            size = 0;
            offset = -1;
        } else {
            offset = 0;
        }
    }

    private void append(Pair<StringBuilder, Boolean> pair, char c) {
        StringBuilder sb = pair.first();
        if (sb == null) {
            sb = new StringBuilder();
            pair.setFirst(sb);
        }
        sb.append(c);
    }

    private void append(Pair<StringBuilder, Boolean> pair, int i, boolean lastWord) {
        StringBuilder sb = pair.first();
        if (sb == null) {
            sb = new StringBuilder(i - offset);
            pair.setFirst(sb);
        }
        sb.append(cb, offset, i - offset);
        pair.setSecond(lastWord);

        offset = i + 2;
    }

    private void appendAll(Pair<StringBuilder, Boolean> pair) {
        StringBuilder sb = pair.first();
        if (sb == null) {
            sb = new StringBuilder(size - offset);
            pair.setFirst(sb);
        }
        sb.append(cb, offset, size - offset);
    }

    private static String formatCRLF(String s) {
        return s.chars().mapToObj(c -> {
            if (c == '\r') return "\\r";
            if (c == '\n') return "\\n";
            return (char) c + "";
        }).collect(Collectors.joining());
    }

    private static String formatCRLF(int lineTerminated) {
        if (lineTerminated == '\r') return "\\r";
        if (lineTerminated == '\n') return "\\n";
        else return "\\r\\n";
    }

    public static String fmt(char c) {
        if (c == '\r') return "\\r";
        if (c == '\n') return "\\n";
        return String.valueOf(c);
    }
}
