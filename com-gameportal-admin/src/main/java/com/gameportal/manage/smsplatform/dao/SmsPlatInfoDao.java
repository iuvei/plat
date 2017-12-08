package com.gameportal.manage.smsplatform.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.smsplatform.model.SmsPlatInfo;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

@Component
public class SmsPlatInfoDao extends BaseIbatisDAO {

	public Class<SmsPlatInfo> getEntityClass() {
		return SmsPlatInfo.class;
	}

	public boolean saveOrUpdate(SmsPlatInfo entity) {
		if(entity.getSpiid() == null) 
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else 
			return update(entity);
	}
	
	public List<SmsPlatInfo> getList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}

}
