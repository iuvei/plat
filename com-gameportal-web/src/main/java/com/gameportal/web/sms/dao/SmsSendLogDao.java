package com.gameportal.web.sms.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gameportal.web.sms.model.SmsSendLog;
import com.gameportal.web.user.dao.BaseIbatisDAO;

/**
 * 短信日志持久层。
 * @author sum
 *
 */
@Repository
@SuppressWarnings("all")
public class SmsSendLogDao extends BaseIbatisDAO{

	@Override
	public Class getEntityClass() {
		return SmsSendLog.class;
	}
	
	/**
	 * 统计日发送短信数量
	 * @param params
	 * @return
	 */
	public Long selectSmsLogCount(Map<String, Object> params){
		return (Long)getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectSmsLogCount", params);
	}
}
