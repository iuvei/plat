package com.gameportal.manage.smsplatform.service.impl;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.smsplatform.dao.SmsPlatAccountDao;
import com.gameportal.manage.smsplatform.dao.SmsPlatBlacklistDao;
import com.gameportal.manage.smsplatform.dao.SmsPlatInfoDao;
import com.gameportal.manage.smsplatform.dao.SmsPlatSendlogDao;
import com.gameportal.manage.smsplatform.model.SmsPlatAccount;
import com.gameportal.manage.smsplatform.model.SmsPlatBlacklist;
import com.gameportal.manage.smsplatform.model.SmsPlatInfo;
import com.gameportal.manage.smsplatform.model.SmsPlatSendlog;
import com.gameportal.manage.smsplatform.service.ISmsPlatInfoService;
import com.todaynic.client.mobile.SMS;

@Service
public class SmsPlatInfoServiceImpl implements ISmsPlatInfoService {
	@Resource(name = "smsPlatInfoDao")
	private SmsPlatInfoDao smsPlatInfoDao = null;
	@Resource(name = "smsPlatAccountDao")
	private SmsPlatAccountDao smsPlatAccountDao = null;
	@Resource(name = "smsPlatBlacklistDao")
	private SmsPlatBlacklistDao smsPlatBlacklistDao = null;
	@Resource(name = "smsPlatSendlogDao")
	private SmsPlatSendlogDao smsPlatSendlogDao = null;
	private Logger logger = Logger.getLogger(SmsPlatInfoServiceImpl.class);// 日志对象

	public SmsPlatInfoServiceImpl() {
		super();
	}

	@Override
	public SmsPlatInfo querySmsPlatInfoById(Long spiid) {
		return (SmsPlatInfo) smsPlatInfoDao.findById(spiid);
	}

	@Override
	public List<Map<String, Object>> querySelectSmsPlatInfos() {
		List<Map<String, Object>> map = (List<Map<String, Object>>) smsPlatInfoDao
				.queryForPager("SmsPlatInfo.selectSmsPlatInfos", null, 0, 0);
		return map;
	}
	
	@Override
	public List<SmsPlatInfo> querySmsPlatInfo(Long spiid, String name,
			Integer startNo, Integer pageSize) {
		return querySmsPlatInfo(spiid, name, null, startNo, pageSize);
	}

	@Override
	public List<SmsPlatInfo> querySmsPlatInfo(Long spiid, String name,
			Integer status, Integer startNo, Integer pageSize) {
		Map params = new HashMap();
		if (StringUtils.isNotBlank(ObjectUtils.toString(spiid))) {
			params.put("spiid", spiid);
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(name))) {
			params.put("name", name);
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
			params.put("status", status);
		}
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return smsPlatInfoDao.getList(params);
	}

	@Override
	public Long querySmsPlatInfoCount(Long spiid, String name) {
		return querySmsPlatInfoCount(spiid, name, null);
	}

	@Override
	public Long querySmsPlatInfoCount(Long spiid, String name, Integer status) {
		Map params = new HashMap();
		if (StringUtils.isNotBlank(ObjectUtils.toString(spiid))) {
			params.put("spiid", spiid);
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(name))) {
			params.put("name", name);
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
			params.put("status", status);
		}
		return smsPlatInfoDao.getRecordCount(params);
	}

	@Override
	public SmsPlatInfo saveSmsPlatInfo(SmsPlatInfo payPlatform)
			throws Exception {
		payPlatform = (SmsPlatInfo) smsPlatInfoDao.save(payPlatform);
		return StringUtils.isNotBlank(ObjectUtils.toString(payPlatform
				.getSpiid())) ? payPlatform : null;
	}

	@Override
	public boolean saveOrUpdateSmsPlatInfo(SmsPlatInfo smsPlatform)
			throws Exception {
		return smsPlatInfoDao.saveOrUpdate(smsPlatform);
	}

	@Override
	public boolean deleteSmsPlatInfo(Long spiid) throws Exception {
		return smsPlatInfoDao.delete(spiid);
	}

	// ******************************短信平台账号******************************
	@Override
	public SmsPlatAccount querySmsPlatAccountById(Long spaid) {
		return (SmsPlatAccount) smsPlatAccountDao.findById(spaid);
	}

	@Override
	public Long querySmsPlatAccountCount(Long spaid, Long spiid, String name) {
		return querySmsPlatAccountCount(spaid, spiid, name, null);
	}

	@Override
	public Long querySmsPlatAccountCount(Long spaid, Long spiid, String name,
			Integer status) {
		Map params = new HashMap();
		if (StringUtils.isNotBlank(ObjectUtils.toString(spaid))) {
			params.put("spaid", spaid);
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(spiid))) {
			params.put("spiid", spiid);
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(name))) {
			params.put("name", name);
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
			params.put("status", status);
		}
		return smsPlatAccountDao.getRecordCount(params);
	}

	@Override
	public List<SmsPlatAccount> querySmsPlatAccount(Long spaid, Long spiid,
			String name, Integer status, Integer startNo, Integer pageSize) {
		Map params = new HashMap();
		if (StringUtils.isNotBlank(ObjectUtils.toString(spaid))) {
			params.put("spaid", spaid);
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(spiid))) {
			params.put("spiid", spiid);
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(name))) {
			params.put("name", name);
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
			params.put("status", status);
		}
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return smsPlatAccountDao.getList(params);
	}

	@Override
	public boolean saveOrUpdateSmsPlatAccount(SmsPlatAccount smsPlatAccount) {
		return smsPlatAccountDao.saveOrUpdate(smsPlatAccount);
	}

	@Override
	public boolean deleteSmsPlatAccount(Long spaid) {
		return smsPlatAccountDao.delete(spaid);
	}
	

	@Override
	public boolean updateSmsPlatAccountOnlyStatus(Long spaid, Integer status) {
		Map params = new HashMap();
		if (StringUtils.isNotBlank(ObjectUtils.toString(spaid))) {
			params.put("spaid", spaid);
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
			params.put("status", status);
		}
		return smsPlatAccountDao.updateSmsPlatAccountOnlyStatus(params);
	}
	
	// ******************************短信平台黑名单******************************
	@Override
	public SmsPlatBlacklist querySmsPlatBlacklistById(Long spbid) {
		return (SmsPlatBlacklist) smsPlatBlacklistDao.findById(spbid);
	}

	@Override
	public Long querySmsPlatBlacklistCount(Long spbid, Long spiid, String mobile) {
		Map params = new HashMap();
		if (StringUtils.isNotBlank(ObjectUtils.toString(spbid))) {
			params.put("spbid", spbid);
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(spiid))) {
			params.put("spiid", spiid);
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(mobile))) {
			params.put("mobile", mobile);
		}
		return smsPlatBlacklistDao.getRecordCount(params);
	}

	@Override
	public List<SmsPlatBlacklist> querySmsPlatBlacklist(Long spbid, Long spiid,
			String mobile, Integer startNo, Integer pageSize) {
		Map params = new HashMap();
		if (StringUtils.isNotBlank(ObjectUtils.toString(spbid))) {
			params.put("spbid", spbid);
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(spiid))) {
			params.put("spiid", spiid);
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(mobile))) {
			params.put("mobile", mobile);
		}
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return smsPlatBlacklistDao.getList(params);
	}

	@Override
	public boolean saveOrUpdateSmsPlatBlacklist(
			SmsPlatBlacklist smsPlatBlacklist) {
		return smsPlatBlacklistDao.saveOrUpdate(smsPlatBlacklist);
	}

	@Override
	public boolean deleteSmsPlatBlacklist(Long spbid) {
		return smsPlatBlacklistDao.delete(spbid);
	}

	@Override
	public Long querySmsLogCount(Map<String, Object> params) {
		return smsPlatSendlogDao.getRecordCount(params);
	}

	@Override
	public List<SmsPlatSendlog> querySmsLoglist(Map<String, Object> params,
			int thisPage, int pageSize) {
		params.put("limit", true);
		params.put("thisPage", thisPage);
		params.put("pageSize", pageSize);
		return smsPlatSendlogDao.getList(params);
	}
	
	

	@Override
	public SmsPlatSendlog saveSmsPlat(SmsPlatSendlog log) {
		return (SmsPlatSendlog)smsPlatSendlogDao.save(log);
	}

	@Override
	public String sendSMS(String mobile, String message,String type,SmsPlatAccount sms) throws Exception {
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

	@Override
	public List<SmsPlatAccount> getList(Map<String, Object> params) {
		return smsPlatAccountDao.getList(params);
	}
}