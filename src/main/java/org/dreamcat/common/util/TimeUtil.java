package org.dreamcat.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * Create by tuke on 2020/9/2
 */
public class TimeUtil {

    private static final ZoneOffset DEFAULT_ZONE = ZoneOffset.UTC;

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
}
