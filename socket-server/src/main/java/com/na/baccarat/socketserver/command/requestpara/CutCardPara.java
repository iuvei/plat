package com.na.baccarat.socketserver.command.requestpara;

import java.math.BigDecimal;

import com.na.user.socketserver.command.requestpara.CommandReqestPara;

/**
 * 切牌请求参数
 * 
 * @author alan
 * @date 2017年4月29日 下午4:37:36
 */
public class CutCardPara extends CommandReqestPara {

	/**
	 * 切点
	 */
    private BigDecimal cutPoint;

	public BigDecimal getCutPoint() {
		return cutPoint;
	}

	public void setCutPoint(BigDecimal cutPoint) {
		this.cutPoint = cutPoint;
	}
    
}
