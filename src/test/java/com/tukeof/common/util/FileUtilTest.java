package com.tukeof.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Create by tuke on 2019-04-17
 */
@Slf4j
public class FileUtilTest {

    @Test
    public void dirname() {
        String[] paths = new String[]{
                "/a/b/c/d.e.f",
                "/a/b/c/d.e.",
                "/a/b/c/d.e",
                "/a/b/c/d.",
                "/a/b/c/d",
                "/a/b/c/",

                "/a/../c/d.e.f",
                "/a../c/d.e.",
                "/a../c/d.e",
                "/a../c/d.",
                "/a../c/d",
                "/a../c/",

                "/a/b/../d.e.f",
                "/a/b/../d.e.",
                "/a/b/../d.e",
                "/a/b/../d.",
                "/a/b/../d",
                "/a/b/../",

                "/a/b/./d.e.f",
                "/a/b/./d.e.",
                "/a/b/./d.e",
                "/a/b/./d.",
                "/a/b/./d",
                "/a/b/./",

                "/a/.././d.e.f",
                "/a/.././d.e.",
                "/a/.././d.e",
                "/a/.././d.",
                "/a/.././d",
                "/a/.././",

                "./a/b/c/d.e.f",
                "./a/b/c/d.e.",
                "./a/b/c/d.e",
                "./a/b/c/d.",
                "./a/b/c/d",
                "./a/b/c/",

                "a/b/c/d.e.f",
                "a/b/c/d.e.",
                "a/b/c/d.e",
                "a/b/c/d.",
                "a/b/c/d",
                "a/b/c/",

                "a/d.e.f",
                "a/d.e.",
                "a/d.e",
                "a/d.",
                "a/d",
                "a/c/",

                "/d.e.f",
                "/d.e.",
                "/d.e",
                "/d.",
                "/d",
                "/c/",

                "d.e.f",
                "d.e.",
                "d.e",
                "d.",
                "d",
                "c/",

                ".",
                "./",
                "../",
                "../.",
                "./..",

                "/"
        };

        for (String path : paths) {
            String line = String.format("%16s\t%10s\t%10s\t%10s",
                    "'" + path + "'",
                    "'" + FileUtil.getDirname(path) + "'",
                    "'" + FileUtil.getFilePrefix(path) + "'",
                    "'" + FileUtil.getFileSuffix(path) + "'");
            log.info(line);
        }

    }
}
