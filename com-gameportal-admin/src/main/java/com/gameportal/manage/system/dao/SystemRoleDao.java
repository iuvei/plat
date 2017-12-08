package com.gameportal.manage.system.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.system.model.SystemRole;

@Component
public class SystemRoleDao extends BaseIbatisDAO {

	public Class<SystemRole> getEntityClass() {
		return SystemRole.class;
	}

	public boolean saveOrUpdate(SystemRole entity) {
		if (entity.getRoleId() == null)
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else
			return update(entity);
	}

	public List<SystemRole> queryRoleByUserId(Long userId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (null != userId) {
			paramMap.put("userId", userId);
		}
		return getSqlMapClientTemplate().queryForList(
				getSimpleName() + ".queryRoleByUserId", paramMap);
	}
	
	public List<SystemRole> getList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}

}
