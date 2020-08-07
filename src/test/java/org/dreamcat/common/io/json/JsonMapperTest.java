package org.dreamcat.common.io.json;

import org.dreamcat.common.core.Pair;
import org.junit.Test;

import java.util.Map;

import static org.dreamcat.common.util.FormatUtil.println;

/**
 * Create by tuke on 2020/5/10
 */
@SuppressWarnings("unchecked")
public class JsonMapperTest {

    @Test
    public void testExtractNumber() {
        String expression = "6.02e-23";
        int len = expression.length();
        StringBuilder s = new StringBuilder();
        Pair<Integer, Boolean> pair = JsonMapper.extractNumber(expression, 0, len, s);
        int offset = pair.first();
        println(s.toString());
    }

    @Test
    public void test() {
        String json;
        Object raw;
        Map<String, Object> object;

        json = "{\"a\": \"123\", \"b\": {\"c\": [1, 2]}}";
        println(json);
        println();
        raw = JsonMapper.parse(json);
        object = (Map<String, Object>) raw;
        println(JsonMapper.stringify(object));
        println();
    }

    @Test
    public void test2() {
        String json;
        Object raw;
        Map<String, Object> object;

        json = "{\n" +
                "\"s\": \"string\", \n" +
                "\"ta\": [false, null, true], \n" +
                "\"n\": 3.14 \n" +
                "}";
        println(json);
        println();
        raw = JsonMapper.parse(json);
        object = (Map<String, Object>) raw;
        println(JsonMapper.stringify(object));
        println();
    }

}
