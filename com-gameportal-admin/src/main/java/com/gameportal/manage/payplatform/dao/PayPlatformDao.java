package com.gameportal.manage.payplatform.dao;


import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.payplatform.model.PayPlatform;
import com.gameportal.manage.sitesettings.model.WebAdEntity;
import com.gameportal.manage.system.dao.BaseIbatisDAO;


@Component
public class PayPlatformDao extends BaseIbatisDAO{

	public Class<PayPlatform> getEntityClass() {
		return PayPlatform.class;
	}
	
	public boolean saveOrUpdate(PayPlatform entity) {
		if(entity.getPpid() == null) 
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else 
			return update(entity);
	}
	
	public List<PayPlatform> getList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}

}
