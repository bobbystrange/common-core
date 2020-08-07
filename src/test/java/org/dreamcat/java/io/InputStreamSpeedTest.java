package org.dreamcat.java.io;

import org.dreamcat.common.core.Timeit;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Create by tuke on 2020/7/30
 */
public class InputStreamSpeedTest {

    @Test
    public void test() {
        File file = new File(System.getenv("HOME"), ".zsh_history");
        for (int i = 1; i <= 16; i *= 2) {
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
                        try (FileReader reader = new FileReader(file)) {
                            char[] buf_4k = new char[4 * 1024];
                            while ((reader.read(buf_4k)) != -1) ;
                        }
                    })
                    .addAction(() -> {
                        try (FileReader reader = new FileReader(file)) {
                            char[] buf_256k = new char[256 * 1024];
                            while ((reader.read(buf_256k)) != -1) ;
                        }
                    })
                    .repeat(i).runAndFormatMs();
            System.out.printf("[%03d] \t %s\n", i, s);
        }

    }
}
