package com.tukeof.common.legacy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum OsNameEnum {
    Any("Any"),
    Linux("Linux"),
    Mac_OS("Mac OS"),
    Mac_OS_X("Mac OS X"),
    Windows("Windows"),
    OS2("OS/2"),
    Solaris("Solaris"),
    SunOS("SunOS"),
    MPEiX("MPE/iX"),
    HP_UX("HP-UX"),
    AIX("AIX"),
    OS390("OS/390"),
    FreeBSD("FreeBSD"),
    Irix("Irix"),
    Digital_Unix("Digital Unix"),
    NetWare_411("NetWare"),
    OSF1("OSF1"),
    OpenVMS("OpenVMS"),
    Others("Others");

    // "line.separator"
    public static final String OS_NAME = System.getProperty("os.name");
    private String description;

    OsNameEnum(String description) {
        this.description = description;
    }

    public static OsNameEnum getCurrent() {
        for (OsNameEnum platform : OsNameEnum.values()) {
            Matcher matcher = Pattern.compile(platform.toString().toLowerCase())
                    .matcher(OS_NAME.toLowerCase());
            if (matcher.find()) return platform;
        }
        return OsNameEnum.Others;
    }

    public String toString() {
        return description;
    }

}
