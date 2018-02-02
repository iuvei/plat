package com.na.manager.enums;
/**
* @author Andy
* @version 创建时间：2017年9月9日 下午1:48:39
*/
public enum RoomStaEnum {
	UNKNOWN(0, "未知"), 
	TEAMSTA(1, "下级团队"),
	PROXYSTA(2, "代理汇总"),
	ROOMSTA(3, "房间汇总"),
	ROUNDSTA(4, "单局汇总"),
	BETVIEW(5, "投注详情"),
	;

	public Integer val;
	public String text;

	private RoomStaEnum(Integer val, String text) {
		this.val = val;
		this.text = text;
	}

	public int get() {
		return val;
	}
	
	public String getText() {
		return text;
	}

	public static RoomStaEnum get(int val) {
		for (RoomStaEnum statisType : RoomStaEnum.values()) {
			if (statisType.get() == val) {
				return statisType;
			}
		}
		return UNKNOWN;
	}
}
