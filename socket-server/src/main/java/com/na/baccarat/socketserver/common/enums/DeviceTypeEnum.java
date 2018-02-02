package com.na.baccarat.socketserver.common.enums;

/**
 * 设备类型.
 */
public enum DeviceTypeEnum {
	UNKNOWN(0),WEB(1), IOS(2), ANDROID(3), PC(4);

	int val;
	DeviceTypeEnum(int val){
		this.val = val;
	}

	public int get(){
		return this.val;
	}

	public static DeviceTypeEnum get(int val) {
		for (DeviceTypeEnum deviceTypeEnum : DeviceTypeEnum.values()) {
			if(deviceTypeEnum.get()==val){
				return deviceTypeEnum;
			}
		}
		return UNKNOWN;
	}
}
