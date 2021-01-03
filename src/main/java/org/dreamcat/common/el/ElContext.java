package org.dreamcat.common.el;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by tuke on 2020/10/26
 */
public interface ElContext {

    ElOption getOption();

    void setOption(ElOption option);

    BigDecimal get(String name);

    void set(String name, BigDecimal value);

    default void set(String name, int value) {
        set(name, BigDecimal.valueOf(value));
    }

    default void set(String name, long value) {
        set(name, BigDecimal.valueOf(value));
    }

    default void set(String name, double value) {
        set(name, BigDecimal.valueOf(value));
    }

    static ElContext getContext() {
        return new Impl();
    }

    ElContext EMPTY = new ElContext() {

        @Override
        public ElOption getOption() {
            return null;
        }

        @Override
        public void setOption(ElOption option) {
            throw new UnsupportedOperationException();
        }

        @Override
        public BigDecimal get(String name) {
            return null;
        }

        @Override
        public void set(String name, BigDecimal value) {
            throw new UnsupportedOperationException();
        }
    };

    class Impl implements ElContext {

        private final Map<String, BigDecimal> map = new HashMap<>();
        protected ElOption option;

        @Override
        public ElOption getOption() {
            return option;
        }

        @Override
        public void setOption(ElOption option) {
            this.option = option;
        }

        @Override
        public BigDecimal get(String name) {
            return map.get(name);
        }

        @Override
        public void set(String name, BigDecimal value) {
            map.put(name, value);
        }
    }
}
