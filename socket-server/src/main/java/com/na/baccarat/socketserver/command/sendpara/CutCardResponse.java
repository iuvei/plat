package com.na.baccarat.socketserver.command.sendpara;

import java.math.BigDecimal;

import com.na.user.socketserver.command.sendpara.IResponse;

/**
 * 发牌响应参数
 * 
 * @author alan
 * @date 2017年5月2日 上午10:22:29
 */
public class CutCardResponse implements IResponse {
	
	/**
	 * 切牌点
	 */
	private BigDecimal cutPoint;

	public BigDecimal getCutPoint() {
		return cutPoint;
	}

	public void setCutPoint(BigDecimal cutPoint) {
		this.cutPoint = cutPoint;
	}
	
}
