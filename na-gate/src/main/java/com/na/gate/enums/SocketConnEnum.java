package com.na.gate.enums;

/**
 * @author Andy
 * @version 创建时间：2017年9月27日 下午12:48:40
 */
public enum SocketConnEnum {
	CONNECTING(0, "连接中"),
	SUCCESS(1, "连接成功"),
	FAIL(2, "连接失败"),
	;

	private int val;
	private String desc;

	SocketConnEnum(int val, String desc) {
		this.val = val;
		this.desc = desc;
	}

	public int get() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	public static SocketConnEnum get(int val) {
		for (SocketConnEnum reponse : SocketConnEnum.values()) {
			if (reponse.get() == val) {
				return reponse;
			}
		}
		return null;
	}
}
