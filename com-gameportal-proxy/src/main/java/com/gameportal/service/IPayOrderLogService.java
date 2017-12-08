package com.gameportal.service;

import com.gameportal.domain.PayOrderLog;

public interface IPayOrderLogService {
	/**
	 * 新增代理帐变记录
	 * @param userXimaSet
	 * @return
	 */
	public int insert(PayOrderLog log);
}
