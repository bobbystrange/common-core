package org.dreamcat.common.script;

import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import org.dreamcat.common.util.ObjectUtil;

/**
 * Create by tuke on 2021/3/23
 */
@SuppressWarnings({"unchecked"})
public class DelegateScriptEngine implements ScriptEngine, Invocable {

    ScriptEngine scriptEngine;

    public DelegateScriptEngine() {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

        scriptEngine = scriptEngineManager.getEngineByName("graal.js");
        if (scriptEngine == null) {
            // only available in JDK 8-14
            scriptEngine = scriptEngineManager.getEngineByName("nashorn");
        }
    }

    public <T> T eval(String script, Map<String, Object> context) throws ScriptException {
        SimpleScriptContext scriptContext = new SimpleScriptContext();
        if (ObjectUtil.isNotEmpty(context)) {
            context.forEach((k, v) -> scriptContext.setAttribute(
                    k, v, ScriptContext.ENGINE_SCOPE));
        }
        return (T) eval(script, scriptContext);
    }

    public <T> T evalMultiKey(String script, Map<List<String>, Object> context)
            throws ScriptException {
        SimpleScriptContext scriptContext = new SimpleScriptContext();
        if (ObjectUtil.isNotEmpty(context)) {
            context.forEach((keys, v) -> keys.forEach(k ->
                    scriptContext.setAttribute(
                            k, v, ScriptContext.ENGINE_SCOPE)));
        }
        return (T) eval(script, scriptContext);
    }

    public <T> T evalWith(String script, Object... args) throws ScriptException {
        return evalWithVariablePrefix(script, VARIABLE_PREFIX, args);
    }

    public <T> T evalWithVariablePrefix(String script, String variablePrefix, Object... args)
            throws ScriptException {
        return evalWithVariablePrefix(script, Collections.singletonList(variablePrefix), args);
    }

    public <T> T evalWithVariablePrefix(String script, List<String> prefix, Object... args)
            throws ScriptException {
        int size = prefix.size() * args.length;
        if (size == 0) {
            return (T) eval(script);
        }
        Map<String, Object> context = new HashMap<>(size);
        for (String s : prefix) {
            int i = 0;
            for (Object arg : args) {
                context.put(s + (++i), arg);
            }
        }
        if (args.length == 1) {
            context.put(DEFAULT_ONE_PARAM, args[0]);
        }
        return eval(script, context);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    @Override
    public Object invokeMethod(Object thiz, String name, Object... args)
            throws ScriptException, NoSuchMethodException {
        return ((Invocable) scriptEngine).invokeMethod(thiz, name, args);
    }

    @Override
    public Object invokeFunction(String name, Object... args)
            throws ScriptException, NoSuchMethodException {
        return ((Invocable) scriptEngine).invokeFunction(name, args);
    }

    @Override
    public <T> T getInterface(Class<T> clazz) {
        return ((Invocable) scriptEngine).getInterface(clazz);
    }

    @Override
    public <T> T getInterface(Object thiz, Class<T> clazz) {
        return ((Invocable) scriptEngine).getInterface(thiz, clazz);
    }

    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        return scriptEngine.eval(script, context);
    }

    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        return scriptEngine.eval(reader, context);
    }

    @Override
    public Object eval(String script) throws ScriptException {
        return scriptEngine.eval(script);
    }

    @Override
    public Object eval(Reader reader) throws ScriptException {
        return scriptEngine.eval(reader);
    }

    @Override
    public Object eval(String script, Bindings n) throws ScriptException {
        return scriptEngine.eval(script, n);
    }

    @Override
    public Object eval(Reader reader, Bindings n) throws ScriptException {
        return scriptEngine.eval(reader, n);
    }

    @Override
    public void put(String key, Object value) {
        scriptEngine.put(key, value);
    }

    @Override
    public Object get(String key) {
        return scriptEngine.get(key);
    }

    @Override
    public Bindings getBindings(int scope) {
        return scriptEngine.getBindings(scope);
    }

    @Override
    public void setBindings(Bindings bindings, int scope) {
        scriptEngine.setBindings(bindings, scope);
    }

    @Override
    public Bindings createBindings() {
        return scriptEngine.createBindings();
    }

    @Override
    public ScriptContext getContext() {
        return scriptEngine.getContext();
    }

    @Override
    public void setContext(ScriptContext context) {
        scriptEngine.setContext(context);
    }

    @Override
    public ScriptEngineFactory getFactory() {
        return scriptEngine.getFactory();
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private static final List<String> VARIABLE_PREFIX = Arrays.asList(
            "$", "_", "a", "arg", "argument", "p", "param", "parameter", "v", "var", "variable");
    private static final String DEFAULT_ONE_PARAM = "it";
}

/*
ext {
    // https://mvnrepository.com/artifact/org.graalvm.js/js
    graalvmVersion = '21.0.0.2'
}

dependencies {
    implementation "org.graalvm.js:js:$graalvmVersion"
    implementation "org.graalvm.js:js-scriptengine:$graalvmVersion"
}
*/
