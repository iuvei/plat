package com.na.baccarat.socketserver.command.requestpara;

import com.na.user.socketserver.command.requestpara.CommandReqestPara;

/**
 * 通过玩法ID,返回交易项列表
 * 
 */
public class TradeItemPara extends CommandReqestPara {
	
	//玩法id
	private Integer playId;

	public Integer getPlayId() {
		return playId;
	}

	public void setPlayId(Integer playId) {
		this.playId = playId;
	}
	
	
	
    
    
}
