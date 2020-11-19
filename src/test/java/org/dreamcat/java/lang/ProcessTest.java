package org.dreamcat.java.lang;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Create by tuke on 2019-04-18
 */
@Slf4j
public class ProcessTest {

    @Test
    public void proc() throws IOException, InterruptedException {
        int i = 0;
        while (i++ < 1000) {
            Process process = Runtime.getRuntime().exec("ls -alh");
            log.info("exit {}", process.waitFor());
            //Thread.sleep(10_000);
        }
    }
}
