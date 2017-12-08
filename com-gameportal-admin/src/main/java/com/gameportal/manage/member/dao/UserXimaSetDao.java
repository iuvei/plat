package com.gameportal.manage.member.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gameportal.manage.member.model.MemberGrade;
import com.gameportal.manage.member.model.UserXimaSet;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

@Component("userXimaSetDao")
public class UserXimaSetDao extends BaseIbatisDAO{

	@Override
	public Class<UserXimaSet> getEntityClass() {
		// TODO Auto-generated method stub
		return UserXimaSet.class;
	}
	
	public UserXimaSet getObject(Map<String, Object> params){
		List<UserXimaSet> list = getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
		if(null != list && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	public List<UserXimaSet> getList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}

}
