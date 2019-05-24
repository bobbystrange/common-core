package org.dreamcat.common.util;

import org.dreamcat.common.crypto.HmacEnum;
import org.dreamcat.common.crypto.MessageDigestEnum;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2019-02-19
 */
public class SignatureUtil {

    public static String joinSorted(Map<String, Object> queryMap) {
        return queryMap.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .sorted()
                .collect(Collectors.joining("&"));
    }

    public static String joinSorted(
            Map<String, Object> queryMap,
            Function<String, String> paramKey,
            Function<Object, String> paramValue) {
        return queryMap.entrySet().stream()
                .map(entry -> {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (paramKey != null) key = paramKey.apply(key);
                    if (paramValue != null) value = paramValue.apply(value);
                    return key + "=" + value;
                })
                .sorted()
                .collect(Collectors.joining("&"));
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static String md5Hex(String input) {
        return md5Hex(input.getBytes());
    }

    /**
     * @param input bytes
     * @return ascii string
     */
    public static String md5Hex(byte[] input) {
        try {
            return MessageDigestEnum.MD5.digestToHex(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5Base64(String input) {
        return md5Base64(input.getBytes());
    }

    public static String md5Base64(byte[] input) {
        try {
            return MessageDigestEnum.MD5.digestToBase64(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String hmacsha1Base64(String input, String key) {
        return hmacsha1Base64(input.getBytes(), key);
    }

    public static String hmacsha1Base64(byte[] input, String key) {
        try {
            return HmacEnum.HMAC_SHA_1.digestToBase64(input, key.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hmacsha256Base64(String input, String key) {
        return hmacsha256Base64(input.getBytes(), key);
    }

    public static String hmacsha256Base64(byte[] input, String key) {
        try {
            return HmacEnum.HMAC_SHA_256.digestToBase64(input, key.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
