package com.gameportal.web.user.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.web.user.model.PayOrderLog;
import com.gameportal.web.user.model.UserGrade;
import com.gameportal.web.user.model.WithdrawalcountLog;

/**
 * 用户属性数据库访问
 * @author Administrator
 *
 */
@Component("userPropertyDao")
public class UserPropertyDao extends BaseIbatisDAO{

	@Override
	public Class<UserGrade> getEntityClass() {
		return UserGrade.class;
	}
	
	public boolean insertWithdrawalcountLog(WithdrawalcountLog entity){
		return StringUtils.isNotBlank(ObjectUtils.toString(getSqlMapClientTemplate().insert(getSimpleName()+".insertWithdrawalcountLog", entity))) ? true: false;
	}
	
	public int updateWithdrawalcountLog(WithdrawalcountLog entity){
		return getSqlMapClientTemplate().update(getSimpleName()+".updateWithdrawalcountLog", entity);
	}
	
	public List<UserGrade> getUserGradeList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectUserGrade", params);
	}
	
	public List<WithdrawalcountLog> getWithdrawalcountLogList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectWithdrawalcountLog", params);
	}
	
	public UserGrade getUserGrade(Map<String, Object> params){
		List<UserGrade> list = getUserGradeList(params);
		if(null != list && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	public WithdrawalcountLog getWithdrawalcountLog(Map<String, Object> params){
		List<WithdrawalcountLog> list = getWithdrawalcountLogList(params);
		if(null != list && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 查询用户当前是否有申请的提款
	 * @param params
	 * @return
	 */
	public Long getPayOrder(Map<String, Object> params){
		return getRecordCount(getSimpleName()+".selectPayOrder", params);
	}
	
	/**
	 * 新增帐变日志
	 * @param log
	 */
	public void insertPayLog(PayOrderLog log){
		super.getSqlMapClientTemplate().insert(getSimpleName()+".insertPayLog", log);
	}
	
	public Long sumPayOrderAmount(Map<String, Object> params){
		return getRecordCount(getSimpleName()+".sumPayOrderAmount", params);
	}
}
