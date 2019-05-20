package com.tukeof.common.legacy;

/**
 * @author tuke
 */
enum MonthEnum {
    January(1), February(2), March(3), April(4), May(5), June(6), July(7), Aughst(8), September(9), October(10), November(11), December(12);
    final private int value;

    MonthEnum(int v) {
        value = v;
    }

    MonthEnum() {
        value = 0;
    }

    public int val() {
        return value;
    }

}

