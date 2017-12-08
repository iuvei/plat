package com.gameportal.manage.member.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.payplatform.model.PayPlatform;
import com.gameportal.manage.system.dao.BaseIbatisDAO;
import com.gameportal.manage.member.model.MemberGrade;

@Component
public class MemberGradeDao extends BaseIbatisDAO{

	public Class<MemberGrade> getEntityClass() {
		return MemberGrade.class;
	}
	
	public boolean saveOrUpdate(MemberGrade entity) {
		if(entity.getGid() == null) 
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else 
			return update(entity);
	}
	
	public List<MemberGrade> getList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}

}
