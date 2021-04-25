package org.dreamcat.java.lang;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import org.junit.Test;

/**
 * Create by tuke on 2020/5/29
 */
@SuppressWarnings({"unchecked"})
public class ClassLoaderTest {

    // -Xbootclasspath, jre/lib/*.jar, jre/classes/*.class
    @Test
    public void testBootStrapClassLoader() {
        // URL[] urls = Launcher.getBootstrapClassPath().getURLs();
        Object aURLClassPath = invoke(null, "sun.misc.Launcher", "getBootstrapClassPath");
        assert  aURLClassPath != null;
        URL[] urls = invoke(aURLClassPath, aURLClassPath.getClass().getName(), "getURLs");
        assert  urls != null;
        for (URL url : urls) {
            System.out.println(url.toExternalForm());
        }
        System.out.println();
        String bootClassPath = System.getProperty("sun.boot.class.path");
        Arrays.stream(bootClassPath.split(":")).forEach(System.out::println);

        // ClassLoader appClassLoader = Launcher.getLauncher().getClassLoader();
        Object aLauncher = invoke(null, "sun.misc.Launcher", "getLauncher");
        assert aLauncher != null;
        ClassLoader appClassLoader = invoke(aLauncher, aLauncher.getClass().getName(), "getClassLoader");
        assert appClassLoader != null;

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
    public void testCustomClassLoader() throws Exception {
        // sun.misc.Launcher$AppClassLoader
        // sun.misc.Launcher$ExtClassLoader
        // null
        printClassLoader(ClassLoaderTest.class);

        // sun.misc.Launcher$ExtClassLoader
        // null
        printClassLoader(Class.forName("jdk.internal.dynalink.DynamicLinker.class"));

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

    private static <T> T invoke(
            Object object,
            String className, String methodName, Class<?>... parameterTypes) {
        try {
            return (T) Class.forName(className).getDeclaredMethod(methodName, parameterTypes).invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
