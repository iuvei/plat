package com.gameportal.web.api.sa;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * SA注册返回
 * @author Administrator
 *
 */
@XStreamAlias("RegUserInfoResponse")
public class UserInfoResponse {
	@XStreamAlias("Username")
	private String username;
	
	@XStreamAlias("ErrorMsgId")
	private String errorMsgId;
	
	@XStreamAlias("ErrorMsg")
	private String errorMsg;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getErrorMsgId() {
		return errorMsgId;
	}

	public void setErrorMsgId(String errorMsgId) {
		this.errorMsgId = errorMsgId;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
