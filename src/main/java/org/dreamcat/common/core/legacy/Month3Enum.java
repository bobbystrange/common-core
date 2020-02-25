package org.dreamcat.common.core.legacy;

enum Month3Enum {
    jan(1), feb(2), mar(3), apr(4), may(5), jun(6), jul(7), aug(8), sep(9), oct(10), nov(11), dec(12);
    final private int value;

    Month3Enum(int v) {
        value = v;
    }

    Month3Enum() {
        value = 0;
    }

    public int val() {
        return value;
    }
}
