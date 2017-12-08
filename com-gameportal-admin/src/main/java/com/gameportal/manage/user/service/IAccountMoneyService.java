package com.gameportal.manage.user.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.user.model.AccountMoney;

public abstract interface IAccountMoneyService {

	boolean saveOrUpdateAccountMoney(AccountMoney member);

	boolean deleteAccountMoney(Long amid);

	boolean updateStatus(Long uiid, Integer status);

	Long queryAccountMoneyCount(Map<String, Object> params);

	List<AccountMoney> queryAccountMoney(Map<String, Object> params,
			Integer startNo, Integer pageSize);

	AccountMoney queryById(Long amid);

	boolean updateTotalamount(AccountMoney am);

	AccountMoney save(AccountMoney am);
	public AccountMoney getMoneyInfo(Map<String, Object> params);
}
