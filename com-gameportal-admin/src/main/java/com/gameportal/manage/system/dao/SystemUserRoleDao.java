package com.gameportal.manage.system.dao;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.system.model.SystemUserRole;

@Component
public class SystemUserRoleDao extends BaseIbatisDAO {

	public Class<SystemUserRole> getEntityClass() {
		return SystemUserRole.class;
	}

	public boolean saveOrUpdate(SystemUserRole entity) {
		if (entity.getUserRoleId() == null)
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else
			return update(entity);
	}

	public void deleteByUserId(Long userId) {
		getSqlMapClientTemplate().delete(this.getSimpleName()+".deleteByUserId", userId);
	}

}
