package org.dreamcat.common.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Create by tuke on 2019-04-17
 */
@Slf4j
public class FileUtilTest {

    @Test
    public void dirname() {
        FileUtil.dirname("/a");
    }

    @Test
    public void dirname2() {
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
                "/",
        };

        System.out.println("\t\t\tpath \t\t\t\t\tdirname \t\tbasename \t\tsuffix \t\tnormalize");
        for (String path : paths) {
            String line = String.format("%20s\t%20s\t%10s\t%10s\t%20s",
                    "'" + path + "'",
                    "'" + FileUtil.dirname(path) + "'",
                    "'" + FileUtil.basename(path) + "'",
                    "'" + FileUtil.suffix(path) + "'",
                    "'" + FileUtil.normalize(path) + "'");
            System.out.println(line);
        }

    }

    @Test
    public void prefix() {
        String[] paths = new String[]{
                "/a/b/../d.e.f",
                "/a/b/../d.e.",
                "/a/b/../d.e",
                "/a/b/../d.",
                "/a/b/../d",
                "/a/b/../",

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

                ".",
                "./",
                "../",
                "../.",
                "./..",

                "/",
                "///a//c//.e.f////g.h.j",
                "///a//c//.e.f////g.h.j",
                "a/./b/../c/.../d..f//g.h.j",
                "..a/./b/../c/.../d.e.f//g.h.j..",
                "a/./b/../c/.../d.e.f///g.h.j...."
        };

        System.out.println("\t\t\t\t\tnormalize \t\t\t\t\tdirname \t\tprefix \tsuffix");
        for (String path : paths) {
            String line = String.format("%32s\t%20s\t%10s\t\t%4s",
                    "'" + FileUtil.normalize(path) + "'",
                    "'" + FileUtil.dirname(path) + "'",
                    "'" + FileUtil.prefix(path) + "'",
                    "'" + FileUtil.suffix(path) + "'");
            System.out.println(line);
        }

    }

    @Test
    public void testWriteFrom() throws Exception {
        for (int i = 0; i <= 100; i++) {
            FileUtil.writeFrom("/Users/tuke/data/tmp/id.log", i * i + "\n", true);
            Thread.sleep(100);
        }
    }
}
