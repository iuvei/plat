package com.na.user.socketserver.command.requestpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.baccarat.socketserver.common.enums.DeviceTypeEnum;

/**
 * 试玩请求参数
 * 
 * @author alan
 * @date 2017年6月5日 上午11:22:52
 */
public class DemoPara extends CommandReqestPara {
	
	/**
	 * 试玩类型
	 * 1. 代理试玩(默认)  2. API试玩
	 */
    @JSONField(name = "type")
    private Integer type;
    
    /**
     * 下注设备类型：1 WEB  2 IOS  3 ANDROID
     */
    @JSONField(name = "deviceType")
    private Integer deviceType;

    @JSONField(name = "deviceInfo")
    private String deviceInfo;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	
	public DeviceTypeEnum getDeviceTypeEnum() {
    	if(deviceType == null) {
    		return null;
    	}
        return DeviceTypeEnum.get(deviceType);
    }
    
}
