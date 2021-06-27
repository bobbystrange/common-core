package org.dreamcat.common.databind;

import static org.dreamcat.common.databind.DataTypes.arrayType;
import static org.dreamcat.common.databind.DataTypes.fromType;

import java.lang.reflect.Method;
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
public class DataTypeMethodReturnTest<X> {

    @Test
    public void test() throws Exception {
        assert method("method1").equals(fromType(int.class));
        assert method("method2").equals(fromType(String[].class));
        assert method("method3").equals(
                arrayType(fromType(Map.class, fromType(Long.class), fromType(String.class))));
        assert method("method4").equals(DataTypeTest.resultType());

        assert method("method5").equals(fromType(Object.class));
        assert method("method6").equals(fromType(Object[].class));
        assert method("method7").equals(
                fromType(Map.class, fromType(Object.class), fromType(Date.class)));
        assert method("method8").equals(
                arrayType(arrayType(fromType(Map.class, fromType(Object[].class), fromType(Date.class)))));
        assert method("method9").equals(
                fromType(List.class, fromType(List.class, fromType(Object.class))));
    }

    int method1() {
        return 0;
    }

    String[] method2() {
        return null;
    }

    Map<Long, String>[] method3() {
        return null;
    }

    Result<int[][], Result<Map<String, Object>, Some<Long[][][]>>> method4() {
        return null;
    }

    X method5() {
        return null;
    }

    X[] method6() {
        return null;
    }

    Map<X, Date> method7() {
        return null;
    }

    Map<X[], Date>[][] method8() {
        return null;
    }

    List<List<X>> method9() {
        return null;
    }

    private DataType method(String methodName) throws NoSuchMethodException {
        Method method = getClass().getDeclaredMethod(methodName);
        DataType dataType = fromType(method.getGenericReturnType());
        log.info("\n{}:\t{}", method, dataType);
        return dataType;
    }
}
