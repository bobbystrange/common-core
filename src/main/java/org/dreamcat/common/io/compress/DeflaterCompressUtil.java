package org.dreamcat.common.io.compress;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class DeflaterCompressUtil {

    private static final int BUFFER_SIZE = 4096;

    // compress
    public static byte[] compress(byte[] data) throws IOException {
        return compress(data, Deflater.DEFAULT_COMPRESSION);
    }

    /**
     * use deflater to compress and inflater to uncompress
     *
     * @param data  input data
     * @param level the compression level (0-9)
     * @return compresses data
     * @throws IOException I/O error
     */
    public static byte[] compress(byte[] data, int level) throws IOException {
        Deflater deflater = new Deflater(level);
        deflater.setInput(data);
        // no input any more
        deflater.finish();
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length)) {
            byte[] buf = new byte[BUFFER_SIZE];
            while (!deflater.finished()) {
                int i = deflater.deflate(buf);
                bos.write(buf, 0, i);
            }
            return bos.toByteArray();
        } finally {
            deflater.end();
        }
        //try (ByteArrayInputStream ins = new ByteArrayInputStream(data)) {
        //    try (ByteArrayOutputStream outs = new ByteArrayOutputStream()) {
        //        compress(ins, outs);
        //        data = outs.toByteArray();
        //        return data;
        //    }
        //}
    }

    public static void compress(InputStream ins, OutputStream outs) throws IOException {
        try (OutputStream o = new DeflaterOutputStream(outs)) {
            int count;
            byte[] data = new byte[BUFFER_SIZE];
            while ((count = ins.read(data)) > 0) {
                o.write(data, 0, count);
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
        compress(srcFile, new File(srcFile + ".z"));
    }

}
