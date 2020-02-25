package org.dreamcat.common.core.legacy;

enum Month2Enum {
    ja(1), fe(2), mr(3), ap(4), ma(5), ju(6), jl(7), au(8), se(9), oc(10), no(11), de(12);
    final private int value;

    Month2Enum(int v) {
        value = v;
    }

    Month2Enum() {
        value = 0;
    }

    public int val() {
        return value;
    }
}
