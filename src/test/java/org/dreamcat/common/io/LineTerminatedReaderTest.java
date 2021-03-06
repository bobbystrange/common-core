package org.dreamcat.common.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.stream.Collectors;
import org.dreamcat.common.Pair;
import org.dreamcat.common.Timeit;
import org.dreamcat.common.function.ThrowableConsumer;
import org.dreamcat.common.io.csv.CsvReaderTest;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Create by tuke on 2020/8/6
 */
@Ignore
public class LineTerminatedReaderTest {

    @Test
    public void testSpeed() {
        File file = new File(System.getenv("HOME"), ".zsh_history");
        System.out.println("repeat read \t buffered \t line \t buffered-readLine \t line-readLine");
        for (int i = 1; i <= 256; i *= 2) {
            String s = Timeit.ofActions()
                    .addAction(() -> {
                        try (FileReader reader = new FileReader(file)) {
                            while ((reader.read()) != -1) ;
                        }
                    })
                    .addAction(() -> {
                        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                            while ((reader.read()) != -1) ;
                        }
                    })
                    .addAction(() -> {
                        try (LineTerminatedReader reader = new LineTerminatedReader(
                                new FileReader(file))) {
                            while ((reader.read()) != -1) ;
                        }
                    })
                    .addAction(() -> {
                        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                            while ((reader.readLine()) != null) ;
                        }
                    })
                    .addAction(() -> {
                        try (LineTerminatedReader reader = new LineTerminatedReader(
                                new FileReader(file))) {
                            while ((reader.readLine()) != null) ;
                        }
                    })
                    .parallel().count(6).skip(2).repeat(i).runAndFormatMs();
            System.out.printf("[%03d] \t %s\n", i, s);
        }

    }

    @Test
    public void testRead() throws Exception {
        testAction(reader -> {
            for (int i = 0; i < 64; i++) {
                System.out.print(formatCRLF(String.valueOf((char) reader.read())));
            }
        });
    }

    @Test
    public void testReadBuf() throws Exception {
        testAction(reader -> {
            char[] b = new char[15];
            System.out.printf("%d \t %s\n", reader.read(b), formatCRLF(new String(b)));
            System.out.printf("%d \t %s\n", reader.read(b), formatCRLF(new String(b)));
            System.out.printf("%d \t %s\n", reader.read(b), formatCRLF(new String(b)));
            System.out.printf("%d \t %s\n", reader.read(b), formatCRLF(new String(b)));
            System.out.printf("%d \t %s\n", reader.read(b), formatCRLF(new String(b)));
        });
    }

    @Test
    public void testReadLine() throws Exception {
        testAction(reader -> {
            Pair<String, Integer> pair;
            while ((pair = reader.readLine()) != null) {
                System.out.printf("%2s \t <%s>\n", formatCRLF(pair.second()), pair.first());
            }
        });
    }

    private void testAction(ThrowableConsumer<LineTerminatedReader> action) throws Exception {
        String input = CsvReaderTest.csvString();
        System.out.println(formatCRLF(input));
        System.out.println();

        try (LineTerminatedReader reader = new LineTerminatedReader(new StringReader(input))) {
            action.accept(reader);
        }
    }

    public static String formatCRLF(String s) {
        return s.chars().mapToObj(c -> {
            if (c == '\r') return "\\r";
            if (c == '\n') return "\\n";
            return (char) c + "";
        }).collect(Collectors.joining());
    }

    public static String formatCRLF(int lineTerminated) {
        if (lineTerminated == '\r') return "\\r";
        if (lineTerminated == '\n') return "\\n";
        else return "\\r\\n";
    }
}
