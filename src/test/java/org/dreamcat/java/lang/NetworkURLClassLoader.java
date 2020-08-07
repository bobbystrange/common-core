package org.dreamcat.java.lang;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Create by tuke on 2020/5/29
 */
public class NetworkURLClassLoader extends URLClassLoader {

    public NetworkURLClassLoader(String baseUrl) throws MalformedURLException {
        super(new URL[]{new URL(baseUrl)});
    }

}
