package com.gameportal.manage.userproxy.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.betlog.model.BetLogTotal;

public interface IProxyManageService {

	/**
	 * 查询代理下级会员
	 * @param params
	 * @param thisPage
	 * @param pageSize
	 * @return
	 */
	public List<BetLogTotal> selectProxyDownUser(Map<String, Object> params,int thisPage,int pageSize);
	public Long selectProxyDownUserCount(Map<String, Object> params);
	
	/**
	 * 代理自助洗码
	 * @param params
	 * @return
	 */
	public String ximajs(Map<String, Object> params)throws Exception;
	
}
