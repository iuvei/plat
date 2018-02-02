package com.na.baccarat.socketserver.common.enums;

/**
 * 订单状态。
 * 输赢状态：1 输 2 赢 3 和 0 取消
 * Created by sunny on 2017/5/2 0002.
 */
public enum BetOrderWinLostStatusEnum {
    LOST(1, "输"),
    WIN(2, "赢"),
    TIE (3, "和"),
    CANCEL(0, "取消"),;

    private int value;
    private String desc;

    public int get() {
        return value;
    }

    BetOrderWinLostStatusEnum(int v, String desc) {
        value = v;
        this.desc = desc;
    }

    public static BetOrderWinLostStatusEnum get(int val) {
        for (BetOrderWinLostStatusEnum item : BetOrderWinLostStatusEnum.values()) {
            if (item.value == val) {
                return item;
            }
        }
        return null;
    }
}
