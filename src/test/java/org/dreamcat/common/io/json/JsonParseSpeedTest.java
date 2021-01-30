package org.dreamcat.common.io.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.util.function.IntFunction;
import org.dreamcat.common.core.Timeit;
import org.dreamcat.common.util.StringUtil;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Create by tuke on 2020/5/7
 */
@Ignore
public class JsonParseSpeedTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Gson gson = new Gson();
    private static final String no_nested = "{\n" +
            "\"s\": \"string\", \n" +
            "\"ta\": [false, null, true], \n" +
            "\"n\": 3.1415e-23 \n" +
            "}";

    @Test
    public void testParse() {
        parse(this::jsonGen, 10, 10, 10);
    }

    @Test
    public void testStringify() {
        stringify(this::jsonGen, 10, 10);
    }

    @Test
    public void testParseSmallJson() {
        parse(this::jsonGen, 17, 1, 1);
    }

    @Test
    public void testStringifySmallJson() {
        stringify(this::jsonGen, 17, 1);
    }

    public void parse(IntFunction<String> jsonGen, int bound, int levelStart, int levelEnd) {
        System.out.println("\t\t\t for-each \t jackson\t\t gson\t common");
        for (int i = 1; i < (1 << bound); i *= 2) {
            for (int k = levelStart; k <= levelEnd; k++) {
                int finalK = k;
                String ts = Timeit.ofActions()
                        .addUnaryAction(() -> jsonGen.apply(finalK), it -> {
                            for (int x = 0, len = it.length(); x < len; x++) {
                                char c = it.charAt(x);
                            }
                        })
                        .addUnaryAction(() -> jsonGen.apply(finalK), it -> {
                            JsonNode ignore = objectMapper.readTree(it);
                        })
                        .addUnaryAction(() -> jsonGen.apply(finalK), it -> {
                            JsonElement ignore = gson.fromJson(it, JsonElement.class);
                        })
                        .addUnaryAction(() -> jsonGen.apply(finalK), it -> {
                            Object ignore = JsonMapper.parse(it);
                        })
                        .count(10).skip(2).repeat(i)
                        .runAndFormatUs();
                System.out.printf("%4d %2d \t %s\n", i, k, ts);
            }
        }
    }

    public void stringify(IntFunction<String> jsonGen, int bound, int level) {
        System.out.println("\t\t\t jackson\t\t gson\t common slow \t ");
        for (int i = 1; i < (1 << bound); i *= 2) {
            for (int k = 1; k <= level; k++) {
                int finalK = k;
                String ts = Timeit.ofActions()
                        .addUnaryAction(() -> objectMapper.readTree(jsonGen.apply(finalK)), it -> {
                            String ignore = objectMapper.writeValueAsString(it);
                        })
                        .addUnaryAction(
                                () -> gson.fromJson(jsonGen.apply(finalK), JsonElement.class),
                                it -> {
                                    String ignore = gson.toJson(it);
                                })
                        .addUnaryAction(() -> JsonMapper.parse(jsonGen.apply(finalK)), it -> {
                            @SuppressWarnings("deprecation")
                            String ignore = JsonMapper.stringify(it);
                        })
                        .count(10).skip(2).repeat(i)
                        .runAndFormatUs();
                System.out.printf("%4d %2d \t %s\n", i, k, ts);
            }
        }
    }

    private String jsonGen(int nestedLevel) {
        if (nestedLevel == 1) return no_nested;

        String s = jsonGen(nestedLevel - 1);
        String a = String.join(", ", StringUtil.repeatArray("null", nestedLevel));
        return String.format("{\"o\": %s, \"a\": [%s]}", s, a);
    }
}
