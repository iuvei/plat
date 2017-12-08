package com.gameportal.manage.user.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.user.model.CardPackage;

public abstract interface ICardPackageService {

	boolean saveOrUpdateCardPackage(CardPackage cardpackage);

	boolean deleteCardPackage(Long cpid);

	boolean updateStatus(Long cpid, Integer status);

	Long queryCardPackageCount(Map<String, Object> params);

	List<CardPackage> queryCardPackage(Map<String, Object> params,
			Integer startNo, Integer pageSize);

	CardPackage queryById(Long cpid);
	
}
