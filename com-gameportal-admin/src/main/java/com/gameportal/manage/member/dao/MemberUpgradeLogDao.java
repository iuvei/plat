package com.gameportal.manage.member.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.member.model.MemberGrade;
import com.gameportal.manage.member.model.MemberUpgradeLog;
import com.gameportal.manage.system.dao.BaseIbatisDAO;


@Component
public class MemberUpgradeLogDao extends BaseIbatisDAO{

	public Class<MemberUpgradeLog> getEntityClass() {
		return MemberUpgradeLog.class;
	}
	
	public boolean saveOrUpdate(MemberUpgradeLog entity) {
		if(entity.getLid() == null) 
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else 
			return update(entity);
	}

	public List<MemberUpgradeLog> getList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}
}
