package com.na.baccarat.socketserver.common.limitrule;

import java.math.BigDecimal;

/**
 * 计算赢额方式
 * 
 * @author alan
 * @date 2017年6月15日 下午3:12:08
 */
public interface LimitCalcPattern {
	
	public BigDecimal calc();

}
