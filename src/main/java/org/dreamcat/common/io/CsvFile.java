package org.dreamcat.common.io;

import org.dreamcat.common.util.ObjectUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Create by tuke on 2018-09-22
 */
@Getter
@Setter
public class CsvFile implements AutoCloseable {
    private static final String ACCESS_MODE = "rw";
    private final List<List<String>> table = new ArrayList<>();
    private char delimiter = ',';
    private boolean trim = true;
    // treat "-, -, -" as one string
    private boolean doubleQuotesEscaping = false;
    private RandomAccessFile file;

    public CsvFile(String filename) throws FileNotFoundException {
        this.file = new RandomAccessFile(new File(filename), ACCESS_MODE);
    }

    public CsvFile(File file) throws FileNotFoundException {
        this.file = new RandomAccessFile(file, ACCESS_MODE);
    }

    @Override
    public void close() throws IOException {
        file.close();
    }

    public String[] readRecord() throws IOException {
        while (true) {
            String line = file.readLine();
            if (ObjectUtil.isBlank(line)) return null;
            if (trim) line = line.trim();

            if (line.startsWith("#")) continue;

            if (!doubleQuotesEscaping) {
                return Arrays.stream(line.split(String.valueOf(delimiter)))
                        .map(String::trim)
                        .toArray(String[]::new);
            }

            List<String> words = new ArrayList<>();
            retrieveWord(line, words);
            return words.toArray(new String[0]);
        }
    }

    // FIXME there may be some bug here, check and fix-up in future.
    private void retrieveWord(String string, List<String> words) {
        if (ObjectUtil.isBlank(string)) return;
        string = string.trim();

        int firstDelimiterIndex = string.indexOf(delimiter);
        int firstQuoteIndex = string.indexOf("\"");

        // not more delimiters
        if (firstDelimiterIndex == -1) {
            int size = string.length();

            // look like this:    "----"
            if (string.startsWith("\"") && string.endsWith("\"") && size >= 2) {
                String word = string.substring(1, size - 1);
                // match \"
                word = word.replaceAll("[\\\\]\"", "\"");
                words.add(word);
            } else {
                words.add(string);
            }
            return;
        }

        // like:   65536, "-, -, -"
        // or:     #!"-, "--, \"--\", --", 65536
        if (firstDelimiterIndex < firstQuoteIndex || !string.startsWith("\"")) {
            String firstWord = string.substring(0, firstDelimiterIndex);
            words.add(firstWord);
            if (firstDelimiterIndex < string.length() - 1) {
                String restString = string.substring(firstDelimiterIndex + 1);
                retrieveWord(restString, words);
            }
            return;
        }

        // look like this:   "--, \"--\", --", 65536
        StringBuilder word = new StringBuilder();
        int i;
        boolean matched = false;
        for (i = 1; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c == '\\') {
                char nextChar = string.charAt(i + 1);
                if (nextChar == '"') {
                    word.append('"');
                    i++;
                    continue;
                }
                word.append(c);
                continue;
            }
            if (c == '"') {
                matched = true;
                break;
            }
            word.append(c);
        }

        if (matched) {
            words.add(word.toString());
            if (i < string.length() - 1) {
                String restString = string.substring(i + 1);
                restString = restString.trim();
                if (restString.startsWith(",")) {
                    restString = restString.substring(1);
                } else {
                    // invaild string, ignore it
                    return;
                }
                retrieveWord(restString, words);
                return;
            }
        }

        words.add(string);
    }


}
