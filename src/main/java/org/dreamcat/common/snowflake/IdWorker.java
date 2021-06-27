package org.dreamcat.common.snowflake;

/**
 * Create by tuke on 2020/5/30
 */
public class IdWorker {

    // 1 unused bit, 41 timestamp bits, 5 worker bits, 5 data-center bits, 12 sequence bits
    private final long workerId;
    private final long datacenterId;
    private final long initialEpochMillis;

    private long sequence;
    private long lastTimestamp;

    // (1L << 40) + (1L << 38) + (1L << 37) + (1L << 36), since Feb 2020
    public IdWorker(long workerId, long datacenterId, long sequence) {
        this(workerId, datacenterId, sequence, 1580547964928L);
    }

    public IdWorker(long workerId, long datacenterId, long sequence, long initialEpochMillis) {
        if (workerId > 0b11111 || workerId < 0) {
            throw new IllegalArgumentException(
                    "workerId can't be greater than 31 or less than 0");
        }
        if (datacenterId > 0b11111 || datacenterId < 0) {
            throw new IllegalArgumentException(
                    "datacenterId cannot be greater than 31 or less than 0");
        }

        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.sequence = sequence;
        this.initialEpochMillis = initialEpochMillis;
    }

    public synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            System.err.printf("Clock is moving backwards. Rejecting requests until %d.",
                    lastTimestamp);
            throw new RuntimeException(String.format(
                    "Clock moved backwards. Refusing to generate id for %d milliseconds",
                    lastTimestamp - timestamp));
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & 0xfff;
            if (sequence == 0) {
                // wait until next millis
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }
        lastTimestamp = timestamp;

        return ((timestamp - initialEpochMillis) << 22) |
                (datacenterId << 17) | (workerId << 12) |
                sequence;

    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

}
