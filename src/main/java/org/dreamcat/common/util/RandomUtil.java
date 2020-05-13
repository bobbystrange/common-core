package org.dreamcat.common.util;

import java.awt.*;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

public class RandomUtil {
    private static final Random random = new Random();

    private static final String NUMBERS = "0123456789";
    private static final String NUMBERS_HEX = "0123456789abcdef";
    private static final String UPPER_CASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";

    private static final String C10 = NUMBERS;
    private static final String C16 = NUMBERS_HEX;
    private static final String C26 = LOWER_CASE_LETTERS;
    private static final String C36 = C10 + C26;
    private static final String C52 = C26 + UPPER_CASE_LETTERS;
    private static final String C62 = C10 + C52;
    private static final String C72 = C10 + C62;

    // (0, 1)
    public static double rand() {
        return random.nextDouble();
    }

    public static double rand(double start, double end) {
        return start + rand() * (end - start);
    }

    // [0, bound]
    public static int randi(int bound) {
        return random.nextInt(bound);
    }

    // [start, end -1]
    public static int randi(int start, int end) {
        return random.nextInt(end - start) + start;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static char choose(String letters) {
        int len = letters.length();
        int index = randi(len);
        return letters.charAt(index);
    }

    public static String choose(int size, String letters) {
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            sb.append(choose(letters));
        }
        return sb.toString();
    }

    public static String choose10(int size) {
        return choose(size, C10);
    }

    public static String choose16(int size) {
        return choose(size, C16);
    }

    public static String choose26(int size) {
        return choose(size, C26);
    }

    public static String choose36(int size) {
        return choose(size, C36);
    }

    public static String choose52(int size) {
        return choose(size, C52);
    }

    public static String choose62(int size) {
        return choose(size, C62);
    }

    public static String choose72(int size) {
        return choose(size, C72);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static String uuid32() {
        return uuid().replaceAll("-", "");
    }

    public static String uuid36() {
        return uuid();
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static byte[] bytes(byte[] randomSeed, int size) {
        byte[] rand = new byte[size];
        Random random = new SecureRandom(randomSeed);
        random.nextBytes(rand);
        return rand;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static Color color(int rgbStart, int rgbEnd) {
        int r = RandomUtil.randi(rgbStart, rgbEnd);
        int g = RandomUtil.randi(rgbStart, rgbEnd);
        int b = RandomUtil.randi(rgbStart, rgbEnd);
        return new Color(r, g, b);
    }
}
