package com.gameportal.manage.system.dao;

import org.springframework.stereotype.Component;

import com.gameportal.manage.system.model.SystemDepartment;

@Component
public class SystemDepartmentDao extends BaseIbatisDAO {

	public Class<SystemDepartment> getEntityClass() {
		return SystemDepartment.class;
	}

	public void saveOrUpdate(SystemDepartment entity) {
		if (entity.getSdid() == null)
			save(entity);
		else
			update(entity);
	}

}
