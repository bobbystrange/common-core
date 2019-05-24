package org.dreamcat.common.util;

import org.dreamcat.common.annotation.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Create by tuke on 2019-02-13
 */
@Slf4j
public class ShellUtil {

    public static List<String> exec(@NotNull String... cmds)
            throws IOException, InterruptedException, RuntimeException {
        return exec(0, TimeUnit.SECONDS, cmds);
    }

    /**
     * log stdout and stderr
     * @param timeout timeout to wait the process
     * @param unit time unit
     * @param cmds some shell
     * @return stdout and stderr
     * @throws IOException IO error
     * @throws InterruptedException interrupted by other thread
     * @throws RuntimeException exit value is not equal 0
     */
    public static List<String> exec(
            long timeout,
            TimeUnit unit,
            @NotNull String... cmds)
            throws IOException, InterruptedException, RuntimeException {
        Process process = Runtime.getRuntime().exec(cmds);
        try {
            if (timeout <= 0){
                process.waitFor();
            } else {
                process.waitFor(timeout, unit);
            }

            List<String> output = new ArrayList<>();
            String line;
            try (BufferedReader stdOutput = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                while ((line = stdOutput.readLine()) != null) {
                    output.add(line);
                    log.info(line);
                }
            }
            try (BufferedReader errorOutput = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                while ((line = errorOutput.readLine()) != null) {
                    output.add(line);
                    log.error(line);
                }
            }

            if (process.exitValue() != 0) {
                throw new RuntimeException(String.format(
                        "failed to exec cmd (%s)", Arrays.toString(cmds)));
            }
            return output;
        } finally {
            process.destroy();
        }
    }

    public static int execQuiet(@NotNull String... cmds) throws IOException, InterruptedException {
        return execQuiet(0, TimeUnit.SECONDS, cmds);
    }

    /**
     * no output
     *
     * @param timeout timeout to wait the process
     * @param unit    time unit
     * @param cmds    some shell like: ['bash', '-c', 'top -l 1 | grep -E "^CPU|^Phys"]
     * @return exit code
     * @throws IOException          IO error
     * @throws InterruptedException interrupted by other thread
     */
    public static int execQuiet(
            long timeout,
            TimeUnit unit,
            @NotNull String... cmds) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(cmds);
        try {
            if (timeout <= 0) {
                process.waitFor();
            } else {
                process.waitFor(timeout, unit);
            }

            return process.exitValue();
        } finally {
            process.destroy();
        }
    }
}
