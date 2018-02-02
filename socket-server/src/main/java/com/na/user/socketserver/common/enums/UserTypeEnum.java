package com.na.user.socketserver.common.enums;


/**
 * 用户类型
 */
public enum UserTypeEnum {
	
    SYSTEM(1, "系统用户"), DEALER(2, "荷官用户"),
    SUBACCOUNT(3, "子账号"), REAL(4, "真人用户");

    private int value;
    private String desc;

    public int get() {
        return value;
    }

    UserTypeEnum(int v, String desc) {
        value = v;
    }
    public static UserTypeEnum get(int val){
        for(UserTypeEnum item: UserTypeEnum.values()){
            if(item.value==val){
                return item;
            }
        }
        return null;
    }
}
