package com.gameportal.web.user.service;

import java.util.List;
import java.util.Map;

import com.gameportal.web.user.model.BetLogTotal;
import com.gameportal.web.user.model.MemberXimaMain;
import com.gameportal.web.user.model.ProxySet;

public interface IMemberXimaMainService {

	/**
	 * 查询会员洗码数据
	 * @param params
	 * @return
	 */
	public List<MemberXimaMain> queryMemberXimaMainList(Map<String, Object> params);
	
	/**
	 * 查询会员洗码总数
	 * @param params
	 * @return
	 */
	public long queryMemberXimaMainCount(Map<String, Object> params);
	
	/**
	 * 查询可洗码会员
	 * @param params
	 * @return
	 */
	public List<BetLogTotal> queryXimaBetTotal(Map<String, Object> params);
	
	/**
	 * 查询可洗码会员总计
	 * @param params
	 * @return
	 */
	public long queryXimaBetLogCount(Map<String, Object> params);
	
	/**
	 * 结算会员洗码
	 * @param params
	 * @return
	 */
	public String saveMemberXima(Map<String, Object> params);
	
	/**
	 * 定时结算会员洗码
	 * @param params
	 * @return
	 */
	public String saveMemberXimaYesterDay(Map<String, Object> params,List<String> accounts);
	
	/**
	 * 定时代理天洗码
	 * @param accounts
	 * @return
	 */
	public String saveProxyDayClearing(Map<String, Object> params,List<ProxySet> accounts) throws Exception;
	
	List<ProxySet> selectProxyDayClearing(Map<String, Object> params);
	
}
