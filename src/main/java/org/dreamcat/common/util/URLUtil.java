package org.dreamcat.common.util;

import org.dreamcat.common.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class URLUtil {

    private static final int TIMEOUT = 10_000;

    public static String concatUrl(String baseUrl, Map<String, String> map) {
        return concatUrl(baseUrl, map, null, true);
    }

    public static String concatUrl(String baseUrl, Map<String, String> map, String statement) {
        return concatUrl(baseUrl, map, statement, true);
    }

    public static String concatUrl(String baseUrl, Map<String, String> map, boolean encode) {
        return concatUrl(baseUrl, map, null, encode);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static String concatUrl(String baseUrl, Map<String, String> map, String statement, boolean encode) {
        StringBuilder uri = new StringBuilder(baseUrl);
        uri.append('?');
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (value == null || value.trim().isEmpty()) continue;

            if (!uri.toString().endsWith("?")) uri.append('&');
            if (encode) value = encodeUrl(value);
            uri.append(key).append('=').append(value);
        }
        if (statement != null) uri.append('#').append(statement);
        return uri.toString();
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static String encodeUrl(String uri) {
        try {
            return URLEncoder.encode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

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
        if (ObjectUtil.isNotEmpty(queryMap)) url = URLUtil.concatUrl(url, queryMap);

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
