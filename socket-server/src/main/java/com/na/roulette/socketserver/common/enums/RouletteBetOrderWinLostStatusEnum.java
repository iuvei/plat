package com.na.roulette.socketserver.common.enums;

/**
 * 订单状态。
 * 输赢状态：1 输 2 赢 3 和 0 取消
 * Created by sunny on 2017/5/2 0002.
 */
public enum RouletteBetOrderWinLostStatusEnum {
    LOST(1, "输"),
    WIN(2, "赢"),
    CANCEL(0, "取消");

    private int value;
    private String desc;

    public int get() {
        return value;
    }

    RouletteBetOrderWinLostStatusEnum(int v, String desc) {
        value = v;
        this.desc = desc;
    }

    public static RouletteBetOrderWinLostStatusEnum get(int val) {
        for (RouletteBetOrderWinLostStatusEnum item : RouletteBetOrderWinLostStatusEnum.values()) {
            if (item.value == val) {
                return item;
            }
        }
        return null;
    }
}
