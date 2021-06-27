package org.dreamcat.common.legacy;

enum Year1Enum {
    z(0), o(1), t(2), r(3), f(4), v(5), x(6), s(7), e(8), n(9);
    private final int value;

    Year1Enum(int v) {
        value = v;
    }

    Year1Enum() {
        value = 0;
    }

    public int val() {
        return value;
    }
}
