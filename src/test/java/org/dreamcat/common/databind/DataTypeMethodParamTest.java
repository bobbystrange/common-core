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
import org.dreamcat.common.util.ReflectUtil;
import org.junit.Test;

/**
 * @author Jerry Will
 * @since 2021-06-27
 */
@Slf4j
public class DataTypeMethodParamTest<X> {

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

    void method1(int v) {
    }

    void method2(String[] v) {
    }

    void method3(Map<Long, String>[] v) {
    }

    void method4(Result<int[][], Result<Map<String, Object>, Some<Long[][][]>>> v) {
    }

    void method5(X x) {
    }

    @SafeVarargs
    final void method6(X... v) {
    }

    void method7(Map<X, Date> v) {
    }

    void method8(Map<X[], Date>[][] v) {
    }

    void method9(List<List<X>> v) {
    }

    private DataType method(String methodName) {
        Method method = ReflectUtil.retrieveMethods(getClass()).stream()
                .filter(m -> m.getName().equals(methodName))
                .findAny().orElse(null);
        assert method != null;
        DataType dataType = fromType(method.getGenericParameterTypes()[0]);
        log.info("\n{}:\t{}", method, dataType);
        return dataType;
    }

}
