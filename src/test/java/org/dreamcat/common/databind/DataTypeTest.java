package org.dreamcat.common.databind;

import static org.dreamcat.common.databind.DataTypes.fromArrayType;
import static org.dreamcat.common.databind.DataTypes.fromType;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Create by tuke on 2021/4/20
 */
@Slf4j
public class DataTypeTest {

    Result<int[][], Result<Map<String, Object>, Some<Long[][][]>>> result = new Result<>();

    @Test
    public void testField() {
        DataType testType = fromType(getClass());
        log.info("\n{}", testType);
        testType.loadFields().forEach((k, v) -> {
            log.info("\n{}, {}", k, v);
        });
    }

    @Test
    public void testField2() {
        DataType testType = fromType(A.class);
        log.info("\n{}", testType);
        testType.loadFields().forEach((k, v) -> {
            log.info("\n{}, {}", k, v);

            Map<String, DataType> vfs = v.loadFields();
            if (vfs != null) vfs.forEach((a, b) -> {
                log.info("\n{}, {} :\t{}, {}", k, v, a, b);

                Map<String, DataType> bfs = b.loadFields();
                if (bfs != null) bfs.forEach((x, y) -> {
                    log.info("\n{}, {} :\t{}, {} :\t{}, {}", k, v, a, b, x, y);

                    assert k.equals("b");
                    assert v.equals(fromType(B.class));

                    assert a.equals("some");
                    assert b.equals(fromType(Some.class, fromType(A.class)));

                    assert x.equals("value");
                    assert y.equals(fromType(Object.class));
                });
            });
        });
    }

    @Test
    public void testArray() {
        DataType int_a_a = fromArrayType(fromArrayType(int.class));
        DataType map = fromType(Map.class, fromType(String.class), fromType(Object.class));
        DataType long_a_a_a = fromArrayType(fromArrayType(fromArrayType(Long.class)));
        DataType some = fromType(Some.class, long_a_a_a);
        DataType result_map_some = fromType(Result.class, map, some);

        DataType resultType = fromType(Result.class, int_a_a, result_map_some);
        log.info("\n{}", resultType);
    }

    private static class Some<V> {

        V value;
    }

    private static class Result<Ok, Err> {

        Ok ok;
        Err error;
    }

    private static class A {

        B b;
    }

    private static class B {

        Some<A> some;
    }
}
