package org.dreamcat.common.io;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.ObjectUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

/**
 * Create by tuke on 2020/4/10
 */
@Slf4j
public class FileSplitUtil {
    private static final int BUFFER_SIZE = 4096;

    public static void split(String file, int splitCount) throws IOException {
        split(new File(file), splitCount);
    }

    public static void split(File file, int splitCount) throws IOException {
        split(file, splitCount, "\n");
    }

    public static void split(String file, int splitCount, String splitStr) throws IOException {
        split(new File(file), splitCount, splitStr);
    }

    public static void split(File file, int splitCount, String splitStr) throws IOException {
        split(file, splitCount, splitStr, it -> {
            String path = file.getAbsolutePath();
            FileUtil.dirname(path);
            FileUtil.prefix(path);
            String num = String.format("%" + (it + "").length() + "d", it);
            return FileUtil.normalize(
                    FileUtil.dirname(path) + "/" +
                            FileUtil.prefix(path) + "_" + num + "." +
                            FileUtil.suffix(path));
        });
    }

    public static void split(String file, int splitCount, String splitStr, IntFunction<String> filenameSupplier) throws IOException {
        split(new File(file), splitCount, splitStr, filenameSupplier);
    }

    public static void split(File file, int splitCount, String splitStr, IntFunction<String> filenameSupplier) throws IOException {
        ObjectUtil.requireRange(splitCount, 2, 65537);
        ObjectUtil.requireNotEmpty(splitStr, "splitStr");
        List<long[]> positions = getPositions(file, splitCount, splitStr);

        //positions.stream().map(Arrays::toString).forEach(System.out::println);
        positions.parallelStream().forEach(it -> {
            String filename = filenameSupplier.apply((int) it[0]);
            long startPos = it[1];
            long totalSize = it[2] - startPos;
            try (RandomAccessFile ins = new RandomAccessFile(file, "r")) {
                ins.seek(startPos);
                try (FileOutputStream outs = new FileOutputStream(filename)) {
                    byte[] buf = new byte[BUFFER_SIZE];
                    long count = totalSize / BUFFER_SIZE;
                    int rem = (int) (totalSize % BUFFER_SIZE);
                    for (int i = 0; i < count; i++) {
                        ins.read(buf);
                        outs.write(buf);
                    }

                    if (rem > 0) {
                        byte[] remBuf = new byte[rem];
                        ins.read(remBuf);
                        outs.write(remBuf);
                    }
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        });
    }

    private static List<long[]> getPositions(File file, int splitCount, String splitStr) throws IOException {
        List<long[]> positions = new ArrayList<>();
        long lastPos = 0;
        int seq = 1;

        long fileLen = file.length();
        long blockSize = fileLen / splitCount;
        tag:
        try (RandomAccessFile input = new RandomAccessFile(file, "r")) {
            for (int i = 1; i < splitCount; i++) {
                long pos = blockSize * i;
                if (lastPos >= pos) continue;
                input.seek(pos);

                int c;
                int k = 0;
                byte[] bytes = splitStr.getBytes(StandardCharsets.ISO_8859_1);
                int width = splitStr.length();

                while (true) {
                    byte splitByte = bytes[k];
                    c = input.read();
                    if (c == -1) break tag;

                    pos++;
                    if (c == splitByte) {
                        k++;
                        if (k == width) {
                            break;
                        }
                    } else {

                        if (k != 0) k = 0;
                    }
                }

                positions.add(new long[]{seq++, lastPos, pos - splitStr.getBytes().length});
                lastPos = pos;
            }
        }
        positions.add(new long[]{seq, lastPos, fileLen});
        return positions;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static void join(CharSequence delimiter, List<File> files, File destFile) throws IOException {
        ObjectUtil.requireNotNull(files, "files");

        try (FileOutputStream outs = new FileOutputStream(destFile)) {
            int n = files.size();

            for (int i = 0; i < n; i++) {
                try (FileInputStream ins = new FileInputStream(files.get(i))) {
                    if (i != 0) {
                        outs.write(delimiter.toString().getBytes(StandardCharsets.ISO_8859_1));
                    }
                    byte[] buf = new byte[BUFFER_SIZE];
                    int readSize;
                    while ((readSize = ins.read(buf)) > 0) {
                        outs.write(buf, 0, readSize);
                    }
                }
            }
        }

    }
}
