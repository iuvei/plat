package com.gameportal.manage.xima.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.system.dao.BaseIbatisDAO;
import com.gameportal.manage.xima.model.MemberXimaMain;
import com.gameportal.manage.xima.model.ProxyXimaMain;


@Component
public class ProxyXimaMainDao extends BaseIbatisDAO{

	public Class<ProxyXimaMain> getEntityClass() {
		return ProxyXimaMain.class;
	}
	
	public boolean saveOrUpdate(ProxyXimaMain entity) {
		if(entity.getPxmid() == null) 
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else 
			return update(entity);
	}

	public List<ProxyXimaMain> getList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}

}
