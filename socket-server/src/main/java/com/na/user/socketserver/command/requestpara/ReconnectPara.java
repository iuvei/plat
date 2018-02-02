package com.na.user.socketserver.command.requestpara;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 重连参数
 * 
 * @author alan
 * @date 2017年5月24日 上午11:50:40
 */
public class ReconnectPara extends CommandReqestPara {

    @JSONField(name = "token")
    private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
    
    
}
