package org.dreamcat.common.core;

import org.dreamcat.common.io.IOUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

public interface Wget<Req, Res> {

    default Req prepare(String url, String method) {
        return prepare(url, method, null);
    }

    Req prepare(String url, String method, Map<String, String> headers);

    Req prepare(String url, String method, Map<String, String> headers, String body);

    Req prepare(String url, String method, Map<String, String> headers, byte[] body);

    Res request(Req req) throws IOException;

    default String string(Res res) throws IOException {
        try (Reader reader = reader(res)) {
            return IOUtil.readAsString(reader);
        }
    }

    default byte[] bytes(Res res) throws IOException {
        try (InputStream inputStream = inputStream(res)) {
            return IOUtil.readFully(inputStream);
        }
    }

    Reader reader(Res res) throws IOException;

    InputStream inputStream(Res res) throws IOException;

    default void save(Res res, String file) throws IOException {
        save(res, new File(file));
    }

    void save(Res res, File file) throws IOException;

    default void download(String url, String file) throws IOException {
        download(url, new File(file));
    }

    default void download(String url, File file) throws IOException {
        download(url, file, null);
    }

    default void download(String url, File file, Map<String, String> headers) throws IOException {
        Req req = prepare(url, "GET", headers);
        Res res = request(req);
        save(res, file);
    }
}
