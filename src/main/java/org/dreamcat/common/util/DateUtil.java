package org.dreamcat.common.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Create by tuke on 2019-03-29
 */
public class DateUtil {

    private static final String gmt_pattern = "E, dd MMM yyyy HH:mm:ss 'GMT'";

    public static Date from(
            int year, int month, int date,
            int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date, hourOfDay, minute, second);
        return calendar.getTime();
    }

    public static Date from(
            int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date);
        return calendar.getTime();
    }

    public static String toGMTString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(gmt_pattern, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(new Date());
    }

    public static String yyMMddHHmmssSSS() {
        return yyMMddHHmmssSSS(new Date());
    }

    public static String yyMMddHHmmssSSS(Date date) {
        return new SimpleDateFormat("yyMMddHHmmssSSS")
                .format(date);
    }

    public static String yyMMddHHmmss() {
        return yyMMddHHmmss(new Date());
    }

    public static String yyMMddHHmmss(Date date) {
        return new SimpleDateFormat("yyMMddHHmmss")
                .format(date);
    }

    public static String yyMMddHHmm() {
        return yyMMddHHmm(new Date());
    }

    public static String yyMMddHHmm(Date date) {
        return new SimpleDateFormat("yyMMddHHmm")
                .format(date);
    }

    public static String yyMMdd() {
        return yyMMdd(new Date());
    }

    public static String yyMMdd(Date date) {
        return new SimpleDateFormat("yyMMdd")
                .format(date);
    }

    private String format(LocalDate localDate, String pattern) {
        return localDate.format(DateTimeFormatter.ofPattern(pattern));
    }

    private String formatLocalNow(String pattern) {
        return format(LocalDate.now(), pattern);
    }

}