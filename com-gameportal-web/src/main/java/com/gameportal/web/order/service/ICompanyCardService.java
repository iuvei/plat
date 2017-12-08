package com.gameportal.web.order.service;

import java.util.List;
import java.util.Map;

import com.gameportal.web.order.model.CompanyCard;

public abstract interface ICompanyCardService {

	Long queryCompanyCardCount(Map<String, Object> params);

	List<CompanyCard> queryCompanyCard(Map<String, Object> params, Integer startNo, Integer pageSize);

	List<CompanyCard> queryByCcgid(Long ccgid);
	
	List<CompanyCard> queryByUuid(Long uiid);
	
	CompanyCard getCompanyCard(long ccid);
	
	List<CompanyCard> queryCompanyCard(Map<String, Object> params);

}
