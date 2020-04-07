package org.dreamcat.common.util;

/**
 * Create by tuke on 2020/2/24
 */
public class ConsoleUtil {

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
        int size = args.length;
        if (size == 0) {
            println();
            return;
        }
        for (int i=0; i<size; i++){
            print(args[i]);
            if (i == size - 1) {
                print('\n');
            } else {
                print(" ");
            }
        }
    }

    public static void printf(String format, Object... args) {
        System.out.printf(format, args);
    }

}
