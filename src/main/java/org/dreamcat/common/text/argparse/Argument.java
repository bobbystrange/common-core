package org.dreamcat.common.text.argparse;

import lombok.AllArgsConstructor;

/**
 * Create by tuke on 2019-03-26
 */
@AllArgsConstructor
class Argument {

    static final int flag_string = 0;
    static final int flag_bool = 1;
    static final int flag_list = 2;
    static final int flag_property = 4;
    String[] names;
    // a magic flag based on bits
    // bsd: bsd style support.
    // 		treat `aux` as `-a -u -x`
    //		treat `-ef` as `-e -f`
    // list: multi-value support
    //		like `-v video1 video2 -a audio extra_args`
    // property: map value support
    //		like: -Da=1 -Db=2
    int flag;

    void assume_not_property(String key) throws ArgParseException {
        if (this.flag == flag_property) {
            throw new ArgParseException(key, ArgParseException.Type.REQUIRE_NOT_PROPERTY);
        }
    }

    void assume_property(String key) throws ArgParseException {
        if (this.flag != flag_property) {
            throw new ArgParseException(key, ArgParseException.Type.REQUIRE_PROPERTY);
        }
    }

    void assume_bool(String name, String key) throws ArgParseException {
        if (this.flag != flag_bool) {
            throw new ArgParseException(name, key, ArgParseException.Type.REQUIRE_BOOL);
        }
    }
}
