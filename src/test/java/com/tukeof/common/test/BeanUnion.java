package com.tukeof.common.test;

import com.tukeof.common.util.DateUtil;
import com.tukeof.common.util.RandomUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Create by tuke on 2019-02-12
 */
@Data
public class BeanUnion {
    private static final Date date1 = DateUtil.from(1752, 9, 1);
    private static final Date date2 = DateUtil.from(1752, 9, 2);
    private static final Date date3 = DateUtil.from(1752, 9, 14);
    private static final Date date4 = DateUtil.from(1752, 9, 15);
    private static final long[] partnerIds = new long[]{123, 456, 789, 1000};
    private static final Random RANDOM = new Random();

    private BeanBlock1 sub1;
    private BeanBlock2 sub2;
    private BeanBlock3 sub3;

    public static BeanUnion newInstance() {
        BeanUnion obj = new BeanUnion();
        obj.setSub1(new BeanBlock1(date1, partnerIds[0], RandomUtil.generateUuid32(), RANDOM.nextLong()));
        obj.setSub2(new BeanBlock2(date2, partnerIds[1], RandomUtil.generateUuid32(), RANDOM.nextLong()));
        obj.setSub3(new BeanBlock3(date3, partnerIds[2], RandomUtil.generateLowerCase(8), RANDOM.nextInt(4), RandomUtil.generateLowerCase(4)));
        return obj;
    }

    public static List<BeanBlock1> ofSub1() {
        List<BeanBlock1> list = new ArrayList<>();
        for (long partnerId : partnerIds) {
            list.add(new BeanBlock1(date1, partnerId, RandomUtil.generateUuid32(), RANDOM.nextLong()));
            list.add(new BeanBlock1(date2, partnerId, RandomUtil.generateUuid32(), RANDOM.nextLong()));
            list.add(new BeanBlock1(date3, partnerId, RandomUtil.generateUuid32(), RANDOM.nextLong()));
            list.add(new BeanBlock1(date4, partnerId, RandomUtil.generateUuid32(), RANDOM.nextLong()));
        }
        return list;
    }

    public static List<BeanBlock2> ofSub2() {
        List<BeanBlock2> list = new ArrayList<>();
        for (long partnerId : partnerIds) {
            list.add(new BeanBlock2(date1, partnerId, RandomUtil.generateUuid32(), RANDOM.nextLong()));
            list.add(new BeanBlock2(date2, partnerId, RandomUtil.generateUuid32(), RANDOM.nextLong()));
            list.add(new BeanBlock2(date3, partnerId, RandomUtil.generateUuid32(), RANDOM.nextLong()));
            list.add(new BeanBlock2(date4, partnerId, RandomUtil.generateUuid32(), RANDOM.nextLong()));
        }
        return list;
    }

    public static List<BeanBlock3> ofSub3() {
        List<BeanBlock3> list = new ArrayList<>();
        for (long partnerId : partnerIds) {
            list.add(new BeanBlock3(date1, partnerId, RandomUtil.generateLowerCase(8), RANDOM.nextInt(4), RandomUtil.generateLowerCase(4)));
            list.add(new BeanBlock3(date2, partnerId, RandomUtil.generateLowerCase(8), RANDOM.nextInt(4), RandomUtil.generateLowerCase(4)));
            list.add(new BeanBlock3(date3, partnerId, RandomUtil.generateLowerCase(8), RANDOM.nextInt(4), RandomUtil.generateLowerCase(4)));
            list.add(new BeanBlock3(date4, partnerId, RandomUtil.generateLowerCase(8), RANDOM.nextInt(4), RandomUtil.generateLowerCase(4)));
        }
        return list;
    }

    @EqualsAndHashCode(callSuper = false)
    public static class BeanBlock1 extends BeanKey {
        String uuid;
        long amount;

        public BeanBlock1(Date yearMonth, long partnerId, String uuid, long amount) {
            super(yearMonth, partnerId);
            this.uuid = uuid;
            this.amount = amount;
        }
    }

    @EqualsAndHashCode(callSuper = false)
    public static class BeanBlock2 extends BeanKey {
        String uuid;
        double speed;

        public BeanBlock2(Date yearMonth, long partnerId, String uuid, double speed) {
            super(yearMonth, partnerId);
            this.uuid = uuid;
            this.speed = speed;
        }
    }

    @EqualsAndHashCode(callSuper = false)
    public static class BeanBlock3 extends BeanKey {
        String code;
        int type;
        String uid;

        public BeanBlock3(Date yearMonth, long partnerId, String code, int type, String uid) {
            super(yearMonth, partnerId);
            this.code = code;
            this.type = type;
            this.uid = uid;
        }
    }
}
