package com.na.gate.enums;

/**
 * @author Andy
 * @version 创建时间：2017年9月27日 下午12:48:40
 */
public enum ReponseStatusEnum {
	SUCCESS(0, "登录成功"),
	WAIT(1, "需再次推送"),
	ERROR(2, "玩家注册失败"),
	;

	private int val;
	private String desc;

	ReponseStatusEnum(int val, String desc) {
		this.val = val;
		this.desc = desc;
	}

	public int get() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	public static ReponseStatusEnum get(int val) {
		for (ReponseStatusEnum reponse : ReponseStatusEnum.values()) {
			if (reponse.get() == val) {
				return reponse;
			}
		}
		return null;
	}
}
