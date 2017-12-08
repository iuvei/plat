package com.gameportal.web.sms.service.impl;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.crypto.Data;

import org.springframework.stereotype.Service;

import com.gameportal.web.sms.dao.SmsSendLogDao;
import com.gameportal.web.sms.model.SmsSendLog;
import com.gameportal.web.sms.service.ISmsLogService;
import com.gameportal.web.util.DateUtil;
/**
 * 短信日志业务类。
 * @author sum
 *
 */
@Service("smsLogService")
@SuppressWarnings("all")
public class SmsLogServiceImpl implements ISmsLogService{

	@Resource(name="smsSendLogDao")
	private SmsSendLogDao smsSendLogDao;
	@Override
	public SmsSendLog saveSmsPlat(SmsSendLog log) {
		return (SmsSendLog)smsSendLogDao.save(log);
	}
	@Override
	public Long selectSmsLogCount(Map<String, Object> params) {
		return smsSendLogDao.selectSmsLogCount(params);
	}
}
