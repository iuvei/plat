package com.gameportal.manage.xima.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.system.dao.BaseIbatisDAO;
import com.gameportal.manage.xima.model.MemberXimaMain;
import com.gameportal.manage.xima.model.MemberXimaSet;


@Component
public class MemberXimaMainDao extends BaseIbatisDAO{

	public Class<MemberXimaMain> getEntityClass() {
		return MemberXimaMain.class;
	}
	
	public boolean saveOrUpdate(MemberXimaMain entity) {
		if(entity.getMxmid() == null) 
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else 
			return update(entity);
	}

	public List<MemberXimaMain> getList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}
}
