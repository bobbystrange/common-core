package org.dreamcat.java.lang;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import org.junit.Test;
import sun.misc.Launcher;

/**
 * Create by tuke on 2020/5/29
 */
public class ClassLoaderTest {

    // -Xbootclasspath, jre/lib/*.jar, jre/classes/*.class
    @Test
    public void testBootStrapClassLoader() {
        URL[] urls = Launcher.getBootstrapClassPath().getURLs();
        for (URL url : urls) {
            System.out.println(url.toExternalForm());
        }
        System.out.println();
        String bootClassPath = System.getProperty("sun.boot.class.path");
        Arrays.stream(bootClassPath.split(":")).forEach(System.out::println);

        ClassLoader appClassLoader = Launcher.getLauncher().getClassLoader();
        // the app class loader
        ClassLoader extClassLoader = appClassLoader.getParent();
        assert extClassLoader.getParent() == null;
    }

    // -Djava.ext.dirs, jre/lib/ext/*.jar
    @Test
    public void testExtensionClassLoader() {
        String extClassPath = System.getProperty("java.ext.dirs");
        Arrays.stream(extClassPath.split(":")).forEach(System.out::println);
    }

    // $CLASSPATH, -Djava.class.path
    @Test
    public void testAppClassLoader() {
        String appClassPath = System.getProperty("java.class.path");
        Arrays.stream(appClassPath.split(":")).forEach(System.out::println);
        System.out.println();

        ClassLoader appClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println(appClassLoader);

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(contextClassLoader);
        assert contextClassLoader == appClassLoader;
    }

    // extends ClassLoader
    @Test
    public void testCustomClassLoader() {
        // sun.misc.Launcher$AppClassLoader
        // sun.misc.Launcher$ExtClassLoader
        // null
        printClassLoader(ClassLoaderTest.class);

        // sun.misc.Launcher$ExtClassLoader
        // null
        printClassLoader(jdk.internal.dynalink.DynamicLinker.class);

        // null
        printClassLoader(javax.tools.JavaCompiler.class);

    }

    private void printClassLoader(Class<?> clazz) {
        System.out.println("# " + clazz);
        ClassLoader loader = clazz.getClassLoader();
        while (true) {
            if (loader instanceof URLClassLoader) {
                System.out.printf("%s extends %s\n", loader.getClass(),
                        URLClassLoader.class.getCanonicalName());
            } else {
                System.out.println(loader);
            }
            if (loader == null) break;
            loader = loader.getParent();
        }
        System.out.println();
    }

}
