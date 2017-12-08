package com.gameportal.manage.user.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.pay.dao.PayOrderDao;
import com.gameportal.manage.user.dao.AccountMoneyDao;
import com.gameportal.manage.user.dao.CardPackageDao;
import com.gameportal.manage.user.model.AccountMoney;
import com.gameportal.manage.user.service.IAccountMoneyService;

@Service("accountMoneyServiceImpl")
public class AccountMoneyServiceImpl implements IAccountMoneyService {
	private static final Logger logger = Logger
			.getLogger(AccountMoneyServiceImpl.class);
	@Resource
	private AccountMoneyDao accountMoneyDao = null;
	@Resource
	private CardPackageDao cardPackageDao = null;
	@Resource
	private PayOrderDao payOrderDao = null;

	public AccountMoneyServiceImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean saveOrUpdateAccountMoney(AccountMoney userInfo) {
		return accountMoneyDao.saveOrUpdate(userInfo);
	}

	@Override
	public boolean deleteAccountMoney(Long uiid) {
		return accountMoneyDao.delete(uiid);
	}

	@Override
	public boolean updateStatus(Long amid, Integer status) {
		Map<String, Object> params = new HashMap<>();
		if (null != amid) {
			params.put("amid", amid);
		}
		if (null != status) {
			params.put("status", status);
		}
		return accountMoneyDao.updateStatus(params);
	}

	@Override
	public Long queryAccountMoneyCount(Map<String, Object> params) {
		return accountMoneyDao.getRecordCount(params);
	}

	@Override
	public List<AccountMoney> queryAccountMoney(Map<String, Object> params,
			Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return accountMoneyDao.getList(params);
	}

	@Override
	public AccountMoney queryById(Long uiid) {
		return (AccountMoney) accountMoneyDao.findById(uiid);
	}

	@Override
	public boolean updateTotalamount(AccountMoney am) {
		return accountMoneyDao.updateTotalamount(am);
	}

	@Override
	public AccountMoney save(AccountMoney am) {
		return (AccountMoney) accountMoneyDao.save(am);
	}

	@Override
	public AccountMoney getMoneyInfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return accountMoneyDao.getMoneyInfo(params);
	}

}
