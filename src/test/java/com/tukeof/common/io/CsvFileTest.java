package com.tukeof.common.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * Create by tuke on 2019-01-26
 */
@Slf4j
public class CsvFileTest {

    private File tempFile;

    @Before
    public void init() throws IOException {
        tempFile = File.createTempFile(getClass().getName(), "csv");
        try (BufferedWriter output = new BufferedWriter(new FileWriter(tempFile))) {
            output.write("  #!/bin/sh -s \"cal, 9, 1752\"");
            output.newLine();

            // cal 9 1752
            output.write("65537, cal 9 1752, 1752-09-14");
            output.newLine();

            output.write("65537, \"cal 9 1752\", 1752-09-14");
            output.newLine();

            output.write("65537, \"cal, 9, 1752\", 1752-09-14");
            output.newLine();

            output.write("\"cal 9 1752\", 65537, 1752-09-14");
            output.newLine();

            output.write("\"cal, 9, 1752\", 65537, 1752-09-14");
            output.newLine();

            output.write("\"echo \\\"cal, 9, 1752\\\"\", 65537, 1752-09-14");
            output.newLine();
        }
    }

    @Test
    public void read() throws IOException {
        CsvFile csvFile = new CsvFile(tempFile);
        csvFile.setDoubleQuotesEscaping(true);

        String[] record;
        while ((record = csvFile.readRecord()) != null) {
            log.info("size: {}, content: {}", record.length, Arrays.toString(record));
        }

    }
}
