package com.tukeof.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLConnection;
import java.nio.charset.Charset;

public final class FileUtil {

    public static String getFileSuffix(String filename) {
        int dotPosition = filename.lastIndexOf(".");
        if (dotPosition == -1 || dotPosition == filename.length() - 1) return "";
        return filename.substring(dotPosition + 1);
    }

    public static String getFilePrefix(String filename) {
        int slashPosition = filename.lastIndexOf("/");
        // if filename is has diretory type
        if (slashPosition != -1 && slashPosition == filename.length() - 1) {
            return "";
        }

        if (slashPosition != -1) {
            // get basename
            filename = filename.substring(slashPosition + 1);
        }

        int dotPosition = filename.lastIndexOf(".");
        if (dotPosition == -1) {
            return filename;
        }
        // only has .
        if (dotPosition == filename.length() - 1 && filename.length() == 1) {
            return "";
        }
        return filename.substring(0, dotPosition);
    }

    /**
     * no verification, almost like dirname in unix
     *
     * @param filename just string
     * @return dirname string
     * @throws IllegalArgumentException invalid filename
     */
    public static String getDirname(String filename) {
        int slashPosition = filename.lastIndexOf("/");
        if (slashPosition == -1) {
            return ".";
        }

        // if filename ends with /
        if (slashPosition == filename.length() - 1) {
            // remove / in the end of string
            filename = filename.substring(0, slashPosition);
            slashPosition = filename.lastIndexOf("/");
            if (slashPosition == -1) {
                return ".";
            }
            if (slashPosition == filename.length() - 1)
                throw new IllegalArgumentException(String.format("invalid filename '%s'", filename));
        }

        return filename.substring(0, slashPosition);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    // recurse to mkdir parent diretory
    public static void recurseMkDir(String file) {
        recurseMkDir(new File(file));
    }

    public static void recurseMkDir(File file) {
        File parent = file.getParentFile();
        if (!parent.getParentFile().exists()) {
            recurseMkDir(parent);
        }

        parent.mkdir();
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static String mimeName(String filename) {
        return URLConnection.getFileNameMap().getContentTypeFor(filename);
    }

    public static byte[] bytes(String filename) throws IOException {
        return bytes(new File(filename));
    }

    public static byte[] bytes(String filename, int len) throws IOException {
        return bytes(new File(filename), len);
    }

    public static byte[] bytes(File file) throws IOException {
        return bytes(file, (int) file.length());
    }

    public static byte[] bytes(File file, int len) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bytes = new byte[len];
            IOUtil.readFully(fis, bytes);
            return bytes;
        }
    }

    public static char[] chars(String filename) throws IOException {
        return chars(new File(filename));
    }

    public static char[] chars(String filename, int len) throws IOException {
        return chars(new File(filename), len);
    }

    public static char[] chars(File file) throws IOException {
        return chars(file, (int) file.length());
    }

    public static char[] chars(File file, int len) throws IOException {
        try (FileReader f = new FileReader(file)) {
            char[] chars = new char[len];
            IOUtil.readFully(f, chars);
            return chars;
        }
    }

    public static String string(String filename) throws IOException {
        return string(new File(filename));
    }

    public static String string(File file) throws IOException {
        return new String(chars(file));
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static void convert(File srcFile, Charset srcCS, File destFile, Charset destCS)
            throws IOException {
        if (srcFile.isFile()) {
            convertFile(srcFile, srcFile, srcCS, destFile, destCS);
        } else {
            File[] files = srcFile.listFiles();
            if (files == null || files.length < 1) return;

            for (File file : files) {
                convertFile(file, srcFile, srcCS, destFile, destCS);
            }
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private static void convertFile(File file, File srcFile, Charset srcCS, File destFile, Charset destCS) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), srcCS))) {
            String path = destFile.getPath()
                    + file.getAbsolutePath().substring(srcFile.getPath().length());
            File outfile = new File(path);
            recurseMkDir(outfile);
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(outfile), destCS))) {
                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    writer.write(line);
                    writer.write(File.separator);
                }
                writer.flush();
            }
        }
    }


}
