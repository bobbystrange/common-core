package org.dreamcat.common.databind;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import org.dreamcat.common.io.ReaderInputStream;

/**
 * @author Jerry Will
 * @since 2021-06-20
 */
public interface BinaryMapper extends DataMapper {

    @Override
    default <T> T readValue(Reader src, DataType type) throws IOException {
        try (InputStream inputStream = new ReaderInputStream(src)) {
            return readValue(inputStream, type);
        }
    }
}
