package org.dreamcat.common.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.Map;

public interface Wget<Req, Resp> {

    String APPLICATION_XML_UTF_8 = "application/xml; charset=UTF-8";
    String APPLICATION_JSON_UTF_8 = "application/json; charset=UTF-8";
    String APPLICATION_OCTET_STREAM = "application/octet-stream";
    String TEXT_PLAIN_UTF_8 = "text/plain; charset=UTF-8";
    int DEFAULT_BUFFER_SIZE = 4096;

    default Req prepare(String url, String method) {
        return prepare(url, method, null);
    }

    Req prepare(String url, String method, Map<String, String> headers);

    default Req prepare(String url, String method, Map<String, String> headers, String body) {
        return prepare(url, method, headers, body, TEXT_PLAIN_UTF_8);
    }

    Req prepare(
            String url, String method, Map<String, String> headers,
            String body, String contentType);

    default Req prepare(String url, String method, Map<String, String> headers, byte[] body) {
        return prepare(url, method, headers, body, APPLICATION_OCTET_STREAM);
    }

    Req prepare(
            String url, String method, Map<String, String> headers,
            byte[] body, String contentType);

    Resp request(Req request) throws IOException;

    /**
     * expect to override it using <strong>Content-Length & Content-Type</strong>
     *
     * @param response http response
     * @return response body
     * @throws IOException I/O error
     */
    String string(Resp response) throws IOException;

    /**
     * expect to override it using <strong>Content-Length</strong>
     *
     * @param response http response
     * @return response body
     * @throws IOException I/O error
     */
    byte[] bytes(Resp response) throws IOException;

    Reader reader(Resp response) throws IOException;

    InputStream inputStream(Resp response) throws IOException;

    default boolean save(Resp response, String file) throws IOException {
        return save(response, new File(file));
    }

    default boolean save(Resp response, File file) throws IOException {
        try (InputStream ins = inputStream(response)) {
            if (ins == null) {
                return false;
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
                int readSize;
                while ((readSize = ins.read(buf)) > 0) {
                    fos.write(buf, 0, readSize);
                }
                return true;
            }
        }
    }

    default boolean saveTo(Resp response, String dir) throws IOException {
        return saveTo(response, new File(dir));
    }

    boolean saveTo(Resp response, File dir) throws IOException;

    default boolean download(String url, String file) throws IOException {
        return download(url, new File(file));
    }

    default boolean download(String url, File file) throws IOException {
        return download(url, file, null);
    }

    default boolean download(String url, File file, Map<String, String> headers)
            throws IOException {
        Req req = prepare(url, "GET", headers);
        Resp res = request(req);
        return save(res, file);
    }

    default boolean downloadTo(String url, String dir) throws IOException {
        return downloadTo(url, new File(dir));
    }

    default boolean downloadTo(String url, File dir) throws IOException {
        return downloadTo(url, dir, null);
    }

    default boolean downloadTo(String url, File dir, Map<String, String> headers)
            throws IOException {
        Req req = prepare(url, "GET", headers);
        Resp res = request(req);
        return saveTo(res, dir);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    default Resp request(String url, String method) throws IOException {
        return request(url, method, null);
    }

    default Resp request(String url, String method, Map<String, String> headers)
            throws IOException {
        Req request = prepare(url, method, headers);
        return request(request);
    }

    default Resp requestString(String url, String method, String body, String contentType)
            throws IOException {
        return requestString(url, method, null, body, contentType);
    }

    default Resp requestString(
            String url, String method, Map<String, String> headers,
            String body, String contentType) throws IOException {
        if (headers != null) {
            if (!headers.containsKey("Content-Type") && !headers.containsKey("content-type")) {
                headers.put("Content-Type", contentType);
            }
        } else {
            headers = Collections.singletonMap("Content-Type", contentType);
        }

        Req request = prepare(url, method, headers, body);
        return request(request);
    }

    default Resp requestJSON(String url, String method, String body) throws IOException {
        return requestJSON(url, method, null, body);
    }

    default Resp requestJSON(String url, String method, Map<String, String> headers, String body)
            throws IOException {
        return requestString(url, method, headers, body, APPLICATION_JSON_UTF_8);
    }

    default Resp requestXML(String url, String method, String body) throws IOException {
        return requestXML(url, method, null, body);
    }

    default Resp requestXML(String url, String method, Map<String, String> headers, String body)
            throws IOException {
        return requestString(url, method, headers, body, APPLICATION_XML_UTF_8);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    void requestAsync(Req request, Callback<Req, Resp> callback);

    default void requestAsync(String url, String method, Callback<Req, Resp> callback) {
        requestAsync(url, method, null, callback);
    }

    default void requestAsync(
            String url, String method, Map<String, String> headers,
            Callback<Req, Resp> callback) {
        Req request = prepare(url, method, headers);
        requestAsync(request, callback);
    }

    default void requestStringAsync(
            String url, String method, String body, String contentType,
            Callback<Req, Resp> callback) {
        requestStringAsync(url, method, null, body, contentType, callback);
    }

    default void requestStringAsync(
            String url, String method, Map<String, String> headers,
            String body, String contentType, Callback<Req, Resp> callback) {
        if (headers != null) {
            if (!headers.containsKey("Content-Type") && !headers.containsKey("content-type")) {
                headers.put("Content-Type", contentType);
            }
        } else {
            headers = Collections.singletonMap("Content-Type", contentType);
        }

        Req request = prepare(url, method, headers, body);
        requestAsync(request, callback);
    }

    default void requestJSONAsync(
            String url, String method, String body,
            Callback<Req, Resp> callback) {
        requestJSONAsync(url, method, null, body, callback);
    }

    default void requestJSONAsync(
            String url, String method, Map<String, String> headers, String body,
            Callback<Req, Resp> callback) {
        requestStringAsync(url, method, headers, body, APPLICATION_JSON_UTF_8, callback);
    }

    default void requestXMLAsync(String url, String method, String body,
            Callback<Req, Resp> callback) {
        requestXMLAsync(url, method, null, body, callback);
    }

    default void requestXMLAsync(String url, String method, Map<String, String> headers,
            String body, Callback<Req, Resp> callback) {
        requestStringAsync(url, method, headers, body, APPLICATION_XML_UTF_8, callback);
    }

    default Resp get(String url) throws IOException {
        return get(url, null);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    default Resp get(String url, Map<String, String> headers) throws IOException {
        return request(url, "GET", headers);
    }

    default void getAsync(String url, Callback<Req, Resp> callback) {
        getAsync(url, null, callback);
    }

    default void getAsync(String url, Map<String, String> headers, Callback<Req, Resp> callback) {
        requestAsync(url, "GET", headers, callback);
    }

    default Resp post(String url) throws IOException {
        return post(url, null);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    default Resp post(String url, Map<String, String> headers) throws IOException {
        return request(url, "POST", headers);
    }

    default void postAsync(String url, Callback<Req, Resp> callback) {
        postAsync(url, null, callback);
    }

    default void postAsync(String url, Map<String, String> headers, Callback<Req, Resp> callback) {
        requestAsync(url, "POST", headers, callback);
    }

    default Resp postJSON(String url, String body) throws IOException {
        return postJSON(url, null, body);
    }

    default Resp postJSON(String url, Map<String, String> headers, String body) throws IOException {
        return requestJSON(url, "POST", headers, body);
    }

    default void postJSONAsync(String url, String body, Callback<Req, Resp> callback) {
        postJSONAsync(url, null, body, callback);
    }

    default void postJSONAsync(String url, Map<String, String> headers, String body,
            Callback<Req, Resp> callback) {
        requestJSONAsync(url, "POST", headers, body, callback);
    }

    default Resp postXML(String url, String body) throws IOException {
        return postXML(url, null, body);
    }

    default Resp postXML(String url, Map<String, String> headers, String body) throws IOException {
        return requestXML(url, "POST", headers, body);
    }

    default void postXMLAsync(String url, String body, Callback<Req, Resp> callback) {
        postXMLAsync(url, null, body, callback);
    }

    default void postXMLAsync(String url, Map<String, String> headers, String body,
            Callback<Req, Resp> callback) {
        requestXMLAsync(url, "POST", headers, body, callback);
    }

    // application/x-www-form-urlencoded
    default Resp postForm(String url, Map<String, String> form) throws IOException {
        return postForm(url, null, form);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    Resp postForm(String url, Map<String, String> headers, Map<String, String> form)
            throws IOException;

    // multipart/form-data
    default Resp postFormData(String url, Map<String, Object> formData) throws IOException {
        return postFormData(url, null, formData);
    }

    Resp postFormData(String url, Map<String, String> headers, Map<String, Object> formData)
            throws IOException;

    default Resp put(String url) throws IOException {
        return put(url, null);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    default Resp put(String url, Map<String, String> headers) throws IOException {
        return request(url, "PUT", headers);
    }

    default void putAsync(String url, Callback<Req, Resp> callback) {
        putAsync(url, null, callback);
    }

    default void putAsync(String url, Map<String, String> headers, Callback<Req, Resp> callback) {
        requestAsync(url, "PUT", headers, callback);
    }

    default Resp putJSON(String url, String body) throws IOException {
        return putJSON(url, null, body);
    }

    default Resp putJSON(String url, Map<String, String> headers, String body) throws IOException {
        return requestJSON(url, "PUT", headers, body);
    }

    default void putJSONAsync(String url, String body, Callback<Req, Resp> callback) {
        putJSONAsync(url, null, body, callback);
    }

    default void putJSONAsync(String url, Map<String, String> headers, String body,
            Callback<Req, Resp> callback) {
        requestJSONAsync(url, "PUT", headers, body, callback);
    }

    default Resp putXML(String url, String body) throws IOException {
        return putXML(url, null, body);
    }

    default Resp putXML(String url, Map<String, String> headers, String body) throws IOException {
        return requestXML(url, "PUT", headers, body);
    }

    default void putXMLAsync(String url, String body, Callback<Req, Resp> callback) {
        putXMLAsync(url, null, body, callback);
    }

    default void putXMLAsync(String url, Map<String, String> headers, String body,
            Callback<Req, Resp> callback) {
        requestXMLAsync(url, "PUT", headers, body, callback);
    }

    default Resp delete(String url) throws IOException {
        return delete(url, null);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    default Resp delete(String url, Map<String, String> headers) throws IOException {
        return request(url, "DELETE", headers);
    }

    default void deleteAsync(String url, Callback<Req, Resp> callback) {
        deleteAsync(url, null, callback);
    }

    default void deleteAsync(String url, Map<String, String> headers,
            Callback<Req, Resp> callback) {
        requestAsync(url, "DELETE", headers, callback);
    }

    default Resp deleteJSON(String url, String body) throws IOException {
        return deleteJSON(url, null, body);
    }

    default Resp deleteJSON(String url, Map<String, String> headers, String body)
            throws IOException {
        return requestJSON(url, "DELETE", headers, body);
    }

    default void deleteJSONAsync(String url, String body, Callback<Req, Resp> callback) {
        deleteJSONAsync(url, null, body, callback);
    }

    default void deleteJSONAsync(String url, Map<String, String> headers, String body,
            Callback<Req, Resp> callback) {
        requestJSONAsync(url, "DELETE", headers, body, callback);
    }

    default Resp deleteXML(String url, String body) throws IOException {
        return deleteXML(url, null, body);
    }

    default Resp deleteXML(String url, Map<String, String> headers, String body)
            throws IOException {
        return requestXML(url, "DELETE", headers, body);
    }

    default void deleteXMLAsync(String url, String body, Callback<Req, Resp> callback) {
        deleteXMLAsync(url, null, body, callback);
    }

    default void deleteXMLAsync(String url, Map<String, String> headers, String body,
            Callback<Req, Resp> callback) {
        requestXMLAsync(url, "DELETE", headers, body, callback);
    }

    interface Callback<Req, Resp> {

        void onError(Req request, Exception e);

        void onComplete(Req request, Resp response);
    }

}
