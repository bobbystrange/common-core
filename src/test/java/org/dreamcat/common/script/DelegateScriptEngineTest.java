package org.dreamcat.common.script;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.script.ScriptException;
import org.junit.Test;

/**
 * Create by tuke on 2021/3/23
 */
public class DelegateScriptEngineTest {

    DelegateScriptEngine scriptEngine = new DelegateScriptEngine();

    @Test
    public void test() throws ScriptException {
        String script = "'target_' + it.substr('source_'.length)";
        Map<List<String>, Object> context = Collections.singletonMap(
                DEFAULT_PARAMS, "source_xxx");
        String result = scriptEngine.evalMultiKey(script, context);
        System.out.println(result);

    }

    private static final List<String> DEFAULT_PARAMS = Arrays.asList(
            "$1", "_1", "it", "a1", "p1", "arg1", "param1", "parameter1");
}
