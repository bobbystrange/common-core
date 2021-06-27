package org.dreamcat.common.io.ini;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import org.dreamcat.common.databind.DataFactory;
import org.dreamcat.common.databind.DataType;
import org.dreamcat.common.databind.TextMapper;

/**
 * @author Jerry Will
 * @since 2021-06-20
 */
@SuppressWarnings({"unchecked"})
public class IniMapper implements TextMapper {

    @Override
    public <T> T readValue(Reader src, DataType type) throws IOException {
        IniFile file = new IniFile();
        Map<String, Map<String, Object>> sections = file.getSections();
        return (T) DataFactory.fromMap(sections, type);
    }

}
