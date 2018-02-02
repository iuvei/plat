package com.na.test.batchbet.common;

/**
 * 命令集合.
 * <ul>
 * <LI>DEALER_开头的为荷官发出的指令。</LI>
 * <LI>CLIENT_开头的为客户发出的指令。</LI>
 * <LI>COMMON_开头的为多个不同端发出的指令。</LI>
 * </ul>
 * Created by Sunny on 2017/4/27 0027.
 */
public enum ResponseCommandEnum {
    
	/**成功*/
    OK ("ok"),
    // 失败
    ERROR("error");

    

    private String val;
    public String get(){return val;}
    ResponseCommandEnum(String val){
        this.val = val;
    }
    public static ResponseCommandEnum get(String val){
        for (ResponseCommandEnum commandEnum : ResponseCommandEnum.values()) {
            if(commandEnum.val.equalsIgnoreCase(val)){
                return commandEnum;
            }
        }
        return null;
    }
}
