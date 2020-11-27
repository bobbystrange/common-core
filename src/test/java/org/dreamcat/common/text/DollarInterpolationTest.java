package org.dreamcat.common.text;

import java.util.Map;
import org.dreamcat.common.util.CollectionUtil;
import org.junit.Test;

/**
 * Create by tuke on 2020/11/18
 */
public class DollarInterpolationTest {

    @Test
    public void test() {
        Map<String, String> context;
        String formatted;
        String template = "$$ $ $a $a_Y_0 $_b_Z ${10:01} ${10:01:02} ${:::} ${{\\:\\}\\\\:{\\:\\}\\\\}";

        context = CollectionUtil.mapOf();
        formatted = DollarInterpolation.format(template, context, "NULL");
        System.out.println(template);
        System.out.println(formatted);

        context = CollectionUtil.mapOf(
                "a", "a_val",
                "a_Y_0", "a_Y_0_val",
                "_b_Z", "_b_Z_val",
                ":", ":_val");
        formatted = DollarInterpolation.format(template, context);
        System.out.println(template);
        System.out.println(formatted);

        template = "${{\\:\\}\\\\:{\\:\\}\\\\}";
        formatted = DollarInterpolation.format(template, context);
        System.out.println(template);
        System.out.println(formatted);

        context = CollectionUtil.mapOf("{:}\\", "xxx");
        formatted = DollarInterpolation.format(template, context);
        System.out.println(template);
        System.out.println(formatted);
    }

}
