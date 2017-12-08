package com.gameportal.manage.smsplatform.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.sitesettings.model.SiteSettings;
import com.gameportal.manage.smsplatform.model.SmsPlatAccount;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

@Component
public class SmsPlatAccountDao extends BaseIbatisDAO {

	public Class<SmsPlatAccount> getEntityClass() {
		return SmsPlatAccount.class;
	}

	public boolean saveOrUpdate(SmsPlatAccount entity) {
		if (entity.getSpaid() == null)
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else
			return update(entity);
	}

	public boolean updateSmsPlatAccountOnlyStatus(Map params) {
		int cnt = getSqlMapClientTemplate().update(
				getSimpleName() + ".updateSmsPlatAccountOnlyStatus", params);
		return cnt > 0;
	}
	
	public List<SmsPlatAccount> getList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}

}
