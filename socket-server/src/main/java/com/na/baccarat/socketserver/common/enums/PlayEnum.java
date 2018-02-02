package com.na.baccarat.socketserver.common.enums;

/**
 * 玩法Enum
 * 
 * @author alan
 * @date 2017年5月3日 上午11:17:12
 */
public enum PlayEnum {
	
	NORMAL(1,"普通台"),
	FREE_COMMISSION(2,"免佣台"),
	B27(3,"B27");


	int val;
	String desc;

	PlayEnum(int val, String desc) {
		this.val = val;
		this.desc = desc;
	}

	public int get() {
		return val;
	}
	
	public String getDesc() {
		return desc;
	}

	public static PlayEnum get(int status) {
		for(PlayEnum item : PlayEnum.values()){
			if(item.get()==status){
				return item;
			}
		}
		return null;
	}
}