package org.dreamcat.common.util;

public class ByteUtil {

    public static String hex(byte input) {
        // substring(1) remove the prefix "1"
        // 0x100 make sure per byte has two char
        // when byte < 0x0f, it will have the prefix "0" and like 0*
        return Integer.toHexString((input & 0xff) + 0x100)
                .substring(1);
    }

    public static String hex(byte[] input) {
        ObjectUtil.requireNotNull(input, "input");

        StringBuilder sb = new StringBuilder();
        for (byte i : input) {
            sb.append(hex(i));
        }
        return sb.toString();
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    // another way
    @Deprecated
    public static String hexString(byte input) {

        StringBuilder hexString = new StringBuilder(Integer.toHexString(input & 0xFF));
        if (hexString.length() < 2) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }

    @Deprecated
    public static String hexString(byte[] input) {
        ObjectUtil.requireNotNull(input, "input");

        StringBuilder sb = new StringBuilder();
        for (byte i : input) {
            sb.append(hexString(i));
        }
        return sb.toString();
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static int join(byte b1, byte b2) {
        return b1 << 8 + b2;
        //return (b1 << 8) + (b2 > 0 ? b2 : b2 + 256);
    }

    public static int join(byte b1, byte b2, byte b3) {
        return b1 << 16 + b2 << 8 + b3;
    }

    public static int join(byte b1, byte b2, byte b3, byte b4) {
        return b1 << 24 + b2 << 16 + b3 << 8 + b4;
    }

    //  \u0000 , \u0080 - \u07ff  110*,10*  11Bit
    public static int toCharAsUtf8(byte x, byte y) {
        return (((x & 0x1f) << 6) + (y & 0x3f));
    }

    //  \u0800 - \uffff  1110*,10*,10*  16Bit
    public static int toCharAsUtf8(byte x, byte y, byte z) {
        return (((x & 0xf) << 12) + ((y & 0x3f) << 6) + (z & 0x3f));
    }

    //  \u00010000 - \uffffffff  11101101,1010*,10*,11101101,1011*,10*  21Bit
    public static int toCharAsUtf8(byte u, byte v, byte w, byte x, byte y, byte z) {
        return (((v & 0x0f) << 16) +
                ((w & 0x3f) << 10) + ((y & 0x0f) << 6) + (z & 0x3f));
    }

    public static int toCharAsUtf8(byte v, byte w, byte y, byte z) {
        return (((v & 0x0f) << 16) +
                ((w & 0x3f) << 10) + ((y & 0x0f) << 6) + (z & 0x3f));
    }

}
