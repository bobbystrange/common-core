package org.dreamcat.common.compress;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class InflaterCompressUtil {
    private static final int BUFFER_SIZE = 4096;

    // uncompress
    public static byte[] uncompress(byte[] data) throws DataFormatException, IOException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length)) {
            byte[] buf = new byte[BUFFER_SIZE];
            while (!inflater.finished()) {
                int i = inflater.inflate(buf);
                bos.write(buf, 0, i);
            }
            return bos.toByteArray();
        } finally {
            inflater.end();
        }

        //try (ByteArrayInputStream ins = new ByteArrayInputStream(data)) {
        //    try (ByteArrayOutputStream outs = new ByteArrayOutputStream()) {
        //        uncompress(ins, outs);
        //        data = outs.toByteArray();
        //        return data;
        //    }
        //}
    }

    public static void uncompress(InputStream ins, OutputStream outs) throws IOException {
        try (InflaterInputStream i = new InflaterInputStream(ins)) {
            int count;
            byte[] data = new byte[BUFFER_SIZE];
            while ((count = i.read(data)) > 0) {
                outs.write(data, 0, count);
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
