package com.na.baccarat.socketserver.common.enums;

/**
 * 桌台类型.
 * Created by sunny on 2017/5/2 0002.
 */
public enum BetOrderTableTypeEnum {
    COMMON(1,"普通台"),
    VIP(2,"VIP台"),
    ;

    private int value;
    private String desc;

    public int get() {
        return value;
    }

    BetOrderTableTypeEnum(int v, String desc) {
        value = v;
        this.desc = desc;
    }

    public static BetOrderTableTypeEnum get(int val) {
        for (BetOrderTableTypeEnum item : BetOrderTableTypeEnum.values()) {
            if (item.value == val) {
                return item;
            }
        }
        return null;
    }
}