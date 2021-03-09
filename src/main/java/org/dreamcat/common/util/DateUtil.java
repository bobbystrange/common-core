package org.dreamcat.common.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Create by tuke on 2020/9/2
 */
public final class DateUtil {

    private DateUtil() {
    }

    private static final ZoneOffset DEFAULT_ZONE = ZoneOffset.UTC;
    private static final String GMT_PATTERN = "E, dd MMM yyyy HH:mm:ss 'GMT'";

    /**
     * use <strong>user.timezone</strong> from the system properties or OS
     *
     * @param timestamp epoch milliseconds
     * @return local datetime
     */
    public static LocalDateTime ofEpochMilliWithDefaultZone(long timestamp) {
        return ofEpochMilli(timestamp, ZoneId.systemDefault());
    }

    /**
     * to utc datetime
     *
     * @param timestamp the milliseconds since epoch (January 1, 1970, 00:00:00 GMT).
     * @return utc datetime
     * @see java.util.Date#Date(long)
     */
    public static LocalDateTime ofEpochMilli(long timestamp) {
        return ofEpochMilli(timestamp, DEFAULT_ZONE);
    }

    public static LocalDateTime ofEpochMilli(long timestamp, ZoneId zoneId) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    public static LocalDateTime ofDateWithDefaultZone(Date date) {
        return ofDate(date, ZoneId.systemDefault());
    }

    public static LocalDateTime ofDate(Date date) {
        return ofDate(date, DEFAULT_ZONE);
    }

    public static LocalDateTime ofDate(Date date, ZoneId zoneId) {
        return LocalDateTime.ofInstant(date.toInstant(), zoneId);
    }

    public static long asEpochMilliWithDefaultZone(LocalDateTime dateTime) {
        return asEpochMilli(dateTime, ZoneId.systemDefault());
    }

    public static long asEpochMilli(LocalDateTime dateTime) {
        return asEpochMilli(dateTime, DEFAULT_ZONE);
    }

    /**
     * local datetime to epoch milliseconds
     *
     * @param localDateTime local datetime
     * @param zoneId        zone
     * @return epoch milliseconds
     * @see LocalDate#atStartOfDay()
     */
    public static long asEpochMilli(LocalDateTime localDateTime, ZoneId zoneId) {
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        return instant.toEpochMilli();
    }

    public static Date asDateWithDefaultZone(LocalDateTime dateTime) {
        return asDate(dateTime, ZoneId.systemDefault());
    }

    public static Date asDate(LocalDateTime dateTime) {
        return asDate(dateTime, DEFAULT_ZONE);
    }

    public static Date asDate(LocalDateTime localDateTime, ZoneId zoneId) {
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        return Date.from(instant);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    /**
     * parse date from string
     * <ul>
     *  <li>yyMMdd                      6</li>
     *  <li>yy-MM-dd                    8</li>
     *  <li>yy/MM/dd                    8</li>
     *
     *  <li>yyyyMMdd                    8</li>
     *  <li>yyyy-MM-dd                 10</li>
     *  <li>yyyy/MM/dd                 10</li>
     *
     *  <li>yy-MM-dd hh:mm             14</li>
     *  <li>yy/MM/dd hh:mm             14</li>
     *  <li>yyyy-MM-dd hh:mm           16</li>
     *  <li>yyyy/MM/dd hh:mm           16</li>
     *
     *  <li>yyMMddhhmmss               12</li>
     *  <li>yy-MM-dd hh:mm:ss          17</li>
     *  <li>yy/MM/dd hh:mm:ss          17</li>
     *
     *  <li>yyyyMMddhhmmss             14</li>
     *  <li>yyyy-MM-dd hh:mm:ss        19</li>
     *  <li>yyyy/MM/dd hh:mm:ss        19</li>
     *
     *  <li>yyyyMMddhhmmssSSS          17</li>
     *  <li>yyyy-MM-dd hh:mm:ss SSS    23</li>
     *  <li>yyyy/MM/dd hh:mm:ss SSS    23</li>
     * </ul>
     *
     * @param formattedDate formatted date string
     * @return {@link Date}
     * @throws NumberFormatException input a invalid date
     */
    public static Date parseDate(String formattedDate) {
        int size = formattedDate.length();
        int year, month, day, hour = 0, minute = 0, second = 0, millisecond = 0;

        // yyMMdd
        if (size == 6) {
            year = Integer.parseInt(formattedDate.substring(0, 2));
            month = Integer.parseInt(formattedDate.substring(2, 4));
            day = Integer.parseInt(formattedDate.substring(4, 6));
        }
        // yy-MM-dd, yy/MM/dd or yyyyMMdd
        else if (size == 8) {
            char c = formattedDate.charAt(2);
            // yyyyMMdd
            if (c < '0' || c > '9') {
                year = Integer.parseInt(formattedDate.substring(0, 2));
                month = Integer.parseInt(formattedDate.substring(3, 5));
                day = Integer.parseInt(formattedDate.substring(6, 8));
            }
            // yy-MM-dd or yy/MM/dd
            else {
                year = Integer.parseInt(formattedDate.substring(0, 4));
                month = Integer.parseInt(formattedDate.substring(4, 6));
                day = Integer.parseInt(formattedDate.substring(6, 8));
            }
        }
        // yyyy-MM-dd, yyyy/MM/dd
        else if (size == 10) {
            year = Integer.parseInt(formattedDate.substring(0, 4));
            month = Integer.parseInt(formattedDate.substring(5, 7));
            day = Integer.parseInt(formattedDate.substring(8, 10));
        }
        // yyMMddhhmmss
        else if (size == 12) {
            year = Integer.parseInt(formattedDate.substring(0, 2));
            month = Integer.parseInt(formattedDate.substring(2, 4));
            day = Integer.parseInt(formattedDate.substring(4, 6));
            hour = Integer.parseInt(formattedDate.substring(6, 8));
            minute = Integer.parseInt(formattedDate.substring(8, 10));
            second = Integer.parseInt(formattedDate.substring(10, 12));
        }
        // yy-MM-dd hh:mm, yy/MM/dd hh:mm or yyyyMMddhhmmss
        else if (size == 14) {
            char c = formattedDate.charAt(2);
            // yyyyMMddhhmmss
            if (c < '0' || c > '9') {
                year = Integer.parseInt(formattedDate.substring(0, 4));
                month = Integer.parseInt(formattedDate.substring(4, 6));
                day = Integer.parseInt(formattedDate.substring(6, 8));
                hour = Integer.parseInt(formattedDate.substring(8, 10));
                minute = Integer.parseInt(formattedDate.substring(10, 12));
                second = Integer.parseInt(formattedDate.substring(12, 14));
            }
            // yy-MM-dd hh:mm or yy/MM/dd hh:mm
            else {
                year = Integer.parseInt(formattedDate.substring(0, 2));
                month = Integer.parseInt(formattedDate.substring(3, 5));
                day = Integer.parseInt(formattedDate.substring(6, 8));
                hour = Integer.parseInt(formattedDate.substring(9, 11));
                minute = Integer.parseInt(formattedDate.substring(12, 14));
            }
        }
        // yyyy-MM-dd hh:mm or yyyy/MM/dd hh:mm
        else if (size == 16) {
            year = Integer.parseInt(formattedDate.substring(0, 4));
            month = Integer.parseInt(formattedDate.substring(5, 7));
            day = Integer.parseInt(formattedDate.substring(8, 10));
            hour = Integer.parseInt(formattedDate.substring(11, 13));
            minute = Integer.parseInt(formattedDate.substring(14, 16));
        }
        // yy-MM-dd hh:mm:ss, yy/MM/dd hh:mm:ss or yyyyMMddhhmmssSSS
        else if (size == 17) {
            char c = formattedDate.charAt(2);
            // yyyyMMddhhmmssSSS
            if (c < '0' || c > '9') {
                year = Integer.parseInt(formattedDate.substring(0, 4));
                month = Integer.parseInt(formattedDate.substring(4, 6));
                day = Integer.parseInt(formattedDate.substring(6, 8));
                hour = Integer.parseInt(formattedDate.substring(8, 10));
                minute = Integer.parseInt(formattedDate.substring(10, 12));
                second = Integer.parseInt(formattedDate.substring(12, 14));
                millisecond = Integer.parseInt(formattedDate.substring(14, 17));
            }
            // yy-MM-dd hh:mm:ss or yy/MM/dd hh:mm:ss
            else {
                year = Integer.parseInt(formattedDate.substring(0, 2));
                month = Integer.parseInt(formattedDate.substring(3, 5));
                day = Integer.parseInt(formattedDate.substring(6, 8));
                hour = Integer.parseInt(formattedDate.substring(9, 11));
                minute = Integer.parseInt(formattedDate.substring(12, 14));
                second = Integer.parseInt(formattedDate.substring(15, 17));
            }
        }
        // yyyy-MM-dd hh:mm:ss or yyyy/MM/dd hh:mm:ss
        else if (size == 19) {
            year = Integer.parseInt(formattedDate.substring(0, 4));
            month = Integer.parseInt(formattedDate.substring(5, 7));
            day = Integer.parseInt(formattedDate.substring(8, 10));
            hour = Integer.parseInt(formattedDate.substring(11, 13));
            minute = Integer.parseInt(formattedDate.substring(14, 16));
            second = Integer.parseInt(formattedDate.substring(17, 19));
        }
        // yyyy-MM-dd hh:mm:ss SSS or yyyy/MM/dd hh:mm:ss SSS
        else {
            year = Integer.parseInt(formattedDate.substring(0, 4));
            month = Integer.parseInt(formattedDate.substring(5, 7));
            day = Integer.parseInt(formattedDate.substring(8, 10));
            hour = Integer.parseInt(formattedDate.substring(11, 13));
            minute = Integer.parseInt(formattedDate.substring(14, 16));
            second = Integer.parseInt(formattedDate.substring(17, 19));
            millisecond = Integer.parseInt(formattedDate.substring(20, 23));
        }
        return from(year, month, day, hour, minute, second, millisecond);
    }

    public static LocalDate parseLocalDate(String value) {
        Date date = parseDate(value);
        return ofDate(date).toLocalDate();
    }

    public static LocalTime parseLocalTime(String value) {
        Date date = parseDate(value);
        return ofDate(date).toLocalTime();
    }

    public static LocalDateTime parseLocalDateTime(String value) {
        Date date = parseDate(value);
        return ofDate(date);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static Date from(
            int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date);
        return calendar.getTime();
    }

    public static Date from(
            int year, int month, int date,
            int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date, hourOfDay, minute, second);
        return calendar.getTime();
    }

    public static Date from(
            int year, int month, int date,
            int hourOfDay, int minute, int second, int millisecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date, hourOfDay, minute, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
        return calendar.getTime();
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static String formatGMTString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(GMT_PATTERN, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(date);
    }

    public static String format(LocalDateTime localDateTime, String pattern) {
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatLocalNow(String pattern) {
        return format(LocalDateTime.now(), pattern);
    }
}
