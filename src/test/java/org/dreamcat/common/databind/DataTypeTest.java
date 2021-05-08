package org.dreamcat.common.databind;

import static org.dreamcat.common.databind.DataType.arrayType;
import static org.dreamcat.common.databind.DataType.type;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Create by tuke on 2021/4/20
 */
@Slf4j
public class DataTypeTest {

    @Test
    public void testArray() {
        Result<int[][], Result<Map<String, Object>, Some<Long>[][][]>> result = new Result<>();

        DataType int_a_a = arrayType(arrayType(int.class));
        DataType map = type(Map.class, type(String.class), type(Object.class));
        DataType long_a_a_a = arrayType(arrayType(arrayType(Long.class)));
        DataType some = type(Some.class, long_a_a_a);
        DataType result_map_some = type(Result.class, map, some);

        DataType resultType = type(Result.class, int_a_a, result_map_some);
        log.info("resultType: \n{}", resultType);
    }

    private static class Some<V> {

        V value;
    }

    private static class Result<Ok, Err> {

        Ok ok;
        Err error;
    }
}
