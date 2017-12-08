package com.gameportal.manage.order.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.order.model.CCGroup;
import com.gameportal.manage.order.model.CompanyCard;
import com.gameportal.manage.system.dao.BaseIbatisDAO;


@Component
public class CCGroupDao extends BaseIbatisDAO{

	public Class<CCGroup> getEntityClass() {
		return CCGroup.class;
	}
	
	public boolean saveOrUpdate(CCGroup entity) {
		if(entity.getCcgid() == null) 
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else 
			return update(entity);
	}
	
	public List<CCGroup> getList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}

}
