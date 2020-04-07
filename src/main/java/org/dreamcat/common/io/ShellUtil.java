package org.dreamcat.common.io;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.ObjectUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Create by tuke on 2019-02-13
 */
@Slf4j
public class ShellUtil {

    public static List<String> exec(String... cmd)
            throws IOException, InterruptedException, RuntimeException {
        return exec(false, cmd);
    }

    public static List<String> exec(boolean verbose, String... cmd)
            throws IOException, InterruptedException, RuntimeException {
        return exec(null, cmd);
    }

    public static List<String> exec(
            Duration timeout,
            String... cmd)
            throws IOException, InterruptedException, RuntimeException {
        return exec(timeout, false, cmd);
    }

    /**
     * execute shell and return output
     *
     * @param timeout timeout to wait the process
     * @param verbose log to stdout & stderr
     * @param cmd     sh -c 'cmd' when cmd.length == 1,
     *                or cmd array when cmd is like
     *                {@code new String[]{"/bin/sh", "-c", "/usr/bin/env ls -lah"}}
     * @return stdout and stderr
     * @throws IOException          IO error
     * @throws InterruptedException interrupted by other thread
     * @throws RuntimeException     exit value is not equal 0
     */
    public static List<String> exec(
            Duration timeout,
            boolean verbose,
            String... cmd)
            throws IOException, InterruptedException, RuntimeException {
        Process process = process(cmd);
        try {
            if (timeout == null) {
                process.waitFor();
            } else {
                process.waitFor(timeout.getSeconds(), TimeUnit.SECONDS);
            }

            List<String> output = new ArrayList<>();
            String line;
            try (BufferedReader stdOutput = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                while ((line = stdOutput.readLine()) != null) {
                    output.add(line);
                    if (verbose) log.info(line);
                }
            }

            try (BufferedReader errorOutput = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                while ((line = errorOutput.readLine()) != null) {
                    output.add(line);
                    if (verbose) log.error(line);
                }
            }

            if (process.exitValue() != 0) {
                throw new RuntimeException(String.format(
                        "failed to exec cmd %s", Arrays.toString(cmd)));
            }
            return output;
        } finally {
            process.destroy();
        }
    }

    public static int execQuiet(String... cmd) throws IOException, InterruptedException {
        return execQuiet(0, TimeUnit.SECONDS, cmd);
    }

    /**
     * no output
     *
     * @param timeout timeout to wait the process
     * @param unit    time unit
     * @param cmd     some shell like: ['bash', '-c', 'top -l 1 | grep -E "^CPU|^Phys"]
     * @return exit code
     * @throws IOException          IO error
     * @throws InterruptedException interrupted by other thread
     */
    public static int execQuiet(
            long timeout,
            TimeUnit unit,
            String... cmd) throws IOException, InterruptedException {
        Process process = process(cmd);
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

    private static Process process(String... cmd) throws IOException {
        ObjectUtil.requireNotEmpty(cmd, "cmd");

        if (cmd.length == 1) {
            return Runtime.getRuntime().exec(new String[]{
                    "/bin/sh", "-c", cmd[0],
            });
        } else {
            return Runtime.getRuntime().exec(cmd);
        }
    }
}
