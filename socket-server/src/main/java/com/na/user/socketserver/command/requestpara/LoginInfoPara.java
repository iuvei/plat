package com.na.user.socketserver.command.requestpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.baccarat.socketserver.common.enums.DeviceTypeEnum;

/**
 * 登陆参数
 */
public class LoginInfoPara extends CommandReqestPara {
    @JSONField(name = "uName")
    private String loginName;

    @JSONField(name = "pwd")
    private String pwd;
    
    /**
     * 下注设备类型：1 WEB  2 IOS  3 ANDROID 4 PC
     */
    @JSONField(name = "deviceType")
    private Integer deviceType;
    
    /**
     * 设备信息
     */
    @JSONField(name = "deviceInfo")
    private String deviceInfo;
    
    /**
     * 手机设备号
     */
    @JSONField(name = "deviceNumber")
    private String deviceNumber;
    
    /**
     * 二维码
     */
    @JSONField(name = "bc")
    private String barcode;

    /**
     * 用户类型  空  或者dealer  默认空为用户
     */
    @JSONField(name = "uType")
    private Integer userType;
    
    /**
     * 用户Token登陆
     */
    @JSONField(name = "token")
    private String token;
    
    /**
     * 用户userID  用于token登陆
     */
    @JSONField(name = "userId")
    private String userId;
    
    public String getLoginName() {
        return loginName;
    }

    public String getPwd() {
        return pwd;
    }

    public Integer getUserType() {
        return userType;
    }

    public Integer getDeviceType() {
        return deviceType;
    }
    
    public DeviceTypeEnum getDeviceTypeEnum() {
    	if(deviceType == null) {
    		return null;
    	}
        return DeviceTypeEnum.get(deviceType);
    }

    public String getBarcode() {
        return barcode;
    }

    public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

	public String getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
