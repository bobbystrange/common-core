package org.dreamcat.common.io;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * Create by tuke on 2021/4/11
 */
@Slf4j
public final class TextFileUtil {

    private TextFileUtil(){
    }

    public static Set<String> readLines(String file, String excludeFile) {
        return readLines(new File(file), new File(excludeFile));
    }

    public static Set<String> readLines(File file, File excludeFile) {
        Set<String> lines;
        Set<String> finished = new HashSet<>();
        try {
            lines = new HashSet<>(FileUtil.readAsList(file));
        } catch (IOException e) {
            log.error("failed to read {}: {}", file.getAbsolutePath(), e.getMessage());
            return null;
        }
        try {
            finished.addAll(FileUtil.readAsList(excludeFile));
        } catch (IOException ignored) {
            // nop
        }
        if (!finished.isEmpty()) {
            lines.removeIf(finished::contains);
        }

        return lines;
    }
}
