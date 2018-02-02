package com.na.baccarat.socketserver.common.enums;

/**
 * 选择房间搜索标志
 * 
 * @author alan
 * @date 2017年8月7日 下午1:58:38
 */
public enum SelectRoomSearchType {
	
	//满人房
	FULL(1),
	//缺人房
	LACK(2),
	//空房
	EMPTY(3),
	;


	int value;

	SelectRoomSearchType(int value) {
		this.value = value;
	}

	public int get() {
		return value;
	}

	public static SelectRoomSearchType get(int value) {
		for(SelectRoomSearchType item : SelectRoomSearchType.values()){
			if(item.get() == value){
				return item;
			}
		}
		return null;
	}
}