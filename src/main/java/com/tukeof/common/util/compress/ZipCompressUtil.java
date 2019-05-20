package com.tukeof.common.util.compress;

import com.tukeof.common.util.ObjectUtil;

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

import static com.tukeof.common.util.FileUtil.recurseMkDir;

public class ZipCompressUtil {

    public static int buffer_size = 1024;

    // level compression_level = [0-9]
    public static void zip(File srcFile, File destFile, int level) throws IOException {
        try (ZipOutputStream outs = new ZipOutputStream(
                new FileOutputStream(destFile))) {
            outs.setLevel(level);
            zip(srcFile, outs, "");
            outs.flush();
        }

        CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(
                destFile), new CRC32());

        ZipOutputStream zos = new ZipOutputStream(cos);
        zip(srcFile, zos, "");
        zos.flush();
        zos.close();
    }

    private static void zip(File srcFile, ZipOutputStream outs, String basePath)
            throws IOException {
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
        }

        for (File file : files) {
            zip(file, outs, basePath + dir.getName() + "/");
        }
    }

    // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-

    private static void zipFile(File file, ZipOutputStream outs, String basePath)
            throws IOException {

        ZipEntry entry = new ZipEntry(basePath + file.getName());
        entry.setSize(file.length());
        outs.putNextEntry(entry);

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            int count;
            byte[] data = new byte[buffer_size];
            while ((count = bis.read(data)) != -1) {
                outs.write(data, 0, count);
            }
        }

        outs.closeEntry();
    }

    public static void unzip(File srcFile, File destFile) throws IOException {
        try (ZipInputStream ins = new ZipInputStream(
                new FileInputStream(srcFile))) {
            unzip(destFile, ins);
        }
    }

    private static void unzip(File destFile, ZipInputStream ins) throws IOException {
        ZipEntry entry;
        while ((entry = ins.getNextEntry()) != null) {
            File dirFile = new File(destFile.getPath() + File.separator + entry.getName());
            recurseMkDir(dirFile);

            if (entry.isDirectory()) {
                dirFile.mkdirs();
            } else {
                unzipFile(dirFile, ins);
            }
        }
    }

    private static void unzipFile(File destFile, ZipInputStream ins) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(destFile))) {
            int count;
            byte[] data = new byte[buffer_size];
            while ((count = ins.read(data)) != -1) {
                bos.write(data, 0, count);
            }
        }
    }
}
