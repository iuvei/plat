package com.gameportal.manage.member.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.member.model.UserManager;
import com.gameportal.manage.pay.model.PayOrder;
import com.gameportal.manage.system.dao.BaseIbatisDAO;
import com.gameportal.manage.xima.model.MemberXimaMain;

@Component("userManagerDao")
public class UserManagerDao extends BaseIbatisDAO{

	@Override
	public Class<UserManager> getEntityClass() {
		return UserManager.class;
	}

	public UserManager getObject(Map<String, Object> params){
		List<UserManager> list = getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	public UserManager getUManager(Map<String, Object> params){
		return (UserManager)getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectUManager", params);
	}
	
	public List<UserManager> getList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}
	
	public boolean save(UserManager entity){
		return StringUtils.isNotBlank(ObjectUtils.toString(super.save(entity))) ? true: false;
	}
	
	public boolean update(UserManager entity){
		return StringUtils.isNotBlank(ObjectUtils.toString(super.update(entity))) ? true: false;
	}
	
	public List<Map<String, Object>> getDXList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".getDXList",params);
	}
	
	/**
	 * 存款 取款 优惠列表
	 * @return
	 */
	public List<PayOrder> getDXPayOrderList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectPayOrderList", params);
	}
	
	/**
	 * 存款 取款 优惠总数
	 * @return
	 */
	public Long getDXPayOrderCount(Map<String, Object> params){
		return (Long) getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectPayOrderCount", params);
	}
	
	
	/**
	 * 洗码列表
	 * @return
	 */
	public List<MemberXimaMain> getDXMemeberXimaList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectXimaList", params);
	}
	
	/**
	 * 洗码总数
	 * @return
	 */
	public Long getDXMemeberXimaCount(Map<String, Object> params){
		return (Long) getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectXimaCount", params);
	}
	
}
