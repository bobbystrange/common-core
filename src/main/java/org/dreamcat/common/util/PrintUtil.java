package org.dreamcat.common.util;

/**
 * Create by tuke on 2020/2/24
 */
public class PrintUtil {

    // c-style System.out, %s %d
    public static void print(String s) {
        System.out.print(s);
    }

    public static void print(Object obj) {
        System.out.print(obj);
    }

    public static void printf(String format, Object... args) {
        System.out.printf(format, args);
    }

    public static void println() {
        System.out.println();
    }

    public static void println(String x) {
        System.out.println(x);
    }

    public static void println(Object... args) {
        int size = args.length;
        if (size == 0) {
            println();
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(args[i]);
            if (i < size - 1) {
                sb.append(" ");
            }
        }
        println(sb.toString());
    }

    // log-style, {}, {}
    public static void log(String format, Object... args) {
        if (ObjectUtil.isEmpty(args)) {
            println(format);
            return;
        }

        String[] sp = format.split("\\{}", args.length + 1);
        if (sp.length == 1) {
            println(format);
            return;
        }

        int count = sp.length - 1;
        StringBuilder sb = new StringBuilder(sp[0]);
        for (int i = 1; i <= count; i++) {
            Object arg = args[i - 1];
            sb.append(arg == null ? "null" : arg.toString());
            sb.append(sp[i]);
        }
        println(sb.toString());
    }

}
