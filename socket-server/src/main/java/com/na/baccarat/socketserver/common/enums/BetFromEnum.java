package com.na.baccarat.socketserver.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum BetFromEnum {
	COMMON(0),MANY(1),VIPCOMMON(2),VIP(3),SPEECHCOMMON(4),SPEECH(5),GOODROAD(6),SEAT(7);
	static Map<Integer, BetFromEnum> box = new HashMap<Integer, BetFromEnum>();
	static {
		box.put(0, BetFromEnum.COMMON);
		box.put(1, BetFromEnum.MANY);
		box.put(2, BetFromEnum.VIPCOMMON);
		box.put(3, BetFromEnum.VIP);
		box.put(4, BetFromEnum.SPEECHCOMMON);
		box.put(5, BetFromEnum.SPEECH);
		box.put(6, BetFromEnum.GOODROAD);
		box.put(7, BetFromEnum.SEAT);		
	}

	int betModel;

	BetFromEnum(int betModel) {
		this.betModel = betModel;
	}

	public int getBetModel() {
		return betModel;
	}
	
	public static int getValue(BetFromEnum enumType) {
		int value = -1;
		for(Map.Entry<Integer, BetFromEnum> entry : box.entrySet()){
			if(entry.getValue().equals(enumType)){
				return entry.getKey();
			}
		}
		return value;
	}

	public static BetFromEnum get(int betModel) {
		return box.get(betModel);
	}
}
