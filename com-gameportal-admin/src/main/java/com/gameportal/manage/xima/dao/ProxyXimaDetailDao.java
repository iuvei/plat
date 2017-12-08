package com.gameportal.manage.xima.dao;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.system.dao.BaseIbatisDAO;
import com.gameportal.manage.xima.model.ProxyXimaDetail;


@Component
public class ProxyXimaDetailDao extends BaseIbatisDAO{

	public Class<ProxyXimaDetail> getEntityClass() {
		return ProxyXimaDetail.class;
	}
	
	public boolean saveOrUpdate(ProxyXimaDetail entity) {
		if(entity.getPxdid() == null) 
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else 
			return update(entity);
	}

}
