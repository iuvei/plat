package com.na.betRobot.entity.enums;


/**
 * 用户类型
 */
public enum LiveUserType {
	
    AGENT(1, "代理"), 
    PLAYER(2, "会员"),
    GENERAL_PLAYER(3, "总代理"),
    ;

    private int value;
    private String desc;

    public int get() {
        return value;
    }

    LiveUserType(int v, String desc) {
        value = v;
    }
    public static LiveUserType get(int val){
        for(LiveUserType item: LiveUserType.values()){
            if(item.value==val){
                return item;
            }
        }
        return null;
    }
}
