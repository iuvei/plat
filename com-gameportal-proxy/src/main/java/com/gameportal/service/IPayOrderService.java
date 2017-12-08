package com.gameportal.service;

import java.util.List;
import java.util.Map;

import com.gameportal.domain.Page;
import com.gameportal.domain.PayOrder;

/**
 * 交易订单service接口
 * @author leron
 *
 */
public interface IPayOrderService {

	/**
	 * 代理下线出入款记录
	 * @param page
	 * @return
	 */
	public List<PayOrder> queryDepositOrderLog(Page page);
	
	/**
	 * 代理下线出入款总计
	 * @param params
	 * @return
	 */
	public String queryOrderLogTotal(Map<String, Object> params);
	
	/**
	 * 新增代理充值记录
	 * @param userXimaSet
	 * @return
	 */
	public int insert(PayOrder log);
}
