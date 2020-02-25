package org.dreamcat.common.util;

import org.dreamcat.common.annotation.Nullable;
import org.dreamcat.common.io.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UrlUtil {

    private static final int TIMEOUT = 5_000;

    public static String concatUrl(String baseUrl, Map<String, String> map) {
        return concatUrl(baseUrl, map, null, true);
    }

    public static String concatUrl(String baseUrl, Map<String, String> map, String statement) {
        return concatUrl(baseUrl, map, statement, true);
    }

    public static String concatUrl(String baseUrl, Map<String, String> map, boolean encode) {
        return concatUrl(baseUrl, map, null, encode);
    }

    public static String concatUrl(String baseUrl, Map<String, String> map, String statement, boolean encode) {
        StringBuilder url = new StringBuilder(baseUrl);
        if (!map.isEmpty()) {
            url.append('?');
            String sortedQueryString = toSortedQueryString(map, it -> it, it -> {
                String value = it.toString();
                if (encode) value = encodeUrl(value);
                return value;
            });
            url.append(sortedQueryString);
        }
        if (statement != null) {
            url.append('#').append(statement);
        }
        return url.toString();
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static String encodeUrl(String uri) {
        try {
            return URLEncoder.encode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static String toSortedQueryString(Map<String, ?> queryMap) {
        return toSortedQueryString(queryMap, it -> it, Object::toString);
    }

    public static String toSortedQueryString(
            Map<String, ?> queryMap,
            Function<String, String> paramKey,
            Function<Object, String> paramValue) {
        return queryMap.entrySet().stream()
                .filter(entry -> !(entry.getValue().toString().trim().isEmpty()))
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

    public InputStream request(
            String url,
            String method,
            @Nullable InputStream source,
            @Nullable Map<String, String> headers,
            @Nullable Map<String, String> queryMap) throws IOException {
        return request(url, method, TIMEOUT, TIMEOUT, source, headers, queryMap);
    }

    public InputStream request(
            String url,
            String method,
            int connectTimeout,
            int readTimeout,
            @Nullable InputStream source,
            @Nullable Map<String, String> headers,
            @Nullable Map<String, String> queryMap) throws IOException {
        if (ObjectUtil.isNotEmpty(queryMap)) url = UrlUtil.concatUrl(url, queryMap);

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);
        connection.setConnectTimeout(connectTimeout);
        connection.setReadTimeout(readTimeout);

        if (ObjectUtil.isNotEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        connection.setDoInput(true);
        if (source != null) {
            connection.setDoOutput(true);
            try (OutputStream target = connection.getOutputStream()) {
                IOUtil.copy(source, target);
            }

        }

        return connection.getInputStream();
    }

}
