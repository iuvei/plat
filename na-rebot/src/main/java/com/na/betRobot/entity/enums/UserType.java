package com.na.betRobot.entity.enums;


/**
 * 用户类型
 */
public enum UserType {
	
    SYSTEM(1, "系统用户"), DEALER(2, "荷官用户"),
    SUBACCOUNT(3, "子账号"), REAL(4, "真人用户");

    private int value;
    private String desc;

    public int get() {
        return value;
    }

    UserType(int v, String desc) {
        value = v;
    }
    public static UserType get(int val){
        for(UserType item: UserType.values()){
            if(item.value==val){
                return item;
            }
        }
        return null;
    }
}
