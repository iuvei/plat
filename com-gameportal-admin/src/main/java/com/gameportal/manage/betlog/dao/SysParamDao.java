package com.gameportal.manage.betlog.dao;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.system.dao.BaseIbatisDAO;


import com.gameportal.manage.betlog.model.SysParam;


@Component
public class SysParamDao extends BaseIbatisDAO{

	public Class<SysParam> getEntityClass() {
		return SysParam.class;
	}
	
	public boolean saveOrUpdate(SysParam entity) {
		if(entity.getId() == null) 
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else 
			return update(entity);
	}

}
