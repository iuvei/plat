package com.na.roulette.socketserver.command.sendpara;

import com.na.user.socketserver.command.sendpara.CommandResponse;

/**
 * 停止下注响应参数
 * 
 * @date 2017年5月23日 下午2:13:47
 */
public class RouletteStopBetResponse extends CommandResponse {
	
	
	/**
     * 桌子展示状态
     */
    private String showTableStatus;
	

	public String getShowTableStatus() {
		return showTableStatus;
	}

	public void setShowTableStatus(String showTableStatus) {
		this.showTableStatus = showTableStatus;
	}
	
	
}
