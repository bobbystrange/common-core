package org.dreamcat.test;

import static org.dreamcat.common.util.RandomUtil.choose72;
import static org.dreamcat.common.util.RandomUtil.randi;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dreamcat.common.util.ArrayUtil;

/**
 * Create by tuke on 2020/3/3
 */
public class BeanData {

    public static Pojo ofPojo() {
        return new Pojo(
                randi(2 << 10),
                System.currentTimeMillis(),
                ArrayUtil.fromMapperAsDouble(randi(1, 64), it -> Math.random()),
                choose72(randi(0, 64)),
                new Date()
        );
    }

    public static PrivatePojo ofPrivatePojo() {
        return new PrivatePojo(
                randi(2 << 10),
                System.currentTimeMillis(),
                ArrayUtil.fromMapperAsDouble(randi(1, 64), it -> Math.random()),
                choose72(randi(0, 64)),
                new Date(System.currentTimeMillis() - randi(2 << 16))
        );
    }

    public static All ofAll() {
        All all = new All();
        all.setD(ArrayUtil.fromMapperAsDouble(randi(1, 64), it -> Math.random()));
        all.setI(randi(2 << 10));
        all.setL(System.currentTimeMillis());
        all.setS(choose72(randi(0, 64)));
        all.setT(new Date(System.currentTimeMillis() - randi(2 << 29)));
        all.setInnerStatic(new All.InnerStatic(
                System.nanoTime(),
                ArrayUtil.fromMapper(randi(1, 64), it -> randi(2 << 29), Integer[]::new)
        ));
        all.inner = all.new Inner();
        all.inner.ia = ArrayUtil.fromMapper(randi(1, 64), it -> randi(2 << 29), Integer[]::new);
        all.inner.l = randi(2 << 29);
        return all;
    }

    public @interface Ann {

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Pojo {

        private int i;
        @Ann
        private Long L;
        private double[] d;
        @Ann
        private String s;
        private Date t;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class PrivatePojo {

        @Ann
        private int i;
        private Long L;
        @Ann
        private double[] d;
        private String s;
        private Date t;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class All {

        @Ann
        private int i;
        private Long L;
        private double[] d;
        private String s;
        @Ann
        private Date t;
        private Inner inner;
        private InnerStatic innerStatic;

        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        static class InnerStatic {

            long l;
            Integer[] ia;
        }

        @NoArgsConstructor
        @AllArgsConstructor
        @Data
        class Inner {

            long l;
            Integer[] ia;
        }
    }
}
