package com.tukeof.common.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by tuke on 2018-09-22
 */
public class IniFile {
    private final String defaultSection;

    private final Map<String, Map<String, String>> sections = new HashMap<>();

    public IniFile() {
        this("DEFAULT");
    }

    public IniFile(String defaultSection) {
        this.defaultSection = defaultSection;
    }

    public void load(String filename) throws IOException {
        load(new FileReader(filename));
    }

    public void load(File file) throws IOException {
        load(new FileReader(file));
    }

    public void load(Reader reader) throws IOException {
        try (BufferedReader ins = new BufferedReader(reader)) {
            Map<String, String> current = null;
            String line;
            while ((line = ins.readLine()) != null) {
                line = line.trim();
                if (line.matches("\\[(.*)\\]")) {
                    String section = line.replaceFirst("\\[(.*)\\]", "$1");
                    current = new HashMap<>();
                    sections.put(section, current);
                } else if (line.matches(".*?=.*")) {

                    if (current == null) {
                        current = sections.computeIfAbsent(defaultSection, key -> new HashMap<>());
                    }

                    int index = line.indexOf('=');
                    String name = line.substring(0, index);
                    String value = line.substring(index + 1);
                    current.put(name, value);
                }
            }
        }
    }

    public void save(String filename) throws IOException {
        save(new FileWriter(filename));
    }

    public void save(File file) throws IOException {
        save(new FileWriter(file));
    }

    public void save(Writer writer) throws IOException {
        try (BufferedWriter outs = new BufferedWriter(writer)) {
            for (String section : sections.keySet()) {
                outs.write('[');
                outs.write(section);
                outs.write('[');
                outs.newLine();

                Map<String, String> nvs = sections.get(section);
                for (String name : nvs.keySet()) {
                    outs.write(name);
                    outs.write('=');
                    outs.write(nvs.get(name));
                    outs.newLine();
                }
            }
        }

    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public Map<String, String> get() {
        return get(false);
    }

    public Map<String, String> get(boolean ignoreCase) {
        return get(defaultSection, ignoreCase);
    }

    public Map<String, String> get(String section) {
        return get(section, false);
    }

    public Map<String, String> get(String section, boolean ignoreCase) {
        if (!ignoreCase) return sections.get(section);

        for (String key : sections.keySet()) {
            if (key.equalsIgnoreCase(section)) {

                return sections.get(key);
            }
        }

        return null;
    }

    public String getValue(String section, String name) {
        return getValue(section, name, false);
    }

    public String getValue(String section, String name, boolean ignoreCase) {
        Map<String, String> p = sections.get(section);
        if (p == null) return null;

        if (!ignoreCase) return p.get(name);

        for (String key : p.keySet()) {
            if (key.equalsIgnoreCase(name)) {
                return p.get(key);
            }
        }

        return null;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public boolean rename(String oldSection, String newSection) {
        if (!sections.containsKey(oldSection) || sections.containsKey(newSection))
            return false;

        Map<String, String> map = sections.get(oldSection);
        sections.remove(oldSection);
        sections.put(newSection, map);
        return true;
    }

    public boolean rename(String section, String oldKey, String newKey) {
        Map<String, String> map = sections.get(section);
        if (map == null) return false;
        if (!map.containsKey(oldKey) || map.containsKey(newKey)) return false;

        String value = map.get(oldKey);
        map.remove(oldKey);
        map.put(newKey, value);
        return true;
    }

    public Map<String, Map<String, String>> getSections() {
        return sections;
    }
}
