package org.dreamcat.java.io;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Create by tuke on 2020/8/7
 */
public class MarkedReaderTest {

    @Test
    public void testMarkReset() throws IOException {
        try (StringReader reader = new StringReader("abc123")) {
            System.out.println((char) reader.read()); // a
            reader.mark(2);
            System.out.println((char) reader.read()); // b
            System.out.println((char) reader.read()); // c
            reader.reset();
            System.out.println((char) reader.read()); // b
        }

        System.out.println();
        try (StringReader reader = new StringReader("abc123")) {
            System.out.println((char) reader.read()); // a
            reader.mark(2);
            System.out.println((char) reader.read()); // b
            System.out.println((char) reader.read()); // c
            System.out.println((char) reader.read()); // 1
            System.out.println((char) reader.read()); // 2
            reader.reset();
            System.out.println((char) reader.read()); // b
        }
    }

    @Test
    public void testMarkResetFile() throws IOException {
        Path tmpFile = null;
        try {
            tmpFile = Files.createTempFile(getClass().getPackage().getName(), ".txt");
            System.out.println(tmpFile);

            Files.write(tmpFile, "abc123".getBytes());
            try (Reader reader = new BufferedReader(new FileReader(tmpFile.toFile()))) {
                System.out.println((char) reader.read()); // a
                reader.mark(2);
                System.out.println((char) reader.read()); // b
                System.out.println((char) reader.read()); // c
                reader.reset();
                System.out.println((char) reader.read()); // b
            }

            System.out.println();
            try (Reader reader = new BufferedReader(new FileReader(tmpFile.toFile()))) {
                System.out.println((char) reader.read()); // a
                reader.mark(2);
                System.out.println((char) reader.read()); // b
                System.out.println((char) reader.read()); // c
                System.out.println((char) reader.read()); // 1
                System.out.println((char) reader.read()); // 2
                reader.reset();
                System.out.println((char) reader.read()); // b
            }
        } finally {
            if (tmpFile != null) {
                Files.deleteIfExists(tmpFile);
            }
        }
    }
}
