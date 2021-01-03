package org.dreamcat.common.core.legacy;

import lombok.Getter;

/**
 * Create by tuke on 2019-02-01
 */
@Getter
public class Version {

    // 0.1.0.alpha-1
    private final int majorVersion;
    private final int minorVersion;
    private final int microVersion;
    private final int buildVersion;
    private final Type type;
    private final String specialVersion;

    public Version(
            int majorVersion, int minorVersion, int microVersion,
            Type type, int buildVersion) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.microVersion = microVersion;
        this.type = type;
        this.buildVersion = buildVersion;
        if (buildVersion == 0) {
            if (Type.RELEASE.equals(type)) {
                this.specialVersion = String.format("%d.%d.%d",
                        majorVersion, microVersion, microVersion);
            } else {
                this.specialVersion = String.format("%d.%d.%d.%s",
                        majorVersion, microVersion, microVersion, type.getValue());
            }
        } else {
            this.specialVersion = String.format("%d.%d.%d.%s-%d",
                    majorVersion, microVersion, microVersion, type.getValue(), buildVersion);
        }

    }

    @Getter
    public enum Type {
        ALPHA("a", "alpha", "alpha"),
        BETA("b", "beta", "beta"),
        RC("rc", "rc", "releaseCandidate"),
        RELEASE("r", "", "release");

        /**
         * @see #name
         */
        private final String value;
        private final String shortName;
        private final String name;
        private final String longName;

        Type(String shortName, String name, String longName) {
            this.shortName = shortName;
            this.name = name;
            this.longName = longName;

            this.value = name;
        }

        public String getValue() {
            return value;
        }

    }
}
