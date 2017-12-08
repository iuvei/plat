package com.gameportal.manage.api.sa;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("KickUserResponse")
public class KickUserResponse {
	
	@XStreamAlias("ErrorMsgId")
	private String errorMsgId;
	
	@XStreamAlias("ErrorMsg")
	private String errorMsg;

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
