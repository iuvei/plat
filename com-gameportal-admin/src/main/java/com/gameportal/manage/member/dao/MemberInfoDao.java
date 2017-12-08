package com.gameportal.manage.member.dao;


import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.member.model.MemberClearingFlag;
import com.gameportal.manage.member.model.MemberInfo;
import com.gameportal.manage.member.model.ProxyXimaFlag;
import com.gameportal.manage.member.model.UserLoginLog;
import com.gameportal.manage.smsplatform.model.SmsPlatAccount;
import com.gameportal.manage.system.dao.BaseIbatisDAO;
@Component
public class MemberInfoDao extends BaseIbatisDAO {
	
	public Class<MemberInfo> getEntityClass() {
		return MemberInfo.class;
	}
	public boolean saveOrUpdate(MemberInfo entity) {
		if (entity.getUiid() == null)
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else
			return update(entity);
	}
	
	public boolean insertMemberClearingFlag(MemberClearingFlag entity){
		return StringUtils.isNotBlank(ObjectUtils.toString(save(getSimpleName()+".insertMemberClearingFlag",entity))) ? true : false;
	}
	
	public boolean insertProxyXimaFlag(ProxyXimaFlag entity){
		return StringUtils.isNotBlank(ObjectUtils.toString(save(getSimpleName()+".insertProxyXimaFlag",entity))) ? true : false;
	}
	
	public List<ProxyXimaFlag> selectProxyXimaFlag(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectProxyXimaFlag", params);
	}
	
	public Long getCountLog(Map<String, Object> params){
		return super.getRecordCount(getSimpleName()+".countLog", params);
	}
	
	public List<UserLoginLog> getListLog(Map<String, Object> params,int thisPage,int pageSize){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".selectLog", params);
	}
	
	public List<MemberInfo> getMemberInfoByAccount(String account){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".selectByAccount", account);
	}
	
	public Long selectBirthDayCount(Map<String, Object> params){
		return (Long) super.getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectBirthDayCount", params);
	}
	
	/**
	 * 查询生日会员列表
	 * @param params
	 * @return
	 */
	public List<MemberInfo> selectBirthDayList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".selectBirthDayResult", params);
	}
}
