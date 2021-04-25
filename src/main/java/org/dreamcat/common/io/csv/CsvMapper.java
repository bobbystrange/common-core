package org.dreamcat.common.io.csv;

import java.io.IOException;
import java.io.Reader;
import org.dreamcat.common.databind.DataMapper;
import org.dreamcat.common.databind.DataType;

/**
 * Create by tuke on 2021/4/15
 */
public class CsvMapper implements DataMapper {

    @Override
    public <T> T readValue(Reader src, DataType type) throws IOException {
        if (type.isArray()) {

        }
        return null;
    }


}
