package com.gameportal.manage.api.sa;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CheckOrderIdResponse")
public class CheckOrderId {

	@XStreamAlias("isExist")
	private String exist;
	
	@XStreamAlias("ErrorMsgId")
	private String errorMsgId;
	
	@XStreamAlias("ErrorMsg")
	private String errorMsg;

	public String getExist() {
		return exist;
	}

	public void setExist(String exist) {
		this.exist = exist;
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
