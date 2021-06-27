package org.dreamcat.common.io.json;

import java.util.Map;
import org.dreamcat.common.Pair;
import org.dreamcat.common.text.NumericSearcher;
import org.junit.Test;

/**
 * Create by tuke on 2020/5/10
 */
@SuppressWarnings("unchecked")
public class JsonMapperTest {

    @Test
    public void testExtractNumber() {
        String expression = "6.02e-23";
        Pair<Number, Integer> pair = NumericSearcher.extractNumber(expression, 0);
        System.out.println(pair);
    }

    @Test
    public void test() {
        String json;
        Object raw;
        Map<String, Object> object;

        json = "{\"a\": \"123\", \"b\": {\"c\": [1, 2]}}";
        System.out.println(json);
        System.out.println();
        raw = JsonMapper.parse(json);
        object = (Map<String, Object>) raw;
        System.out.println(JsonMapper.stringify(object));
        System.out.println();
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
        System.out.println(json);
        System.out.println();
        raw = JsonMapper.parse(json);
        object = (Map<String, Object>) raw;
        System.out.println(JsonMapper.stringify(object));
        System.out.println();
    }

}
