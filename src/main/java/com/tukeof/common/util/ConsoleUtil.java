package com.tukeof.common.util;

/**
 * Create by tuke on 2018-09-09
 */
public class ConsoleUtil {

    public static void puts(String message) {
        println(message);
    }

    public static void p(String format, Object... args) {
        printf(format, args);
    }

    public static void println(String message) {
        System.out.println(message);
    }

    public static void printf(String format, Object... args) {
        System.out.printf(format, args);
    }
}
