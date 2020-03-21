package org.dreamcat.common.io;

import org.dreamcat.common.function.TriConsumer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.*;

public final class FileUtil {

    private static final int BUFFER_SIZE = 1024 * 4;

    public static String getSuffix(String filename) {
        int dotPosition = filename.lastIndexOf(".");
        if (dotPosition == -1 || dotPosition == filename.length() - 1) return "";
        return filename.substring(dotPosition + 1);
    }

    public static String getBasename(String filename) {
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

    public static String getMimeType(String filename) {
        return URLConnection.getFileNameMap().getContentTypeFor(filename);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static byte[] readAsByteArray(String filename) throws IOException {
        return readAsByteArray(new File(filename));
    }

    public static byte[] readAsByteArray(String filename, int size) throws IOException {
        return readAsByteArray(new File(filename), size);
    }

    public static byte[] readAsByteArray(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return IOUtil.readFully(fis);
        }
    }

    public static byte[] readAsByteArray(File file, int size) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bytes = new byte[size];
            IOUtil.readFully(fis, bytes);
            return bytes;
        }
    }

    public static char[] readAsCharArray(String filename) throws IOException {
        return readAsCharArray(new File(filename));
    }

    public static char[] readAsCharArray(String filename, int size) throws IOException {
        return readAsCharArray(new File(filename), size);
    }

    public static char[] readAsCharArray(File file) throws IOException {
        try (FileReader f = new FileReader(file)) {
            return IOUtil.readFully(f);
        }
    }

    public static char[] readAsCharArray(File file, int size) throws IOException {
        try (FileReader f = new FileReader(file)) {
            char[] chars = new char[size];
            IOUtil.readFully(f, chars);
            return chars;
        }
    }

    public static String readAsString(String filename) throws IOException {
        return readAsString(new File(filename));
    }

    public static String readAsString(File file) throws IOException {
        return new String(readAsCharArray(file));
    }

    public static List<String> readAsList(String filename) throws IOException {
        return readAsList(new File(filename));
    }

    public static List<String> readAsList(File file) throws IOException {
        try (BufferedReader f = new BufferedReader(new FileReader(file))) {
            List<String> list = new ArrayList<>();
            String line;
            while ((line = f.readLine()) != null) {
                list.add(line);
            }
            return list;
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static void writeFrom(String filename, String text) throws IOException {
        writeFrom(filename, text, false);
    }

    public static void writeFrom(String filename, String text, boolean append) throws IOException {
        writeFrom(new File(filename), text, false);
    }

    public static void writeFrom(File file, String text) throws IOException {
        writeFrom(file, text, false);
    }

    public static void writeFrom(File file, String text, boolean append) throws IOException {
        try (FileWriter fw = new FileWriter(file, append)) {
            fw.write(text);
        }
    }

    public static void writeFrom(String filename, byte[] bytes) throws IOException {
        writeFrom(filename, bytes, false);
    }

    public static void writeFrom(String filename, byte[] bytes, boolean append) throws IOException {
        writeFrom(new File(filename), bytes, false);
    }

    public static void writeFrom(File file, byte[] bytes) throws IOException {
        writeFrom(file, bytes, false);
    }

    public static void writeFrom(File file, byte[] bytes, boolean append) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file, append)) {
            fos.write(bytes);
        }
    }

    public static void writeFrom(String filename, Reader reader) throws IOException {
        writeFrom(filename, reader, false);
    }

    public static void writeFrom(String filename, Reader reader, boolean append) throws IOException {
        writeFrom(new File(filename), reader, false);
    }

    public static void writeFrom(File file, Reader reader) throws IOException {
        writeFrom(file, reader, false);
    }

    public static void writeFrom(File file, Reader reader, boolean append) throws IOException {
        try (FileWriter fw = new FileWriter(file, append)) {
            char[] buf = new char[BUFFER_SIZE];
            int readSize;
            while ((readSize = reader.read(buf)) > 0) {
                fw.write(buf, 0, readSize);
            }
        }
    }

    public static void writeFrom(String filename, InputStream inputStream) throws IOException {
        writeFrom(filename, inputStream, false);
    }

    public static void writeFrom(String filename, InputStream inputStream, boolean append) throws IOException {
        writeFrom(new File(filename), inputStream, false);
    }

    public static void writeFrom(File file, InputStream inputStream) throws IOException {
        writeFrom(file, inputStream, false);
    }

    public static void writeFrom(File file, InputStream inputStream, boolean append) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file, append)) {
            byte[] buf = new byte[BUFFER_SIZE];
            int readSize;
            while ((readSize = inputStream.read(buf)) > 0) {
                fos.write(buf, 0, readSize);
            }
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    // recurse to mkdir parent diretory
    public static void mkdir(String file) {
        mkdir(new File(file));
    }

    public static void mkdir(File file) {
        File parent = file.getParentFile();
        if (!parent.getParentFile().exists()) {
            mkdir(parent);
        }

        parent.mkdir();
    }

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

    private static void convertFile(File file, File srcFile, Charset srcCS, File destFile, Charset destCS) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), srcCS))) {
            String path = destFile.getPath()
                    + file.getAbsolutePath().substring(srcFile.getPath().length());
            File outfile = new File(path);
            mkdir(outfile);
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

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    @SuppressWarnings("unchecked")
    public void watchPath(Path path, long interval, TimeUnit intervalUnit,
                          TriConsumer<WatchEvent.Kind<Path>, Integer, Path> consumer)
            throws Exception {

        WatchService watcher = FileSystems.getDefault().newWatchService();
        path.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

        interval = intervalUnit.toMillis(interval);
        boolean loop = true;
        while (loop) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                loop = false;
            }

            WatchKey key = watcher.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                // events may have been lost or discarded.
                if (kind == OVERFLOW) continue;

                WatchEvent<Path> e = (WatchEvent<Path>) event;
                consumer.accept(e.kind(), e.count(), e.context());
            }
            if (!key.reset()) {
                break;
            }
        }
    }


}
