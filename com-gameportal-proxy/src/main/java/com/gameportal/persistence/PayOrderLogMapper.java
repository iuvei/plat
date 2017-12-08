package com.gameportal.persistence;

import com.gameportal.domain.PayOrderLog;

/**
 * 交易订单Dao
 * @author leron
 *
 */
public interface PayOrderLogMapper {
	/**
	 * 新增代理帐变记录
	 * @param userXimaSet
	 * @return
	 */
	public int insert(PayOrderLog log);
}
