package com.gameportal.persistence;

import java.util.List;
import java.util.Map;

import com.gameportal.domain.Page;
import com.gameportal.domain.PayOrder;
import com.gameportal.domain.ProxyTransferLog;

/**
 * 交易订单Dao
 * @author leron
 *
 */
public interface PayOrderMapper {

	/**
	 * 代理下线出入款记录
	 * @param page
	 * @return
	 */
	public List<PayOrder> findlistPageOrderLog(Page page);
	
	/**
	 * 查询代理下线出入款总计
	 * @param params
	 * @return
	 */
	public String selectProxyPayOrderTotal(Map<String, Object> params);
	
	/**
	 * 新增代理帐变记录
	 * @param userXimaSet
	 * @return
	 */
	public int insert(PayOrder log);
}
