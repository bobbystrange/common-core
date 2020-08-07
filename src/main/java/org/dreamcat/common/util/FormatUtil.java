package org.dreamcat.common.util;

import org.dreamcat.common.io.LineTerminatedReader;

import java.io.PrintStream;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2020/2/24
 */
public class FormatUtil {

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

    // err
    public static void printe(String s) {
        System.err.print(s);
    }

    public static void printe(Object obj) {
        System.err.print(obj);
    }

    public static void printfe(String format, Object... args) {
        System.err.printf(format, args);
    }

    public static void printlne(String x) {
        System.err.println(x);
    }

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
        out.println(sb.toString());
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static String fmt(char c) {
        if (c == '\r') return "\\r";
        if (c == '\n') return "\\n";
        return String.valueOf(c);
    }

    public static String fmtCRLF(String s) {
        return s.chars().mapToObj(c -> {
            if (c == '\r') return "\\r";
            if (c == '\n') return "\\n";
            return (char) c + "";
        }).collect(Collectors.joining());
    }

    /**
     * format a magic int to a human-readable line-terminated string in literal
     *
     * @param lineTerminated line terminated string
     * @return \r, \n or \r\n in literal
     * @see LineTerminatedReader#CRLF
     * @see LineTerminatedReader#readLine()
     */
    public static String fmtCRLF(int lineTerminated) {
        if (lineTerminated == '\r') return "\\r";
        if (lineTerminated == '\n') return "\\n";
        else return "\\r\\n";
    }

}
