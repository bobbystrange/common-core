package org.dreamcat.common.io.csv;

import java.io.IOException;
import java.io.Reader;
import org.dreamcat.common.databind.DataType;
import org.dreamcat.common.databind.TextMapper;

/**
 * Create by tuke on 2021/4/15
 */
public class CsvMapper implements TextMapper {

    @Override
    public <T> T readValue(Reader src, DataType type) throws IOException {
        return null;
    }
}
