package com.na.roulette.socketserver.command.requestpara;

import com.na.user.socketserver.command.requestpara.CommandReqestPara;

public class GameResultPara extends CommandReqestPara{

	private String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
}
