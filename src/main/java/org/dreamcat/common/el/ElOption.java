package org.dreamcat.common.el;

import java.math.RoundingMode;

/**
 * Create by tuke on 2020/11/28
 */
public interface ElOption {

    int getScale();

    RoundingMode getRoundingMode();

    class Base implements ElOption {

        @Override
        public int getScale() {
            return 6;
        }

        @Override
        public RoundingMode getRoundingMode() {
            return RoundingMode.HALF_EVEN;
        }
    }

    ;
}
