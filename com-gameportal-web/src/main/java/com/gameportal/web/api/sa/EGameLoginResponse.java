package com.gameportal.web.api.sa;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("SlotLoginRequestResponse")
public class EGameLoginResponse {

	@XStreamAlias("Token")
	private String token;
	
	@XStreamAlias("DisplayName")
	private String displayName;
	
	@XStreamAlias("GameURL")
	private String gameURL;
	
	@XStreamAlias("ErrorMsgId")
	private String errorMsgId;
	
	@XStreamAlias("ErrorMsg")
	private String errorMsg;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getGameURL() {
		return gameURL;
	}

	public void setGameURL(String gameURL) {
		this.gameURL = gameURL;
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
