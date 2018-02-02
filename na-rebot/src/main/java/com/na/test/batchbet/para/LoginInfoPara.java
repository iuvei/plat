package com.na.test.batchbet.para;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * 
 * @author alan
 * @date 2017年8月15日 下午6:27:00
 */
public class LoginInfoPara{
    @JSONField(name = "uName")
    private String loginName;

    @JSONField(name = "pwd")
    private String pwd;
    
    private String token;

    @JSONField(name = "deviceType")
    private Integer deviceType;

    @JSONField(name = "deviceInfo")
    private String deviceInfo;

    @JSONField(name = "bc")
    private String barcode;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
