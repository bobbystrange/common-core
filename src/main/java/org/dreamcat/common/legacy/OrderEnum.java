package org.dreamcat.common.legacy;

enum OrderEnum {
    first("first"),
    second("second"),
    third("third"),
    four("four"),
    fifth("fifth"),
    sixth("sixth"),
    seventh("seventh"),
    eighth("eighth"),
    ninth("ninth"),
    tenth("tenth"),
    eleventh("eleventh"),
    twelfth("twelfth");

    private final String value;

    OrderEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
