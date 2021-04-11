package org.dreamcat.common.text.argparse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.dreamcat.common.core.Pair;
import org.dreamcat.common.util.CollectionUtil;

/**
 * Create by tuke on 2020/5/2
 */
@SuppressWarnings("unchecked")
class ArgParserImpl extends ArgParser {

    private static final Set<String> TRUE_STRINGS = CollectionUtil
            .setOf("true", "t", "yes", "y", "1");
    private static final Set<String> FALSE_STRINGS = CollectionUtil
            .setOf("false", "f", "no", "n", "0");

    @Override
    public void parse(String[] args, boolean bsd) throws ArgParseException {
        if (Objects.requireNonNull(args).length == 0) return;
        this.init_name_key_map();

        LinkedList<String> shift = new LinkedList<>(Arrays.asList(args));
        // 	handle bsd part
        // like ps -ef ... or ps aux ...
        if (bsd) {
            String argname = shift.pollFirst();
            this.parse_bsd_args(Objects.requireNonNull(argname));
            if (shift.isEmpty()) return;
        }

        // handle no-bsd part
        // -n default -oyaml --type pom --rm=true --force
        // -Dspring.active.profile=dev -Dlog4j.debug=true
        // -i 1 2 3 --property:key=value extra1 extra2
        String arg;
        while (!shift.isEmpty()) {
            arg = shift.pollFirst();
            if (arg == null) break;

            String argname = arg;
            // name case
            // --rm=true --type pom
            if (argname.startsWith("--")) {
                this.parse_double_minus(argname, shift);
            }
            // -P3306, -o yml, -f=%h-%m-%s, -Dlogger.level=debug
            else if (argname.startsWith("-")) {
                this.parse_minus(argname, shift);
            } else {
                // not `-o --output`
                this.values.add(argname);
            }
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private void init_name_key_map() {
        key_argument_map.forEach((k, v) -> {
            for (String n : v.names) {
                this.names_key_map.put(n, k);
            }
        });
    }

    private void parse_bsd_args(String bsd_args) throws ArgParseException {
        if (bsd_args.startsWith("-")) {
            bsd_args = bsd_args.substring(1);
        }

        char[] chars = bsd_args.toCharArray();
        for (char c : chars) {
            String name = String.valueOf(c);
            Pair<String, Argument> pair = this.get_key_or_throw(name);
            String key = pair.first();
            Argument argument = pair.second();

            argument.assume_bool(name, key);
            this.key_value_map.put(key, true);
        }
    }

    private void parse_double_minus(String arg, LinkedList<String> shift) throws ArgParseException {
        // treat argument behind `--` as extra values
        if (arg.length() == 2) {
            this.add_values(shift);
            return;
        }

        String argname = arg.substring(2);
        int ind = argname.indexOf('=');
        // the case, not `--rm=true` but `--type pom`
        if (ind == -1) {
            Pair<String, Argument> pair = this.get_key_or_throw(argname);
            String key = pair.first();
            Argument argument = pair.second();

            // property argument must contains `=`
            argument.assume_not_property(key);

            this.add_all_forward(key, argument, shift);
        }
        // the case, not `--type pom` but `--rm=true`
        else {
            String name = argname.substring(0, ind);
            Pair<String, Argument> pair = this.get_key_or_throw(name);
            String key = pair.first();
            Argument argument = pair.second();

            // the case `--rm=`
            if (ind == argname.length() - 1) {
                argument.assume_bool(name, key);
                this.key_value_map.put(key, true);
                return;
            }

            String value = argname.substring(ind + 1, argname.length());
            this.add_all(key, argument, value);
        }
    }

    private void parse_minus(String arg, LinkedList<String> shift) throws ArgParseException {
        // only a `-`
        if (arg.length() == 1) {
            this.values.add(arg);
            return;
        }

        String argname = arg.substring(1);
        String name = argname.substring(0, 1);
        Pair<String, Argument> pair = this.get_key_or_throw(name);
        String key = pair.first();
        Argument argument = pair.second();

        // the case `-o yaml`
        if (argname.length() == 1) {
            this.add_all_forward(key, argument, shift);
            return;
        }

        // the case `-P3306 -f=%h-%m-%s, -Dlogger.level=debug`
        String value = argname.substring(1);
        int ind = value.indexOf('=');
        // the case `-P3306`
        if (ind == -1) {
            this.add_all(key, argument, value);
            return;
        }

        // the case `-f=%h-%m-%s`
        if (ind == 0) {
            // the case, `-o=`
            if (value.length() == 1) {
                // treat `=` as a value
                this.add_all(key, argument, value);
                return;
            }

            value = value.substring(1);
            this.add_all(key, argument, value);
            return;
        }

        // the case, `-Dlogger.level=debug`
        argument.assume_property(key);

        String hkey = value.substring(0, ind);
        String hval;
        // `-Dlogger.level=`
        if (ind == value.length() - 1) {
            hval = "";
        } else {
            hval = value.substring(ind + 1);
        }

        add_property(key, hkey, hval);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private void add_all(String key, Argument argument, String value) throws ArgParseException {
        argument.assume_not_property(key);

        if (argument.flag == Argument.flag_bool) {
            add_bool(key, value);
            return;
        }
        if (argument.flag == Argument.flag_string) {
            this.key_value_map.put(key, value);
            return;
        }
        if (argument.flag == Argument.flag_list) {
            add_list(key, value);
        }
    }

    private void add_all_forward(String key, Argument argument, LinkedList<String> shift)
            throws ArgParseException {
        argument.assume_not_property(key);

        if (argument.flag == Argument.flag_bool) {
            this.key_value_map.put(key, true);
            return;
        }

        if (argument.flag == Argument.flag_string) {
            String value = shift.pollFirst();
            if (value == null) {
                return;
            }
            this.key_value_map.put(key, value);
            return;
        }

        if (argument.flag == Argument.flag_list) {
            String value = shift.pollFirst();
            while (value != null) {
                if (value.startsWith("-")) {
                    shift.addFirst(value);
                    return;
                }
                add_list(key, value);
                value = shift.pollFirst();
            }
        }
    }

    private void add_list(String key, String value) {
        Object list_value = this.key_value_map.get(key);

        if (list_value != null) {
            ArrayList<String> list = (ArrayList<String>) list_value;
            list.add(value);
        } else {
            ArrayList<String> list = new ArrayList<>();
            list.add(value);
            this.key_value_map.put(key, list);
        }
    }

    private void add_bool(String key, String value) throws ArgParseException {
        value = value.toLowerCase();
        if (TRUE_STRINGS.contains(value)) {
            this.key_value_map.put(key, true);
        } else if (FALSE_STRINGS.contains(value)) {
            this.key_value_map.put(key, false);
        } else {
            throw new ArgParseException(key, value, ArgParseException.Type.BOOL_TEXT_PARSE);
        }
    }

    private void add_property(String key, String hkey, String hval) {
        Object map_value = this.key_value_map.get(key);
        if (map_value != null) {
            Map<String, String> map = (Map<String, String>) map_value;
            map.put(hkey, hval);
        } else {
            Map<String, String> map = new HashMap<>();
            map.put(hkey, hval);
            this.key_value_map.put(key, map);
        }
    }

    private void add_values(LinkedList<String> rest) {
        String value = rest.pollFirst();
        while (value != null) {
            this.values.add(value);
            value = rest.pollFirst();
        }
    }

    private Pair<String, Argument> get_key_or_throw(String name) throws ArgParseException {
        String key = this.names_key_map.get(name);
        if (key == null) {
            throw new ArgParseException(name, ArgParseException.Type.KEY_NOT_FOUND);
        }
        return new Pair<>(key, this.key_argument_map.get(key));
    }

}
