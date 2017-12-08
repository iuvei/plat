package com.gameportal.web.sms.service;

import java.util.Map;

import com.gameportal.web.sms.model.SmsSendLog;

public interface ISmsLogService {
	/**
	 * 保存短信日志。
	 * 
	 * @param log
	 *            短信发送日志信息
	 * @return 返回短信实体。
	 */
	SmsSendLog saveSmsPlat(SmsSendLog log);
	
	/**
	 * 查询日发送数量。
	 * @param params
	 * @return
	 */
	Long selectSmsLogCount(Map<String, Object> params);
}
