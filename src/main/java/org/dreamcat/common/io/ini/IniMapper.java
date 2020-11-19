package org.dreamcat.common.io.ini;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.dreamcat.common.core.Pair;
import org.dreamcat.common.util.ObjectUtil;
import org.dreamcat.common.util.StringUtil;

/**
 * Create by tuke on 2018-09-22
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class IniMapper {

    private final Map<String, Map<String, Object>> sections = new HashMap<>();
    private String defaultSection = "default";
    private boolean sectionIgnoreCase = true;
    private boolean nameIgnoreCase = false;
    private boolean failedIfInvalid = true;
    private boolean richText = false;

    public static IniMapper newInstance() {
        return new IniMapper();
    }

    public IniMapper defaultSection(String defaultSection) {
        this.defaultSection = defaultSection;
        return this;
    }

    public IniMapper sectionIgnoreCase(boolean sectionIgnoreCase) {
        this.sectionIgnoreCase = sectionIgnoreCase;
        return this;
    }

    public IniMapper nameIgnoreCase(boolean nameIgnoreCase) {
        this.nameIgnoreCase = nameIgnoreCase;
        return this;
    }

    public IniMapper failedIfInvalid(boolean failedIfInvalid) {
        this.failedIfInvalid = failedIfInvalid;
        return this;
    }

    public IniMapper richText(boolean richText) {
        this.richText = richText;
        return this;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public void load(String filename) throws IOException {
        load(new File(filename));
    }

    public void load(String filename, Charset charset) throws IOException {
        load(new File(filename), charset);
    }

    public void load(File file) throws IOException {
        try (FileReader r = new FileReader(file)) {
            load(r);
        }
    }

    public void load(File file, Charset charset) throws IOException {
        try (Reader r = new InputStreamReader(new FileInputStream(file), charset)) {
            load(r);
        }
    }

    /**
     * load text stream and build sections
     *
     * @param reader ini reader
     * @throws IOException              I/O erroor
     * @throws IllegalArgumentException invalid ini input
     */
    public void load(Reader reader) throws IOException {
        BufferedReader ins;
        if (reader instanceof BufferedReader) {
            ins = (BufferedReader) reader;
        } else {
            ins = new BufferedReader(reader);
        }

        Map<String, Object> current = null;
        String line;
        while ((line = ins.readLine()) != null) {
            line = line.trim();
            // skip comment
            if (line.startsWith("#")) continue;

            Matcher sectionMatcher = Pattern.compile("^\\[(.*)]$").matcher(line);
            if (sectionMatcher.find()) {
                String section = sectionMatcher.group(1);
                if (sectionIgnoreCase) section = section.toLowerCase();

                current = sections.computeIfAbsent(section, it -> new HashMap<>());
                continue;
            }

            int index = line.indexOf('=');
            if (index != -1) {
                String name = line.substring(0, index).trim();
                String value = line.substring(index + 1).trim();
                if (nameIgnoreCase) name = name.toLowerCase();

                if (current == null) {
                    current = sections.computeIfAbsent(defaultSection, key -> new HashMap<>());
                }

                if (!richText) {
                    current.put(name, value);
                    continue;
                }

                Object richValue;

                Pair<Number, Integer> pair = StringUtil.extractNumber(value, 0);
                if (pair == null) {
                    Boolean b = StringUtil.extractBool(value, 0);
                    if (b != null) {
                        richValue = b;
                    } else {
                        richValue = value;
                    }
                } else {
                    if (pair.second() == value.length()) {
                        richValue = pair.first();
                    } else {
                        richValue = value;
                    }
                }

                Object o = current.remove(name);
                if (o == null) {
                    current.put(name, richValue);
                } else if (o instanceof List) {
                    ((List) o).add(richValue);
                    current.put(name, o);
                } else {
                    Class<?> componentClass = o.getClass();
                    Class<?> richClass = richValue.getClass();
                    if (!componentClass.equals(richClass)) {
                        if (failedIfInvalid) {
                            String msg = String
                                    .format("incompatible type in line: `%s`, between %s and %s",
                                            line, componentClass.getSimpleName(),
                                            richClass.getSimpleName());
                            throw new IllegalArgumentException(msg);
                        } else {
                            continue;
                        }
                    }

                    if (componentClass.equals(Double.class)) {
                        List<Double> list = new ArrayList<>();
                        list.add((Double) o);
                        list.add((Double) richValue);
                        current.put(name, list);
                    } else if (componentClass.equals(Long.class)) {
                        List<Long> list = new ArrayList<>();
                        list.add((Long) o);
                        list.add((Long) richValue);
                        current.put(name, list);
                    } else if (componentClass.equals(Boolean.class)) {
                        List<Boolean> list = new ArrayList<>();
                        list.add((Boolean) o);
                        list.add((Boolean) richValue);
                        current.put(name, list);
                    } else {
                        List<String> list = new ArrayList<>();
                        list.add((String) o);
                        list.add((String) richValue);
                        current.put(name, list);
                    }
                }
                continue;
            }

            if (failedIfInvalid) {
                throw new IllegalArgumentException("invalid line: `" + line + "`");
            }
        }
    }

    public void loadFrom(String text) throws IOException {
        load(new StringReader(text));
    }

    public void save(String filename) throws IOException {
        save(new FileWriter(filename));
    }

    public void save(File file) throws IOException {
        save(new FileWriter(file));
    }

    @SuppressWarnings("rawtypes")
    public void save(Writer writer) throws IOException {
        try (BufferedWriter outs = new BufferedWriter(writer)) {
            Set<Map.Entry<String, Map<String, Object>>> sectionEntrySet = sections.entrySet();
            for (Map.Entry<String, Map<String, Object>> sectionEntry : sectionEntrySet) {
                String section = sectionEntry.getKey();
                Map<String, Object> nameValues = sectionEntry.getValue();

                outs.write('[');
                outs.write(section);
                outs.write('[');
                outs.newLine();

                Set<Map.Entry<String, Object>> entrySet = nameValues.entrySet();
                for (Map.Entry<String, Object> entry : entrySet) {
                    String name = entry.getKey();
                    Object value = entry.getValue();

                    if (!richText) {
                        outs.write(name);
                        outs.write('=');
                        outs.write((String) value);
                        outs.newLine();
                        continue;
                    }

                    if (value instanceof List) {
                        List list = (List) value;
                        for (Object i : list) {
                            outs.write(name);
                            outs.write('=');
                            outs.write(i.toString());
                            outs.newLine();
                        }
                    } else {
                        outs.write(name);
                        outs.write('=');
                        outs.write(value.toString());
                        outs.newLine();
                    }
                }
            }
        }

    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public Map<String, Map<String, Object>> getSections() {
        return sections;
    }

    public Set<String> sections() {
        return sections.keySet();
    }

    public Set<String> names(String section) {
        return getAsRaw(section).keySet();
    }

    public String get(String section, String name) {
        if (richText) return getAsString(section, name);
        return (String) getAsRaw(section, name);
    }

    public String getDefaultValue(String name) {
        return get(defaultSection, name);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public Double getAsDouble(String section, String name) {
        if (!richText) {
            throw new UnsupportedOperationException("not in the rich-text mode");
        }
        return (Double) getAsRaw(section, name);
    }

    public Long getAsLong(String section, String name) {
        if (!richText) {
            throw new UnsupportedOperationException("not in the rich-text mode");
        }
        return (Long) getAsRaw(section, name);
    }

    public Boolean getAsBoolean(String section, String name) {
        if (!richText) {
            throw new UnsupportedOperationException("not in the rich-text mode");
        }
        return (Boolean) getAsRaw(section, name);
    }

    public String getAsString(String section, String name) {
        if (!richText) {
            throw new UnsupportedOperationException("not in the rich-text mode");
        }
        return (String) getAsRaw(section, name);
    }

    public List<Double> getAsDoubleList(String section, String name) {
        if (!richText) {
            throw new UnsupportedOperationException("not in the rich-text mode");
        }
        return (List<Double>) getAsRaw(section, name);
    }

    public List<Long> getAsLongList(String section, String name) {
        if (!richText) {
            throw new UnsupportedOperationException("not in the rich-text mode");
        }
        return (List<Long>) getAsRaw(section, name);
    }

    public List<Boolean> getAsBooleanList(String section, String name) {
        if (!richText) {
            throw new UnsupportedOperationException("not in the rich-text mode");
        }
        return (List<Boolean>) getAsRaw(section, name);
    }

    public List<String> getAsStringList(String section, String name) {
        if (!richText) {
            throw new UnsupportedOperationException("not in the rich-text mode");
        }
        return (List<String>) getAsRaw(section, name);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public String set(String section, String name, String value) {
        return setRow(section, name, value);
    }

    public String setDefaultValue(String name, String value) {
        return set(defaultSection, name, value);
    }

    public Double set(String section, String name, double value) {
        return setRow(section, name, value);
    }

    public Long set(String section, String name, long value) {
        return setRow(section, name, value);
    }

    public Boolean set(String section, String name, boolean value) {
        return setRow(section, name, value);
    }

    public List set(String section, String name, List value) {
        return setRow(section, name, value);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public boolean contains(String section) {
        if (section == null) return false;
        return getAsRaw(section) != null;
    }

    public boolean rename(String oldSection, String newSection) {
        ObjectUtil.requireNonNull(oldSection, newSection);

        if (!contains(oldSection) || contains(newSection))
            return false;

        Map<String, Object> map = sections.get(oldSection);
        sections.remove(oldSection);
        sections.put(newSection, map);
        return true;
    }

    public boolean rename(String section, String oldKey, String newKey) {
        ObjectUtil.requireNonNull(section, oldKey, newKey);

        Map<String, Object> map = getAsRaw(section);
        if (map == null) return false;
        if (!map.containsKey(oldKey) || map.containsKey(newKey)) return false;

        Object value = map.get(oldKey);
        map.remove(oldKey);
        map.put(newKey, value);
        return true;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private Map<String, Object> getAsRaw(String section) {
        if (sectionIgnoreCase) {
            section = section.toLowerCase();
        }

        return sections.get(section.toLowerCase());
    }

    private Object getAsRaw(String section, String name) {
        Map<String, Object> nameValues = getAsRaw(section);
        if (nameValues == null) return null;

        if (nameIgnoreCase) name = name.toLowerCase();

        return nameValues.get(name);
    }

    private Map<String, Object> setRow(String section, Map<String, Object> map) {
        if (sectionIgnoreCase) section = section.toLowerCase();
        return sections.put(section, map);
    }

    private <T> T setRow(String section, String name, T value) {
        ObjectUtil.requireNonNull(section, name, value);

        Map<String, Object> map = getAsRaw(section);
        if (map == null) {
            map = new HashMap<>();
            setRow(section, map);
        }

        if (nameIgnoreCase) name = name.toLowerCase();
        Object oldValue = map.put(name, value);
        return (T) oldValue;
    }

}
