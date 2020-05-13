package org.dreamcat.common.core.argparse;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.dreamcat.common.util.PrintUtil.println;

/**
 * Create by tuke on 2019-03-27
 */
public class ArgParserTest {

    @Test
    public void parse() throws ArgParseException {
        String args1 = "aux -n3 -o yaml -P6379 --rm=true -owide " +
                "-H x-opts=gzip -Ffilename=awesome.rb -Ffilemode=777";
        String args2 = "-R svc ep ds -- nowarn noredirect";

        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(args1.split(" ")));
        list.addAll(Arrays.asList("-H", "Accept: */*", "-H", "User-Agent: curl/7.54.0"));
        list.addAll(Arrays.asList(args2.split(" ")));

        String[] args = list.toArray(new String[0]);

        ArgParser parser = newParser();
        parser.parse(args, true);
        parser.key_value_map.forEach((k, v) -> {
            println(k, "=>", v, "\t", v.getClass());
        });

        println("values\t", parser.values);
        parser.getMap('F').forEach((k, v) -> {
            println(k, "=>", v);
        });

        assert parser.getInt("p").orElseThrow(AssertionError::new) == 6379;
        assert parser.getBool("a");

        List<String> R = parser.getList("R");
        assert R.size() == 3;

        ///

        parser = newParser();
        parser.parse(args);

        List<String> vs = parser.getValues();

        assert vs.get(0).equals("aux");
        assert vs.toString().equals("[aux, nowarn, noredirect]");
        assert !parser.getBool("a");
        assert !parser.get("u").isPresent();

        ///

        parser = newParser();
        parser.parse(args2.split(" "));

        println("\n==== ==== ==== ====\n");
        parser.key_value_map.forEach((k, v) -> {
            println(k, "=>", v, "\t", v.getClass());
        });
        println("values\t", parser.values);
    }

    private ArgParser newParser() {
        ArgParser parser = ArgParser.newInstance();

        parser.add("n", "n", "number");
        parser.add("p", "P", "port");
        parser.addBool("rm", "remove flag", "r", "rm", "remove");

        parser.addBool("a", "a");
        parser.addBool("u", "u");
        parser.addBool("x", "x");

        parser.addList("o", "o", "output");
        parser.addList("H", "H", "header");

        parser.addProperty('F');
        parser.addList("R", "resource", "R");
        return parser;
    }

}
