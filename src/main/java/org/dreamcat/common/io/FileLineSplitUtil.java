package org.dreamcat.common.io;

import org.dreamcat.common.util.ObjectUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.BinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by tuke on 2020/6/3
 */
public class FileLineSplitUtil {

    public static void split(String file, String pattern, BinaryOperator<String> nameSupplier) throws IOException {
        split(new File(file), pattern, nameSupplier);
    }

    public static void split(File file, String pattern, BinaryOperator<String> nameSupplier) throws IOException {
        Pattern pat = Pattern.compile(pattern);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (ObjectUtil.isBlank(line)) continue;
                ;
                Matcher matcher = pat.matcher(line);
                if (matcher.matches()) {

                }
            }
        }
    }


}
