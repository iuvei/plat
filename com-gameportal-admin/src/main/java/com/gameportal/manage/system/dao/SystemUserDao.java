package com.gameportal.manage.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.system.model.SystemUser;

@Component
public class SystemUserDao extends BaseIbatisDAO {

	public Class<SystemUser> getEntityClass() {
		return SystemUser.class;
	}

	public boolean saveOrUpdate(SystemUser entity) {
		if (entity.getUserId() == null)
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else
			return update(entity);
	}
	
	public List<SystemUser> getList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}

}
