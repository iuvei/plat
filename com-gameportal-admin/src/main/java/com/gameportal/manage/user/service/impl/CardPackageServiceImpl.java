package com.gameportal.manage.user.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.gameportal.manage.user.dao.CardPackageDao;
import com.gameportal.manage.user.model.AccountMoney;
import com.gameportal.manage.user.model.CardPackage;
import com.gameportal.manage.user.service.ICardPackageService;



@Service("cardPackageServiceImpl")
public class CardPackageServiceImpl implements ICardPackageService {
 @Resource
 private  CardPackageDao cardPackageDao;
	
	
	@Override
	public boolean saveOrUpdateCardPackage(CardPackage cardpackage) {
		return cardPackageDao.saveOrUpdate(cardpackage);
	}

	@Override
	public boolean deleteCardPackage(Long cpid) {
		return cardPackageDao.delete(cpid);
	}

	@Override
	public boolean updateStatus(Long cpid, Integer status) {
		Map<String, Object> params = new HashMap<>();
		if (null != cpid) {
			params.put("cpid", cpid);
		}
		if (null != status) {
			params.put("status", status);
		}
		return cardPackageDao.updateStatus(params);
	}

	@Override
	public Long queryCardPackageCount(Map<String, Object> params) {
		return cardPackageDao.getRecordCount(params);
	}

	@Override
	public List<CardPackage> queryCardPackage(Map<String, Object> params,
			Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return cardPackageDao.getList(params);
	}

	@Override
	public CardPackage queryById(Long cpid) {
		return (CardPackage) cardPackageDao.findById(cpid);
	}

}
