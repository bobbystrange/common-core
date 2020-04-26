package org.dreamcat.common.bean;

import lombok.Data;
import org.junit.Test;

import static org.dreamcat.common.util.PrintUtil.log;

/**
 * Create by tuke on 2020/4/13
 */
public class BeanMapUtilTest {

    @Test
    public void test() {
        Query query = new Query();
        query.setDigest("A");
        query.setNonce(1);
        query.setTimestamp(System.currentTimeMillis());
        log("{}", BeanFormatUtil.pretty(BeanMapUtil.toMap(query)));
        log("{}", BeanFormatUtil.pretty(BeanMapUtil.toProps(query)));

    }

    @Data
    private static class Query {
        private String digest;
        private Integer nonce;
        private Long timestamp;
        private String sign;
    }
}
