package com.tukeof.common.util;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

public class RandomUtil {
    private static final Random random = new Random();

    private static final String C10_CONTENT =
            "0123456789";
    private static final String C26_CONTENT =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String C32_CONTENT = C10_CONTENT + C26_CONTENT;
    private static final String C72_CONTENT = C32_CONTENT +
            "0123456789abcdefghijklmnopqrstuvwxyz";

    public static String generateUuid32() {
        return generateUuid().replaceAll("-", "");
    }

    public static String generateUuid36() {
        return generateUuid();
    }

    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }

    public static String nonce72(int size) {
        return nonce(size, C72_CONTENT);
    }

    public static String nonce32(int size) {
        return nonce(size, C32_CONTENT);
    }

    public static String nonce(int size, String content) {
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++)
            sb.append(content.charAt(random.nextInt(content.length())));
        return sb.toString();
    }

    public static String generateLowerCase(int size) {
        return generateUpperCase(size).toLowerCase();
    }

    public static String generateUpperCase(int size) {
        return nonce(size, C26_CONTENT);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static byte[] randomBytes(byte[] seed, int size) {
        byte[] rand = new byte[size];
        Random random = new SecureRandom(seed);
        random.nextBytes(rand);
        return rand;
    }
}
