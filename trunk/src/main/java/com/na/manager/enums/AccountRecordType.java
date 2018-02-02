package com.na.manager.enums;

/**
 * Created by sunny on 2017/6/21 0021.
 */
public enum AccountRecordType {
    INTO(1,"转入"),
    OUT(2,"转出"),
    BET(3,"下注"),
    RETURN_REWARD(4,"返奖"),
    REBACK(5,"返还")
    ;
    private int val;
    private String desc;

    AccountRecordType(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }
    public int get(){return val;}

    public String getDesc() {
        return desc;
    }

    public static AccountRecordType get(int val){
        for (AccountRecordType userType : AccountRecordType.values()) {
            if(userType.get()==val){
                return userType;
            }
        }
        return null;
    }
}
