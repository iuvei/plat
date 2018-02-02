package com.na.test.batchbet.common;

/**
 * 投注类型.
 * Created by sunny on 2017/5/2 0002.
 */
public enum BetOrderBetTypeEnum {
    COMMON(1,"普通投注"),
    VIP(2,"语音投注"),
    ;

    private int value;
    private String desc;

    public int get() {
        return value;
    }

    BetOrderBetTypeEnum(int v, String desc) {
        value = v;
        this.desc = desc;
    }

    public static BetOrderBetTypeEnum get(int val) {
        for (BetOrderBetTypeEnum item : BetOrderBetTypeEnum.values()) {
            if (item.value == val) {
                return item;
            }
        }
        return null;
    }
}