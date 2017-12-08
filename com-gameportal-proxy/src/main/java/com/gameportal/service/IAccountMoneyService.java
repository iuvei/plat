package com.gameportal.service;

import java.util.Map;

import com.gameportal.domain.AccountMoney;
import com.gameportal.domain.MemberInfo;

/**
 * 钱包余额Service接口
 * @author leron
 *
 */
public interface IAccountMoneyService {

	/**
	 * 查询钱包余额
	 * @param params
	 * @return
	 */
	public AccountMoney queryAccountMoney(Map<String, Object> params);
	

	/**
	 * 新增代理钱包记录
	 * @param money
	 * @return
	 */
	public int insertAccountMoney(AccountMoney money);
	
	/**
	 * 修改代理钱包记录
	 * @param money
	 * @return
	 */
	public int updateAccountMoney(AccountMoney money);
	
	
	public void updateProxyTransfer(MemberInfo member,MemberInfo lower,Integer amount);
	
}
