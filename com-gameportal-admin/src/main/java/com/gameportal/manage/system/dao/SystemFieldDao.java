package com.gameportal.manage.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.system.model.SystemField;
import com.gameportal.manage.system.model.SystemRole;

@Component
public class SystemFieldDao extends BaseIbatisDAO {

	public Class<SystemField> getEntityClass() {
		return SystemField.class;
	}

	public boolean saveOrUpdate(SystemField entity) {
		if (entity.getFieldId() == null)
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else
			return update(entity);
	}
	
	public List<SystemField> getList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}

}
