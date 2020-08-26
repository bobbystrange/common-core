package org.dreamcat.common.util;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Create by tuke on 2020/2/24
 */
public class FormatUtil {

    // log-style, {}, {}
    public static void log(String format, Object... args) {
        log(System.out, format, args);
    }

    public static void log(String message, Throwable t) {
        log(message);
        t.printStackTrace();
    }

    public static void log(PrintStream out, String format, Object... args) {
        if (ObjectUtil.isEmpty(args)) {
            System.out.println(format);
            return;
        }

        String[] sp = format.split("\\{}", args.length + 1);
        if (sp.length == 1) {
            System.out.println(format);
            return;
        }

        int count = sp.length - 1;
        StringBuilder sb = new StringBuilder(sp[0]);
        for (int i = 1; i <= count; i++) {
            Object arg = args[i - 1];
            sb.append(arg == null ? "null" : arg.toString());
            sb.append(sp[i]);
        }
        out.println(sb.toString());
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static String format(BigDecimal n) {
        if (n == null) return null;
        n = n.setScale(2, RoundingMode.HALF_EVEN);
        return n.stripTrailingZeros().toPlainString();
    }

}
