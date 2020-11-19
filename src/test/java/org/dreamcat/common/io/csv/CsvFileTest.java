package org.dreamcat.common.io.csv;

import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.core.Timeit;
import org.junit.Test;

/**
 * Create by tuke on 2019-01-26
 */
@Slf4j
public class CsvFileTest {

    @Test
    public void speedTest() throws IOException {
        String input = CsvReaderTest.csvString();

        System.out.println("repeat \t CsvReader \t OpneCSV \t CsvFile");
        for (int i = 1; i <= 256; i *= 2) {
            String s = Timeit.ofActions()
                    .addAction(() -> {
                        try (CsvReader reader = new CsvReader(new StringReader(input))) {
                            while ((reader.readRecord()) != null) ;
                        }
                    })
                    .addAction(() -> {
                        try (CSVReader reader = new CSVReader(new StringReader(input))) {
                            while ((reader.readNext()) != null) ;
                        }
                    })
                    .addAction(() -> {
                        try (CsvFile reader = new CsvFile(new StringReader(input))) {
                            while ((reader.readRecord()) != null) ;
                        }
                    })
                    .parallel().count(6).skip(2).repeat(i).runAndFormatMs();
            System.out.printf("[%03d] \t %s\n", i, s);
        }
    }

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
