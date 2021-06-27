package org.dreamcat.common.databind;

import static org.dreamcat.common.databind.DataTypes.arrayType;
import static org.dreamcat.common.databind.DataTypes.fromType;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.databind.DataTypeTest.Result;
import org.dreamcat.common.databind.DataTypeTest.Some;
import org.dreamcat.common.util.CollectionUtil;
import org.junit.Test;

/**
 * @author Jerry Will
 * @since 2021-06-27
 */
@Slf4j
public class DataTypeGenericFieldTest {

    @Test
    public void test() throws Exception {
        assert field("a1").equals(fromType(A.class, String.class));
        assert field("a2").equals(fromType(A.class, Date.class));
        assert field("c1").equals(
                fromType(C.class, Integer.class, Byte.class));
        assert field("c2").equals(
                fromType(C.class, fromType(Void[].class), fromType(A.class, Integer.class)));
        assert field("c3").equals(
                fromType(C.class, arrayType(fromType(A.class, BigDecimal.class)),
                        fromType(C.class, fromType(List.class, String.class), Integer.class)));
    }

    @Test
    public void testFields() throws Exception {
        testField("a1", CollectionUtil.mapOf(
                "t", arrayType(arrayType(String.class)),
                "map", fromType(Map.class, Long.class, String[].class),
                "list", fromType(List.class, String.class)
        ));
        testField("a2", CollectionUtil.mapOf(
                "t", arrayType(arrayType(Date.class)),
                "map", fromType(Map.class, Long.class, Date[].class),
                "list", fromType(List.class, Date.class)
        ));
        testField("c1", CollectionUtil.mapOf(
                "result", fromType(Result.class, Integer[].class, Byte.class),
                // A<C<T2, T1[]>[]>[][] a;
                // C<Integer, Byte> c1;
                "a", arrayType(arrayType(fromType(A.class,
                        arrayType(fromType(C.class, fromType(Byte.class), fromType(Integer[].class))
                        ))))
        ));
        testField("c2", CollectionUtil.mapOf(
                "result", fromType(Result.class,
                        arrayType(fromType(Void[].class)),
                        fromType(A.class, fromType(Integer.class))),
                // A<C<T2, T1[]>[]>[][] a;
                // C<Void[], A<Integer>> c2;
                "a", arrayType(arrayType(fromType(A.class,
                        arrayType(fromType(C.class, fromType(A.class, fromType(Integer.class)),
                                arrayType(fromType(Void[].class))))
                )))
        ));
        testField("c3", CollectionUtil.mapOf(
                "result", fromType(Result.class,
                        arrayType(arrayType(fromType(A.class, fromType(BigDecimal.class)))),
                        fromType(C.class, fromType(List.class, fromType(String.class)), fromType(Integer.class))),
                // A<C<T2, T1[]>[]>[][] a;
                // C<A<BigDecimal>[], C<List<String>, Integer>> c3;
                "a", arrayType(arrayType(fromType(A.class,
                        arrayType(fromType(C.class,
                                fromType(C.class, fromType(List.class, fromType(String.class)),
                                        fromType(Integer.class)),
                                arrayType(arrayType(fromType(A.class, fromType(BigDecimal.class))))))
                )))
        ));
    }

    void testField(String fieldName, Map<String, DataType> map) throws Exception {
        DataType dataType = field(fieldName, false);
        dataType.loadFields().forEach((k, v) -> {
            log.info("\n{}:\t\t{}, {}\n", dataType, k, v);
            assert v.equals(map.get(k));
        });
    }

    A<String> a1;
    A<Date> a2;
    C<Integer, Byte> c1;
    C<Void[], A<Integer>> c2;
    C<A<BigDecimal>[], C<List<String>, Integer>> c3;

    static class A<T> {

        List<T> list;
        Map<Long, T[]> map;
        T[][] t;
    }

    static class C<T1, T2> {

        Result<T1[], T2> result;
        A<C<T2, T1[]>[]>[][] a;
    }

    static class D<T3> extends C<int[], Result<Map<String, Object>, Some<Long[][][]>>> {

        Some<Some<T3>> field3;
    }

    private DataType field(String fieldName) throws NoSuchFieldException {
        return field(fieldName, true);
    }

    private DataType field(String fieldName, boolean verbose) throws NoSuchFieldException {
        Field field = getClass().getDeclaredField(fieldName);
        DataType dataType = fromType(field.getGenericType());
        if (verbose) log.info("\n{}:\n\t\t{}", field, dataType);
        return dataType;

    }
}
