package com.gameportal.web.sms.service.impl;

import java.util.Hashtable;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.web.sms.dao.SmsPlatAccountDao;
import com.gameportal.web.sms.model.SmsPlatAccount;
import com.gameportal.web.sms.service.ISmsService;
import com.todaynic.client.mobile.SMS;

/**
 * 发送短信业务类。
 * 
 * @author sum
 *
 */
@Service("smsService")
@SuppressWarnings("all")
public class SmsServiceImpl implements ISmsService {
	private static final Logger logger = Logger.getLogger(SmsServiceImpl.class);
	@Resource(name = "smsPlatAccountDao")
	private SmsPlatAccountDao smsPlatAccountDao;

	@Override
	public String sendSMS(String mobile, String message, String time, String type) throws Exception {
		SmsPlatAccount sms = smsPlatAccountDao.getSmsPlatAccount();
		if (sms != null) {
			Hashtable configTable = new Hashtable();
			configTable.put("VCPSERVER", sms.getServername().trim());
			configTable.put("VCPSVPORT", sms.getServerport().trim());
			configTable.put("VCPUSERID", sms.getAccount().trim());
			configTable.put("VCPPASSWD", sms.getPwd().trim());
			SMS smssender = new SMS(configTable);
			smssender.sendSMS(mobile, message, "0", type);
			String sendXml = smssender.getSendXml();
			logger.info("send sms content:" + sendXml);
			Hashtable recTable = smssender.getRespData();
			String receiveXml = smssender.getRecieveXml();
			String code = smssender.getCode();
			String recmsg = smssender.getMsg();
			logger.info("response sms code:" + code + ",msg:" + recmsg);
			return code;
		} 
		logger.info("请开启短信配置。");
		return null;
	}

	@Override
	public SmsPlatAccount getSmsPlatAccount() {
		return smsPlatAccountDao.getSmsPlatAccount();
	}
}
