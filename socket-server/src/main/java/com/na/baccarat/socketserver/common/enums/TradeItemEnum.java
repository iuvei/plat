package com.na.baccarat.socketserver.common.enums;

/**
 * 玩法Enum
 * 
 * @author alan
 * @date 2017年5月3日 上午11:17:12
 */
public enum TradeItemEnum {
	
	BANKER("B","庄"),
	PLAYER("P","闲"),
	TIE("T","和"),
	BANKER_DOUBLE("BD","庄对"),
	PLAYER_DOUBLE("PD","闲对"),
	BANKER_CASE("BC","庄例"),
	PLAYER_CASE("PC","闲例"),
	ODD("OD","单"),
	EVEN("EV","双"),
	BIG("BG","大"),
	SMALL("SL","小"),
	SUPER_SIX("N6","免拥6"),
	B27("B27","B27"),
	;


	String val;
	String desc;

	TradeItemEnum(String val, String desc) {
		this.val = val;
		this.desc = desc;
	}

	public String get() {
		return val;
	}
	
	public String getDesc() {
		return desc;
	}

	public static TradeItemEnum get(String status) {
		for(TradeItemEnum item : TradeItemEnum.values()){
			if(item.get().equals(status)){
				return item;
			}
		}
		return null;
	}
}