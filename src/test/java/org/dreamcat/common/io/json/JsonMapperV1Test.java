package org.dreamcat.common.io.json;

import org.dreamcat.common.core.Timeit;
import org.dreamcat.common.util.StringUtil;
import org.junit.Test;

import java.util.Map;

import static org.dreamcat.common.util.PrintUtil.printf;
import static org.dreamcat.common.util.PrintUtil.println;

/**
 * Create by tuke on 2020/5/7
 */
@SuppressWarnings("unchecked")
public class JsonMapperV1Test {

    private static final String no_nested = "{\n" +
            "    \"c\": {\n" +
            "        \"a\": 909,\n" +
            "        \"b\": \"Let it be\"\n" +
            "    },\n" +
            "    \"d\": {\n" +
            "        \"a\": 3,\n" +
            "        \"b\": \"Lennon\"\n" +
            "    }\n" +
            "}";

    @Test
    public void test() {
        String json;
        Object raw;
        Map<String, Object> object;

        json = "{\"a\": \"123\", \"b\": {\"c\": [1, 2]}}";
        println(json);
        println();
        raw = JsonMapperV1.parse(json);
        object = (Map<String, Object>) raw;
        println(JsonMapperV1.stringify(object));
        println();

        ///

        json = no_nested;
        println(json);
        println();
        raw = JsonMapperV1.parse(json);
        object = (Map<String, Object>) raw;
        println(JsonMapperV1.stringify(object));
    }

    @Test
    public void testSpeed() {
        println("\t\t\t copy \t\t zero copy");
        for (int i = 1; i <= 1024; i *= 2) {
            for (int k = 10; k <= 20; k++) {
                int finalK = k;
                String ts = Timeit.ofActions()
                        .addUnaryAction(() -> json(finalK), it -> {
                            Object ignore = JsonMapperV1.parse(it);
                        })
                        .addUnaryAction(() -> json(finalK), it -> {
                            Object ignore = JsonZeroCopyMapper.parse(it);
                        })
                        .count(10).skip(2).repeat(i)
                        .runAndFormatUs();
                printf("%4d %2d \t %s\n", i, k, ts);
            }
        }
    }

    private String json(int nestedLevel) {
        if (nestedLevel == 1) return no_nested;

        String s = json(nestedLevel - 1);
        String a = String.join(", ", StringUtil.repeatArray("1", nestedLevel));
        return String.format("{\"a\": %s, \"b\": [%s]}", s, a);
    }

}
