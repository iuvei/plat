package com.gameportal.manage.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.system.model.SystemModule;

@Component
public class SystemModuleDao extends BaseIbatisDAO {

	public Class<SystemModule> getEntityClass() {
		return SystemModule.class;
	}

	public boolean saveOrUpdate(SystemModule entity) {
		if (entity.getModuleId() == null)
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else
			return update(entity);
	}
	
	public List<SystemModule> getList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}

}
