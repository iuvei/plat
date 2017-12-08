package com.gameportal.manage.gameplatform.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.gameplatform.model.MGElectronic;

public interface IMGElectronicService {

	public Long getCount(Map<String, Object> params);
	
	public List<MGElectronic> getListPageSize(Map<String, Object> params,int pageSize,int thisPage);
	
	public List<MGElectronic> getList(Map<String, Object> params);
}
