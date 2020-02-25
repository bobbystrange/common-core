package org.dreamcat.common.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

/**
 * Create by tuke on 2019-01-26
 */
@Slf4j
public class CsvReaderTest {

    private String str;

    @Before
    public void init() throws IOException {
        StringBuilder sb = new StringBuilder(2 << 8);

        sb.append("  #!/bin/sh -s \"cal, 9, 1752\"");
        sb.append('\n');

        // cal 9 1752
        sb.append("65537, cal 9 1752, 1752-09-14");
        sb.append('\n');

        sb.append("65537, \"cal 9 1752\", 1752-09-14");
        sb.append('\n');

        sb.append("65537, \"cal, 9, 1752\", 1752-09-14");
        sb.append('\n');

        sb.append("\"cal 9 1752\", 65537, 1752-09-14");
        sb.append('\n');

        sb.append("\"cal, 9, 1752\", 65537, 1752-09-14");
        sb.append('\n');

        sb.append("\"echo \\\"cal, 9, 1752\\\"\", 65537, 1752-09-14");
        sb.append('\n');

        str = sb.toString();
        log.info("str:\n{}", str);
    }

    @Test
    public void read() throws IOException {
        CsvReader csvReader = new CsvReader(new StringReader(str));
        csvReader.setDoubleQuotesEscaping(true);

        String[] record;
        while ((record = csvReader.readRecord()) != null) {
            log.info("size: {}, content: {}", record.length, Arrays.toString(record));
        }

    }
}
