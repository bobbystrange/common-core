package org.dreamcat.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Create by tuke on 2019-05-14
 */
@Getter
@Setter
@AllArgsConstructor
public class BeanKey {

    @Anno
    private Date yearMonth;
    @Anno
    private long partnerId;

    @Override
    public int hashCode() {
        int id = (int) partnerId;
        return Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(yearMonth))
                + id * 10000_0000;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof BeanKey) {
            return hashCode() == obj.hashCode();
        }
        return false;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
            ElementType.LOCAL_VARIABLE})
    public @interface Anno {

    }
}
