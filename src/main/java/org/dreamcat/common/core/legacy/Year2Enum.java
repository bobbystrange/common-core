package org.dreamcat.common.core.legacy;

enum Year2Enum {
    ze(0), on(1), to(2), th(3), fo(4), fi(5), si(6), se(7), ei(8), ni(9);
    private final int value;

    Year2Enum(int v) {
        value = v;
    }

    Year2Enum() {
        value = 0;
    }

    public int val() {
        return value;
    }
}
