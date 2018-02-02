package com.na.user.socketserver.common.enums;


/**
 * 用户类型
 */
public enum LiveUserTypeEnum {
	
    AGENT(1, "代理"), 
    PLAYER(2, "会员"),
    GENERAL_PLAYER(3, "总代理"),
    ;

    private int value;
    private String desc;

    public int get() {
        return value;
    }

    LiveUserTypeEnum(int v, String desc) {
        value = v;
    }
    public static LiveUserTypeEnum get(int val){
        for(LiveUserTypeEnum item: LiveUserTypeEnum.values()){
            if(item.value==val){
                return item;
            }
        }
        return null;
    }
}
