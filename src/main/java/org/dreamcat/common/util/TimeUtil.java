package org.dreamcat.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * Create by tuke on 2020/9/2
 */
public class TimeUtil {

    /**
     * use <strong>user.timezone</strong> from the system properties or OS
     *
     * @param timestamp epoch milliseconds
     * @return local datetime
     */
    public static LocalDateTime ofEpochMilliWithDefaultZone(long timestamp) {
        ZoneId zoneId = ZoneId.systemDefault();
        return ofEpochMilli(timestamp, zoneId);
    }

    /**
     * to utc datetime
     *
     * @param timestamp the milliseconds since epoch (January 1, 1970, 00:00:00 GMT).
     * @return utc datetime
     * @see java.util.Date#Date(long)
     */
    public static LocalDateTime ofEpochMilli(long timestamp) {
        return ofEpochMilli(timestamp, ZoneOffset.UTC);
    }

    public static LocalDateTime ofEpochMilli(long timestamp, ZoneId zoneId) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    public static long asEpochMilli(LocalDateTime dateTime) {
        return asEpochMilli(dateTime, ZoneOffset.UTC);
    }

    public static long asEpochMilli(LocalDateTime localDateTime, ZoneId zoneId) {
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        return instant.toEpochMilli();
    }
}
