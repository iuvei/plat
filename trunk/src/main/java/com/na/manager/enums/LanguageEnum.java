package com.na.manager.enums;

/**
 * Created by Administrator on 2017/8/7.
 */
public enum LanguageEnum {
    China(1,"zh-CN"),
    ChinaTaiwan(2,"zh-TW"),
    English(3,"en-US"),
    Korean(4,"sk-SU"),
    ;
    private int val;
    private String desc;

    LanguageEnum(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }
    public int get(){return val;}

    public String getDesc() {
        return desc;
    }

    public static LanguageEnum get(int val){
        for (LanguageEnum languageType : LanguageEnum.values()) {
            if(languageType.get()==val){
                return languageType;
            }
        }
        return null;
    }
}
