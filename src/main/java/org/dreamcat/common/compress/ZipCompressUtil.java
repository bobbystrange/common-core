package org.dreamcat.common.compress;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.ObjectUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Slf4j
public class ZipCompressUtil {
    private static final int BUFFER_SIZE = 4096;

    // level compression_level = [0-9]
    public static void zip(File srcFile, File destFile, int level) throws IOException {
        CheckedOutputStream cos = new CheckedOutputStream(
                new FileOutputStream(destFile), new CRC32());
        try(ZipOutputStream zos = new ZipOutputStream(cos)) {
            zos.setLevel(level);
            zip(srcFile, zos, "");
        }
    }

    private static void zip(File srcFile, ZipOutputStream outs, String basePath)
            throws IOException {
        if (!srcFile.exists()) return;

        if (srcFile.isDirectory()) {
            zipDir(srcFile, outs, basePath);
        } else {
            zipFile(srcFile, outs, basePath);
        }
    }

    private static void zipDir(File dir, ZipOutputStream outs, String basePath) throws IOException {
        File[] files = dir.listFiles();

        if (ObjectUtil.isEmpty(files)) {
            ZipEntry entry = new ZipEntry(basePath + dir.getName() + "/");
            outs.putNextEntry(entry);
            outs.closeEntry();
            return;
        }

        for (File file : files) {
            zip(file, outs, basePath + dir.getName() + "/");
        }
    }

    private static void zipFile(File file, ZipOutputStream outs, String basePath) throws IOException {
        ZipEntry entry = new ZipEntry(basePath + file.getName());
        entry.setSize(file.length());
        outs.putNextEntry(entry);

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            int count;
            byte[] data = new byte[BUFFER_SIZE];
            while ((count = bis.read(data)) > 0) {
                outs.write(data, 0, count);
            }
        }
        outs.closeEntry();
    }

    // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-

    public static void unzip(File srcFile, File destFile) throws IOException {
        try (ZipInputStream ins = new ZipInputStream(
                new FileInputStream(srcFile))) {
            unzip(destFile, ins);
        }
    }

    private static void unzip(File destFile, ZipInputStream ins) throws IOException {
        ZipEntry entry;
        while ((entry = ins.getNextEntry()) != null) {
            File file = new File(destFile.getPath() + File.separator + entry.getName());
            if (entry.isDirectory()) {
                if (!file.mkdirs() && !file.exists()) {
                    if (log.isDebugEnabled()) {
                        log.debug("Failed to mkdirs {}", file);
                    }
                }
            } else {
                unzipFile(file, ins);
            }
        }
    }

    private static void unzipFile(File destFile, ZipInputStream ins) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(destFile))) {
            int count;
            byte[] data = new byte[BUFFER_SIZE];
            while ((count = ins.read(data)) > 0) {
                bos.write(data, 0, count);
            }
        }
    }
}
