package org.dreamcat.common.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipCompressUtil {

    private static final int BUFFER_SIZE = 4096;

    // compress
    public static void compress(InputStream ins, OutputStream outs) throws IOException {
        try (OutputStream o = new GZIPOutputStream(outs)) {
            int count;
            byte[] data = new byte[BUFFER_SIZE];
            while ((count = ins.read(data)) > 0) {
                o.write(data, 0, count);
            }
        }
    }

    public static byte[] compress(byte[] data) throws IOException {
        try (ByteArrayInputStream ins = new ByteArrayInputStream(data)) {
            try (ByteArrayOutputStream outs = new ByteArrayOutputStream()) {
                compress(ins, outs);
                data = outs.toByteArray();
                return data;
            }
        }
    }

    public static void compress(File srcFile, File destFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(srcFile)) {
            try (FileOutputStream fos = new FileOutputStream(destFile)) {
                compress(fis, fos);
            }
        }
    }

    public static void compress(File srcFile) throws IOException {
        compress(srcFile, new File(srcFile + ".gz"));
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    // decompress
    public static void uncompress(InputStream ins, OutputStream outs) throws IOException {
        try (InputStream i = new GZIPInputStream(ins)) {
            int count;
            byte[] data = new byte[BUFFER_SIZE];
            while ((count = i.read(data)) > 0) {
                outs.write(data, 0, count);
            }
        }
    }

    public static byte[] uncompress(byte[] data) throws IOException {
        try (ByteArrayInputStream ins = new ByteArrayInputStream(data)) {
            try (ByteArrayOutputStream outs = new ByteArrayOutputStream()) {
                uncompress(ins, outs);
                data = outs.toByteArray();
                return data;
            }
        }
    }

    public static void uncompress(File srcFile, File destFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(srcFile)) {
            try (FileOutputStream fos = new FileOutputStream(destFile)) {
                uncompress(fis, fos);
            }
        }
    }

    public static void uncompress(File srcFile) throws IOException {
        String destPath = srcFile.getAbsolutePath();
        destPath = destPath.substring(0, destPath.length() - 3);
        uncompress(srcFile, new File(destPath));
    }
}
