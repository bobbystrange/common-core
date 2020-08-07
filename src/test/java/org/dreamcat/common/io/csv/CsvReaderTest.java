package org.dreamcat.common.io.csv;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

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

        fill(sb).append(" ").append("\n");
        fill(sb).append("\t").append("\n");
        fill(sb).append(" \b\t").append("\n");

        fill(sb).append(" ").append("\r");
        fill(sb).append("\t").append("\r");
        fill(sb).append(" \b\t").append("\r");

        fill(sb).append(" ").append("\r\n");
        fill(sb).append("\t").append("\n\n");
        fill(sb).append(" \b\t").append("\n\n");

        return sb.toString();
    }

    @Test
    public void test() throws IOException {
        CsvFile reader = new CsvFile(csvString());
        String[] record;
        while ((record = reader.readRecord()) != null) {
            System.out.println(Arrays.toString(record));
        }
    }

}
