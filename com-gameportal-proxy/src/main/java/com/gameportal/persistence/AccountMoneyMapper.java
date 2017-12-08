package com.gameportal.persistence;

import java.util.List;
import java.util.Map;

import com.gameportal.domain.AccountMoney;

/**
 * 钱包余额Dao
 * @author leron
 *
 */
public interface AccountMoneyMapper {

	/**
	 * 查询钱包余额
	 * @param params
	 * @return
	 */
	public List<AccountMoney> getMoneyInfo(Map<String, Object> params);
	
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
	
}
