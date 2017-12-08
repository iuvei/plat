package com.gameportal.manage.system.dao;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.system.model.SystemRoleModule;

@Component
public class SystemRoleModuleDao extends BaseIbatisDAO {

	public Class<SystemRoleModule> getEntityClass() {
		return SystemRoleModule.class;
	}

	public boolean saveOrUpdate(SystemRoleModule entity) {
		if (entity.getRoleModuleId() == null)
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else
			return update(entity);
	}

	public void deleteByRoleId(Long roleId) {
		getSqlMapClientTemplate().delete(getSimpleName() + ".deleteByRoleId",
				roleId);
	}
	
}
