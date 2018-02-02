package com.na.user.socketserver.command.sendpara;

/**
 * 修改秘钥
 * 
 * @author alan
 * @date 2017年5月4日 上午10:26:14
 */
public class ChangeKeyResponse extends CommandResponse{

	private String key;
	
	private String test;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
	
}
