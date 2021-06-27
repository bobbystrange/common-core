package org.dreamcat.common.databind;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * @author Jerry Will
 * @since 2021-06-20
 */
public interface TextMapper extends DataMapper {

    @Override
    default <T> T readValue(InputStream src, DataType type) throws IOException {
        try (Reader reader = new InputStreamReader(src, StandardCharsets.UTF_8)) {
            return readValue(reader, type);
        }
    }
}
