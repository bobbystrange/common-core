package org.dreamcat.common.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public final class ByteUtil {

    private ByteUtil() {
    }

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

    // assume input.length() == 2
    public static byte[] unhex(String input) {
        if (input == null || input.length() % 2 != 0) {
            throw new IllegalArgumentException("input.length() must be even");
        }
        int len = input.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            int i1 = unhex(input.charAt(i));
            int i2 = unhex(input.charAt(i + 1));
            bytes[i / 2] = (byte) ((i1 << 4) | i2);
        }
        return bytes;
    }

    public static int unhex(char c) {
        int i = c - '0';
        // ['0', '9']
        if (i >= 0 && i < 10) {
            return i;
        }
        // ['a', 'f']
        else if (i >= 49 && i <= 54) {
            return i - 39;
        }
        // ['A', 'F']
        else if (i >= 17 && i <= 22) {
            return i - 7;
        } else {
            throw new IllegalArgumentException("require [0-9a-fA-F], but got " + c);
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static byte[] convert(byte[] bytes, Charset srcCS, Charset destCS) {
        CharBuffer charBuffer = srcCS.decode(ByteBuffer.wrap(bytes));
        return destCS.encode(charBuffer).array();
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

    // Java UTF-16 treat \u0000 as 1100_0000_1000_0000
    // \u0000 - \u007f   0*        7Bit
    public static int fromUtf8(byte x) {
        return x & 0b0111_1111;
    }

    //  \u0080 - \u07ff  110*,10*  11Bit
    public static int fromUtf8(byte x, byte y) {
        return (((x & 0b0001_1111) << 6) + (y & 0b0011_1111));
    }

    //  \u0800 - \uffff  1110*,10*,10*  16Bit
    public static int fromUtf8(byte x, byte y, byte z) {
        return (((x & 0b0000_1111) << 12) + ((y & 0b0011_1111) << 6) + (z & 0b0011_1111));
    }

    //  \u0001_0000 - \u001f_ffff  1111_0*,10*,10*,10*  21Bit
    public static int fromUtf8(byte w, byte x, byte y, byte z) {
        return (((w & 0b0000_0111) << 18) +
                ((x & 0b0011_1111) << 12) + ((y & 0b0011_1111) << 6) + (z & 0b0011_1111));
    }

    //  \u0020_0000 - \u03ff_ffff  1111_10*,10*,10*,10*,10*  26Bit
    public static int fromUtf8(byte v, byte w, byte x, byte y, byte z) {
        return (((v & 0b0000_0011) << 24) + ((w & 0b0011_1111) << 18) +
                ((x & 0b0011_1111) << 12) + ((y & 0b0011_1111) << 6) + (z & 0b0011_1111));
    }

    //  \u0400_0000 - \u7fff_ffff  1111_110*,10*,10*,10*,10*,10*  31Bit
    public static int fromUtf8(byte u, byte v, byte w, byte x, byte y, byte z) {
        return (((u & 0b0000_0001) << 30) + ((v & 0b0011_1111) << 24) +
                ((w & 0b0011_1111) << 18) +
                ((x & 0b0011_1111) << 12) + ((y & 0b0011_1111) << 6) + (z & 0b0011_1111));
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====


}
