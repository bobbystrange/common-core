package org.dreamcat.common.text.argparse;

import static org.dreamcat.common.text.argparse.Argument.flag_bool;
import static org.dreamcat.common.text.argparse.Argument.flag_list;
import static org.dreamcat.common.text.argparse.Argument.flag_property;
import static org.dreamcat.common.text.argparse.Argument.flag_string;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.ObjectUtil;

/**
 * Create by tuke on 2019-03-26
 */
@Slf4j
@SuppressWarnings("unchecked")
public abstract class ArgParser {

    // key, argument
    final Map<String, Argument> key_argument_map = new HashMap<>();
    // key, value
    final Map<String, Object> key_value_map = new HashMap<>();
    // many names to one key
    final Map<String, String> names_key_map = new HashMap<>();
    // values
    final List<String> values = new ArrayList<>();

    public static ArgParser newInstance() {
        return new ArgParserImpl();
    }

    public void parse(String[] args) throws ArgParseException {
        parse(args, false);
    }

    public abstract void parse(String[] args, boolean bsd) throws ArgParseException;

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public String get(String key) {
        Object o = key_value_map.get(key);
        return (String) o;
    }

    public boolean getBool(String key) {
        return Objects.equals(key_value_map.get(key), true);
    }

    public Integer getInt(String key) {
        String s = get(key);
        return s == null ? null : Integer.valueOf(s);
    }

    public Long getLong(String key) {
        String s = get(key);
        return s == null ? null : Long.valueOf(s);
    }

    public Double getDouble(String key) {
        String s = get(key);
        return s == null ? null : Double.valueOf(s);
    }

    public List<String> getList(String key) {
        Object o = key_value_map.get(key);
        if (o == null) return Collections.emptyList();
        return (List<String>) o;
    }

    public Map<String, String> getMap(char key) {
        Object o = key_value_map.get(String.valueOf(key));
        if (o == null) return Collections.emptyMap();
        return (Map<String, String>) o;
    }

    public List<String> getValues() {
        return values;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    // -L 3, -L=3, -L3, --level 3, --level=3
    // -o yaml, -o=yaml, -oyaml, --output yaml, --output=yaml
    // -r, -r=true, -r true, --rm, --rm=true, --rm true
    public ArgParser add(String key, String... names) {
        return addByFlag(key, flag_string, names);
    }

    // aux, lah, cvzf, -ef
    // only allow single char key and bool value
    public ArgParser addBool(String key, String... names) {
        return addByFlag(key, flag_bool, names);
    }

    // -a 1.ts -a 2.ts
    public ArgParser addList(String key, String... names) {
        return addByFlag(key, flag_list, names);
    }

    // only support single char argparse
    // -Dfile.encoding=utf8 -Dspring.active.profile=dev
    public ArgParser addProperty(char key) {
        String name = String.valueOf(key);
        return addByFlag(name, flag_property, name);
    }

    private ArgParser addByFlag(String key, int flag, String... names) {
        if (ObjectUtil.isEmpty(names)) names = new String[]{key};
        this.key_argument_map.put(key, new Argument(names, flag));
        return this;
    }

}
