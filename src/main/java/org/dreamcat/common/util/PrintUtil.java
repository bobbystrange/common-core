package org.dreamcat.common.util;

/**
 * Create by tuke on 2020/2/24
 */
public class PrintUtil {

    public static void p(String format, Object... args) {
        printf(format, args);
    }

    public static void puts(String message) {
        println(message);
    }

    public static void print(String s) {
        System.out.print(s);
    }

    public static void print(Object obj) {
        System.out.print(obj);
    }

    public static void println() {
        System.out.println();
    }

    public static void println(String x) {
        System.out.println(x);
    }

    public static void println(Object... args) {
        for (Object arg : args) {
            System.out.println(arg);
        }
    }

    public static void printf(String format, Object... args) {
        System.out.printf(format, args);
    }

}
