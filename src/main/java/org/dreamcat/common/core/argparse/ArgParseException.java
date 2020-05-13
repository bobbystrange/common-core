package org.dreamcat.common.core.argparse;

import lombok.Getter;

/**
 * Create by tuke on 2019-03-27
 */
@Getter
public class ArgParseException extends Exception {
    String name;
    String key;
    String value;
    Type type;

    public ArgParseException(String key, Type type) {
        this.key = key;
        this.type = type;
    }

    public ArgParseException(String name, String key, Type type) {
        this.name = name;
        this.key = key;
        this.type = type;
    }

    public enum Type {
        // name
        KEY_NOT_FOUND,

        // key
        REQUIRE_NOT_PROPERTY,

        // key
        REQUIRE_PROPERTY,

        // key, value
        BOOL_TEXT_PARSE,

        // name, key
        REQUIRE_BOOL
    }
}
