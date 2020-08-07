package org.dreamcat.common.io.csv;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

/**
 * Create by tuke on 2019-01-26
 */
@Slf4j
public class CsvFileTest {

    @Test
    public void read() throws IOException {
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

        sb.append("\"echo \"\"cal, 9, 1752\"\"\", 65537, 1752-09-14");
        sb.append('\n');

        String str = sb.toString();
        System.out.println(str);

        CsvFile csvFile = new CsvFile(str);

        String[] record;
        while ((record = csvFile.readRecord()) != null) {
            System.out.printf("%d \t %s\n", record.length, Arrays.toString(record));
        }
    }

}
