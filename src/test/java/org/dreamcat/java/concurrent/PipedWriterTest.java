package org.dreamcat.java.concurrent;

import org.junit.Test;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

/**
 * Create by tuke on 2020/4/22
 */
public class PipedWriterTest {

    @Test
    public void testSend() throws IOException {
        PipedWriter writer = new PipedWriter();
        PipedReader reader = new PipedReader();
        writer.connect(reader);

        new Thread(() -> {
            for (int i = 1; i <= 99; i += 2) {
                try {
                    System.out.println(i);
                    writer.write(i + 1);
                } catch (IOException e) {
                    break;
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 2; i <= 100; i += 2) {
                try {
                    int n = reader.read();
                    System.out.println(n);
                } catch (IOException e) {
                    break;
                }
            }
        }).start();
    }
}
