package com.na.baccarat.socketserver.common.enums;

/**
 * Created by sunny on 2017/4/28 0028.
 */
public enum  GameTableStatusEnum {
    NOR(0,"正常"),
    CLOSE(1,"关闭");

    private String desc;
    private int val;

    GameTableStatusEnum(int val,String desc){
        this.val = val;
        this.desc = desc;
    }
    public int get(){return val;}

    public static GameTableStatusEnum get(int val){
        for(GameTableStatusEnum item : GameTableStatusEnum.values()){
            if(item.get()==val){
                return item;
            }
        }
        return null;
    }
}
