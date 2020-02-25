package org.dreamcat.common.core;

import org.dreamcat.common.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

public interface Wget<Req, Res> {

    @Nullable
    Req prepare(String url, String method,
                @Nullable Map<String, String> headers, @Nullable String body);

    @Nullable
    Req prepare(String url, String method,
                @Nullable Map<String, String> headers, @Nullable byte[] body);

    Res request(Req req) throws IOException;

    void save(Res res, String path) throws IOException;

    String string(Res res) throws IOException;

    byte[] bytes(Res res) throws IOException;

    Reader reader(Res res) throws IOException;

    InputStream inputStream(Res res) throws IOException;
}
