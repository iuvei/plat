package com.na.baccarat.socketserver.common.enums;

/**
 * 投注来源.
 * Created by sunny on 2017/5/2 0002.
 */
public enum BetOrderSourceEnum {
    SEAT(1,"座位下注"),
    SIDENOTE(2,"旁注"),
    MANY_TYPE(3,"多台下注"),
    GOOD_ROAD(4,"好路下注"),
    ;

    private int value;
    private String desc;

    public int get() {
        return value;
    }

    BetOrderSourceEnum(int v, String desc) {
        value = v;
        this.desc = desc;
    }

    public static BetOrderSourceEnum get(int val) {
        for (BetOrderSourceEnum item : BetOrderSourceEnum.values()) {
            if (item.value == val) {
                return item;
            }
        }
        return null;
    }
}