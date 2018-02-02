package com.na.baccarat.socketserver.common.enums;

/**
 * 账户类型。
 * 类型(1转入 2转出 3 下注 4返奖 5 返还)
 * Created by sunny on 2017/5/2 0002.
 */
public enum AccountRecordTypeEnum {
    IN(1, "转入"),
    OUT(2, "转出"),
    BET(3, "下注"),
    RETURN_BONUS(4, "4返奖"),
    RETURN(5, "返还"),
    ;

    private int value;
    private String desc;

    public int get() {
        return value;
    }

    AccountRecordTypeEnum(int v, String desc) {
        value = v;
        this.desc = desc;
    }

    public static AccountRecordTypeEnum get(int val) {
        for (AccountRecordTypeEnum item : AccountRecordTypeEnum.values()) {
            if (item.value == val) {
                return item;
            }
        }
        return null;
    }
}
