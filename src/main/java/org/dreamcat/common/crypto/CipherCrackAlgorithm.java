package org.dreamcat.common.crypto;

import java.io.File;
import java.io.RandomAccessFile;
import org.dreamcat.common.io.FileUtil;

/**
 * Create by tuke on 2020/5/5
 */
public interface CipherCrackAlgorithm {

    byte[] decryptCbc(byte[] data, int offset, int length, byte[] key) throws Exception;

    default void decryptCbc(File input, File output, byte[] key) throws Exception {
        decryptCbc(input, output, key, 65536);
    }

    default void decryptCbc(File input, File output, byte[] key, int maxSize) throws Exception {
        decryptCbc(input, output, key, maxSize, 0L);
    }

    default void decryptCbc(File input, File output, byte[] key, int maxSize, long offset)
            throws Exception {
        byte[] buffer = new byte[maxSize];
        int n;
        try (RandomAccessFile raf = new RandomAccessFile(input, "r")) {
            if (offset > 0) {
                raf.seek(offset);
            }
            n = raf.read(buffer);
            // reach EOF
            if (n == -1) return;

            n = Math.min(n, maxSize);
        }

        int pos;
        boolean found = false;
        for (pos = 16; pos <= n; pos += 16) {
            try {
                byte[] decrypted = decryptCbc(buffer, 0, pos, key);
                FileUtil.writeFrom(output, decrypted, true);
                found = true;
                break;
            } catch (Exception ignore) {
            }
        }

        if (!found) {
            throw new IllegalStateException("reach max size " + offset);
        }

        decryptCbc(input, output, key, maxSize, offset + pos);
    }

}
