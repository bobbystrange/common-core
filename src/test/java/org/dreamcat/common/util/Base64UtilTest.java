package org.dreamcat.common.util;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.junit.Test;

/**
 * Create by tuke on 2020/10/23
 */
public class Base64UtilTest {

    @Test
    public void test() {
        // YWI=
        encode("ab");
        // YWJj
        encode("abc");
        // YWJjZA==
        encode("abcd");
        // YWJjZGU=
        encode("abcde");

        String s = "ca627bfdf9824bd0b0ba439e80d755cb:f71272c7dd0644a288244232f3d798a0";

        System.out.println(Arrays.toString(s.getBytes()));
        System.out.println(Arrays.toString(s.getBytes(StandardCharsets.ISO_8859_1)));
        System.out.println(Arrays.toString(s.getBytes(StandardCharsets.UTF_8)));
        System.out.println(Base64Util.encodeAsString(
                "ca627bfdf9824bd0b0ba439e80d755cb:f71272c7dd0644a288244232f3d798a0"));
        System.out.println(Base64Util.decodeAsString(
                "Y2E2MjdiZmRmOTgyNGJkMGIwYmE0MzllODBkNzU1Y2I6ZjcxMjcyYzdkZDA2NDRhMjg4MjQ0MjMyZjNkNzk4YTAK"));
        System.out.println(Base64Util.decodeAsString(
                "Y2E2MjdiZmRmOTgyNGJkMGIwYmE0MzllODBkNzU1Y2I6ZjcxMjcyYzdkZDA2NDRhMjg4MjQ0MjMyZjNkNzk4YTA="));
    }

    private void encode(String s) {
        System.out.println("encode");
        System.out.println(Arrays.toString(Base64Util.encode(s.getBytes())));
        System.out.println(Arrays.toString(Base64Util.encode(s.getBytes(), true)));
        System.out.println(Arrays.toString(Base64Util.encode(s)));
        System.out.println(Arrays.toString(Base64Util.encode(s, true)));
        System.out.println(Base64Util.encodeAsString(s.getBytes()));
        System.out.println(Base64Util.encodeAsString(s.getBytes(), true));
        System.out.println(Base64Util.encodeAsString(s));
        System.out.println(Base64Util.encodeAsString(s, true));
        System.out.println("encodeUrlSafe");
        System.out.println(Arrays.toString(Base64Util.encodeUrlSafe(s.getBytes())));
        System.out.println(Arrays.toString(Base64Util.encodeUrlSafe(s.getBytes(), true)));
        System.out.println(Arrays.toString(Base64Util.encodeUrlSafe(s)));
        System.out.println(Arrays.toString(Base64Util.encodeUrlSafe(s, true)));
        System.out.println(Base64Util.encodeUrlSafeAsString(s.getBytes()));
        System.out.println(Base64Util.encodeUrlSafeAsString(s.getBytes(), true));
        System.out.println(Base64Util.encodeUrlSafeAsString(s));
        System.out.println(Base64Util.encodeUrlSafeAsString(s, true));
        System.out.println();
    }
}
