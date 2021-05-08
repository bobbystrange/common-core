package org.dreamcat.common.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.ObjectUtil;

/**
 * Create by tuke on 2019-02-13
 */
@Slf4j
public final class ShellUtil {

    private ShellUtil() {
    }

    public static List<String> exec(String... cmd)
            throws IOException, InterruptedException {
        return exec(false, cmd);
    }

    public static List<String> exec(boolean verbose, String... cmd)
            throws IOException, InterruptedException {
        return exec(null, verbose, cmd);
    }

    public static List<String> exec(Duration timeout, String... cmd)
            throws IOException, InterruptedException {
        return exec(timeout, false, cmd);
    }

    /**
     * execute shell and return output
     *
     * @param timeout timeout to wait the process
     * @param verbose print to stdout and stderr
     * @param cmd     sh -c 'cmd' when cmd.length == 1,
     *                or cmd array when cmd is like
     *                {@code new String[]{"/bin/sh", "-c", "/usr/bin/env ls -lah"}}
     * @return stdout and stderr
     * @throws IOException          IO error
     * @throws InterruptedException interrupted by other thread
     * @throws RuntimeException     exit value is not equal 0
     */
    public static List<String> exec(Duration timeout, boolean verbose, String... cmd)
            throws IOException, InterruptedException {
        Process process = process(cmd);
        try {
            if (timeout == null) {
                process.waitFor();
            } else {
                process.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS);
            }

            List<String> output = new ArrayList<>();
            String line;
            try (BufferedReader stdOutput = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                while ((line = stdOutput.readLine()) != null) {
                    output.add(line);
                    if (verbose) log.info(line);
                }
            }

            try (BufferedReader errorOutput = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()))) {
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
        return execQuiet(null, cmd);
    }

    public static int execQuiet(boolean verbose, String... cmd)
            throws IOException, InterruptedException {
        return execQuiet(null, verbose, cmd);
    }

    public static int execQuiet(Duration timeout, String... cmd)
            throws IOException, InterruptedException {
        return execQuiet(timeout, false, cmd);
    }

    /**
     * not return output
     *
     * @param timeout timeout to wait the process
     * @param verbose print to stdout and stderr
     * @param cmd     some shell like: ['bash', '-c', 'top -l 1 | grep -E "^CPU|^Phys"]
     * @return exit code
     * @throws IOException          IO error
     * @throws InterruptedException interrupted by other thread
     */
    public static int execQuiet(Duration timeout, boolean verbose, String... cmd)
            throws IOException, InterruptedException {
        Process process = process(cmd);
        try {
            if (timeout == null) {
                process.waitFor();
            } else {
                process.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS);
            }

            if (verbose) {
                String line;
                try (BufferedReader stdOutput = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                    while ((line = stdOutput.readLine()) != null) {
                        log.info(line);
                    }
                }

                try (BufferedReader errorOutput = new BufferedReader(
                        new InputStreamReader(process.getErrorStream()))) {
                    while ((line = errorOutput.readLine()) != null) {
                        log.error(line);
                    }
                }
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

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static long getFreeSize() {
        try {
            List<String> lines = exec("/usr/bin/env df | head -2  | tail -1 | awk '{print $4}'");
            if (ObjectUtil.isNotEmpty(lines)) {
                return Long.parseLong(lines.get(0));
            }
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt(); // Restore interrupted state...
            }
            log.error(e.getMessage());
        }
        throw new RuntimeException("Failed to get the free size of disk");
    }

    public static long getUsedSize(String path) {
        String cmd = String.format("/usr/bin/env du -s %s | awk '{print $1}'", path);
        try {
            List<String> lines = ShellUtil.exec(cmd);
            if (ObjectUtil.isNotEmpty(lines)) {
                return Long.parseLong(lines.get(0));
            }
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt(); // Restore interrupted state...
            }
            log.error(e.getMessage());
        }
        throw new RuntimeException("Failed to get the used size of path " + path);
    }

    public static long getFileCount(String directory) {
        return getFileCount(new File(directory));
    }

    public static long getFileCount(File directory) {
        if (!directory.exists()) {
            throw new RuntimeException(directory.getAbsolutePath() + " doesn't exist");
        }

        File[] files = directory.listFiles();
        if (ObjectUtil.isEmpty(files)) return 0;

        long count = 0;
        for (File file : files) {
            if (file.isDirectory()) {
                count += getFileCount(file);
            } else {
                count++;
            }
        }
        return count;
    }
}
