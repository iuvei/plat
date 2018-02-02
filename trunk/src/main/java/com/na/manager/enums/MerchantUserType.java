package com.na.manager.enums;

/**
 * Created by sunny on 2017/6/21 0021.
 */
public enum MerchantUserType {
    UNKNOWN(0,"未知"),
    LINE_MERCHANT(1,"线路商"),
    MERCHANT(2,"商户"),
    ;
    private int val;
    private String desc;

    MerchantUserType(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }
    public int get(){return val;}

    public String getDesc() {
        return desc;
    }

    public static MerchantUserType get(int val){
        for (MerchantUserType userType : MerchantUserType.values()) {
            if(userType.get()==val){
                return userType;
            }
        }
        return UNKNOWN;
    }
}
