package com.gameportal.manage.proxy.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.gameportal.manage.proxy.dao.ProxyUserXimaDao;
import com.gameportal.manage.proxy.model.ProxyUserXimaLog;
import com.gameportal.manage.proxy.service.IProxyUserXimaService;
import com.gameportal.manage.user.dao.AccountMoneyDao;
import com.gameportal.manage.user.model.AccountMoney;
@Service("proxyUserXimaService")
public class ProxyUserXimaServiceImpl implements IProxyUserXimaService{
	
	@Resource(name = "proxyUserXimaDao")
	private ProxyUserXimaDao proxyUserXimaDao;
	
	@Resource(name = "accountMoneyDao")
	private AccountMoneyDao accountMoneyDao;

	@Override
	public List<ProxyUserXimaLog> getList(Map<String, Object> params,
			int thisPage, int pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(thisPage))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			thisPage = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", thisPage);
		params.put("pageSize", pageSize);
		return proxyUserXimaDao.getList(params);
	}

	@Override
	public Long getCount(Map<String, Object> params) {
		return proxyUserXimaDao.getRecordCount(params);
	}

	@Override
	public boolean update(ProxyUserXimaLog entity) {
		return proxyUserXimaDao.update(entity);
	}

	@Override
	public boolean save(ProxyUserXimaLog entity) {
		return proxyUserXimaDao.save(entity);
	}

	@Override
	public ProxyUserXimaLog getObject(Map<String, Object> params) {
		return proxyUserXimaDao.getObject(params);
	}

	@Override
	public String rz(ProxyUserXimaLog entity) throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uiid", entity.getUiid());
		AccountMoney accountMoney = accountMoneyDao.getMoneyInfo(params);
		accountMoney.setTotalamount(new BigDecimal(entity.getXimamoney()));
		accountMoney.setUpdateDate(new Date());
		if(!accountMoneyDao.updateTotalamount(accountMoney)){
			throw new Exception("系统错误入账失败：更新用户钱包失败。");
		}
		entity.setStatus(1);
		if(!proxyUserXimaDao.update(entity)){
			throw new Exception("系统错误入账失败：更新洗码记录失败。");
		}
		return "0";
	}

}
