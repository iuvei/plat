package com.gameportal.manage.betlog.dao;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.betlog.model.CollectLog;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

@Component
public class CollectLogDao extends BaseIbatisDAO {

	public Class<CollectLog> getEntityClass() {
		return CollectLog.class;
	}

	public boolean saveOrUpdate(CollectLog entity) {
		if (entity.getPid() == null)
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true : false;
		else
			return update(entity);
	}
}
