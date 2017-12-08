package com.gameportal.manage.smsplatform.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.sitesettings.model.WebAdEntity;
import com.gameportal.manage.smsplatform.model.SmsPlatBlacklist;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

@Component
public class SmsPlatBlacklistDao extends BaseIbatisDAO {

	public Class<SmsPlatBlacklist> getEntityClass() {
		return SmsPlatBlacklist.class;
	}

	public boolean saveOrUpdate(SmsPlatBlacklist entity) {
		if(entity.getSpbid() == null) 
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else 
			return update(entity);
	}
	
	public List<SmsPlatBlacklist> getList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}

}
