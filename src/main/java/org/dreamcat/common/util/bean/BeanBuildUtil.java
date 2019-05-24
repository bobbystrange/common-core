package org.dreamcat.common.util.bean;

import org.dreamcat.common.core.component.BeanComponent;

import java.util.Arrays;
import java.util.List;

/**
 * Create by tuke on 2019-05-05
 */
public class BeanBuildUtil {

    private static final BeanComponent component = new BeanComponent();

    public static <T> T fromStringList(Class<T> clazz, List<String> list) throws IllegalAccessException, InstantiationException {
        return component.fromStringList(clazz, list);
    }

    public static <T> T fromStringArray(Class<T> clazz, String[] strings) throws IllegalAccessException, InstantiationException {
        return fromStringList(clazz, Arrays.asList(strings));
    }
}

