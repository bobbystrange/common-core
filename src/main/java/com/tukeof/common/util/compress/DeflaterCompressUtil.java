package com.tukeof.common.util.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterInputStream;
import java.util.zip.DeflaterOutputStream;

public class DeflaterCompressUtil {

    // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    public static int buffer_size = 1024;

    // compress
    public static byte[] compress(byte[] data, int level) throws IOException {
        try (ByteArrayInputStream ins = new ByteArrayInputStream(data)) {
            try (ByteArrayOutputStream outs = new ByteArrayOutputStream()) {
                compress(ins, outs, level);
                data = outs.toByteArray();
                return data;
            }
        }
    }

    public static byte[] compress(byte[] data) throws IOException {
        return compress(data, -1);
    }

    public static void compress(InputStream ins, OutputStream outs, int level) throws IOException {

        try (DeflaterOutputStream o = new DeflaterOutputStream(outs, new Deflater(level))) {
            int count;
            byte[] data = new byte[buffer_size];
            while ((count = ins.read(data)) != -1) {
                o.write(data, 0, count);
            }
        }
    }

    public static void compress(InputStream ins, OutputStream outs) throws IOException {
        compress(ins, outs, -1);
    }

    public static void compress(File srcFile, File destFile, int level) throws IOException {
        try (FileInputStream fis = new FileInputStream(srcFile)) {
            try (FileOutputStream fos = new FileOutputStream(destFile)) {
                compress(fis, fos, level);
            }
        }
    }

    public static void compress(File srcFile) throws IOException {
        compress(srcFile, new File(srcFile + ".deflater"), -1);
    }

    // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    // decompress
    public static byte[] decompress(byte[] data) throws Exception {
        try (ByteArrayInputStream ins = new ByteArrayInputStream(data)) {
            try (ByteArrayOutputStream outs = new ByteArrayOutputStream()) {
                decompress(ins, outs);
                data = outs.toByteArray();
                return data;
            }
        }
    }

    public static void decompress(InputStream ins, OutputStream outs) throws IOException {
        try (DeflaterInputStream i = new DeflaterInputStream(ins)) {
            int count;
            byte[] data = new byte[buffer_size];
            while ((count = i.read(data)) != -1) {
                outs.write(data, 0, count);
            }
        }
    }

    public static void decompress(File srcFile, File destFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(srcFile)) {
            try (FileOutputStream fos = new FileOutputStream(destFile)) {
                decompress(fis, fos);
            }
        }
    }

    public static void decompress(File srcFile) throws IOException {
        String destPath = srcFile.getAbsolutePath();
        destPath = destPath.substring(0, destPath.length() - 9);
        decompress(srcFile, new File(destPath));
    }
}
