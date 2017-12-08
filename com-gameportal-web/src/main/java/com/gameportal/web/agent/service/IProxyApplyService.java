package com.gameportal.web.agent.service;

import java.util.Map;

import com.gameportal.web.agent.model.ProxyApply;

public abstract interface IProxyApplyService {

	/**
	 * 保存代理舍申请。
	 * 
	 * @param proxy
	 *            代理申请信息
	 * @return 返回代理申请实体。
	 */
	ProxyApply saveWebProxy(ProxyApply proxy);

	/**
	 * 根据条件查询代理申请数。
	 * 
	 * @param paramMap
	 *            检索条件
	 * @return 返回符合条件的代理申请数。
	 */
	long queryProxyApplyCount(Map<String, Object> paramMap);
}
