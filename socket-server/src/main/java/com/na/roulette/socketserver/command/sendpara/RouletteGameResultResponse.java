package com.na.roulette.socketserver.command.sendpara;

import com.na.user.socketserver.command.sendpara.CommandResponse;


/**
 * 游戏结果返回参数
 * 
 * @author alan
 * @date 2017年5月6日 下午5:49:33
 */
public class RouletteGameResultResponse extends CommandResponse {
    /**
     * 游戏结果
     */
    private String result;

    
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
    
}
