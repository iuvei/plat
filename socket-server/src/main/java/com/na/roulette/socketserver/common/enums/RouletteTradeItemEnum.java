package com.na.roulette.socketserver.common.enums;

/**
 * 轮盘交易项
 * 
 * @author alan
 * @date 2017年5月12日 下午2:50:26
 */
public enum RouletteTradeItemEnum {
	
	DIRECT("ONE","直接注"),
	POINT("TWO","分注"),
	STREET("STR","街注"),
	THREE("THR","三数"),
	ANGLE("ANG","角注"),
	FOUR("FOR","四个号码"),
	LINE("LIN","线注"),
	COLUMN("COL","列注"),
	DOZEN("FIG","下注一打"),
	RED("RED","红"),
	BLACK("BLA","黑"),
	ODD("ODD","单"),
	EVEN("EVE","双"),
	BIG("BIG","大"),
	SMALL("SMA","小"),
	;
	
	private String desc;
    private String val;

    RouletteTradeItemEnum(String val,String desc){
        this.val = val;
        this.desc = desc;
    }
    public String get(){return val;}

    public static RouletteTradeItemEnum get(String val){
        for(RouletteTradeItemEnum item : RouletteTradeItemEnum.values()){
            if(item.get().equals(val)){
                return item;
            }
        }
        return null;
    }
}
