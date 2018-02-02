package com.na.baccarat.socketserver.command.sendpara;

import com.na.user.socketserver.command.sendpara.IResponse;


/**
 * 游戏结果返回参数
 * 
 * @author alan
 * @date 2017年5月6日 下午5:49:33
 */
public class GameResultResponse implements IResponse {
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
