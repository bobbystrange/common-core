package org.dreamcat.java.lang;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.ObjectUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Create by tuke on 2020/5/29
 */
@Slf4j
public class NetworkClassLoader extends ClassLoader {
    private final String baseUrl;

    public NetworkClassLoader(String baseUrl) {
        if (ObjectUtil.requireNotBlank(baseUrl, "baseUrl").endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        this.baseUrl = baseUrl;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String path = baseUrl + "/" + name.replace(".", "/") + ".class";

        byte[] classBytes;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             InputStream is = new URL(path).openConnection().getInputStream()) {
            byte[] buf = new byte[4096];
            int readSize;
            while ((readSize = is.read(buf)) > 0) {
                baos.write(buf, 0, readSize);
            }
            classBytes = baos.toByteArray();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ClassNotFoundException();
        }
        return defineClass(name, classBytes, 0, classBytes.length);
    }

}
