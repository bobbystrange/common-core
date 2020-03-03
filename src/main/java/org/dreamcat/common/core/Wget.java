package org.dreamcat.common.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

public interface Wget<Req, Res> {

    Req prepare(String url, String method,
                Map<String, String> headers, String body);

    Req prepare(String url, String method,
                Map<String, String> headers, byte[] body);

    Res request(Req req) throws IOException;

    void save(Res res, String path) throws IOException;

    String string(Res res) throws IOException;

    byte[] bytes(Res res) throws IOException;

    Reader reader(Res res) throws IOException;

    InputStream inputStream(Res res) throws IOException;
}
