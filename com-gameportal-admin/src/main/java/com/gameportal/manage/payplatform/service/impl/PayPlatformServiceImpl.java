package com.gameportal.manage.payplatform.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.payplatform.dao.PayPlatformDao;
import com.gameportal.manage.payplatform.model.PayPlatform;
import com.gameportal.manage.payplatform.service.IPayPlatformService;

@Service
public class PayPlatformServiceImpl implements IPayPlatformService {
	@Resource(name = "payPlatformDao")
	private PayPlatformDao payPlatformDao = null;
	private Logger logger = Logger.getLogger(PayPlatformServiceImpl.class);// 日志对象

	public PayPlatformServiceImpl() {
		super();
	}

	@Override
	public PayPlatform queryPayPlatformById(Long ppid) {
		return (PayPlatform)payPlatformDao.findById(ppid);
	}

	@Override
	public List<PayPlatform> queryPayPlatform(Long ppid, String pname,String channelType,
			Integer startNo, Integer pageSize) {
		return queryPayPlatform(ppid, pname, null, null, null, null, null,
				null, channelType, startNo, pageSize);
	}

	@Override
	public List<PayPlatform> queryPayPlatform(Long ppid, String pname,
			Integer status,String channelType, Integer startNo, Integer pageSize) {
		return queryPayPlatform(ppid, pname, null, null, null, null, null,
				status,channelType, startNo, pageSize);
	}

	@Override
	public List<PayPlatform> queryPayPlatform(Long ppid, String pname,
			String domainname, String platformkey, String ciphercode,
			String returnUrl, String noticeUrl, Integer status,String channelType,
			Integer startNo, Integer pageSize) {
		Map params = new HashMap();
		if(StringUtils.isNotBlank(ObjectUtils.toString(ppid))){
			params.put("ppid", ppid);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(pname))){
			params.put("pname", pname);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(domainname))){
			params.put("domainname", domainname);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(platformkey))){
			params.put("platformkey", platformkey);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(ciphercode))){
			params.put("ciphercode", ciphercode);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(returnUrl))){
			params.put("returnUrl", returnUrl);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(noticeUrl))){
			params.put("noticeUrl", noticeUrl);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(status))){
			params.put("status", status);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(channelType))){
			params.put("channelType", channelType);
		}
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		params.put("sortColumns", "sequence asc,status desc");
		return payPlatformDao.getList(params);
	}

	@Override
	public Long queryPayPlatformCount(Long ppid, String pname,String channelType) {
		return queryPayPlatformCount(ppid, pname, null, null, 
				null, null, null, null,channelType);
	}

	@Override
	public Long queryPayPlatformCount(Long ppid, String pname, Integer status,String channelType) {
		return queryPayPlatformCount(ppid, pname, null, null, 
				null, null, null, status,channelType);
	}

	@Override
	public Long queryPayPlatformCount(Long ppid, String pname,
			String domainname, String platformkey, String ciphercode,
			String returnUrl, String noticeUrl, Integer status,String channelType) {
		Map<String, Object> params = new HashMap<>();
		if(StringUtils.isNotBlank(ObjectUtils.toString(ppid))){
			params.put("ppid", ppid);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(pname))){
			params.put("pname", pname);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(domainname))){
			params.put("domainname", domainname);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(platformkey))){
			params.put("platformkey", platformkey);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(ciphercode))){
			params.put("ciphercode", ciphercode);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(returnUrl))){
			params.put("returnUrl", returnUrl);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(noticeUrl))){
			params.put("noticeUrl", noticeUrl);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(status))){
			params.put("status", status);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(channelType))){
			params.put("channelType", channelType);
		}
		return payPlatformDao.getRecordCount(params);
	}

	@Override
	public PayPlatform savePayPlatform(PayPlatform payPlatform)
			throws Exception {
		payPlatform = (PayPlatform) payPlatformDao.save(payPlatform);
		return StringUtils.isNotBlank(ObjectUtils.toString(payPlatform
				.getPpid())) ? payPlatform : null;
	}

	@Override
	public boolean saveOrUpdatePayPlatform(PayPlatform payPlatform)
			throws Exception {
		return payPlatformDao.saveOrUpdate(payPlatform);
	}

	@Override
	public boolean deletePayPlatform(Long ppid) throws Exception {
		return payPlatformDao.delete(ppid);
	}
}