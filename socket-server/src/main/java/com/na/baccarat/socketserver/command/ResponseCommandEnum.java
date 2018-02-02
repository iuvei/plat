package com.na.baccarat.socketserver.command;

/**
 * 命令集合.
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
