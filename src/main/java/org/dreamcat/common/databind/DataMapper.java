package org.dreamcat.common.databind;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

/**
 * Create by tuke on 2021/4/15
 */
public interface DataMapper {

    default <T> T readValue(Reader src, Class<T> clazz) throws IOException {
        return readValue(src, DataTypes.fromType(clazz));
    }

    default <T> T readValue(InputStream src, Class<T> clazz) throws IOException {
        return readValue(src, DataTypes.fromType(clazz));
    }

    default <T> T readValue(String content, Class<T> clazz) {
        return readValue(content, DataTypes.fromType(clazz));
    }

    default <T> T readValue(byte[] content, Class<T> clazz) throws IOException {
        return readValue(content, DataTypes.fromType(clazz));
    }

    default <T> T readValue(byte[] content, int offset, int length, Class<T> clazz) throws IOException {
        return readValue(content, offset, length, DataTypes.fromType(clazz));
    }

    default <T> T readValue(File src, Class<T> clazz) throws IOException {
        return readValue(src, DataTypes.fromType(clazz));
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    <T> T readValue(Reader src, DataType type) throws IOException;

    <T> T readValue(InputStream src, DataType type) throws IOException;

    default <T> T readValue(String content, DataType type) {
        try (Reader reader = new StringReader(content)) {
            return readValue(reader, type);
        } catch (IOException e) {
            // never happen since we use a string reader
            throw new IllegalStateException(e);
        }
    }

    default <T> T readValue(byte[] content, DataType type) throws IOException {
        try (ByteArrayInputStream src = new ByteArrayInputStream(content)) {
            return readValue(src, type);
        }
    }

    default <T> T readValue(byte[] content, int offset, int length, DataType type) throws IOException {
        try (ByteArrayInputStream src = new ByteArrayInputStream(content, offset, length)) {
            return readValue(src, type);
        }
    }

    default <T> T readValue(File src, DataType type) throws IOException {
        try (Reader reader = new FileReader(src)) {
            return readValue(reader, type);
        }
    }

}
