package org.dreamcat.common.io;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;

/**
 * Create by tuke on 2018/11/25
 */
public class IOUtil {
    private static final int BUFFER_SIZE = 1024 * 4;

    public static long copy(InputStream input, OutputStream output) throws IOException {
        return copy(input, output, BUFFER_SIZE);
    }

    public static long copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        long count = 0;
        int readSize;
        byte[] buffer = new byte[bufferSize];
        while ((readSize = input.read(buffer)) > 0) {
            output.write(buffer, 0, readSize);
            count += readSize;
        }
        return count;
    }

    public static long copy(Reader input, Writer output) throws IOException {
        return copy(input, output, BUFFER_SIZE);
    }

    public static long copy(Reader input, Writer output, int bufferSize) throws IOException {
        long count = 0;
        int readSize;
        char[] buffer = new char[bufferSize];
        while ((readSize = input.read(buffer)) > 0) {
            output.write(buffer, 0, readSize);
            count += readSize;
        }
        return count;
    }

    public static void copy(ReadableByteChannel input, WritableByteChannel output) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        while (input.read(buffer) > 0) {
            buffer.flip();
            output.write(buffer);
            buffer.clear();
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static String readAsString(Reader input) throws IOException {
        return new String(readFully(input));
    }

    public static String readAsString(InputStream input) throws IOException {
        return new String(readFully(new InputStreamReader(input)));
    }

    public static String readAsString(InputStream input, Charset charset) throws IOException {
        return new String(readFully(new InputStreamReader(input, charset)));
    }

    // read all to array
    public static void readFully(InputStream input, byte[] sink) throws IOException {
        int offset = 0;
        while (offset < sink.length) {
            int read = input.read(sink, offset, sink.length - offset);
            if (read == -1) return;
            offset += read;
        }
    }

    public static byte[] readFully(InputStream input) throws IOException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            int readSize;
            byte[] buf = new byte[BUFFER_SIZE];
            while ((readSize = input.read(buf)) > 0) {
                output.write(buf, 0, readSize);
            }
            return output.toByteArray();
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

    public static char[] readFully(Reader input) throws IOException {
        try (CharArrayWriter output = new CharArrayWriter()) {
            int readSize;
            char[] buf = new char[BUFFER_SIZE];
            while ((readSize = input.read(buf)) > 0) {
                output.write(buf, 0, readSize);
            }
            return output.toCharArray();
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====


}
