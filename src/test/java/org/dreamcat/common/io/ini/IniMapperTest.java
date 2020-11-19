package org.dreamcat.common.io.ini;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.dreamcat.common.bean.BeanUtil;
import org.dreamcat.common.core.Timeit;
import org.junit.Test;

/**
 * Create by tuke on 2020/5/5
 */
public class IniMapperTest {

    // ini is much faster then json
    @Test
    public void testSpeed() {
        System.out.println("\t \t jackson\t ini");
        for (int i = 1; i < 1024; i *= 2) {
            String ts = Timeit.ofActions()
                    .addUnaryAction(this::json, it -> {
                        ObjectMapper mapper = new ObjectMapper();
                        Json ignore = mapper.readValue(it, Json.class);
                    })
                    .addUnaryAction(this::ini, it -> {
                        IniMapper iniMapper = IniMapper.newInstance()
                                .richText(true);
                        iniMapper.loadFrom(it);
                        Ini ignore = BeanUtil.fromMap(iniMapper.getSections(), Ini.class);
                    })
                    .count(20)
                    .skip(4)
                    .repeat(i)
                    .runAndFormatUs();
            System.out.printf("%4d \t %s\n", i, ts);
        }
    }

    private String json() {

        return "{\n" +
                "    \"c\": {\n" +
                "        \"a\": 909,\n" +
                "        \"b\": \"Let it be\"\n" +
                "    },\n" +
                "    \"d\": {\n" +
                "        \"a\": 3,\n" +
                "        \"b\": \"Lennon\"\n" +
                "    }\n" +
                "}";
    }

    private String ini() {
        return "[c]\n" +
                "a = 909\n" +
                "b = Let it be\n" +
                "[d]\n" +
                "a = 3\n" +
                "b = Lennon";
    }

    @Getter
    @Setter
    public static class Json {

        Base c;
        Base d;
    }

    @Data
    public static class Ini {

        Base c;
        Base d;
    }

    @Getter
    @Setter
    public static class Base {

        int a;
        String b;
    }


}
/*
{
    "c": {
        "a": 909,
        "b": "Let it be"
    },
    "d": {
        "a": 3,
        "b": "Lennon"
    }
}

[default]
a = 1
b = John
[c]
a = 909
b = Let it be
[d]
a = 3
b = Lennon
 */
