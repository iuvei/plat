package com.gameportal.manage.order.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.order.model.CompanyCard;
import com.gameportal.manage.system.model.SystemUser;

public abstract interface ICompanyCardService {

	boolean saveOrUpdateCompanyCard(CompanyCard card);

	boolean deleteCompanyCard(Long ccid);

	boolean updateStatus(Long ccid, Integer status, SystemUser sysuser);

	Long queryCompanyCardCount(Map<String, Object> params);

	List<CompanyCard> queryCompanyCard(Map<String, Object> params, Integer startNo, Integer pageSize);

	List<CompanyCard> queryByCcgid(Long ccgid);
	
	CompanyCard getByUuid(Long uiid);

}
