package com.gameportal.pay.dao;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.gameportal.pay.model.PayOrder;
import com.gameportal.pay.model.PayOrderLog;
import com.gameportal.pay.model.UserManager;

@Component
public class PayOrderDao extends BaseIbatisDAO {

	public Class getEntityClass() {
		return PayOrder.class;
	}

	public void saveOrUpdate(PayOrder entity) {
		if (entity.getPoid() == null)
			save(entity);
		else
			update(entity);
	}
	
	public boolean updateUManager(UserManager entity){
		try {
			prepareObjectForSaveOrUpdate(entity);
			Object primaryKey = getSqlMapClientTemplate().update(getSimpleName()+".updateUManager", entity);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public UserManager getUserManager(Map<String, Object> params){
		return (UserManager)getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectUManager", params);
	}
	
	/**
	 * 新增帐变日志
	 * @param log
	 */
	public void insertPayLog(PayOrderLog log){
		super.getSqlMapClientTemplate().insert(getSimpleName()+".insertPayLog", log);
	}
}
