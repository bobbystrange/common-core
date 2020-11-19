package org.dreamcat.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.dreamcat.common.io.IOUtil;

public class UrlUtil {

    private static final int TIMEOUT = 5_000;

    public static String concatUrl(String baseUrl, Map<String, Object> map) {
        return concatUrl(baseUrl, map, (String) null);
    }

    public static String concatUrl(String baseUrl, Map<String, Object> map, String statement) {
        return concatUrl(baseUrl, map, statement, true);
    }

    public static String concatUrl(String baseUrl, Map<String, Object> map, String statement,
            boolean encode) {
        StringBuilder sb = new StringBuilder(baseUrl);
        if (ObjectUtil.isNotEmpty(map)) {
            sb.append("?").append(toSortedQueryString(
                    map, (k, v) -> {
                        if (v == null) return false;
                        if (v instanceof Collection) {
                            return !((Collection<?>) v).isEmpty();
                        }
                        if (v instanceof String) {
                            return ObjectUtil.isNotBlank((String) v);
                        }
                        return true;
                    },
                    it -> it,
                    it -> encode ? encodeUrl(it.toString()) : it.toString()));
        }
        if (statement != null) {
            sb.append("#").append(statement);
        }
        return sb.toString();
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

    public static String toSortedQueryString(Map<String, Object> queryMap) {
        return toSortedQueryString(queryMap, (k, v) -> ObjectUtil.isNotBlank(k));
    }

    public static String toSortedQueryString(
            Map<String, Object> queryMap,
            BiPredicate<String, Object> filterKeyValue) {
        return toSortedQueryString(
                queryMap,
                filterKeyValue,
                it -> it, Object::toString);
    }

    public static String toSortedQueryString(
            Map<String, Object> queryMap,
            Function<String, String> paramKey,
            Function<Object, String> paramValue) {
        return toSortedQueryString(
                queryMap,
                (k, v) -> ObjectUtil.isNotBlank(k),
                paramKey, paramValue);
    }

    public static String toSortedQueryString(
            Map<String, Object> queryMap,
            BiPredicate<String, Object> filterKeyValue,
            Function<String, String> paramKey,
            Function<Object, String> paramValue) {
        return queryMap.entrySet().stream()
                .filter(entry -> filterKeyValue.test(entry.getKey(), entry.getValue()))
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

    public static InputStream request(
            String url,
            String method,
            InputStream source,
            Map<String, String> headers,
            Map<String, Object> queryMap) throws IOException {
        return request(url, method, TIMEOUT, TIMEOUT, source, headers, queryMap);
    }

    public static InputStream request(
            String url,
            String method,
            int connectTimeout,
            int readTimeout,
            InputStream source,
            Map<String, String> headers,
            Map<String, Object> queryMap) throws IOException {
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
