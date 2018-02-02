package com.na.user.socketserver.common.enums;

/**
 * @author andy
 * @date 2017年6月28日 下午1:50:12
 * 
 */
public enum IpBlackWhitePlatType {
	DEALER(0, "荷官端-白名单"),
	GAMEUI(1, "游戏前台"),
	SYSTEM(2, "系统后台-白名单"),
	MANAGE(3, "代理后台"),
	API(4, "API现金网"),
	;

	public Integer val;
	public String text;

	private IpBlackWhitePlatType(Integer val, String text) {
		this.val = val;
		this.text = text;
	}

	public int get() {
		return val;
	}

	public String getText() {
		return text;
	}

	public static IpBlackWhitePlatType get(int val) {
		for (IpBlackWhitePlatType plat : IpBlackWhitePlatType.values()) {
			if (plat.get() == val) {
				return plat;
			}
		}
		return SYSTEM;
	}
}
