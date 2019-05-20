package com.tukeof.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * Create by tuke on 2018/11/25
 */
public class IOUtil {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static long copy(InputStream input, OutputStream output) throws IOException {
        return copy(input, output, DEFAULT_BUFFER_SIZE);
    }

    public static long copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        return copy(input, output, new byte[bufferSize]);
    }

    public static long copy(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        long count = 0;
        int readSize;
        while ((readSize = input.read(buffer)) > 0) {
            output.write(buffer, 0, readSize);
            count += readSize;
        }
        return count;
    }

    public static long copy(Reader input, Writer output) throws IOException {
        return copy(input, output, DEFAULT_BUFFER_SIZE);
    }

    public static long copy(Reader input, Writer output, int bufferSize) throws IOException {
        return copy(input, output, new char[bufferSize]);
    }

    public static long copy(Reader input, Writer output, char[] buffer) throws IOException {
        long count = 0;
        int readSize;
        while ((readSize = input.read(buffer)) > 0) {
            output.write(buffer, 0, readSize);
            count += readSize;
        }
        return count;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    // read all to array
    public static void readFully(InputStream istream, byte[] sink) throws IOException {
        int offset = 0;
        while (offset < sink.length) {
            int read = istream.read(sink, offset, sink.length - offset);
            if (read == -1) return;
            offset += read;
        }
    }

    public static void readFully(Reader reader, char[] sink) throws IOException {
        int offset = 0;
        while (offset < sink.length) {
            int read = reader.read(sink, offset, sink.length - offset);
            if (read == -1) return;
            offset += read;
        }
    }
}
