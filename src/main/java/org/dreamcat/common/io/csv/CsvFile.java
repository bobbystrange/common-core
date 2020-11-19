package org.dreamcat.common.io.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.dreamcat.common.util.ObjectUtil;

/**
 * Create by tuke on 2018-09-22
 *
 * @see CsvReader
 */
public class CsvFile implements AutoCloseable {

    private final BufferedReader reader;
    @Getter
    @Setter
    private char delimiter = ',';

    public CsvFile(String content) {
        this.reader = new BufferedReader(new StringReader(content));
    }

    public CsvFile(File file) throws FileNotFoundException {
        this.reader = new BufferedReader(new FileReader(file));
    }

    public CsvFile(Reader reader) {
        this.reader = new BufferedReader(reader);
    }

    public CsvFile(InputStream inputStream) {
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public CsvFile(InputStream inputStream, Charset charset) {
        this.reader = new BufferedReader(new InputStreamReader(inputStream, charset));
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    // note that word including \r\n is unsupported
    public String[] readRecord() throws IOException {
        while (true) {
            String line = reader.readLine();
            if (line == null) return null;

            if (ObjectUtil.isBlank(line)) continue;
            if (line.startsWith("#")) continue;

            List<String> words = new ArrayList<>();
            retrieveWord(line, words);
            return words.toArray(new String[0]);
        }
    }

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
                word = word.replaceAll("\"\"", "\"");
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
                    // invalid string, ignore it
                    return;
                }
                retrieveWord(restString, words);
                return;
            }
        }

        words.add(string);
    }


}
