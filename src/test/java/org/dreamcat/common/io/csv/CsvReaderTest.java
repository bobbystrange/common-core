package org.dreamcat.common.io.csv;

import org.dreamcat.common.core.Pair;
import org.dreamcat.common.io.LineTerminatedReaderTest;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

import static org.dreamcat.common.io.LineTerminatedReaderTest.formatCRLF;

/**
 * Create by tuke on 2020/8/3
 */
public class CsvReaderTest {

    private static StringBuilder fill(StringBuilder sb) {
        sb.append("65537").append(",");
        sb.append("\"").append("3.14").append("\"").append(",");
        sb.append("6.02214076E23").append(",");
        sb.append("\"").append("a,b,c").append("\"").append(",");
        sb.append("\"").append("a, \n, b, \r, c, \r\n, d, \n\r, e").append("\"");
        return sb;
    }

    public static String csvString() {
        StringBuilder sb = new StringBuilder();

        fill(sb).append("\n");
        fill(sb).append(",");
        fill(sb).append("\r");
        fill(sb).append(",");
        fill(sb).append("\r\n");

        return sb.toString();
    }

    public static String csvString2() {
        StringBuilder sb = new StringBuilder();
        sb.append(",,,,").append("\n");
        sb.append(",,,").append("\r");
        sb.append(",,").append("\r\n");
        sb.append(",").append("\n\r");

        sb.append("\n\r\n\r\r\n\r");
        sb.append(",\n,\r,\n,\r,\r,\n,\r,");
        sb.append(",,\n\r,,,\n\r , , \r , \n, ,\r");
        return sb.toString();
    }

    @Test
    public void readWordTest() throws IOException {
        readWord(csvString());
    }

    @Test
    public void readWordTest2() throws IOException {
        readWord(csvString2());
    }

    @Test
    public void test() throws IOException {
        readRecord(csvString());
    }

    @Test
    public void test2() throws IOException {
        readRecord(csvString2());
    }

    private void readWord(String input) throws IOException {
        System.out.println(formatCRLF(input));
        System.out.println();

        try (CsvReader reader = new CsvReader(new BufferedReader(new StringReader(input)))) {
            Pair<String, Boolean> word;
            while ((word = reader.readWord()) != null) {
                System.out.printf("[%s] \t %s\n", formatCRLF(word.first()), word.second());
            }
        }
    }

    public void readRecord(String input) throws IOException {
        System.out.println(formatCRLF(input));
        System.out.println();

        try (CsvReader reader = new CsvReader(new BufferedReader(new StringReader(input)))) {
            List<String> record;
            while ((record = reader.readRecord()) != null) {
                System.out.printf("[%s]\n", record.stream()
                        .map(LineTerminatedReaderTest::formatCRLF)
                        .collect(Collectors.joining("]  ---  [")));
            }
        }
    }

}
