package org.dreamcat.common.databind;

import static org.dreamcat.common.databind.DataTypes.arrayType;
import static org.dreamcat.common.databind.DataTypes.fromType;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.databind.DataTypeTest.Result;
import org.dreamcat.common.databind.DataTypeTest.Some;
import org.junit.Test;

/**
 * @author Jerry Will
 * @since 2021-06-27
 */
@Slf4j
public class DataTypeFieldTest<X> {

    @Test
    public void test() throws Exception {
        assert field("field1").equals(fromType(int.class));
        assert field("field2").equals(fromType(String[].class));
        assert field("field3").equals(
                arrayType(fromType(Map.class, fromType(Long.class), fromType(String.class))));
        assert field("field4").equals(DataTypeTest.resultType());

        assert field("field5").equals(fromType(Object.class));
        assert field("field6").equals(fromType(Object[].class));
        assert field("field7").equals(
                fromType(Map.class, fromType(Object.class), fromType(Date.class)));
        assert field("field8").equals(
                arrayType(arrayType(fromType(Map.class, fromType(Object[].class), fromType(Date.class)))));
        assert field("field9").equals(
                fromType(List.class, fromType(List.class, fromType(Object.class))));
    }

    int field1;

    String[] field2;

    Map<Long, String>[] field3;

    Result<int[][], Result<Map<String, Object>, Some<Long[][][]>>> field4;

    X field5;

    X[] field6;

    Map<X, Date> field7;

    Map<X[], Date>[][] field8;

    List<List<X>> field9;

    private DataType field(String fieldName) throws NoSuchFieldException {
        Field field = getClass().getDeclaredField(fieldName);
        DataType dataType = fromType(field.getGenericType());
        log.info("\n{}:\n\t\t{}\n", field, dataType);
        return dataType;

    }
}
