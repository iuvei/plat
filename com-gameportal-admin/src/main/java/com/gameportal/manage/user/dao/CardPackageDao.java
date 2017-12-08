package com.gameportal.manage.user.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.system.dao.BaseIbatisDAO;
import com.gameportal.manage.user.model.CardPackage;

@Component
public class CardPackageDao extends BaseIbatisDAO {

	public Class<CardPackage> getEntityClass() {
		return CardPackage.class;
	}

	public boolean saveOrUpdate(CardPackage entity) {
		if (entity.getCpid() == null)
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else
			return update(entity);
	}
	
	public boolean updateStatus(Map<String,Object> params) {
		int cnt = getSqlMapClientTemplate().update(
				getSimpleName() + ".updateStatus", params);
		return cnt == 1;
	}
	
	public List<CardPackage> getList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}

}
