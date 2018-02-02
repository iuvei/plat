package com.na.baccarat.socketserver.common.enums;

/**
 * 订单状态。
 * 状态：1 新建 2 确认 3 结算 0 取消
 * Created by sunny on 2017/5/2 0002.
 */
public enum BetOrderStatusEnum {
    NEW(1, "新建"),
    CONFIRM(2, "确认"),
    SETTLE (3, "结算"),
    CANCEL(4, "取消"),;

    private int value;
    private String desc;

    public int get() {
        return value;
    }

    BetOrderStatusEnum(int v, String desc) {
        value = v;
        this.desc = desc;
    }

    public static BetOrderStatusEnum get(int val) {
        for (BetOrderStatusEnum item : BetOrderStatusEnum.values()) {
            if (item.value == val) {
                return item;
            }
        }
        return null;
    }
}
