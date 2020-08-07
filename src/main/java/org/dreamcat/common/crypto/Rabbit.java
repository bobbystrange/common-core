package org.dreamcat.common.crypto;

/**
 * Create by tuke on 2020/5/13
 */
public class Rabbit {
    private final int[] x = new int[8];
    private final int[] c = new int[8];
    private int carry = 0;

    // byte array to int , Big-Endian format
    private static int os2ip(byte[] a, int i) {
        int x0 = a[i + 3] & 0x000000ff;
        int x1 = a[i + 2] << 8 & 0x0000ff00;
        int x2 = a[i + 1] << 16 & 0x00ff0000;
        int x3 = a[i] << 24 & 0xff000000;

        return x0 | x1 | x2 | x3;
    }

    // int to 4-bytes array, Big-Endian format
    private static byte[] i2osp(int x) {
        byte[] s = new byte[4];
        s[3] = (byte) (x & 0x000000ff);
        s[2] = (byte) ((x & 0x0000ff00) >>> 8);
        s[1] = (byte) ((x & 0x00ff0000) >>> 16);
        s[0] = (byte) ((x & 0xff000000) >>> 24);
        return s;
    }

    // p_key.length == 128
    public void initKey(byte[] p_key) {
        int k0, k1, k2, k3, i;

        k0 = os2ip(p_key, 12);
        k1 = os2ip(p_key, 8);
        k2 = os2ip(p_key, 4);
        k3 = os2ip(p_key, 0);

        x[0] = k0;
        x[2] = k1;
        x[4] = k2;
        x[6] = k3;
        x[1] = (k3 << 16) | (k2 >>> 16);
        x[3] = (k0 << 16) | (k3 >>> 16);
        x[5] = (k1 << 16) | (k0 >>> 16);
        x[7] = (k2 << 16) | (k1 >>> 16);

        c[0] = rotL(k2, 16);
        c[2] = rotL(k3, 16);
        c[4] = rotL(k0, 16);
        c[6] = rotL(k1, 16);
        c[1] = (k0 & 0xffff0000) | (k1 & 0x0000ffff);
        c[3] = (k1 & 0xffff0000) | (k2 & 0x0000ffff);
        c[5] = (k2 & 0xffff0000) | (k3 & 0x0000ffff);
        c[7] = (k3 & 0xffff0000) | (k0 & 0x0000ffff);

        carry = 0;
        for (i = 0; i < 4; i++) {
            next_state();
        }

        for (i = 0; i < 8; i++) {
            c[(i + 4) & 7] ^= x[i];
        }
    }

    // p_dest.length >= p_src.length && p_src.length % 16 == 0 && p_dest.length % 16 == 0
    public void cipher(byte[] p_src, byte[] p_dest) {
        int i, j, m;
        int[] k = new int[4];
        byte[] t;

        int data_size = p_src.length;
        for (i = 0; i < data_size; i += 16) {
            next_state();
            k[0] = os2ip(p_src, i + 0) ^ x[0] ^ (x[5] >>> 16) ^ (x[3] << 16);
            k[1] = os2ip(p_src, i + 4) ^ x[2] ^ (x[7] >>> 16) ^ (x[5] << 16);
            k[2] = os2ip(p_src, i + 8) ^ x[4] ^ (x[1] >>> 16) ^ (x[7] << 16);
            k[3] = os2ip(p_src, i + 12) ^ x[6] ^ (x[3] >>> 16) ^ (x[1] << 16);
            for (j = 0; j < 4; j++) {
                t = i2osp(k[j]);
                for (m = 0; m < 4; m++) {
                    p_dest[i + j * 4 + m] = t[m];
                }
            }
        }
    }

    private int g_func(int x) {
        int a = x & 0xffff;
        int b = x >>> 16;

        int h = ((((a * a) >>> 17) + (a * b)) >>> 15) + b * b;
        int l = x * x;

        return h ^ l;
    }

    // (unsigned x) < (unsigned y), then return 1, else return 0
    private int compare(int x, int y) {
        long a = x;
        long b = y;
        a &= 0x00000000ffffffffL;
        b &= 0x00000000ffffffffL;
        return (a < b) ? 1 : 0;
    }

    private int rotL(int x, int y) {
        return (x << y) | (x >>> (32 - y));
    }

    private void next_state() {
        int[] g = new int[8];
        int[] c_old = new int[8];
        int i;

        for (i = 0; i < 8; i++) {
            c_old[i] = c[i];
        }

        c[0] += 0x4d34d34d + carry;
        c[1] += 0xd34d34d3 + compare(c[0], c_old[0]);
        c[2] += 0x34d34d34 + compare(c[1], c_old[1]);
        c[3] += 0x4d34d34d + compare(c[2], c_old[2]);
        c[4] += 0xd34d34d3 + compare(c[3], c_old[3]);
        c[5] += 0x34d34d34 + compare(c[4], c_old[4]);
        c[6] += 0x4d34d34d + compare(c[5], c_old[5]);
        c[7] += 0xd34d34d3 + compare(c[6], c_old[6]);
        carry = compare(c[7], c_old[7]);

        for (i = 0; i < 8; i++) {
            g[i] = g_func(x[i] + c[i]);
        }

        x[0] = g[0] + rotL(g[7], 16) + rotL(g[6], 16);
        x[1] = g[1] + rotL(g[0], 8) + g[7];
        x[2] = g[2] + rotL(g[1], 16) + rotL(g[0], 16);
        x[3] = g[3] + rotL(g[2], 8) + g[1];
        x[4] = g[4] + rotL(g[3], 16) + rotL(g[2], 16);
        x[5] = g[5] + rotL(g[4], 8) + g[3];
        x[6] = g[6] + rotL(g[5], 16) + rotL(g[4], 16);
        x[7] = g[7] + rotL(g[6], 8) + g[5];
    }

}
