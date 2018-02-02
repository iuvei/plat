package com.na.manager.common.websocket;

/**
 * Created by sunny on 2017/6/27 0027.
 */
public enum CmdEnum {
    UNKNOW(0,"未知"),
    LOGIN(1,"登陆"),
    LOGOUT(2,"退出"),
    ;
    private int cmd;
    private String desc;

    CmdEnum(int cmd, String desc) {
        this.cmd = cmd;
        this.desc = desc;
    }
    public static CmdEnum get(int val){
        for(CmdEnum item : CmdEnum.values()){
            if(val==item.get()){
                return item;
            }
        }
        return UNKNOW;
    }
    public int get(){
        return cmd;
    }
}
