package org.dreamcat.common.core;

import org.dreamcat.common.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

public interface Wget<Req, Resp> {

    @Nullable
    Req prepare(String url, String method,
                @Nullable Map<String, String> headers, @Nullable String body);

    @Nullable
    Req prepare(String url, String method,
                @Nullable Map<String, String> headers, @Nullable byte[] body);

    Resp request(Req req) throws IOException;

    void save(Resp resp, String path) throws IOException;

    String string(Resp resp) throws IOException;

    byte[] bytes(Resp resp) throws IOException;

    Reader reader(Resp resp) throws IOException;

    InputStream inputStream(Resp resp) throws IOException;
}
