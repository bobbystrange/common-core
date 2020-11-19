package org.dreamcat.common.util;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * Create by tuke on 2019-05-09
 */
public class JavaCompilerUtil {

    public static Class<?> fromSourceCode(String className, String sourceCode)
            throws ClassNotFoundException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager jfm = compiler.getStandardFileManager(
                null, null, null);

        JavaFileObject jfo = new StringJavaObject(className, sourceCode);
        JavaCompiler.CompilationTask task = compiler.getTask(
                null, jfm, null,
                Collections.emptyList(), null, Collections.singletonList(jfo));
        task.call();
        return Class.forName(className);
    }

    private static class StringJavaObject extends SimpleJavaFileObject {

        private String content = "";

        public StringJavaObject(String javaFileName, String content) {
            super(createStringJavaObjectUri(javaFileName), Kind.SOURCE);
            this.content = content;
        }

        private static URI createStringJavaObjectUri(String name) {
            return URI.create("String:///" + name + Kind.SOURCE.extension);
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return content;
        }
    }

}
